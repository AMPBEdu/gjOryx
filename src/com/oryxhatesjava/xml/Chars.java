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

import org.jdom.Document;
import org.jdom.Element;

public class Chars {

	public int nextCharId;
	public int maxNumChars;
	public Account account;
	public List<Char> chars;
	public List<NewsItem> news;
	public List<Server> servers;
	
	public Chars() {
		
	}
	
	public Chars(Element e) {
		parseElement(e);
	}
	
	@SuppressWarnings("unchecked")
	public void parseElement(Element e) {
		/*if (!e.getName().equals("Chars")) {
			throw new IllegalArgumentException();
		}*/
		
		nextCharId = Integer.parseInt(e.getAttributeValue("nextCharId"));
		maxNumChars = Integer.parseInt(e.getAttributeValue("maxNumChars"));
		
		chars = new LinkedList<Char>();
		Iterator<Element> itr = e.getChildren("Char").iterator();
		while (itr.hasNext()) {
			Element ce = itr.next();
			Char c = new Char(ce);
			chars.add(c);
		}
		
		account = new Account(e.getChild("Account"));
		
		news = new LinkedList<NewsItem>();
		itr = e.getChild("News").getChildren("Item").iterator();
		while (itr.hasNext()) {
			Element ie = itr.next();
			NewsItem i = new NewsItem(ie);
			news.add(i);
		}
		
		servers = new LinkedList<Server>();
		itr = e.getChild("Servers").getChildren("Server").iterator();
		while (itr.hasNext()) {
			Element se = itr.next();
			Server s = new Server(se);
			servers.add(s);
		}
		
	}
	
	@Override
	public String toString() {
		return "Chars max " + maxNumChars + ": " + chars;
	}
}
