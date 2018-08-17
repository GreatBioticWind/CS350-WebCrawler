package edu.odu.cs.cs350;

import java.sql.*;
import java.util.Vector;

public class DB
{
    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String protocol = "jdbc:derby:";
    private static final String dbName = "WebsiteAnalysisDB";
    private static final String htmlTableName = "htmlAnalysis";
    private static Connection conn = null;
    private static Statement stmt = null;

    public static PreparedStatement getPstmt() {
        return pstmt;
    }

    public static void setPstmt(PreparedStatement pstmt) {
        DB.pstmt = pstmt;
    }

    private static PreparedStatement pstmt = null;

    public static String getHtmlTableName() {
        return htmlTableName;
    }

    public static Connection getConn() {
        return conn;
    }

    public static void setConn(Connection conn) {
        DB.conn = conn;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static void setStmt(Statement stmt) {
        DB.stmt = stmt;
    }

    public static String getDbName() {
        return dbName;
    }


    /**
     * Using Apache Derby this function creates the embedded database, establishes a connection to it, and
     * calls the functions that setup the tables within.
     */
    public static boolean setupDB()
    {

        String dbPath = protocol + getDbName() + ";create=true;user=app;password=admin";

        try
        {
            //Load DB driver
            Class.forName(driver).newInstance();

            //Connect database
            setConn(DriverManager.getConnection(dbPath));

            //Create tables
            setupHTMLTable();
        }

        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except)
        {
            except.printStackTrace();
            return false;
        }
        
        return true;
    }

    /**
     * Sets up the table in which all analyzed links are stored.
     */
    static void setupHTMLTable()
    {
        try
        {
            setStmt(getConn().createStatement());
            getStmt().executeUpdate(
                    "CREATE TABLE " + getHtmlTableName() +
                            " (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                            " DIR VARCHAR(254)," +
                            " PAGENUM INTEGER," +
                            " TAGTYPE CHAR(10)," +
                            " DESTINATION CHAR(10)," +
                            " ISIZE DECIMAL(5,2))"
            );
            getStmt().close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Drops all created tables and closes the DB connection
     * Does not delete the entire DB but that does not appear to break anything yet.
     * The program simply overwrites existing files, as long as its not a table.
     */
    public static boolean cleanupDB()
    {
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate ("DROP TABLE " + htmlTableName);
            stmt.close();
            conn.close();
            //PathMap.deleteDir2(dbName);
        }

        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    /**
     * Inserts the data retrieved into the HTMLTable.
     * @param dir
     * @param pageNum
     * @param tagType
     * @param destination
     * @param size
     */
    public static boolean insertIntoHTMLTable(String dir, Integer pageNum, String tagType, String destination, Double size) {
        String query = "INSERT INTO " + getHtmlTableName() +
                " (DIR, PAGENUM, TAGTYPE, DESTINATION, ISIZE)" +
                " VALUES (?, ?, ?, ?, ?)";
        try {
            pstmt = getConn().prepareStatement(query);
            pstmt.setString(1, dir);
            pstmt.setInt(2, pageNum);
            pstmt.setString(3, tagType);
            pstmt.setString(4, destination);
            pstmt.setDouble(5, size);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getPstmt() != null) {
                    getPstmt().close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    /**
     * Formats and prints the entire htmlAnalysis table for debugging purposes.
     * We will delete this before production. Don't make a unit test for this.
     */
    public static void printTable()
    {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM " + getHtmlTableName()
            );

            Vector<String> columnNames = new Vector<>();

            System.out.print("\n\nPrinting table\n\n");

            for (int a = 0; a < 151; a++) {
                System.out.print("-");
            }

            System.out.print("\n");

            if (rs != null) {
                ResultSetMetaData columns = rs.getMetaData();
                int i = 0;
                while (i < columns.getColumnCount()) {
                    i++;
                    System.out.print("|    " + columns.getColumnName(i));
                    if (columns.getColumnName(i).length() < 24) {
                        for (int j = 0; j < (20-columns.getColumnName(i).length()); j++) {
                            System.out.print(" ");
                        }
                    }
                    columnNames.add(columns.getColumnName(i));
                }

                System.out.print("|\n");

                for (int a = 0; a < 151; a++) {
                    System.out.print("-");
                }

                System.out.print("\n");

                while (rs.next()) {
                    for (i = 0; i < columnNames.size(); i++) {
                        String row = rs.getString(columnNames.get(i));
                        if (row.length() > 20) {
                            System.out.print("|  " + row.substring(0,20) + "  ");
                        }
                        else if (row.length() < 20) {
                            System.out.print("|  " + row);
                            for (int j = 0; j < (20-row.length()); ++j) {
                                System.out.print(" ");
                            }
                            System.out.print("  ");
                        }
                        else
                            System.out.print("                    ");

                    }
                    System.out.print("|\n");
                }

            }
            for (int a = 0; a < 151; a++) {
                System.out.print("-");
            }

            System.out.print("\n\n");

        }   catch (SQLException e) {
                e.printStackTrace();
            }

    }

    /**
     * Print out all query results for each page analyzed. For debugging only. Don't unit test this either.
     *
     * @param pageCount Total number of pages containing HTML
     */
    public static void testDBQueries(int pageCount) {
        for (int i =0; i < pageCount; i++) {
            System.out.println("On page " + (i+1) + ":");

            int countImageInternal = DB.getLocalImageCount(i+1);
            int countImageExternal = DB.getExternalImageCount(i+1);
            int countScript = DB.getScriptCount(i+1);
            int countSS = DB.getStylesheetCount(i+1);
            int countLinkIntraPage = DB.getIntrapageLinkCount(i+1);
            int countLinkInterSite = DB.getIntersiteLinkCount(i+1);
            int countLinkExternal = DB.getExternalLinkCount(i+1);

            System.out.println("\tNumber of internal images: " + countImageInternal);
            System.out.println("\tNumber of external images: " + countImageExternal);
            if (countImageInternal > 0 || countImageExternal > 0)
            {
                Vector<String> all = DB.getListAllImagesOnPage(i+1);
                for (String ALL : all) {
                    System.out.println("\t\t" + ALL);
                }
            }
            System.out.println("\tNumber of scripts referenced: " + countScript);
            if (countScript > 0)
            {
                Vector<String> all = DB.getListAllScriptsOnPage(i+1);
                for (String ALL : all) {
                    System.out.println("\t\t" + ALL);
                }
            }
            System.out.println("\tNumber of stylesheets utilized: " + countSS);
            if (countSS > 0)
            {
                Vector<String> all = DB.getListAllStylesheetsOnPage(i+1);
                for (String ALL : all) {
                    System.out.println("\t\t" + ALL);
                }
            }
            System.out.println("\tNumber of intra-page links: " + countLinkIntraPage);
            System.out.println("\tNumber of inter-site links: " + countLinkInterSite);
            System.out.println("\tNumber of external links: " + countLinkExternal);
            System.out.println();
        }
    }

    /**
     * Queries the database and returns a count of the local images on a given page.
     *
     * @param pageNumber Int corresponding to a page
     * @return Count of all internal images on a page
     */
    public static Integer getLocalImageCount(int pageNumber) {
        int count = 0;

        try {
            setStmt(getConn().createStatement());
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) " +
                            "AS icount " +
                            "FROM " + getHtmlTableName() +
                            " WHERE " + getHtmlTableName() + ".PAGENUM = " + pageNumber +
                            " AND " + getHtmlTableName() + ".TAGTYPE = 'IMAGE'" +
                            " AND " + getHtmlTableName() + ".DESTINATION = 'INTERNAL'");
            while(rs.next()) {
                count = rs.getInt("icount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Queries the database and returns a count of the scripts on a given page.
     *
     * @param pageNumber Int corresponding to a page
     * @return Count of all scripts on a page
     */
    public static Integer getScriptCount(int pageNumber) {
        int count = 0;

        try {
            stmt = getConn().createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) " +
                            "AS icount " +
                            "FROM " + getHtmlTableName() +
                            " WHERE " + getHtmlTableName() + ".PAGENUM = " + pageNumber +
                            " AND " + getHtmlTableName() + ".TAGTYPE = 'JAVASCRIPT'");
            while(rs.next()) {
                count = rs.getInt("icount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Queries the database and returns a count of the stylesheets on a given page.
     *
     * @param pageNumber Int corresponding to a page
     * @return Count of all stylesheets on a page
     */
    public static Integer getStylesheetCount(int pageNumber) {
        int count = 0;

        try {
            stmt = getConn().createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) " +
                            "AS icount " +
                            "FROM " + getHtmlTableName() +
                            " WHERE " + getHtmlTableName() + ".PAGENUM = " + pageNumber +
                            " AND " + getHtmlTableName() + ".TAGTYPE = 'STYLESHEET'");
            while(rs.next()) {
                count = rs.getInt("icount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Queries the database and returns a count of the local intra-page links on a given page.
     *
     * @param pageNumber Int corresponding to a page
     * @return Count of all intra-page links on a page
     */
    public static Integer getIntrapageLinkCount(int pageNumber) {
        int count = 0;

        try {
            stmt = getConn().createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) " +
                            "AS icount " +
                            "FROM " + getHtmlTableName() +
                            " WHERE " + getHtmlTableName() + ".PAGENUM = " + pageNumber +
                            " AND " + getHtmlTableName() + ".TAGTYPE = 'LINK'" +
                            " AND " + getHtmlTableName() + ".DESTINATION = 'INTRAPAGE'");
            while(rs.next()) {
                count = rs.getInt("icount");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Queries the database and returns a count of the local inter-site links on a given page.
     *
     * @param pageNumber Int corresponding to a page
     * @return Count of all inter-site links on a page
     */
    public static Integer getIntersiteLinkCount(int pageNumber) {
        int count = 0;

        try {
            stmt = getConn().createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) " +
                            "AS icount " +
                            "FROM " + getHtmlTableName() +
                            " WHERE " + getHtmlTableName() + ".PAGENUM = " + pageNumber +
                            " AND " + getHtmlTableName() + ".TAGTYPE = 'LINK'" +
                            " AND " + getHtmlTableName() + ".DESTINATION = 'INTERSITE'");
            while(rs.next()) {
                count = rs.getInt("icount");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Queries the database and returns a count of the local External links on a given page.
     *
     * @param pageNumber Int corresponding to a page
     * @return Count of all External links on a page
     */
    public static Integer getExternalLinkCount(int pageNumber) {
        int count = 0;

        try {
            stmt = getConn().createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) " +
                            "AS icount " +
                            "FROM " + getHtmlTableName() +
                            " WHERE " + getHtmlTableName() + ".PAGENUM = " + pageNumber +
                            " AND " + getHtmlTableName() + ".TAGTYPE = 'LINK'" +
                            " AND " + getHtmlTableName() + ".DESTINATION = 'EXTERNAL'");
            while(rs.next()) {
                count = rs.getInt("icount");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Queries the database and returns a count of the external images on a given page.
     *
     * @param pageNumber Int corresponding to a page
     * @return Count of all external images on a page
     */
    public static Integer getExternalImageCount(int pageNumber) {
        int count = 0;

        try {
            stmt = getConn().createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) " +
                            "AS icount " +
                            "FROM " + getHtmlTableName() +
                            " WHERE " + getHtmlTableName() + ".PAGENUM = " + pageNumber +
                            " AND " + getHtmlTableName() + ".TAGTYPE = 'IMAGE'" +
                            " AND " + getHtmlTableName() + ".DESTINATION = 'EXTERNAL'");
            while(rs.next()) {
                count = rs.getInt("icount");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Queries the database and returns a list of the images on a given page.
     *
     * @param pageNumber Int corresponding to a page
     * @return List of all images on a page
     */
    public static Vector<String> getListAllImagesOnPage(int pageNumber) {
        Vector<String> list = new Vector<>();

        try {
            stmt = getConn().createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT DIR " +
                            "AS iList " +
                            "FROM " + getHtmlTableName() +
                            " WHERE " + getHtmlTableName() + ".PAGENUM = " + pageNumber +
                            " AND " + getHtmlTableName() + ".TAGTYPE = 'IMAGE'");
            while(rs.next()) {
                list.add(rs.getString("iList"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Queries the database and returns a list of the scripts on a given page.
     *
     * @param pageNumber Int corresponding to a page
     * @return List of all scripts on a page
     */
    public static Vector<String> getListAllScriptsOnPage(int pageNumber) {
        Vector<String> list = new Vector<>();

        try {
            stmt = getConn().createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT DIR " +
                            "AS iList " +
                            "FROM " + getHtmlTableName() +
                            " WHERE " + getHtmlTableName() + ".PAGENUM = " + pageNumber +
                            " AND " + getHtmlTableName() + ".TAGTYPE = 'JAVASCRIPT'");
            while(rs.next()) {
                list.add(rs.getString("iList"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Queries the database and returns a list of the stylesheets on a given page.
     *
     * @param pageNumber Int corresponding to a page
     * @return List of all stylesheets on a page
     */
    public static Vector<String> getListAllStylesheetsOnPage(int pageNumber) {
        Vector<String> list = new Vector<>();

        try {
            stmt = getConn().createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT DIR " +
                            "AS iList " +
                            "FROM " + getHtmlTableName() +
                            " WHERE " + getHtmlTableName() + ".PAGENUM = " + pageNumber +
                            " AND " + getHtmlTableName() + ".TAGTYPE = 'STYLESHEET'");
            while(rs.next()) {
                list.add(rs.getString("iList"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}



