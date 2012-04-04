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
