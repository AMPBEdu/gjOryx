package com.oryxhatesjava.xml;

import java.util.Scanner;

import org.jdom.Element;

public class FameListElem {

	public boolean mine;
	public int accountId;
	public int charId;
	public String name;
	public int objectType;
	public int tex1;
	public int tex2;
	public int[] equipment;
	public int totalFame;
	
	public FameListElem() {
		
	}
	
	public FameListElem(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		if (!e.getName().equals("FameListElem") && !e.getName().equals("MyFameListElem")) {
			return;
		}
		
		if (e.getName().equals("MyFameListElem")) {
			mine = true;
		}
		
		accountId = Integer.parseInt(e.getAttributeValue("accountId"));
		charId = Integer.parseInt(e.getAttributeValue("charId"));
		
		name = e.getChildText("Name");
		objectType = Integer.parseInt(e.getChildText("ObjectType"));
		tex1 = Integer.parseInt(e.getChildText("Tex1"));
		tex2 = Integer.parseInt(e.getChildText("Tex2"));
		
		equipment = new int[12];
		Scanner scan = new Scanner(e.getChildText("Equipment"));
		scan.useDelimiter(",");
		for (int i = 0; i < 12; i++) {
			equipment[i] = scan.nextInt();
		}
		
		totalFame = Integer.parseInt(e.getChildText("TotalFame"));
	}
}
