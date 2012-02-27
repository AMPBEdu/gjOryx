package com.oryxhatesjava.xml;

import org.jdom.Element;


public class Server {

	public String name;
	public String host;
	public float lat;
	public float lon;
	public float usage;
	
	public Server() {
		
	}
	
	public Server(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		if (!e.getName().equals("Server")) {
			return;
		}
		
		name = e.getChildText("Name");
		host = e.getChildText("DNS");
		lat = Float.parseFloat(e.getChildText("Lat"));
		lon = Float.parseFloat(e.getChildText("Long"));
		usage = Float.parseFloat(e.getChildText("Usage"));
	}
}
