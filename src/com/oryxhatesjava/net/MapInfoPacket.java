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
	public int width;
	public int height;
	public String name;
	public String seed;
	public int fp;
	public int background;
	public int obf1;
	public boolean allowPlayerTeleport;
	public boolean showDisplays;
	public String[] clientXML = new String[0];
	public String[] extraXML = new String[0];
	
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
		seed = in.readUTF();
		fp = in.readInt(); // TODO: fp is supposed to be unsigned
		background = in.readInt();
		obf1 = in.readInt();
		allowPlayerTeleport = in.readBoolean();
		showDisplays = in.readBoolean();
		clientXML = new String[in.readShort()];
		for (int i = 0; i < clientXML.length; i++) {
			byte[] utf = new byte[in.readInt()];
			in.readFully(utf);
			clientXML[i] = new String(utf, "UTF-8");
		}
		extraXML = new String[in.readShort()];
		for (int i = 0; i < extraXML.length; i++) {
			byte[] utf = new byte[in.readInt()];
			in.readFully(utf);
			extraXML[i] = new String(utf, "UTF-8");
		}
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeInt(width);
		out.writeInt(height);
		out.writeUTF(name);
		out.writeUTF(seed);
		out.writeInt(fp);
		out.writeInt(background);
		out.writeInt(obf1);
		out.writeBoolean(allowPlayerTeleport);
		out.writeBoolean(showDisplays);
		out.writeShort(clientXML.length);
		for (String xml: clientXML) {
			byte[] utf = xml.getBytes("UTF-8");
			out.writeInt(utf.length);
			out.write(utf);
		}
		out.writeShort(extraXML.length);
		for (String xml: extraXML) {
			byte[] utf = xml.getBytes("UTF-8");
			out.writeInt(utf.length);
			out.write(utf);
		}
	}
	
	@Override
	public String toString() {
		return "MAPINFO " + width + "x" + height + " " + name + " " + fp + " " + background + " " + allowPlayerTeleport + " " + showDisplays + " " + clientXML + " " + extraXML;
	}
	
}
