package com.oryxhatesjava.xml;

import org.jdom.Element;

import com.oryxhatesjava.util.HexValueParser;


public class ObjectType {

	public int type;
	public String id;
	public Element element;
	
	public ObjectType() {
		
	}
	
	public ObjectType(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		if (!e.getName().equals("Object")) {
			return;
		}
		
		type = HexValueParser.parseInt(e.getAttributeValue("type"));
		id = e.getAttributeValue("id");
		element = e;
	}
}
