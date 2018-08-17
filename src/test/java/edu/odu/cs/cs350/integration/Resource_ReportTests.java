package edu.odu.cs.cs350.integration;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import edu.odu.cs.cs350.Arguments;
import edu.odu.cs.cs350.DB;
import edu.odu.cs.cs350.HTMLParser;
import edu.odu.cs.cs350.PathMap;
import edu.odu.cs.cs350.Report;
import edu.odu.cs.cs350.Resource;

public class Resource_ReportTests {

	@Test
	public void testPrintText() throws IOException 
	{
	    Report.cleanText();
		String sep = File.separator;
	    String root = "." + sep + "htmlTestFiles" + sep + "image";
	    String[] arg = {root, "www.google.com"};
	    Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		DB.setupDB();
		
		int pageCounter = 0;
	    int dirSize = PathMap.pathMap.size();
	
	    while (pageCounter < dirSize) 
	    {
			Resource.setPagePath(PathMap.pathMap.get(pageCounter));
			Resource.parsePageDirectory();
			Elements elements = HTMLParser.parsePage(pageCounter);
	        for (Element e: elements) 
	        {
		        Resource.analyzeElement(e, Resource.getPagePath());
		        boolean pass = DB.insertIntoHTMLTable(
		                Resource.getElementPath(), pageCounter+1,
		                Resource.getElementTagType(),
		                Resource.getLocation(),
		                Resource.getSize());
		        assertTrue(pass);
	        }
	        ++pageCounter;
	    }
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		Vector<String> names = new Vector();
		names.add("."+ sep + "htmlTestFiles" + sep + "image" + sep + "exampleExternalImage.html");
		names.add("." + sep + "htmlTestFiles" + sep + "image" + sep + "exampleImage.html");
			
		Report.printText(names.get(0), 0.0, 0.0, false);
		Report.printText(names.get(1), 0.07216644287109375, 0.07, true);
		String expectedOutput  = "Text File Name: test.txt\n";
		String out = outContent.toString();
		//assertEquals(expectedOutput.trim(), out.trim());
		
		String output = "";
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
		    DB.cleanupDB();
		}

		String expectedFileOutput =
        		"0.00 " + "." + sep +"htmlTestFiles" + sep + "image" + sep +"exampleExternalImage.html%n" +
        		"0.07 " + "." + sep +"htmlTestFiles" + sep +"image" + sep +"exampleImage.html%n" +
        		"0.07 ";
        		
        DB.cleanupDB();

		expectedFileOutput = String.format(expectedFileOutput);
        assertEquals(expectedFileOutput.trim(), output.trim());
        
        DB.cleanupDB();
	}
	
	@Test
	public void testPrintJSON()  throws IOException
	{
		Report.cleanJSON();
		String sep = File.separator;
	    String root = "." + sep + "htmlTestFiles" + sep + "image";
	    String[] arg = {root, "www.google.com"};
	    Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		DB.setupDB();
		
        int idCounter = 1;
        int pageCounter = 0;
        int dirSize = PathMap.pathMap.size();
        Vector<String> pageNames = new Vector<String>();
        Vector<Double> pageSize = new Vector<>();
        double imgTotalSize=0.00;
        HashMap<String, ArrayList<String>> picPages = new HashMap<String, ArrayList<String>>();
        Vector<String> pageKey = new Vector<String>();
        while (pageCounter < dirSize) 
        {
        	double imgSize=0.00;
            Resource.setPagePath(PathMap.pathMap.get(pageCounter));
            pageNames.add(Resource.getPagePath());
            Resource.parsePageDirectory();
            Elements elements = HTMLParser.parsePage(pageCounter);
            for (Element e : elements) {
                //Classifies the element and analyze it.
                Resource.analyzeElement(e, Resource.getPagePath());
                
                if (Resource.getIsValid()) {
                    DB.insertIntoHTMLTable(
                            Resource.getElementPath(),
                            pageCounter + 1,
                            Resource.getElementTagType(),
                            Resource.getLocation(),
                            Resource.getSize());
                    if(Resource.getElementTagType()=="IMAGE") {
                    	ArrayList<String> list = new ArrayList<String>();
                    	pageKey.add(Resource.getElementPath());
                    	if(picPages.containsKey(Resource.getElementPath())){
                    	    
                    	    list = picPages.get(Resource.getElementPath());
                    	    list.add(Resource.getPagePath());
                    	} else {
                    	    list = new ArrayList<String>();
                    	    list.add(Resource.getPagePath());
                    	    picPages.put(Resource.getElementPath(), list);
                    	}
                    }
                    	
                    imgSize=imgSize+Resource.getSize();
                    imgTotalSize=imgTotalSize+Resource.getSize();
                    ++idCounter;
                }
            }
            pageSize.add(imgSize);
            ++pageCounter;
        }
        Map<String,Double> textData = new TreeMap<String, Double>();
        Vector<String> pageNames2 = new Vector<String>();
        for (int p =0;p<pageNames.size();p++)
        {
        textData.put(pageNames.get(p),pageSize.get(p));
        pageNames2.add(pageNames.get(p));
        }
        Collections.sort(pageNames);
        Report.cleanJSON();
        Report.cleanText();
        DB.testDBQueries(pageCounter);
        Vector<String> jsonObjects = new Vector<String>();
        JSONObject pics = new JSONObject();
        Vector<String> pageKeyDuplicate = new Vector<String>();
        pageKeyDuplicate.addAll(pageKey);
        Set<String> pageKeyUnique = new HashSet<>();
        pageKeyUnique.addAll(pageKey);
        pageKey.clear();
        pageKey.addAll(pageKeyUnique);
        for (int l=0; l<pageKey.size();l++){
        	JSONArray jsonpics = new JSONArray();
        	int pageCount=0;
        	for(int m=0;m<pageKeyDuplicate.size();m++) {
        		if(pageKeyDuplicate.get(m).matches(pageKey.get(l))) {
        			pageCount=pageCount+1;
        		}
        	}
        	pics.put("Image", pageKey.get(l));
        	pics.put("Number Of Pages on which it is displayed", pageCount);
        	pics.put("Listing of images", picPages.get(pageKey.get(l)));
        	Report.printJSON(pics);
        }
        for (int i =0; i < pageCounter; i++) {
        	int countImageInternal = DB.getLocalImageCount(i+1);
        	int countImageExternal = DB.getExternalImageCount(i+1);
        	int countScript = DB.getScriptCount(i+1);
        	int countSS = DB.getStylesheetCount(i+1);
        	int countLinkIntraPage = DB.getIntrapageLinkCount(i+1);
        	int countLinkInterSite = DB.getIntersiteLinkCount(i+1);
        	int countLinkExternal = DB.getExternalLinkCount(i+1);
        	Vector<String> listScripts= DB.getListAllScriptsOnPage(i+1);
        	Vector<String> listSS=  DB.getListAllStylesheetsOnPage(i+1);
        	Vector<String> listImage = DB.getListAllImagesOnPage(i+1);
        	JSONObject obj = new JSONObject();
        	JSONArray jsonListImage = new JSONArray();
        	JSONArray jsonListScripts = new JSONArray();
        	JSONArray jsonListSS = new JSONArray();
        	obj.put("Page ", pageNames2.get(i));
        	obj.put("Number of local images", countImageInternal);
        	obj.put("Number of external images", countImageExternal);
        	obj.put("Number of scripts referenced", countScript);
        	obj.put("Number of stylesheets utilized", countSS);
        	for (int p=0;p < listImage.size();p++) 
        		jsonListImage.add(listImage.get(p));
        	for (int p=0;p < listScripts.size();p++)
        		jsonListScripts.add(listScripts.get(p));
        	for (int p=0;p < listSS.size();p++)
        		jsonListSS.add(listSS.get(p));
        	obj.put("Listing of images", jsonListImage);
        	obj.put("Listing of scripts", jsonListScripts);
        	obj.put("Listing of stylesheets", jsonListSS);
        	obj.put("Number of intra-page links", countLinkIntraPage);
        	obj.put("Number of inter-site links", countLinkInterSite);
        	obj.put("Number of external links", countLinkExternal);
       
        	Report.printJSON(obj);   
        }
        
		String output = "";
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
		    DB.cleanupDB();
		}
		
		//used so expectedFileOutput changes with each operating system
		String path = pageNames2.get(0);
		boolean forwardSlash = path.contains("/");
		
		String expectedFileOutput = "";
		
		if(forwardSlash == true)
		{
			expectedFileOutput =
					"{%n" +
					"  \"Number Of Pages on which it is displayed\": 1,%n" +
					"  \"Listing of images\": [%n" +
					"    \"." + sep + "htmlTestFiles" + sep + "image" + sep + "exampleExternalImage.html\"%n" +						"  ],%n" +
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
		}
		
		else if(forwardSlash == false)
		{
			expectedFileOutput =
				"{%n" +
				"  \"Number Of Pages on which it is displayed\": 1,%n" +
				"  \"Listing of images\": [%n" +
				"    \"." + sep + sep + "htmlTestFiles" + sep + sep + "image" + sep + sep + "exampleExternalImage.html\"%n" +
				"  ],%n" +
				"  \"Image\": \"https://www.themeshnews.com/wp-content/uploads/2016/03/Top-10-Most-Beautiful-Countries-in-the-World-2016-USA.jpg\"%n" +
				"}%n" +
				"{%n" +
				"  \"Number Of Pages on which it is displayed\": 1,%n" +
				"  \"Listing of images\": [%n" +
				"    \"." + sep + sep + "htmlTestFiles" + sep + sep + "image" + sep + sep + "exampleImage.html\"%n" +
				"  ],%n" +
				"  \"Image\": \"pic_mountain.jpg\"%n" +
				"}%n" +	
        		"{%n" +
        		"  \"Number of intra-page links\": 0,%n" +
        		"  \"Listing of scripts\": [],%n" +
        		"  \"Page \": \"." + sep + sep + "htmlTestFiles" + sep + sep + "image" + sep + sep + "exampleExternalImage.html\",%n" +
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
        		"  \"Page \": \"." + sep + sep + "htmlTestFiles" + sep + sep + "image" + sep + sep + "exampleImage.html\",%n" +
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
		}


        DB.cleanupDB();
        
        expectedFileOutput = String.format(expectedFileOutput);
        assertEquals(expectedFileOutput, output);
        DB.cleanupDB();
	}
	
}
