/* jOryx - A Realm of the Mad God client.
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

package com.joryx.db;

import org.jdom.Element;

import com.oryxhatesjava.util.HexValueParser;

public class LevelIncreaseDefinition {

	public int stat;
	public int max;
	public int min;
	
	public static final int MAXHP = 0;
	public static final int MAXMP = 1;
	public static final int ATT = 2;
	public static final int DEF = 3;
	public static final int SPD = 4;
	public static final int DEX = 5;
	public static final int VIT = 6;
	public static final int WIS = 7;
	
	public LevelIncreaseDefinition(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		String statS = e.getTextTrim();
		
		max = HexValueParser.parseInt(e.getAttributeValue("max"));
		min = HexValueParser.parseInt(e.getAttributeValue("min"));
		if (statS.equals("MaxHitPoints")) {
			stat = MAXHP;
		}
		if (statS.equals("MaxMagicPoints")) {
			stat = MAXMP;
		}
		if (statS.equals("Attack")) {
			stat = ATT;
		}
		if (statS.equals("Defense")) {
			stat = DEF;
		}
		if (statS.equals("Speed")) {
			stat = SPD;
		}
		if (statS.equals("Dexterity")) {
			stat = DEX;
		}
		if (statS.equals("HpRegen")) {
			stat = VIT;
		}
		if (statS.equals("MpRegen")) {
			stat = WIS;
		}
	}
}
