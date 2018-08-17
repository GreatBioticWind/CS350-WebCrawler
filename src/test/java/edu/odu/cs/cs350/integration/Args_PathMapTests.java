package edu.odu.cs.cs350.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import edu.odu.cs.cs350.Arguments;
import edu.odu.cs.cs350.PathMap;

public class Args_PathMapTests {

	@Test
	public void testArgs_PathMap() throws IOException 
	{
		//tests Argument Class and PathMap Class path
		String sep = File.separator;
		String[] arg = {String.format(".%shtmlTestFiles%stest", sep, sep), "www.google.com"};
		boolean correct = Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		PathMap.walkDirectory();
		PathMap.filterPathMap(PathMap.getPathMap());
		Path current = Paths.get("");
		String s = current.toAbsolutePath().normalize().toString();
		assertTrue(PathMap.filterPathMap(PathMap.getPathMap()));
		assertTrue(correct);
		assertEquals(s, PathMap.getWorkingDirectory());
	}

}
