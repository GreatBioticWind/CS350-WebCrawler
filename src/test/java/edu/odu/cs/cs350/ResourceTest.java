package edu.odu.cs.cs350;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResourceTest {
	@Test
	public final void testIsValid()
	{
		assertTrue(Resource.getIsValid());
	}
	
	@Test
	public final void testConstructorStringStringStringStringStringBooleanDouble()
	{
        Resource resource = new Resource("Audrey", "Audrey", "Audrey", "Audrey", "Audrey", 4.4, "Image");
        assertEquals("Audrey", Resource.getElementTagType());
        assertEquals("Audrey", Resource.getPageDirectory());
        assertEquals("Audrey", Resource.getLocation());
        assertEquals("Audrey", Resource.getPagePath());
        assertEquals("Audrey", Resource.getElementPath());
        assertEquals(4.4, Resource.getSize(), 0);
        assertEquals("Image", Resource.getAttributeKey());
    }

	@Test
	public final void testElementTagType()
	{
		Resource resource = new Resource();
		resource.setElementTagType("Audrey");
        assertEquals("Audrey", Resource.getElementTagType());

        assertEquals(0.0, Resource.getSize(), 0);
    }
	
	@Test
	public final void testLocation()
	{
		Resource resource = new Resource();
        Resource.setLocation("location");
        assertEquals("location", Resource.getLocation());

        assertEquals(0.0, Resource.getSize(), 0);
    }
	
	@Test
	public final void testElementPath()
	{
		Resource resource = new Resource();
		resource.setElementPath("C:/Users/Katie/git/Blue8/dummy");
        assertEquals("C:/Users/Katie/git/Blue8/dummy", Resource.getElementPath());

        assertEquals(0.0, Resource.getSize(), 0);
    }
	
	@Test
	public final void testPagePath()
	{
		Resource resource = new Resource();
        Resource.setPagePath("1");
        assertEquals("1", Resource.getPagePath());

        assertEquals(0.0, Resource.getSize(), 0);
    }
	
	@Test
	public final void testPageDirectory()
	{
		Resource resource = new Resource();
        Resource.setPageDirectory("1");
        assertEquals("1", Resource.getPageDirectory());

        assertEquals(0.0, Resource.getSize(), 0);
    }

	@Test
    public final void testStripSiteRoot() throws IOException 
	{
		//Also an integration test for classes Arguments, PathMap, and Resource
		String sep = File.separator;
        String[] arg = {"." + sep + "htmlTestFiles" + sep + "test", "www.google.com", "www.wikipedia.com"};
        Arguments.argCheck(arg);
        PathMap.findWorkingDirectory();
        PathMap.walkDirectory();
        PathMap.filterPathMap(PathMap.getPathMap());
        Resource.setPagePath(PathMap.pathMap.get(0));

		String link = "http://www.google.com/page0011.html";
		String stripped = Resource.stripSiteRoot(link);
		assertEquals("page0011.html", stripped);
    }

	@Test
	public final void testClassifyTags() throws IOException
	{
		String result = "";
		String elementsToSearchFor = ("a, img, link, script, style");
		String br = "<html><body><img src=\"pic_mountain.jpg\" alt=\"Mountain View\" style=\"width:304px;height:228px;\"></body></html>";
		Document doc = Jsoup.parse(br);
        Elements element = doc.select(elementsToSearchFor);
        for (Element e: element)
		{
        	result = Resource.classifyTag(e);
    		assertEquals("img", result);
		}
	}

	@Test
	public final void testAnalyzeElementAndClassifyTags() throws IOException
	{
		//Also an integration test for classes Arguments, PathMap, and Resource
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

        File input2 = new File(imageTest);
        Document doc2 = Jsoup.parse(input2, "UTF-8");
        Elements e02 = doc2.select("img");
        String imageResource = Resource.analyzeElement(e02.first(), "null");
        assertEquals("Found an image link", imageResource);
        assertEquals("IMAGE", Resource.getElementTagType());

        File input3 = new File(scriptTest);
        Document doc3 = Jsoup.parse(input3, "UTF-8");
        Elements e03 = doc3.select("script");
        String scriptResource = Resource.analyzeElement(e03.first(), "null");
        assertEquals("Found a script", scriptResource);
        assertEquals("JAVASCRIPT", Resource.getElementTagType());

        File input4 = new File(cssTest);
        Document doc4 = Jsoup.parse(input4, "UTF-8");
        Elements e04 = doc4.select("[rel=\"stylesheet\"]");
        String cssResource = Resource.analyzeElement(e04.first(), "null");
        assertEquals("Found a stylesheet", cssResource);
    }
	
	
	@Test
	public final void testLinkContainsSiteRoots()
	{
		String sep = File.separator;
		String[] arg = {"." + sep + "htmlTestFiles" + sep + "test", "www.google.com"};
		Arguments.argCheck(arg);

		String link = "http://www.google.com/page0011.html";
		boolean pass = Resource.linkContainsSiteRoot(link);
		assertTrue(pass);
		
		String badLink = "http://www.google.com";
		boolean fail = Resource.linkContainsSiteRoot(badLink);
		assertTrue(fail);
	}
	
	@Test
	public final void testParsePageDirectory()
	{
		Resource.setPagePath("./test/AnalyzeElement/exampleExternalCss.html");
		Resource.parsePageDirectory();
		assertEquals("/home/gitlab-runner/builds/5dc73e18/0/Blue8/Blue8/./test/AnalyzeElement", Resource.getPageDirectory());
	}

}
