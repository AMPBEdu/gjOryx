/* oryx-hates-java
 * Copyright (C) 2011-2012 Furyhunter <furyhunter600@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
	public Pet pet;
	
	public String accountName;
	
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
		pet = new Pet(e.getChild("Pet"));
		
		// opt
		if (e.getChild("Account") != null) {
			accountName = e.getChild("Account").getChildText("Name");
		}
	}
	
	@Override
	public String toString() {
		return "Char [" + id + " " + objectType + " " + level + " " + exp + " " + currentFame + "]";
	}
}
