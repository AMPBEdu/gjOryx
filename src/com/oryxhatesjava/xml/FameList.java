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

public class FameList {

	public String timespan;
	public List<FameListElem> chars;
	public FameListElem mine;
	
	public FameList() {
		
	}
	
	public FameList(Element e) {
		parseElement(e);
	}
	
	@SuppressWarnings("unchecked")
	public void parseElement(Element e) {
		if (!e.getName().equals("FameList")) {
			throw new IllegalArgumentException();
		}
		
		timespan = e.getAttributeValue("timespan");
		
		chars = new LinkedList<FameListElem>();
		Iterator<Element> itr = e.getChildren("FameListElem").iterator();
		while (itr.hasNext()) {
			chars.add(new FameListElem(itr.next()));
		}
		
		mine = new FameListElem(e.getChild("MyFameListElem"));
		chars.add(mine);
	}
	
	@Override
	public String toString() {
		return "Legends of the " + timespan + ": " + chars;
	}
}
