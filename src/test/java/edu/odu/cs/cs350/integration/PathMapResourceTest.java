package edu.odu.cs.cs350.integration;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import edu.odu.cs.cs350.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class PathMapResourceTest {

	@Test
	public void test_PathMap_Resource_More() throws IOException {
		//integration test that tests how the classes arg, PathMap, Resource, Link, Script, Image
		String sep = File.separator;
        String root = "." + sep + "htmlTestFiles";
        String[] arg = {root, "www.google.com"};
        String anchorTest = root + sep + "test" + sep + "AnalyzeElement"+ sep + "exampleAnchorLink.html";
        String imageTest = root + sep + "test" + sep + "AnalyzeElement" + sep + "exampleImage.html";
        String scriptTest = root + sep + "scripts" + sep + "exampleExternalJavascript.html";
        String cssTest = root + sep + "test" + sep + "AnalyzeElement" + sep + "exampleExternalCss.html";

		Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		Resource.setPagePath(PathMap.pathMap.get(0));

        File input1 = new File(anchorTest);
        Document doc1 = Jsoup.parse(input1, "UTF-8");
        Elements e01 = doc1.select("a[href]");
        String linkAResource = Resource.analyzeElement(e01.first(), "null");
        assertEquals("Found an anchor link", linkAResource);
        assertEquals("LINK", Resource.getElementTagType());
        
        //checks that Link class fills in the correct variables
        assertEquals("page0001.html", Resource.getElementPath());
        assertEquals("EXTERNAL", Resource.getLocation());
        assertEquals("href", Resource.getAttributeKey());
        
        File input2 = new File(imageTest);
        Document doc2 = Jsoup.parse(input2, "UTF-8");
        Elements e02 = doc2.select("img");
        String imageResource = Resource.analyzeElement(e02.first(), "null");
        assertEquals("Found an image link", imageResource);
        assertEquals("IMAGE", Resource.getElementTagType());
        
        //checks that Image class fills in the correct variables
        assertEquals("pic_mountain.jpg", Resource.getElementPath());
        assertEquals("EXTERNAL", Resource.getLocation());
        assertEquals("src", Resource.getAttributeKey());
        

        File input3 = new File(scriptTest);
        Document doc3 = Jsoup.parse(input3, "UTF-8");
        Elements e03 = doc3.select("script");
        String scriptResource = Resource.analyzeElement(e03.first(), "null");
        assertEquals("Found a script", scriptResource);
        assertEquals("JAVASCRIPT", Resource.getElementTagType());
        
        //checks that script class fills in the correct variables
        assertEquals("https://www.w3schools.com/js/myScript.js", Resource.getElementPath());
        assertEquals("EXTERNAL", Resource.getLocation());
        assertEquals("src", Resource.getAttributeKey());

        File input4 = new File(cssTest);
        Document doc4 = Jsoup.parse(input4, "UTF-8");
        Elements e04 = doc4.select("[rel=\"stylesheet\"]");
        String cssResource = Resource.analyzeElement(e04.first(), "null");
        assertEquals("Found a stylesheet", cssResource);
        
        //checks that css class fills in the correct variables
        assertEquals("styles.css", Resource.getElementPath());
        assertEquals("EXTERNAL", Resource.getLocation());
        assertEquals("href", Resource.getAttributeKey());
    }
	
	@Test
	public void test_PathMap_Resource_Link() throws IOException {
		//integration test that tests how the classes arg, PathMap, Resource, Link, Script, Image
		String sep = File.separator;
        String root = "." + sep + "htmlTestFiles";
        String[] arg = {root, "www.google.com"};
        String anchorTest = root + sep + "test" + sep + "AnalyzeElement"+ sep + "exampleAnchorLink.html";
        String imageTest = root + sep + "test" + sep + "AnalyzeElement" + sep + "exampleImage.html";
        String scriptTest = root + sep + "scripts" + sep + "exampleExternalJavascript.html";
        String cssTest = root + sep + "test" + sep + "AnalyzeElement" + sep + "exampleExternalCss.html";

		Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		Resource.setPagePath(PathMap.pathMap.get(0));

        File input1 = new File(anchorTest);
        Document doc1 = Jsoup.parse(input1, "UTF-8");
        Elements e01 = doc1.select("a[href]");
        String linkAResource = Resource.analyzeElement(e01.first(), "null");
        assertEquals("Found an anchor link", linkAResource);
        assertEquals("LINK", Resource.getElementTagType());
	}

}
