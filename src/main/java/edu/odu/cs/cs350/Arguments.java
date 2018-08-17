package edu.odu.cs.cs350;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * This class handles parsing and validating the command line arguments provided.
 */
public class Arguments {

    static Vector<String> urls;
    private static String directory;
    public static String arg0;
    /**
     * This function checks the arguments provided via command line at execution.
     * The program will exit with an appropriate error message if one of the checks fails.
     *
     * @param args Array containing the arguments provided at the programs execution.
     * @return true if all argument checks are passed
     */
    public static boolean argCheck(String[] args) {

        //Check for arguments by checking arg[] length.
        if (args.length == 0) {
            //Exit if no arguments are provided.
            System.out.println("No arguments provided");
            return false;
        }

        if (args.length == 1) {
            //Exit if no URLs are provided.
            System.out.println("No URLs provided");
            return false;
        }

        //Check if the first argument is a valid path and directory
        File file = new File(args[0]);

        if (file.exists()) {
            if (file.isDirectory()) {
                //Set directory path
                setDirectory(args[0]);
                setArg0(args[0]);
            } else {
                System.out.println("The path provided is not a directory");
                return false;
            }
        } else {
            System.out.println("The path provided does not exist or cannot be accessed");
            return false;
        }


        //Parse the URLs provided in String[] args and add them to Vector<String> urls
        Vector<String> urlStore = new Vector<>();

        for (int i = 1; i < args.length; ++i) {
                urlStore.add(args[i]);
        }
        
        //removes duplicate urls
        Set<String> set = new HashSet<>();
        set.addAll(urlStore);
        urlStore.clear();
        urlStore.addAll(set);
        setUrls(urlStore);
        return true;
    }

    /**
     * Getters and setters
     */
    static void setUrls(Vector<String> urls) {
        Arguments.urls = urls;
    }

    static void setDirectory(String directory) {
        Arguments.directory = directory;
    }

    static String getDirectory() {
        return directory;
    }

    static String getURL(int i)
    {
        return urls.get(i);
    }

    public static Vector<String> getUrls() {
        return urls;
    }

    static int urlSize()
    {
        return urls.size();
    }
    static void setArg0(String arg0) {
        Arguments.arg0 = arg0;
    }
	public static String getArg0() {
		return arg0;
	}
}
