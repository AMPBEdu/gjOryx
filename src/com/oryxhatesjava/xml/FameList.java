package com.oryxhatesjava.xml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

public class FameList {

	public String timespan;
	public List<FameListElem> chars;
	public FameListElem mine;
	
	public FameList() {
		
	}
	
	public FameList(Element e) {
		parseElement(e);
	}
	
	@SuppressWarnings("unchecked")
	public void parseElement(Element e) {
		if (!e.getName().equals("FameList")) {
			return;
		}
		
		timespan = e.getAttributeValue("timespan");
		
		chars = new LinkedList<FameListElem>();
		Iterator<Element> itr = e.getChildren("FameListElem").iterator();
		while (itr.hasNext()) {
			chars.add(new FameListElem(itr.next()));
		}
		
		mine = new FameListElem(e.getChild("MyFameListElem"));
		chars.add(mine);
	}
}
