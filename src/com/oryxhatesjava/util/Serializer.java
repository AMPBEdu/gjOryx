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

import java.io.DataOutput;
import java.io.IOException;

import com.oryxhatesjava.net.data.Parsable;

public class Serializer {
	
	public static final int AS_INT = 0;
	public static final int AS_BYTE = 1;
	public static final int AS_SHORT = 2;
	
	public static void writeArray(DataOutput out, Parsable[] array) throws IOException {
		int size = array.length;
		out.writeShort(size);
		if (size != 0) {
			for (Parsable w : array) {
				w.writeToDataOutput(out);
			}
		}
	}
	
	public static void writeArray(DataOutput out, int[] array, int cast) throws IOException {
		int size = array.length;
		out.writeShort(size);
		if (size != 0) {
			for (int i : array) {
				switch (cast) {
				case AS_INT:
					out.writeInt(i);
					break;
				case AS_BYTE:
					out.writeByte(i);
					break;
				case AS_SHORT:
					out.writeShort(i);
					break;
				default:
					throw new IllegalArgumentException("invalid cast operation");
				}
			}
		}
	}
	
	public static void writeArray(DataOutput out, boolean[] array) throws IOException {
		int size = array.length;
		out.writeShort(size);
		if (size != 0) {
			for (boolean i : array) {
				out.writeBoolean(i);
			}
		}
	}
}
