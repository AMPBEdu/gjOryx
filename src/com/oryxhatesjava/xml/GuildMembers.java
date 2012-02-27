package com.oryxhatesjava.xml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

public class GuildMembers {

	public int id;
	public String name;
	public int totalFame;
	public int currentFame;
	public String hallType;
	public List<GuildMember> members;
	
	public GuildMembers() {
		
	}
	
	public GuildMembers(Element e) {
		parseElement(e);
	}
	
	@SuppressWarnings("unchecked")
	public void parseElement(Element e) {
		if (!e.getName().equals("Guild")) {
			return;
		}
		
		id = Integer.parseInt(e.getAttributeValue("id"));
		name = e.getAttributeValue("name");
		totalFame = Integer.parseInt(e.getChildText("TotalFame"));
		currentFame = Integer.parseInt(e.getChildText("CurrentFame"));
		hallType = e.getChildText("HallType");
		
		members = new LinkedList<GuildMember>();
		Iterator<Element> itr = e.getChildren("Member").iterator();
		while (itr.hasNext()) {
			Element me = itr.next();
			GuildMember m = new GuildMember(me);
			members.add(m);
		}
	}
}
