package edu.odu.cs.cs350;

import org.jsoup.nodes.Element;

public class CSS extends Resource {

	public CSS(Element e) {
		super();
		setElementTagType("STYLESHEET");
		setAttributeKey("href");
		extractPath(e, getAttributeKey());
		extractLocation();
	}
}