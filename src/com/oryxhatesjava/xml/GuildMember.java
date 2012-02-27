package com.oryxhatesjava.xml;

import org.jdom.Element;

public class GuildMember {

	public String name;
	public int rank;
	public int fame;
	
	public GuildMember() {
		
	}
	
	public GuildMember(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		if (!e.getName().equals("Member")) {
			return;
		}
		
		name = e.getChildText("Name");
		rank = Integer.parseInt(e.getChildText("Rank"));
		fame = Integer.parseInt(e.getChildText("Fame"));
	}
}
