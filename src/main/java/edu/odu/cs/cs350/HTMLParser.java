package edu.odu.cs.cs350;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * Class HTMLParser holds all functions needed to analyze and classify links on a given page
 */
public class HTMLParser {

    /**
     * Checks all the mapped pages for HTML content defined by the presence of select tags.
     *
     * @param url Directory path to the file to be analyzed for content.
     * @return True if HTML is present. False if not.
     */
    static boolean hasHTML(String url) {
        Scanner scanner = null;
        String content;

        try {
            scanner = new Scanner(new FileInputStream(url), "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (scanner != null && scanner.hasNextLine()) {
            content = scanner.nextLine();

            if (content.contains("<html")
                    || content.contains("<a")
                    || content.contains("<link")
                    || content.contains("<style")
                    || content.contains("<script")
                    || content.contains("<img")
                    || content.contains("<body") && content.contains("/body>")
                    || content.contains("<head")
                    || content.contains("<div")
                    || content.contains("<span")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reads a file from pathMap into the JSoup parser for analysis.
     * All elements we care to find are defined in here as elementsToSearchFor
     *
     * @param pageNumber An integer corresponding to an index in pathMap
     * @return An ArrayList with all HTML elements of interest discovered.
     */
    public static Elements parsePage(int pageNumber) {

        String elementsToSearchFor = ("a, img, link, script, style");
        String contents = null;

        try {
            contents = new String(Files.readAllBytes(Paths.get(PathMap.pathMap.get(pageNumber))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document doc = Jsoup.parse(contents);
        return doc.select(elementsToSearchFor);
    }
}