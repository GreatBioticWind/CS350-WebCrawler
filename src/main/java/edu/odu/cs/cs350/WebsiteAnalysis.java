package edu.odu.cs.cs350;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Contains the main function
 */

public class WebsiteAnalysis
{
    public static void main(String[] args) throws IOException
    {	
    	
        //Check arguments
    	boolean correctArgs = Arguments.argCheck(args);
    	if(!correctArgs)
    	{
    		System.exit(0);
    	}

        //Get working directory and target subdirectory
        PathMap.findWorkingDirectory();


        //Map the given directory
        PathMap.walkDirectory();
        
        //Check pages in directory for HTML
        boolean checkPathMapSizeIsValid = PathMap.filterPathMap(PathMap.getPathMap());
        if(!checkPathMapSizeIsValid)
        {
        	System.exit(0);
        }

        //Create database and tables
        DB.setupDB();

        //Initialize counters
        int idCounter = 1;
        int pageCounter = 0;
	    
	// This block of structures is required for the JSON report
	// TODO: Refactor this, with the report generating code below, into its own class
	double imgTotalSize=0.00;
        int dirSize = PathMap.pathMap.size();
        Vector<String> pageNames = new Vector<>();
        Vector<Double> pageSize = new Vector<>();
        HashMap<String, ArrayList<String>> picPages = new HashMap<>();
        Vector<String> pageKey = new Vector<>();

        //Analyze each page in the directory map and push results to the database.
        while (pageCounter < dirSize) {
        	double imgSize=0.00;

            //Get page path at counter position.
            Resource.setPagePath(PathMap.pathMap.get(pageCounter));

            //Add pages to a list
            pageNames.add(Resource.getPagePath());
            //System.out.println(Resource.getPagePath());
            Resource.parsePageDirectory();

            //Get all elements on page.
            Elements elements = HTMLParser.parsePage(pageCounter);

            //Analyze each element in the list.
            for (Element e : elements) {
                //Classifies the element and analyze it.
                Resource.analyzeElement(e, Resource.getPagePath());
                
                if (Resource.getIsValid()) {
                    //Insert
                    DB.insertIntoHTMLTable(
                            Resource.getElementPath(),
                            pageCounter + 1,
                            Resource.getElementTagType(),
                            Resource.getLocation(),
                            Resource.getSize());
                    
                    //Increment id for next element insertion
                    if(Objects.equals(Resource.getElementTagType(), "IMAGE")) {
                    	ArrayList<String> list;
                    	pageKey.add(Resource.getElementPath());
                    	if(picPages.containsKey(Resource.getElementPath())){
                    	    // if the key has already been used,
                    	    // we'll just grab the array list and add the value to it
                    	    list = picPages.get(Resource.getElementPath());
                    	    list.add(Resource.getPagePath());
                    	} else {
                    	    // if the key hasn't been used yet,
                    	    // we'll create a new ArrayList<String> object, add the value
                    	    // and put it in the array list with the new key
                    	    list = new ArrayList<>();
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
        Map<String,Double> textData = new TreeMap<>();
        Vector<String> pageNames2 = new Vector<>();
        for (int p =0;p<pageNames.size();p++)
        {
        	textData.put(pageNames.get(p),pageSize.get(p));
        	pageNames2.add(pageNames.get(p));
        }
        Collections.sort(pageNames);
        
        //if the chosen files already exists, these calls empty those files 
        Report.cleanJSON();
        Report.cleanText();
        
        //creates the text file with pages and pic size
        for (int p =0;p<textData.size();p++) {
        	boolean check=false;
        	if(p==textData.size()-1) 
        		check=true;
        	Report.printText(pageNames.get(p),textData.get(pageNames.get(p)),imgTotalSize,check);
        }

        
        
        //DB.printTable(); //table of values
        //DB.testDBQueries(pageCounter); //gives a count of values
        
        //Creates a json object for the pictures and calls the printJson function
		JSONObject pics = new JSONObject();
        Vector<String> pageKeyDuplicate = new Vector<>();
        pageKeyDuplicate.addAll(pageKey);
        Set<String> pageKeyUnique = new HashSet<>();
        pageKeyUnique.addAll(pageKey);
        pageKey.clear();
        pageKey.addAll(pageKeyUnique);
		for (String aPageKey : pageKey) {
			new JSONArray();
			int pageCount = 0;
			for (String aPageKeyDuplicate : pageKeyDuplicate) {
				if (aPageKeyDuplicate.matches(aPageKey)) {
					pageCount = pageCount + 1;
				}
			}
			pics.put("Image", aPageKey);
			pics.put("Number Of Pages on which it is displayed", pageCount);
			pics.put("Listing of Pages on which image is displayed", picPages.get(aPageKey));
			Report.printJSON(pics);
		}
      //Creates a json object for the each page and calls the printJson function
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
			jsonListImage.addAll(listImage);
			jsonListScripts.addAll(listScripts);
			jsonListSS.addAll(listSS);
        	obj.put("Listing of images", jsonListImage);
        	obj.put("Listing of scripts", jsonListScripts);
        	obj.put("Listing of stylesheets", jsonListSS);
        	obj.put("Number of intra-page links", countLinkIntraPage);
        	obj.put("Number of inter-site links", countLinkInterSite);
        	obj.put("Number of external links", countLinkExternal);
       
        	Report.printJSON(obj);   
        }

        //Prints created file names to the terminal
        Report.printFilenames();

        //Remove all DB tables 
        DB.cleanupDB();
    }

}
