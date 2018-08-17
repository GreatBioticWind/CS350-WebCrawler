package edu.odu.cs.cs350.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import edu.odu.cs.cs350.Arguments;
import edu.odu.cs.cs350.HTMLParser;
import edu.odu.cs.cs350.PathMap;
import edu.odu.cs.cs350.Resource;

public class PathMapHTMLParserTest {

	@Test
	public void testargCheck_Directory_ParsePage() throws IOException {
		String sep = File.separator;
		String[] arg = {"." + sep + "htmlTestFiles" + sep + "image", "www.google.com"};
		boolean correctArgs = Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		Resource.setPagePath(PathMap.pathMap.get(0));
		int counter = 0;
		Elements elements = HTMLParser.parsePage(0);
		Vector<String> result = new Vector();
		Vector<String> correct = new Vector();
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
		assertEquals(correct.firstElement(), result.firstElement());
		assertEquals(correct.elementAt(1).trim(), result.elementAt(1).trim());
		
	}
	
	@Test
	public void testPathMap_hasHMTL() throws IOException
	{
		String sep = File.separator;
		String[] arg = {"." + sep + "htmlTestFiles" + sep + "scripts", "www.google.com"};
		boolean correctArgs = Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		List<String> result = new Vector<>();
		result.add("." + sep + "htmlTestFiles" + sep + "scripts" + sep + "exampleExternalJavascript.html");
		result.add("." + sep + "htmlTestFiles" + sep + "scripts" + sep + "exampleJavascript.html");
		result.add("." + sep + "htmlTestFiles" + sep + "scripts" + sep + "exampleInternalJavascript.html");
		assertEquals(result, PathMap.getPathMap());
	}

}
