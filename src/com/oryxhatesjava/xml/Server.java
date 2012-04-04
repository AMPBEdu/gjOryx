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


public class Server {

	public String name;
	public String host;
	public float lat;
	public float lon;
	public float usage;
	
	public Server() {
		
	}
	
	public Server(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		if (!e.getName().equals("Server")) {
			return;
		}
		
		name = e.getChildText("Name");
		host = e.getChildText("DNS");
		lat = Float.parseFloat(e.getChildText("Lat"));
		lon = Float.parseFloat(e.getChildText("Long"));
		usage = Float.parseFloat(e.getChildText("Usage"));
	}
	
	@Override
	public String toString() {
		return "Server " + name;
	}
}
