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

public class GuildMembers {

	public int id;
	public String name;
	public int totalFame;
	public int currentFame;
	public String hallType;
	public List<GuildMember> members;
	
	public GuildMembers() {
		
	}
	
	public GuildMembers(Element e) {
		parseElement(e);
	}
	
	@SuppressWarnings("unchecked")
	public void parseElement(Element e) {
		if (!e.getName().equals("Guild")) {
			throw new IllegalArgumentException();
		}
		
		id = Integer.parseInt(e.getAttributeValue("id"));
		name = e.getAttributeValue("name");
		totalFame = Integer.parseInt(e.getChildText("TotalFame"));
		currentFame = Integer.parseInt(e.getChildText("CurrentFame"));
		hallType = e.getChildText("HallType");
		
		members = new LinkedList<GuildMember>();
		Iterator<Element> itr = e.getChildren("Member").iterator();
		while (itr.hasNext()) {
			Element me = itr.next();
			GuildMember m = new GuildMember(me);
			members.add(m);
		}
	}
	
	@Override
	public String toString() {
		return "Guild " + name + ": " + members;
	}
}
