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
	public String obf0;
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
		this.width = in.readInt();
		this.height = in.readInt();
		this.name = in.readUTF();
		this.obf0 = in.readUTF();
		this.fp = in.readInt(); // TODO: fp is supposed to be unsigned
		this.background = in.readInt();
		this.obf1 = in.readInt();
		this.allowPlayerTeleport = in.readBoolean();
		this.showDisplays = in.readBoolean();
		this.clientXML = new String[in.readShort()];
		for (int i = 0; i < this.clientXML.length; i++) {
			byte[] utf = new byte[in.readInt()];
			in.readFully(utf);
			this.clientXML[i] = new String(utf, "UTF-8");
		}
		this.extraXML = new String[in.readShort()];
		for (int i = 0; i < this.extraXML.length; i++) {
			byte[] utf = new byte[in.readInt()];
			in.readFully(utf);
			this.extraXML[i] = new String(utf, "UTF-8");
		}
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeInt(this.width);
		out.writeInt(this.height);
		out.writeUTF(this.name);
		out.writeUTF(this.obf0);
		out.writeInt(this.fp);
		out.writeInt(this.background);
		out.writeInt(this.obf1);
		out.writeBoolean(this.allowPlayerTeleport);
		out.writeBoolean(this.showDisplays);
		out.writeShort(this.clientXML.length);
		for (String xml: this.clientXML) {
			byte[] utf = xml.getBytes("UTF-8");
			out.writeInt(utf.length);
			out.write(utf);
		}
		out.writeShort(this.extraXML.length);
		for (String xml: this.extraXML) {
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
