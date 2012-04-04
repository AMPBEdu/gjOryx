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

package com.joryx.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileRenamer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		String name = "chars16x16dEncounters";
		File file = new File("assets/Textures/" + name + "/");

		String[] files = file.list();

		for (String txt : files) {
			String re1=".*?";	// Non-greedy match on filler
		    String re2="(\\d+)";	// Integer Number 1

		    Pattern p = Pattern.compile(re1+re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		    Matcher m = p.matcher(txt);

			if (m.find()) {
				int y = Integer.parseInt(m.group(1));

				File ren = new File("assets/Textures/" + name + "/" + txt);
				ren.renameTo(new File("assets/Textures/" + name + "/" + (y) + ".png"));
			}
		}
	}
}
