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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Element;

public class CharFame {

	public Char chara;
	public int baseFame;
	public int totalFame;
	public int shots;
	public int hitShots;
	public int specialUse;
	public int tiles;
	public int teleports;
	public int potions;
	public int monsterKills;
	public int monsterAssists;
	public int quests;
	public int levelAssists;
	public int minutes;
	public List<FameBonus> bonuses;
	public int createdOn;
	public int prevTotalFame;
	public String killedBy;
	
	public CharFame() {
		
	}
	
	public CharFame(Element e) {
		
	}
	
	@SuppressWarnings("unchecked")
	public void parseElement(Element e) {
		if (!e.getName().equals("Fame")) {
			return;
		}
		
		chara = new Char(e.getChild("Char"));
		baseFame = Integer.parseInt(e.getChildText("BaseFame"));
		totalFame = Integer.parseInt(e.getChildText("TotalFame"));
		shots = Integer.parseInt(e.getChildText("Shots"));
		hitShots = Integer.parseInt(e.getChildText("ShotsThatDamage"));
		specialUse = Integer.parseInt(e.getChildText("SpecialAbilityUses"));
		tiles = Integer.parseInt(e.getChildText("TilesUncovered"));
		teleports = Integer.parseInt(e.getChildText("Teleports"));
		potions = Integer.parseInt(e.getChildText("PotionsDrunk"));
		monsterKills = Integer.parseInt(e.getChildText("MonsterKills"));
		monsterAssists = Integer.parseInt(e.getChildText("MonsterAssists"));
		quests = Integer.parseInt(e.getChildText("QuestsCompleted"));
		levelAssists = Integer.parseInt(e.getChildText("LevelUpAssists"));
		minutes = Integer.parseInt(e.getChildText("MinutesActive"));
		
		bonuses = new LinkedList<FameBonus>();
		Iterator<Element> itr = e.getChildren("Bonus").iterator();
		while (itr.hasNext()) {
			bonuses.add(new FameBonus(itr.next()));
		}
		
		createdOn = Integer.parseInt(e.getChildText("CreatedOn"));
		prevTotalFame = Integer.parseInt(e.getChildText("PreviousAccountTotalFame"));
		killedBy = e.getChildText("KilledBy");
	}
}
