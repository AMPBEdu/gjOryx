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

import com.oryxhatesjava.net.data.Parsable;

public class NotificationPacket extends Packet implements Parsable {

	public int objectId; //int
	public String text; //UTF
	public int color; //int
	
	public NotificationPacket() {
		this(0, "", 0);
	}
	
	public NotificationPacket(int id, String txt, int col) {
		type = Packet.NOTIFICATION;
		objectId = id;
		text = new String(txt);
		color = col;
	}
	
	public NotificationPacket(DataInput in) {
		try {
			type = Packet.NOTIFICATION;
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeInt(objectId);
		out.writeUTF(text);
		out.writeInt(color);
	}
	
	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		objectId = in.readInt();
		text = in.readUTF();
		color = in.readInt();
	}
	
	@Override
	public String toString() {
		return "NOTIFICATION " + objectId + " " + text + " " + color;
	}
}
