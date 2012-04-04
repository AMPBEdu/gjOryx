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

public class AnimatedTextureDefinition {

	public int index;
	public String file;
	
	public AnimatedTextureDefinition(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		index = HexValueParser.parseInt(e.getChildTextTrim("Index"));
		file = e.getChildTextTrim("File");
	}
}
