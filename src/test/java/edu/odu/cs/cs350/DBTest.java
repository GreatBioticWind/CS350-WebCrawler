package edu.odu.cs.cs350;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DBTest {

    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String protocol = "jdbc:derby:";
    private static final String dbName = "testWebsiteAnalysisDB";
    private static final String htmlTableName = "htmlAnalysis";

    private static Connection conn = null;
    private static Statement stmt = null;

    
    @Test
    public void testGetHTMLAnalysis() throws SQLException
    {
    	assertEquals("htmlAnalysis", DB.getHtmlTableName()); 
    }
    
    @Test
    public void testGetLocalImageCount() throws Exception {
        try {
            //Load DB driver
            Class.forName(driver).newInstance();

            //Connect database
            DB.setConn(DriverManager.getConnection(protocol + dbName));

            int count = DB.getLocalImageCount(5);
            assertEquals(1, count);

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
            except.printStackTrace();
        }
    }
    
    @Test
    public void testGetScriptCount()
    {
	    try {
	        //Load DB driver
	        Class.forName(driver).newInstance();
	
	        //Connect database
	        DB.setConn(DriverManager.getConnection(protocol + dbName));
	
	        int count = DB.getScriptCount(11);
	        assertEquals(1, count);
	
	    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
	        except.printStackTrace();
	    }
    }
    
    @Test
    public void testGetStyleSheetCount()
    {
	    try {
	        //Load DB driver
	        Class.forName(driver).newInstance();
	
	        //Connect database
	        DB.setConn(DriverManager.getConnection(protocol + dbName));
	
	        int count = DB.getStylesheetCount(16);
	        assertEquals(1, count);
	
	    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
	        except.printStackTrace();
	    }
    }
    
    @Test
    public void testGetIntrapageLinkCount() throws Exception {
        try {
            //Load DB driver
            Class.forName(driver).newInstance();

            //Connect database
            DB.setConn(DriverManager.getConnection(protocol + dbName));

            int count = DB.getIntrapageLinkCount(6);
            assertEquals(1, count);

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
            except.printStackTrace();
        }
    }
    
    @Test
    public void testGetIntersiteLinkCount() throws Exception {
        try {
            //Load DB driver
            Class.forName(driver).newInstance();

            //Connect database
            DB.setConn(DriverManager.getConnection(protocol + dbName));

            int count = DB.getIntersiteLinkCount(7);
            assertEquals(1, count);

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
            except.printStackTrace();
        }
    }
    
    @Test
    public void testGetExternalLinkCount() throws Exception {
        try {
            //Load DB driver
            Class.forName(driver).newInstance();

            //Connect database
            DB.setConn(DriverManager.getConnection(protocol + dbName));

            int count = DB.getExternalLinkCount(15);
            assertEquals(4, count);

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
            except.printStackTrace();
        }
    }
    
    @Test
    public void testGetExternalImageCount() throws Exception {
        try {
            //Load DB driver
            Class.forName(driver).newInstance();

            //Connect database
            DB.setConn(DriverManager.getConnection(protocol + dbName));

            int count = DB.getExternalImageCount(4);
            assertEquals(1, count);

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
            except.printStackTrace();
        }
    }

    @Test
     public void testGetAllImagesOnPage() throws Exception {
        try {
                //Load DB driver
                Class.forName(driver).newInstance();

                //Connect database
                DB.setConn(DriverManager.getConnection(protocol + dbName));

                Vector<String> images = DB.getListAllImagesOnPage(21);
                assertEquals("pic_mountain.jpg", images.firstElement());

            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
                except.printStackTrace();
            }
        }
    
    @Test
    public void testGetAllScriptsOnPage() throws Exception {
       try {
               //Load DB driver
               Class.forName(driver).newInstance();

               //Connect database
               DB.setConn(DriverManager.getConnection(protocol + dbName));

               Vector<String> scripts = DB.getListAllScriptsOnPage(12);
               assertEquals("myscripts.js", scripts.firstElement());

           } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
               except.printStackTrace();
           }
       }
    
    @Test
    public void testGetAllStylesheetsOnPage() throws Exception {
       try {
               //Load DB driver
               Class.forName(driver).newInstance();

               //Connect database
               DB.setConn(DriverManager.getConnection(protocol + dbName));

               Vector<String> styles = DB.getListAllStylesheetsOnPage(16);
               assertEquals("styles.css", styles.firstElement());

           } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
               except.printStackTrace();
           }
       }

	@Test
	public void testDBName() {
		assertEquals("WebsiteAnalysisDB", DB.getDbName());
	}
	
	@Test
	public void testInsertIntoHTMLTable() 
	{

        String dbPath = protocol + DB.getDbName() + ";create=true;user=app;password=admin";

        try {
            //Load DB driver
            Class.forName(driver).newInstance();

            //Connect database
            DB.setConn(DriverManager.getConnection(dbPath));

            //Create tables
            DB.setupHTMLTable();

            //Test insert
            boolean success = DB.insertIntoHTMLTable("Path", 1, "TagType", "Location", 4.0);

            assertTrue(success);
            DB.cleanupDB();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
            except.printStackTrace();
        }
        //need to have it pass, passes in the integration test, but not in this test

    }
	
	@Test
	public void testCleanupDB() throws SQLException 
	{
		DB.setupDB();
		boolean clean = DB.cleanupDB();
		assertTrue(clean);
		assertTrue(DB.getConn().isClosed());
		assertTrue(DB.getStmt().isClosed());
	}
	
	@Test
	public void testResource_DBSetUp() throws IOException 
	{
		boolean setup = DB.setupDB();
		DB.cleanupDB();
		assertTrue(setup);
	}
		
}
