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
