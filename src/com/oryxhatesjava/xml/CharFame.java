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
	
	@Override
	public String toString() {
		return chara.accountName + " was killed by " + killedBy + " at " + chara.level + ". Bonuses: " + bonuses;
	}
}
