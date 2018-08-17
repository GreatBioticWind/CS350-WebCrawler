package edu.odu.cs.cs350;


import org.jsoup.nodes.Element;

public class Script extends Resource {

    public Script(Element e)
	{
		super();
		setElementTagType("JAVASCRIPT");
		setAttributeKey("src");
		extractPath(e, getAttributeKey());
		extractLocation();
	}
}
