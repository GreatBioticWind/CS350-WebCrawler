package edu.odu.cs.cs350;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.junit.Test;

public class ArgumentsTest {
	

	@Test
	public void argCheckTest() throws NullPointerException, IOException{
		String sep = File.separator;
        String root = "." + sep + "htmlTestFiles" + sep + "test";
        String[] empty = {};
		String[] one= {root};
		String[] correct = {root, "https://www.google.com"};
		String[] correctNoHome = {root, "www.google.com"};
		String[] bothURL = {"www.google.com", "www.google.com"};
        String[] notDirectory = {root + sep + "notADirectory", "www.google.com"};
        boolean isEmpty = Arguments.argCheck(empty);
		boolean isOne = Arguments.argCheck(one);
		boolean isCorrect = Arguments.argCheck(correct);
		boolean isCorrectNoHome = Arguments.argCheck(correctNoHome);
		boolean isBothURL = Arguments.argCheck(bothURL);
		boolean isNotDirectory = Arguments.argCheck(notDirectory);
		assertFalse(isEmpty);
		assertFalse(isOne);
		assertFalse(isNotDirectory);
		assertFalse(isBothURL);
		assertTrue(isCorrect);
		assertTrue(isCorrectNoHome);
	}
	
	@Test
	public void urlsTest()  throws IOException{
		String sep = File.separator;
		Vector<String> temp = new Vector<>();
		Vector<String> result = new Vector<>();
		temp.add("." + sep + "html" + sep + "indexPage.html");
		temp.add("." + sep + "html" + sep + "page0001.html");
		temp.add("." + sep + "html" + sep + "page0002.html");
		result.add("." + sep + "html" + sep + "indexPage.html");
		result.add("." + sep + "html" + sep + "page0001.html");
		result.add("." + sep + "html" + sep + "page0002.html");
		
		Arguments.setUrls(temp);
		assertEquals("." + sep + "html" + sep + "page0002.html", Arguments.getURL(2));
		assertEquals(result, Arguments.getUrls());
	}
	
	@Test
	public void directoryTest() {
		Arguments.setDirectory("home");
		assertEquals("home", Arguments.getDirectory());
	}
	
	@Test
	public void urlSizeTest() {
		Vector<String> temp = new Vector<>();
		temp.add("There will be cake there");
		temp.add("Guests love both cake and Haiku");
		temp.add("The cake is a lie");
		Arguments.setUrls(temp);
		assertEquals(3, Arguments.urlSize());
	}
	@Test
	public void arg0Test() {
		Arguments.setArg0("home");
		assertEquals("home", Arguments.getArg0());
	}

}
