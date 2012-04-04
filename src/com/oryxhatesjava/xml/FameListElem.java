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
	
	@Override
	public String toString() {
		return name + ": " + totalFame;
	}
}
