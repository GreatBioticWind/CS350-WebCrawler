package edu.odu.cs.cs350.integration;

import edu.odu.cs.cs350.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DB_ResourceTest {

	/*Test for a function that is never used, so it is not needed
	@Test
	public void testResource_DBPrint() throws IOException 
	{
		String sep = File.separator;
        String root = "." + sep + "htmlTestFiles" + sep + "image";
        String[] arg = {root, "www.google.com"};
        Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		DB.setupDB();
		
		int pageCounter = 0;
	    int dirSize = PathMap.pathMap.size();

	    while (pageCounter < dirSize) 
	    {
			Resource.setPagePath(PathMap.pathMap.get(pageCounter));
			Resource.parsePageDirectory();
			Elements elements = HTMLParser.parsePage(pageCounter);
	
	        //Analyze each element in the list.
	        for (Element e: elements) 
	        {
		        Resource.analyzeElement(e, Resource.getPagePath());
		        boolean pass = DB.insertIntoHTMLTable(
		                Resource.getElementPath(), pageCounter+1,
		                Resource.getElementTagType(),
		                Resource.getLocation(),
		                Resource.getSize());
	        }
	        ++pageCounter;
	    }
    
        DB.cleanupDB();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		DB.printTable();
		String expectedOutput  = 
		"Printing table\n\n"+
		"-------------------------------------------------------------------------------------------------------------------------------------------------------\n"+
		"|    ID                  |    DIR                 |    PAGENUM             |    TAGTYPE             |    DESTINATION         |    ISIZE               |\n"+
		"-------------------------------------------------------------------------------------------------------------------------------------------------------\n"+
		"|  1                     |  https://www.themeshn  |  1                     |  IMAGE                 |  EXTERNAL              |  0.00                  |\n"+
		"|  2                     |  pic_mountain.jpg      |  2                     |  IMAGE                 |  INTERNAL              |  0.07                  |\n"+
		"-------------------------------------------------------------------------------------------------------------------------------------------------------";
		String out = outContent.toString();
		assertEquals(expectedOutput.trim(), out.trim());
		
		//needed
		DB.cleanupDB();
	}*/
	
	@Test
	public void testResource_DBQueries() throws IOException 
	{
		String sep = File.separator;
	    String root = "." + sep + "htmlTestFiles" + sep + "image";
	    String[] arg = {root, "www.google.com"};
	    Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		DB.setupDB();
		
		int pageCounter = 0;
	    int dirSize = PathMap.pathMap.size();
	
	    while (pageCounter < dirSize) 
	    {
			Resource.setPagePath(PathMap.pathMap.get(pageCounter));
			Resource.parsePageDirectory();
			Elements elements = HTMLParser.parsePage(pageCounter);
	
	        //Analyze each element in the list.
	        for (Element e: elements) 
	        {
		        Resource.analyzeElement(e, Resource.getPagePath());
		        boolean pass = DB.insertIntoHTMLTable(
		                Resource.getElementPath(), pageCounter+1,
		                Resource.getElementTagType(),
		                Resource.getLocation(),
		                Resource.getSize());
		        assertTrue(pass);
	        }
	        ++pageCounter;
	    }
        DB.printTable();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        DB.testDBQueries(pageCounter);
        
        int internalImageCount1 = DB.getLocalImageCount(1);
        assertEquals(0, internalImageCount1);
        int externalImageCount1 = DB.getExternalImageCount(1);
        assertEquals(1, externalImageCount1);
        int scripts1 = DB.getScriptCount(1);
        assertEquals(0, scripts1);
        int intraPageLink1 = DB.getIntrapageLinkCount(1);
        assertEquals(0, intraPageLink1);
        int interSiteLink1 = DB.getIntersiteLinkCount(1);
        assertEquals(0, interSiteLink1);
        int externalLink1 = DB.getExternalLinkCount(1);
        assertEquals(0, externalLink1);
        
        int internalImageCount2 = DB.getLocalImageCount(2);
        assertEquals(1, internalImageCount2);
        int externalImageCount2 = DB.getExternalImageCount(2);
        assertEquals(0, externalImageCount2);
        int scripts2 = DB.getScriptCount(2);
        assertEquals(0, scripts2);
        int intraPageLink2 = DB.getIntrapageLinkCount(2);
        assertEquals(0, intraPageLink2);
        int interSiteLink2 = DB.getIntersiteLinkCount(2);
        assertEquals(0, interSiteLink2);
        int externalLink2 = DB.getExternalLinkCount(2);
        assertEquals(0, externalLink2);
        
        List<String> images = DB.getListAllImagesOnPage(1);
        assertEquals("https://www.themeshnews.com/wp-content/uploads/2016/03/Top-10-Most-Beautiful-Countries-in-the-World-2016-USA.jpg", images.get(0));

        List<String> stylesheets = DB.getListAllStylesheetsOnPage(1);
        assertEquals("[]", stylesheets.toString());
        List<String> stylesheets2 = DB.getListAllStylesheetsOnPage(2);
        assertEquals("[]", stylesheets.toString());
        
        List<String> scripts = DB.getListAllStylesheetsOnPage(1);
        assertEquals("[]", scripts.toString());
        List<String> scriptsList2 = DB.getListAllStylesheetsOnPage(2);
        assertEquals("[]", scriptsList2.toString());
        /*String expectedOutput =
                "On page 1:%n" +
                        "\tNumber of internal images: 0%n" +
                        "\tNumber of external images: 1%n" +
                        "\t\thttps://www.themeshnews.com/wp-content/uploads/2016/03/Top-10-Most-Beautiful-Countries-in-the-World-2016-USA.jpg%n" +
                        "\tNumber of scripts referenced: 0%n" +
                        "\tNumber of stylesheets utilized: 0%n" +
                        "\tNumber of intra-page links: 0%n" +
                        "\tNumber of inter-site links: 0%n" +
                        "\tNumber of external links: 0%n%n" +

                        "On page 2:%n" +
                        "\tNumber of internal images: 1%n" +
                        "\tNumber of external images: 0%n" +
                        "\t\tpic_mountain.jpg%n" +
                        "\tNumber of scripts referenced: 0%n" +
                        "\tNumber of stylesheets utilized: 0%n" +
                        "\tNumber of intra-page links: 0%n" +
                        "\tNumber of inter-site links: 0%n" +
                        "\tNumber of external links: 0";

        expectedOutput = String.format(expectedOutput);

		String out = outContent.toString();
        assertEquals(expectedOutput.trim(), out.trim());*/

	    DB.cleanupDB();
	}
	
	
	@Test
	public void testResource_InsertIntoDBTable() throws IOException 
	{
		String sep = File.separator;
	    String root = "." + sep + "htmlTestFiles" + sep + "image";
	    String[] arg = {root, "www.google.com"};
	    Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		DB.setupDB();
		
		int pageCounter = 0;
	    int dirSize = PathMap.pathMap.size();
	
	    while (pageCounter < dirSize) 
	    {
			Resource.setPagePath(PathMap.pathMap.get(pageCounter));
			Resource.parsePageDirectory();
			Elements elements = HTMLParser.parsePage(pageCounter);
	
	        //Analyze each element in the list.
	        for (Element e: elements) 
	        {
		        Resource.analyzeElement(e, Resource.getPagePath());
		        boolean pass = DB.insertIntoHTMLTable(
		                Resource.getElementPath(), pageCounter+1,
		                Resource.getElementTagType(),
		                Resource.getLocation(),
		                Resource.getSize());
		        DB.cleanupDB();
		        assertTrue(pass);
	        }
	        ++pageCounter;
	    }
	    
	    DB.cleanupDB();
	}
	
	@Test
	public void testResource_DBClean() throws IOException 
	{
		String sep = File.separator;
        String root = "." + sep + "htmlTestFiles" + sep + "image";
        String[] arg = {root, "www.google.com"};
        Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		DB.setupDB();
		
		int pageCounter = 0;
	    int dirSize = PathMap.pathMap.size();

	    while (pageCounter < dirSize) 
	    {
			Resource.setPagePath(PathMap.pathMap.get(pageCounter));
			Resource.parsePageDirectory();
			Elements elements = HTMLParser.parsePage(pageCounter);
	
	        //Analyze each element in the list.
	        for (Element e: elements) 
	        {
		        Resource.analyzeElement(e, Resource.getPagePath());
		        boolean pass = DB.insertIntoHTMLTable(
		                Resource.getElementPath(), pageCounter+1,
		                Resource.getElementTagType(),
		                Resource.getLocation(),
		                Resource.getSize());
	        }
	        ++pageCounter;
	    }
    
	   boolean clean = DB.cleanupDB();
	   assertTrue(clean);
	}

}
