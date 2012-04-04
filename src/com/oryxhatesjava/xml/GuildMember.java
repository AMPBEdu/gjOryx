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

import org.jdom.Element;

public class GuildMember {

	public String name;
	public int rank;
	public int fame;
	
	public GuildMember() {
		
	}
	
	public GuildMember(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		if (!e.getName().equals("Member")) {
			return;
		}
		
		name = e.getChildText("Name");
		rank = Integer.parseInt(e.getChildText("Rank"));
		fame = Integer.parseInt(e.getChildText("Fame"));
	}
	
	@Override
	public String toString() {
		return name;
	}
}
