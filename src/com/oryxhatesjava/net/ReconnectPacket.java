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
import java.util.Arrays;

import com.oryxhatesjava.net.data.Parsable;

public class ReconnectPacket extends Packet implements Parsable {
	public String name;
	public String host;
	public int port; //int
	public int gameId; //int
	public int keyTime; //int
	public byte[] key;
	
	public ReconnectPacket(DataInput in) {
		try {
			type = Packet.RECONNECT;
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	public ReconnectPacket() {
		type = Packet.RECONNECT;
	}
	
	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		name = in.readUTF();
		host = in.readUTF();
		port = in.readInt();
		gameId = in.readInt();
		keyTime = in.readInt();
		int size = in.readShort();
		key = new byte[size];
		in.readFully(key);
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeUTF(name);
		out.writeUTF(host);
		out.writeInt(port);
		out.writeInt(gameId);
		out.writeInt(keyTime);
		out.writeShort(key.length);
		out.write(key);
	}
	
	@Override
	public String toString() {
		return "RECONNECT " + name + " " + host + ":" + port + " " + gameId + " " + keyTime + " " + Arrays.toString(key);
	}
}
