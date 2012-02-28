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
import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;

import com.oryxhatesjava.net.data.Parsable;

public class MapInfoPacket extends Packet implements Parsable {

	public int width; //int
	public int height; //int
	public String name; //UTF
	public int fp; //uint
	public int background; //int
	public boolean allowPlayerTeleport;
	public boolean showDisplays;
	public List<String> clientXML;
	public List<String> extraXML;
	
	public MapInfoPacket(DataInput in) {
		try {
			type = Packet.MAPINFO;
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	public MapInfoPacket() {
		type = Packet.MAPINFO;
	}
	
	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		width = in.readInt();
		height = in.readInt();
		name = in.readUTF();
		fp = in.readInt();
		background = in.readInt();
		allowPlayerTeleport = in.readBoolean();
		showDisplays = in.readBoolean();
		
		int lines = in.readUnsignedShort();
		clientXML = new Vector<String>();
		for (int i = 0; i < lines; i++) {
			int size = in.readInt();
			byte[] buf = new byte[size];
			in.readFully(buf);
			String line = new String(buf, Charset.forName("UTF-8"));
			clientXML.add(line);
		}
		
		lines = in.readUnsignedShort();
		extraXML = new Vector<String>();
		for (int i = 0; i < lines; i++) {
			int size = in.readInt();
			byte[] buf = new byte[size];
			in.readFully(buf);
			String line = new String(buf, Charset.forName("UTF-8"));
			extraXML.add(line);
		}
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeInt(width);
		out.writeInt(height);
		out.writeUTF(name);
		out.writeInt(fp);
		out.writeInt(background);
		out.writeBoolean(allowPlayerTeleport);
		out.writeBoolean(showDisplays);
		
		int size = clientXML.size();
		out.writeShort(size);
		for (String s : clientXML) {
			byte[] buf = s.getBytes("UTF-8");
			out.writeInt(buf.length);
			out.write(buf);
		}
		
		size = extraXML.size();
		out.writeShort(size);
		for (String s : extraXML) {
			byte[] buf = s.getBytes("UTF-8");
			out.writeInt(buf.length);
			out.write(buf);
		}
	}
	
	@Override
	public String toString() {
		return "MAPINFO " + width + "x" + height + " " + name + " " + fp + " " + background + " " + allowPlayerTeleport + " " + showDisplays + " " + clientXML + " " + extraXML;
	}
	
}
