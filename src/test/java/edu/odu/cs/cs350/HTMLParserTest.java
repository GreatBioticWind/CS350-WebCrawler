package edu.odu.cs.cs350;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Vector;

public class HTMLParserTest {

	@Test
	public final void testHasHTML() throws IOException
	{
		String urlFail = "text";
		String urlPass = "./htmlTestFiles/css/exampleCss.html";
		boolean fail = HTMLParser.hasHTML(urlFail);
		boolean pass = HTMLParser.hasHTML(urlPass);
		assertFalse(HTMLParser.hasHTML(urlFail));
		assertTrue(HTMLParser.hasHTML(urlPass));
	}

	@Test
    //Extracts all HTML tags from a given page
    public final void testParsePage() throws IOException {
		//This is technically an integration test, but with how we set up the code, the classes are inter-dependent.
		//We wanted to walk directory once at the beginning, instead of whenever we found an element.
		
		String[] arg = {"./htmlTestFiles/test", "www.google.com"};
		boolean correctArgs = Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		Resource.setPagePath(PathMap.pathMap.get(0));
		int counter = 0;
		Elements elements = HTMLParser.parsePage(0);
		Vector<String> result = new Vector<String>();
		Vector<String> correct = new Vector<String>();
		String img = "";
		String script = "";
		correct.add("<img src=\"https://www.themeshnews.com/wp-content/uploads/2016/03/Top-10-Most-Beautiful-Countries-in-the-World-2016-USA.jpg\" alt=\"HTML5 Icon\" style=\"width:128px;height:128px;\">");
		correct.add("<img src=\"pic_mountain.jpg\" alt=\"Mountain View\" style=\"width:304px;height:228px;\">");

		while(counter<2)
		{
			elements = HTMLParser.parsePage(counter);
			System.out.println("Elements Parse: ");
			System.out.println(elements);
			for(Element e: elements)
			{
				result.add(e.toString().trim());
			}
			counter++;
		}
		assertEquals(correct.elementAt(0), result.elementAt(0));
		assertEquals(correct.elementAt(1).trim(), result.elementAt(1).trim());

    }

}
