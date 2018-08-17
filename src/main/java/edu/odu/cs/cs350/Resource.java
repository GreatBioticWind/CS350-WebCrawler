package edu.odu.cs.cs350;

import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 *
 */
public class Resource {
    private static boolean elementIsValid;
    private static String pageDirectory;
    private static String pagePath;
    private static String elementPath;
    private static String elementTagType;
    private static String location;
    private static String attributeKey;
    static double size;

    //Default Constructor
    Resource() {
        elementIsValid = true;
    	size = 0.0;
    }


    Resource(String pD, String pP, String eP, String eT, String l, double s, String aK) {
        pageDirectory = pD;
    	pagePath = pP;
    	elementPath = eP;
    	elementTagType = eT;
    	location = l;
        attributeKey = aK;
        size = s;
    }

    /**
     * Analyze the Jsoup element passed. Classifies the tag, location, and type.
     * @param element
     * @param pPath
     * @return
     * @throws IOException
     */
    public static String analyzeElement(Element element, String pPath) throws IOException {
        //Create a new instance of Resource with the default values.
        new Resource();
        String type = "";

        //Set the elementPath of the pagePath being analyzed
        setPagePath(pPath);

        //Classify the element by tag
        String tag = classifyTag(element);

        //Tag specific analysis
        switch (tag) {
            case "img":
                new Image(element);
                type="Found an image link";
                break;

            case "script":
                if (element.hasAttr("src"))
                {
                    new Script(element);
                    type="Found a script";
                } else {
                    elementIsValid = false;
                }
                break;

            case "a":
                if (element.hasAttr("href"))
                {
                    new Link(element);
                    type="Found an anchor link";
                } else {
                    elementIsValid = false;
                }
                break;

            case "link":
                new CSS(element);
                type = "Found a stylesheet";
                break;
        }
        return type;

    }

    /**
     * Trims a link to its relative path if it matches a known URL.
     *
     * @param link A raw link to an element
     * @return Link trimmed of URL matching those provided at the programs execution.
     */
    static String stripSiteRoot(String link) {
        Vector<String> urls = Arguments.getUrls();
        String trimmedLink = null;

        for (String url : urls) {
            if (link.contains(url)) {
                String segments[] = link.split(url + "/");

                if (segments.length > 1)
                {
                    trimmedLink = segments[segments.length - 1];
                }
            }
        }
        return trimmedLink;
    }

    public static void parsePageDirectory() {
        String pagePath = getPagePath();
        setPageDirectory(new File(pagePath).getAbsoluteFile().getParent());
    }

    /**
     * Compares a link against the URL arguments provided via command line at the programs execution.
     * Determines if the link in question points internally or externally
     *
     * @param link A link extracted from a HTML element
     */
    static boolean linkContainsSiteRoot(String link) {
        Vector<String> urls = Arguments.getUrls();

        //Checks if the link contains one of the URLs provided
        for (String url : urls) {
            if (link.contains(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Precondition: extractPath has been called.
     * Default location extracting function. Checks if a file is where the element link says it is.
     */
    void extractLocation() {
        if (PathMap.fileExistsInDirectory(getPageDirectory(), getElementPath())) {
            setLocation("INTERNAL");
        } else {
            setLocation("EXTERNAL");
        }
    }

    /**
     *
     * @param e A selected JSoup Element
     * @param attributeKey Attribute key corresponding to the element being analyzed (eg "a", "src", etc.)
     */
    void extractPath(Element e, String attributeKey) {
        String path = e.attr(attributeKey);

        if (linkContainsSiteRoot(path)) {
            setElementPath(stripSiteRoot(path));
        }
        else {
            setElementPath(path);
        }
    }

    public static String classifyTag(Element element){
        return element.tagName();
    }

    public static String getPagePath() {
        return pagePath;
    }

    public static String getElementTagType() {
        return elementTagType;
    }

    public static String getElementPath() {
        return elementPath;
    }

    public static String getLocation() {
        return location;
    }

    public static double getSize()
    {
        return size;
    }

    public static String getPageDirectory() {
        return pageDirectory;
    }

    public static void setPagePath(String page) {
        Resource.pagePath = page;
    }

    public void setElementPath(String path) {
        Resource.elementPath = path;
    }

    public void setElementTagType(String tt) {
        Resource.elementTagType = tt;
    }

    public static void setLocation(String tt) {
        Resource.location = tt;
    }

    public void setSize(double nn)
    {
        size = nn;
    }
    
    public static boolean getIsValid()
    {
        return elementIsValid;
    }

    public static void setPageDirectory(String pageDirectory) {
        Resource.pageDirectory = pageDirectory;
    }

    public static String getAttributeKey() {
        return attributeKey;
    }

    public static void setAttributeKey(String attributeKey) {
        Resource.attributeKey = attributeKey;
    }
}
