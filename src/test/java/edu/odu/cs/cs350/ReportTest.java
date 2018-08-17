package edu.odu.cs.cs350;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.omg.CORBA.portable.InputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.simple.JSONObject;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Vector;

import javax.xml.crypto.Data;

public class ReportTest {
	@Test
	public void testPrintFilenames()
	{
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		Report.printFilenames();
		String expectedOutput  = "JSON Filename: test.json%n" + "Text Filename: test.txt%n";
		expectedOutput = String.format(expectedOutput);
		String out = outContent.toString().trim();
		assertEquals(expectedOutput.trim(), out.trim());
	}
	
	@Test
	public void testPrintJSON()  throws IOException
	{
		Report.cleanJSON();
		
		String output = "";
		String sep = File.separator;
		Data data;
		//testTest.json was generated by the program previously
		BufferedReader br = new BufferedReader(new FileReader("reportTestExamples" + sep+ "testTest.json"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    output = everything;
		} finally {
		    br.close();
		}
		
		//had to add the JSON text quirks after, but the data I was expecting did not change after the function was implemented, just the format
		String expectedFileOutput =
			  "{%n" +
			  "  \"Number Of Pages on which it is displayed\": 1,%n" +
			  "  \"Listing of images\": [%n" +
		      "    \"." + sep + "htmlTestFiles" + sep +  "image" + sep +  "exampleExternalImage.html\"%n" +
			  "  ],%n" +
			  "  \"Image\": \"https://www.themeshnews.com/wp-content/uploads/2016/03/Top-10-Most-Beautiful-Countries-in-the-World-2016-USA.jpg\"%n" +
			  "}%n" +
			  "{%n" +
			  "  \"Number Of Pages on which it is displayed\": 1,%n" +
			  "  \"Listing of images\": [%n" +
		      "    \"." + sep + "htmlTestFiles" + sep + "image" + sep + "exampleImage.html\"%n" +
		      "  ],%n" +
		      "  \"Image\": \"pic_mountain.jpg\"%n" +
			  "}%n" +
        	  "{%n" +
			  "  \"Number of intra-page links\": 0,%n" +
			  "  \"Listing of scripts\": [],%n" +
			  "  \"Page \": \"." + sep + "htmlTestFiles" + sep + "image" + sep + "exampleExternalImage.html\",%n" +
			  "  \"Number of local images\": 0,%n" +
			  "  \"Listing of images\": [%n" +
			  "    \"https://www.themeshnews.com/wp-content/uploads/2016/03/Top-10-Most-Beautiful-Countries-in-the-World-2016-USA.jpg\"%n" +
			  "  ],%n" +
			  "  \"Listing of stylesheets\": [],%n" +
			  "  \"Number of external images\": 1,%n" +
			  "  \"Number of external links\": 0,%n" +
			  "  \"Number of inter-site links\": 0,%n" +
			  "  \"Number of scripts referenced\": 0,%n" +
			  "  \"Number of stylesheets utilized\": 0%n" +
			  "}%n" +
			  "{%n" +
			  "  \"Number of intra-page links\": 0,%n" +
			  "  \"Listing of scripts\": [],%n" +
			  "  \"Page \": \"." + sep + "htmlTestFiles" + sep + "image" + sep + "exampleImage.html\",%n" +
			  "  \"Number of local images\": 1,%n" +
			  "  \"Listing of images\": [%n" +
			  "    \"pic_mountain.jpg\"%n" +
			  "  ],%n" +
			  "  \"Listing of stylesheets\": [],%n" +
			  "  \"Number of external images\": 0,%n" +
			  "  \"Number of external links\": 0,%n" +
			  "  \"Number of inter-site links\": 0,%n" +
			  "  \"Number of scripts referenced\": 0,%n" +
			  "  \"Number of stylesheets utilized\": 0%n"+
			"}%n";
		
		expectedFileOutput = String.format(expectedFileOutput);
        assertEquals(expectedFileOutput, output);
	}

	@Test
	public void testPrintText() throws IOException 
	{
	    Report.cleanText();		
	    String sep = File.separator;
		Vector<String> names = new Vector();
		names.add("."+ sep + "htmlTestFiles" + sep + "image" + sep + "exampleExternalImage.html");
		names.add("." + sep + "htmlTestFiles" + sep + "image" + sep + "exampleImage.html");
			
		Report.printText(names.get(0), 0.0, 0.0, false);
		Report.printText(names.get(1), 0.07216644287109375, 0.07, true);
		
		String output = "";
		//testTest.txt was generated by the program previously
		BufferedReader br = new BufferedReader(new FileReader("reportTestExamples" + sep + "testTest.txt"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    output = everything;
		} finally {
		    br.close();
		}

		
		String expectedFileOutput =
        		"0.00 " + "." + sep +"htmlTestFiles" + sep + "image" + sep +"exampleExternalImage.html%n" +
        		"0.07 " + "." + sep +"htmlTestFiles" + sep +"image" + sep +"exampleImage.html%n" +
        		"0.07 ";

		expectedFileOutput = String.format(expectedFileOutput);
        assertEquals(expectedFileOutput.trim(), output.trim());
	}
	
	@Test
	public void testCleanText() throws IOException 
	{
		String sep = File.separator;
		Vector<String> names = new Vector();
		names.add("."+ sep + "htmlTestFiles" + sep + "image" + sep + "exampleExternalImage.html");
		names.add("." + sep + "htmlTestFiles" + sep + "image" + sep + "exampleImage.html");
		String output = "test";
		
		Report.printText(names.get(0), 0.0, 0.0, false);
		Report.printText(names.get(1), 0.07216644287109375, 0.07, true);
		boolean isClean = Report.cleanText();
		assertTrue(isClean);
		BufferedReader br = new BufferedReader(new FileReader("test.txt"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    output = everything;
		} finally {
		    br.close();
		}
		assertEquals("", output);
	}
	
	@Test
	public void testCleanJSON() throws IOException 
	{
		
		String sep = File.separator;
		String output = "test";
		JSONObject pics = new JSONObject();
		pics.put("Number Of Pages on which it is displayed ", 2);
		
		Report.printJSON(pics);
		boolean isClean = Report.cleanJSON();
		assertTrue(isClean);
		BufferedReader br = new BufferedReader(new FileReader("test.json"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    output = everything;
		} finally {
		    br.close();
		}
		assertEquals("", output);
	}

}