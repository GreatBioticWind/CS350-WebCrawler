package edu.odu.cs.cs350.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import edu.odu.cs.cs350.Arguments;
import edu.odu.cs.cs350.PathMap;
import edu.odu.cs.cs350.Resource;

public class PathMapIntegrationTests {
	
	@Test
	public void findWorkingDirectoryTest() throws Exception 
	{
		String sep = File.separator;
	    String root = "." + sep + "htmlTestFiles" + sep + "image";
	    String[] arg = {root, "www.google.com"};
	    Arguments.argCheck(arg);
		PathMap.findWorkingDirectory();
		Path current = Paths.get("");
		String s = current.toAbsolutePath().normalize().toString();
		assertEquals(s, PathMap.getWorkingDirectory());
	}
}
