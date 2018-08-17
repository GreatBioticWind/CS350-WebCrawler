package edu.odu.cs.cs350;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class ImageTest
{
	@Test
	public final void testConstructorElement_ExtractSize_ExtractLocation() throws IOException {
		//This is technically an integration test, but with how we set up the code, the classes are inter-dependent.
		//We wanted to walk directory once at the beginning, instead of whenever we found an element.
		
		//Initialize separator and paths
		String sep = File.separator;
		String root = "." + sep + "htmlTestFiles" + sep + "image";
		String[] arg = {root, "https://www.cs.odu.edu/~tkennedy/cs350/sum17/"};
		String external = (root + sep + "exampleExternalImage.html");
		String internal = (root + sep + "exampleImage.html");

		Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		Resource.setPagePath(PathMap.pathMap.get(0));
		Resource.parsePageDirectory();

		//Parse external image test file
		File input = new File(external);
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements img = doc.select("img");
		new Image(img.first());

		assertEquals("IMAGE", Image.getElementTagType());
		assertEquals("EXTERNAL", Image.getLocation());
		assertEquals("https://www.themeshnews.com/wp-content/uploads/2016/03/Top-10-Most-Beautiful-Countries-in-the-World-2016-USA.jpg", Image.getElementPath());

		//Parse internal image test file
		File input2 = new File(internal);
		Document doc2 = Jsoup.parse(input2, "UTF-8");
		Elements img2 = doc2.select("img");
		new Image(img2.first());

		assertEquals("IMAGE", Image.getElementTagType());
		assertEquals("INTERNAL", Image.getLocation());
		assertEquals(0.07216644287109375, Image.getSize(), 0);//(75672 bytes)
		assertEquals("pic_mountain.jpg", Image.getElementPath());

	}
}
