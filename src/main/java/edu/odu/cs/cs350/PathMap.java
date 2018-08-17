package edu.odu.cs.cs350;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Contains all functions needed to crawl a given directory and map it.
 * Results are stored in List<String> pathMap.
 */
//Crawls a given directory and maps all pages within limits
public class PathMap
{
    /**
     * List to hold directory map.
     */
    public static List<String> pathMap;

	/**
	 * Walks through a given directory and maps it to a list recursively.
	 * @throws IOException
	 */
    public static void walkDirectory() throws IOException {

		List<String> pathStore2;
		List<String> pathStore = new Vector<>();

		pathStore2 = Files.walk(new File(Arguments.getDirectory()).toPath())
						.filter(path -> path.toFile().isFile())
						.map(Path::toString)
						.collect(Collectors.toList());

		for (String j:pathStore2) {
			if (!pathStore.contains(j))
				pathStore.add(j);
		}
		setPathMap(pathStore);
    }


	/**
	 * Checks all files in a list for HTML content and filter false positives. Adds all valid results to  a list.
	 * Finally checks of the size of the filtered list to determine if it is within bounds (1 < x < 1000).
	 *
	 * @param directoryStore A list of file paths.
	 * @throws IOException If the file cannot be read.
	 */
	public static boolean filterPathMap(List<String> directoryStore) throws IOException {
		List<String> pathStoreChecked = new Vector<>();

		//For all mapped pages in pathMap
		for (String aDirectoryStore : directoryStore) {
			//Check if file exists in the directory
			if (HTMLParser.hasHTML(aDirectoryStore)) {
				//Check if that file is an image because Image files throw false positives for HTML
				if (ImageIO.read(new File(aDirectoryStore)) == null) {
					//Add the validated file
					pathStoreChecked.add(aDirectoryStore);
				}
            }
		}

		//Set pathMap to new filtered pathMap
		setPathMap(pathStoreChecked);

		//Check pathMap Size. If too large or too small - exit program with message
		if (PathMap.pathMap.size() > 1000 ) {
			return false;
		}
		else if (PathMap.pathMap.size() < 1) {
			return false;
		}

		return true;
	}

	/**
	 * Finds and sets the working directory of the program.
	 */
	public static void findWorkingDirectory()
    {
        Path current = Paths.get("");
        String s = current.toAbsolutePath().normalize().toString();
        setWorkingDirectory(s);
    }

	/**
	 * Checks if a file exists in a given directory.
	 *
	 * @param dir Directory to search
	 * @param name Name of file to search for
	 * @return True if found
	 */
    public static boolean fileExistsInDirectory(String dir, String name) {
		return new File(dir, name).exists();
	}

	/**
     * String to hold the programs working directory.
     */
    private static String workingDirectory;

	/**
	 * Setter for path map.
	 *
	 * @param pathMap List of paths
	 */
	public static void setPathMap(List<String> pathMap) {
		PathMap.pathMap = pathMap;
	}

    /**
     * Sets the programs working directory.
     * @param workingDirectory Path of working directory.
     */
    public static void setWorkingDirectory(String workingDirectory) {
        PathMap.workingDirectory = workingDirectory;
    }

    /**
     * Returns the working directory of the program.
	 *
     * @return Path of working directory
     */
    public static String getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Getter for pathMap
	 *
     * @return List<String>
     */
    public static List<String> getPathMap() {
        return pathMap;
    }
}
