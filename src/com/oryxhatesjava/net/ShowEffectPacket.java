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

package com.oryxhatesjava.net;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.oryxhatesjava.net.data.Location;
import com.oryxhatesjava.net.data.Parsable;

public class ShowEffectPacket extends Packet implements Parsable {

	public int effectType; //ubyte
	public int targetObjectId; //int
	public Location pos1;
	public Location pos2;
	public int color; //int
    public static final int UNKNOWN_0 = 0;
    public static final int UNKNOWN_1 = 1;
    public static final int UNKNOWN_2 = 2;
    public static final int UNKNOWN_3 = 3;
    public static final int UNKNOWN_4 = 4;
    public static final int UNKNOWN_5 = 5;
    public static final int UNKNOWN_6 = 6;
    public static final int UNKNOWN_7 = 7;
    public static final int UNKNOWN_8 = 8;
    public static final int UNKNOWN_9 = 9;
    public static final int UNKNOWN_10 = 10;
    public static final int UNKNOWN_11 = 11;
    public static final int UNKNOWN_12 = 12;
    public static final int UNKNOWN_13 = 13;
    public static final int UNKNOWN_14 = 14;
    public static final int UNKNOWN_15 = 15;
	
	
	public ShowEffectPacket(DataInput in) {
		try {
			type = Packet.SHOW_EFFECT;
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	public ShowEffectPacket() {
		type = Packet.SHOW_EFFECT;
	}
	
	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		effectType = in.readUnsignedByte();
		targetObjectId = in.readInt();
		pos1 = new Location(in);
		pos2 = new Location(in);
		color = in.readInt();
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeByte(effectType);
		out.writeInt(targetObjectId);
		pos1.writeToDataOutput(out);
		pos2.writeToDataOutput(out);
		out.writeInt(color);
	}
	
	@Override
	public String toString() {
		return "SHOW_EFFECT " + effectType + " " + targetObjectId + " " + pos1 + " " + pos2 + " " + Integer.toHexString(color);
	}
}
