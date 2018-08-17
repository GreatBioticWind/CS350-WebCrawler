package edu.odu.cs.cs350;

import org.jsoup.nodes.Element;

public class Link extends Resource
{

	public Link(Element e)
	{
		super();
		setElementTagType("LINK");
		setAttributeKey("href");
		extractPath(e, getAttributeKey());
		extractLocation(e);
	}

	private void extractLocation(Element e)
	{
		String path = e.attr(getAttributeKey());
		if (path.startsWith("#")) {
			setLocation("INTRAPAGE");
		}
		else if (PathMap.fileExistsInDirectory(getPageDirectory(), getElementPath())) {
			setLocation("INTERSITE");
		}
		else {
			setLocation("EXTERNAL");
		}


	}
}
