package edu.odu.cs.cs350;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ScriptTest {

	@Test
	public final void testConstructorElementExtractPathExtractLocation() throws IOException {
		//This is technically an integration test, but with how we set up the code, the classes are inter-dependent.
		//We wanted to walk directory once at the beginning, instead of whenever we found an element.
		
		//Initialize separator and paths
		String sep = File.separator;
		String root = "." + sep + "htmlTestFiles" + sep + "scripts";
		String[] arg = {root, "https://www.cs.odu.edu/~tkennedy/cs350/sum17/"};
		String external = (root + sep + "exampleExternalJavascript.html");
		String internal = (root + sep + "exampleInternalJavascript.html");

		Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		Resource.setPagePath(PathMap.pathMap.get(0));
		Resource.parsePageDirectory();

		File input = new File(external);
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements script = doc.select("script");
		new Script(script.first());

		assertEquals("JAVASCRIPT", Script.getElementTagType());
		assertEquals("EXTERNAL", Script.getLocation());
		assertEquals("https://www.w3schools.com/js/myScript.js", Script.getElementPath());

		File input2 = new File(internal);
		Document doc2 = Jsoup.parse(input2, "UTF-8");
		Elements script2 = doc2.select("script");
		new Script(script2.first());

		assertEquals("JAVASCRIPT", Script.getElementTagType());
		assertEquals("INTERNAL", Script.getLocation());
		assertEquals("myscripts.js", Script.getElementPath());
    }
}
