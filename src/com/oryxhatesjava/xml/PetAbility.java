package com.oryxhatesjava.xml;

import org.jdom.Element;

public class PetAbility {

	public int type;
	public int power;
	public int points;
	
	public PetAbility(Element e){
		parseElement(e);
	}
	
	public void parseElement(Element e){
		this.type = Integer.parseInt(e.getAttributeValue("type"));
		this.type = Integer.parseInt(e.getAttributeValue("power"));
		this.type = Integer.parseInt(e.getAttributeValue("points"));
	}
	
}
