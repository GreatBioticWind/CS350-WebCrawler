package edu.odu.cs.cs350;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class CSSTest
{
	@Test
	public final void testConstructorElement() throws IOException {
		//This is technically an integration test, but with how we set up the code, the classes are inter-dependent.
		//We wanted to walk directory once at the beginning, instead of whenever we found an element.
		
		//Initialize separator and paths
		String sep = File.separator;
		String root = "." + sep + "htmlTestFiles" + sep + "css";
		String[] arg = {root, "https://www.cs.odu.edu/~tkennedy/cs350/sum17/"};
		String toInternalCSS = (root + sep + "exampleInternalCss.html");
		String toExternalCSS = (root + sep + "exampleExternalCss.html");

		//Setup Directory
		Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		Resource.setPagePath(PathMap.pathMap.get(0));
		Resource.parsePageDirectory();

		//Parse internal CSS test file
		File input = new File(toInternalCSS);
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements css = doc.select("[rel=\"stylesheet\"]");
		new CSS(css.first());

		assertEquals("STYLESHEET", CSS.getElementTagType());
		assertEquals("styles.css", CSS.getElementPath());
		assertEquals("INTERNAL", CSS.getLocation());

		//Parse external css test file
		File input2 = new File(toExternalCSS);
		Document doc2 = Jsoup.parse(input2, "UTF-8");
		Elements css2 = doc2.select("[rel=\"stylesheet\"]");
		new CSS(css2.first());

		assertEquals("STYLESHEET", CSS.getElementTagType());
		assertEquals("https://www.w3schools.com/html/styles.css", CSS.getElementPath());
		assertEquals("EXTERNAL", CSS.getLocation());
	}
}