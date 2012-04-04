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

package com.oryxhatesjava.util;

public final class HexValueParser {
	public static int parseInt(String string) {
		String sub;
		try {
			sub = string.substring(0, 2);
		} catch (Exception e) {
			return Integer.parseInt(string);
		}
		
		if (sub.equals("0x") || sub.equals("0X")) {
			return Integer.parseInt(string.substring(2), 16);
		} else {
			return Integer.parseInt(string);
		}
	}
}
