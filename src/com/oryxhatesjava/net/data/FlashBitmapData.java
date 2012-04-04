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

public class FlashBitmapData implements Parsable {

	public int width;
	public int height;
	public byte[] data;
	
	public FlashBitmapData(DataInput in) {
		try {
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	public FlashBitmapData() {
		
	}

	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		width = in.readInt();
		height = in.readInt();
		data = new byte[width*height*4];
		in.readFully(data);
	}

	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeInt(width);
		out.writeInt(height);
		out.write(data);
	}
	
	@Override
	public String toString() {
		return "FlashBitmapData {" + width + "x" + height + "}";
	}

}
