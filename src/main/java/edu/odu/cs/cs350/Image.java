package edu.odu.cs.cs350;

import org.jsoup.nodes.Element;

import java.io.File;
import java.util.Objects;

/**
 * Inherited from class Resource. Contains all functions specific to analyze an element designated as an image.
 */
public class Image extends Resource
{
    Image(){
    }

    public Image(Element e) {
        super();
        setElementTagType("IMAGE");
        setAttributeKey("src");
        extractPath(e, getAttributeKey());
        extractLocation(e.attr("src"));
        if (Objects.equals(getLocation(), "INTERNAL"))
        {
            extractSize(getElementPath());
        }
    }
 
    /**
     * Extracts the size of a file in MiB. Sets size.
     *
     * @param link Name of the file
     */
    void extractSize(String link) {
        String absLink = getPageDirectory() + File.separator + link;

        //Get file size in bytes
        double s = new File(absLink).length();

        //Convert to MiB and set
        s = s/1048576;
        setSize(s);
    }

    /**
     * Determines the location of a file (internal or external) by searching for its presence in the root directory.
     *
     * @param link Name of the file to search for
     */
    void extractLocation(String link) {
        String absLink = getPageDirectory() + File.separator + link;
        
        if (new File(absLink).isFile())
        {
            setLocation("INTERNAL");
        } else {
            setLocation("EXTERNAL");
        }
    }
}
