package com.oryxhatesjava.xml;

import java.util.Scanner;

import org.jdom.Element;

public class Char {

	public int id;
	public int objectType;
	public int level;
	public int exp;
	public int currentFame;
	public int[] equipment;
	public int maxHP;
	public int HP;
	public int maxMP;
	public int MP;
	public int att;
	public int def;
	public int spd;
	public int dex;
	public int vit;
	public int wis;
	public String pcStats;
	public boolean dead;
	public int pet;
	
	public Char() {
		
	}
	
	public Char(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		if (!e.getName().equals("Char")) {
			return;
		}
		
		id = Integer.parseInt(e.getAttributeValue("id"));
		objectType = Integer.parseInt(e.getChildText("ObjectType"));
		level = Integer.parseInt(e.getChildText("Level"));
		exp = Integer.parseInt(e.getChildText("Exp"));
		currentFame = Integer.parseInt(e.getChildText("CurrentFame"));
		equipment = new int[12];
		Scanner scan = new Scanner(e.getChildText("Equipment"));
		scan.useDelimiter(",");
		for (int i = 0; i < 12; i++) {
			equipment[i] = scan.nextInt();
		}
		maxHP = Integer.parseInt(e.getChildText("MaxHitPoints"));
		HP = Integer.parseInt(e.getChildText("HitPoints"));
		maxMP = Integer.parseInt(e.getChildText("MaxMagicPoints"));
		MP = Integer.parseInt(e.getChildText("MagicPoints"));
		att = Integer.parseInt(e.getChildText("Attack"));
		def = Integer.parseInt(e.getChildText("Defense"));
		spd = Integer.parseInt(e.getChildText("Speed"));
		dex = Integer.parseInt(e.getChildText("Dexterity"));
		vit = Integer.parseInt(e.getChildText("HpRegen"));
		wis = Integer.parseInt(e.getChildText("MpRegen"));
		pcStats = e.getChildText("PCStats");
		dead = Boolean.parseBoolean(e.getChildText("Dead"));
		pet = Integer.parseInt(e.getChildText("Pet"));
	}
	
	@Override
	public String toString() {
		return "Char [" + id + " " + objectType + " " + level + " " + exp + " " + currentFame + "]";
	}
}
