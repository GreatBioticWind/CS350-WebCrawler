package edu.odu.cs.cs350;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class LinkTest {

	@Test
	public final void testConstructorElementExtractPathExtractLocation() throws IOException {
		//This is technically an integration test, but with how we set up the code, the classes are inter-dependent.
		//We wanted to walk directory once at the beginning, instead of whenever we found an element.

		String sep = File.separator;
		String root = "." + sep + "htmlTestFiles" + sep + "link";
		String[] arg = {root, "www.google.com"};
		String intraLink = (root + sep + "intraPageLinkTest");
		String link = (root + sep + "page0012.html");

		boolean correctArgs = Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		Resource.setPagePath(PathMap.pathMap.get(0));
		Resource.parsePageDirectory();

		File input = new File(link);
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements links = doc.select("a[href]");
		new Link(links.first());

		assertEquals("LINK", Link.getElementTagType());
		assertEquals("page0001.html", Link.getElementPath());
		assertEquals("EXTERNAL", Link.getLocation());

		new Link(links.get(1));
		assertEquals("INTERSITE", Link.getLocation());
		assertEquals("page0011.html", Link.getElementPath());

		File intraInput = new File(intraLink);
		Document intraDoc = Jsoup.parse(intraInput, "UTF-8");
		Elements intraLinks = intraDoc.select("a[href]");
		new Link(intraLinks.first());

		assertEquals("INTRAPAGE", Link.getLocation());
		assertEquals("#news", Link.getElementPath());
    }
}
