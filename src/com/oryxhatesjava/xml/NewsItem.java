package com.oryxhatesjava.xml;

import org.jdom.Element;

public class NewsItem {

	public String icon;
	public String title;
	public String tagLine;
	public String link;
	public int date;
	
	public NewsItem() {
		
	}
	
	public NewsItem(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		if (!e.getName().equals("Item")) {
			return;
		}
		icon = e.getChildText("Icon");
		title = e.getChildText("Title");
		tagLine = e.getChildText("TagLine");
		link = e.getChildText("Link");
		date = Integer.parseInt(e.getChildText("Date"));
	}
}
