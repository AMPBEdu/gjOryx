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

package com.oryxhatesjava.net.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Tile implements Parsable {

	public int x; //short
	public int y; //short
	public int type; //byte
	
	public Tile() {
		this(0, 0, 0);
	}
	
	public Tile(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public Tile(DataInput in) {
		try {
			parseFromDataInput(in);
		} catch (IOException e) {
			e.printStackTrace();
			x = 0;
			y = 0;
			type = 0;
		}
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeShort(x);
		out.writeShort(y);
		out.writeByte(type);
	}

	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		x = in.readShort();
		y = in.readShort();
		type = in.readUnsignedByte();
	}

	@Override
	public String toString() {
		return "Tile {" + x + " " + y + " " + type + "}";
	}
}
