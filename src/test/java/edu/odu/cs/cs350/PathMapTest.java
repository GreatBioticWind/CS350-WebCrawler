package edu.odu.cs.cs350;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class PathMapTest {
	@Test
	public void TestPathMap() throws Exception 
	{
		List<String> path = new Vector<>();
		List<String> pm = new Vector<>();
		path.add("C:/Users/");
		path.add("Katie/git/Blue8/HTML/html");
		pm.add("C:/Users/");
		pm.add("Katie/git/Blue8/HTML/html");
		PathMap.setPathMap(path);
		assertEquals(pm, PathMap.getPathMap());
	}
	
	@Test
	public void workingDirectoryTest() throws Exception 
	{
		String wd = "C:/Users/Katie/git/Blue8/HTML/html";
		PathMap.setWorkingDirectory(wd);
		assertEquals("C:/Users/Katie/git/Blue8/HTML/html", PathMap.getWorkingDirectory());
	}
	
	@Test
	public void fileExistsInDirectoryTest()
	{
		boolean fail = PathMap.fileExistsInDirectory("directory", "name");
		assertFalse(fail);
		
		boolean pass = PathMap.fileExistsInDirectory("./htmlTestFiles/image", "pic_mountain.jpg");
		assertTrue(pass);
	}
	
	@Test
	public void filterPathMapTest() throws Exception 
	{
		//Integration test that deals with Args and PathMap
		//Initialize args, file separator
		String sep = File.separator;
		String[] arg = {String.format(".%shtmlTestFiles%stest", sep, sep), "www.google.com"};
		String[] arg2 = {String.format(".%shtmlTestFiles%sfake", sep, sep), "www.google.com"};

		//Check args, walk directory, set working directory
		Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();

		//Filter the pathMap
		PathMap.filterPathMap(PathMap.getPathMap());

		//Check the size of the path map: /../test has 7 valid files.
		assertEquals( PathMap.getPathMap().size(), 7);
		assertTrue(PathMap.filterPathMap(PathMap.getPathMap()));
		
		//Too few files test:
		Arguments.argCheck(arg2);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();

		//Filter pathMap
		PathMap.filterPathMap(PathMap.getPathMap());

		//Check the size of the path map: /../fake has 0 valid files.
		assertEquals(PathMap.getPathMap().size(), 0);
		assertFalse(PathMap.filterPathMap(PathMap.getPathMap()));
		
		//I tested to make sure that too many files limit worked by lowering the limit down from 1000
		//to 10, where it caught the dummy folder(which had 23 files) correctly.
		//Since 1000 was the limit, I changed it back though it looks like it wasn't tested.
		//I didn't want to add a folder with 1000 empty files to the build.
		/*
		String[] args = {"./dummy", "www.google.com"};
		boolean wrongArgs = Arguments.argCheck(args);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		boolean fail = PathMap.filterPathMap(PathMap.getPathMap());
		assertFalse(fail);
		 */
	}
	
	@Test
	public void findWorkingDirectoryTest() throws Exception 
	{
		PathMap.findWorkingDirectory();
		Path current = Paths.get("");
		String s = current.toAbsolutePath().normalize().toString();
		assertEquals(s, PathMap.getWorkingDirectory());
	}
	
	@Test
	public void walkDirectoryTest() throws Exception 
	{
		//Integration test that deals with Args and PathMap
		//Initialize args, file separator, and list
		String sep = File.separator;
		List<String> result = new Vector<>();
		String[] args = {String.format(".%shtmlTestFiles%sfake", sep, sep), "www.google.com"};

		//Add test string to list
		result.add(String.format(".%shtmlTestFiles%sfake%sfake.txt", sep, sep, sep));

		Arguments.argCheck(args);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		assertEquals(result, PathMap.getPathMap());
	}
}
