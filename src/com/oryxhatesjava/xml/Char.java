/*
 * Copyright (C) 2011 Furyhunter <furyhunter600@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the creator nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
	public int pet;
	
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
		pet = Integer.parseInt(e.getChildText("Pet"));
		
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
