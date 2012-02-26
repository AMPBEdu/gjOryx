/*
 * Copyright (C) 2011 Furyhunter <furyhunter600@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the creator nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.oryxhatesjava.net.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StatData implements Parsable {
	public int type; //byte
	public int value; //int
	public String valueString = "";
	
	public static final int UNKNOWN_1 = 1;
	public static final int UNKNOWN_2 = 2;
	public static final int UNKNOWN_3 = 3;
	public static final int UNKNOWN_4 = 4;
	public static final int UNKNOWN_5 = 5;
	public static final int UNKNOWN_6 = 6;
	public static final int UNKNOWN_7 = 7;
	public static final int UNKNOWN_20 = 20;
	public static final int UNKNOWN_21 = 21;
	public static final int UNKONWN_22 = 22;
	public static final int INVENTORY_0_STAT = 8;
	public static final int INVENTORY_1_STAT = 9;
	public static final int INVENTORY_2_STAT = 10;
	public static final int INVENTORY_3_STAT = 11;
	public static final int INVENTORY_4_STAT = 12;
	public static final int INVENTORY_5_STAT = 13;
	public static final int INVENTORY_6_STAT = 14;
	public static final int INVENTORY_7_STAT = 15;
	public static final int INVENTORY_8_STAT = 16;
	public static final int INVENTORY_9_STAT = 17;
	public static final int INVENTORY_10_STAT = 18;
	public static final int INVENTORY_11_STAT = 19;
	public static final int UNKNOWN_26 = 26;
	public static final int UNKNOWN_27 = 27;
	public static final int UNKNOWN_28 = 28;
	public static final int UNKNOWN_29 = 29;
	public static final int UNKNOWN_30 = 30;
	public static final int NAME = 31;
	public static final int TEX1_STAT = 32;
	public static final int TEX2_STAT = 33;
	public static final int UNKNOWN_34 = 34;
	public static final int UNKNOWN_35 = 35;
	public static final int UNKNOWN_36 = 36;
	public static final int UNKNOWN_37 = 37;
	public static final int UNKNOWN_38 = 38;
	public static final int UNKNOWN_39 = 39;
	public static final int UNKNOWN_40 = 40;
	public static final int UNKNOWN_41 = 41;
	public static final int UNKNOWN_42 = 42;
	public static final int UNKNOWN_43 = 43;
	public static final int UNKNOWN_44 = 44;
	public static final int UNKNOWN_45 = 45;
	public static final int UNKNOWN_46 = 46;
	public static final int UNKNOWN_47 = 47;
	public static final int UNKNOWN_48 = 48;
	public static final int UNKNOWN_49 = 49;
	public static final int UNKNOWN_50 = 50;
	public static final int UNKNOWN_51 = 51;
	public static final int UNKNOWN_52 = 52;
	public static final int UNKNOWN_53 = 53;
	public static final int UNKNOWN_54 = 54;
	public static final int UNKNOWN_55 = 55;
	public static final int UNKNOWN_56 = 56;
	public static final int UNKNOWN_57 = 57;
	public static final int UNKNOWN_58 = 58;
	public static final int UNKNOWN_59 = 59;
	public static final int UNKNOWN_60 = 60;
	public static final int UNKNOWN_61 = 61;
	public static final int UNKNOWN_62 = 62;
	public static final int UNKNOWN_63 = 63;
	public static final int UNKNOWN_64 = 64;
	
	public static final int[] STRINGS = {NAME, UNKNOWN_62};
	
	public StatData(DataInput in) {
		try {
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
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
