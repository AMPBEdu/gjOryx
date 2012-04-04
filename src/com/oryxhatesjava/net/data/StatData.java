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

public class StatData implements Parsable {
	public int type; //byte
	public int value; //int
	public String valueString = "";
	
	public static final int MAXHP = 0;
	public static final int HP = 1;
	public static final int SIZE = 2;
	public static final int MAXMP = 3;
	public static final int MP = 4;
	public static final int XP = 6;
	public static final int LEVEL = 7;
	public static final int INVENTORY0 = 8;
	public static final int INVENTORY1 = 9;
	public static final int INVENTORY2 = 10;
	public static final int INVENTORY3 = 11;
	public static final int INVENTORY4 = 12;
	public static final int INVENTORY5 = 13;
	public static final int INVENTORY6 = 14;
	public static final int INVENTORY7 = 15;
	public static final int INVENTORY8 = 16;
	public static final int INVENTORY9 = 17;
	public static final int INVENTORY10 = 18;
	public static final int INVENTORY11 = 19;
	public static final int ATT = 20;
	public static final int DEF = 21;
	public static final int SPD = 22;
	public static final int VIT = 26;
	public static final int WIS = 27;
	public static final int DEX = 28;
	public static final int UNKNOWN_29 = 29;
	public static final int STARS = 30;
	public static final int NAME = 31;
	public static final int TEX1 = 32;
	public static final int TEX2 = 33;
	public static final int UNKNOWN_34 = 34;
	public static final int REALMGOLD = 35;
	public static final int UNKNOWN_36 = 36;
	public static final int UNKNOWN_37 = 37;
	public static final int UNKNOWN_38 = 38;
	public static final int CURRENTFAME = 39;
	public static final int UNKNOWN_40 = 40;
	public static final int UNKNOWN_41 = 41;
	public static final int UNKNOWN_42 = 42;
	public static final int UNKNOWN_43 = 43;
	public static final int UNKNOWN_44 = 44;
	public static final int UNKNOWN_45 = 45;
	public static final int UNKNOWN_46 = 46;
	public static final int UNKNOWN_47 = 47;
	public static final int ATTBONUS = 48;
	public static final int DEFBONUS = 49;
	public static final int SPDBONUS = 50;
	public static final int VITBONUS = 51;
	public static final int WISBONUS = 52;
	public static final int DEXBONUS = 53;
	public static final int UNKNOWN_54 = 54;
	public static final int UNKNOWN_55 = 55;
	public static final int UNKNOWN_56 = 56;
	public static final int FAME = 57;
	public static final int FAMEGOAL = 58;
	public static final int UNKNOWN_59 = 59;
	public static final int UNKNOWN_60 = 60;
	public static final int UNKNOWN_61 = 61;
	public static final int GUILD = 62;
	public static final int GUILDRANK = 63;
	public static final int UNKNOWN_64 = 64;
	
	public static final int[] STRINGS = {NAME, GUILD};
	
	public StatData(DataInput in) {
		try {
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	public StatData() {
		
	}
	
	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		type = in.readUnsignedByte();
		for (int i : STRINGS) {
			if (type == i) {
				valueString = in.readUTF();
				return;
			}
		}
		value = in.readInt();
	}

	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeByte(type);
		for (int i : STRINGS) {
			if (type == i) {
				out.writeUTF(valueString);
				return;
			}
		}
		out.writeInt(value);
	}
	
	@Override
	public String toString() {
		return "StatData {" + type + " " + value + " " + valueString + "}";
	}

}
