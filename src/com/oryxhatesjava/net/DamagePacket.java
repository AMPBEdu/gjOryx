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

public class DamagePacket extends Packet implements Parsable {

	public int targetId;
	public int[] effects;
	public int damageAmount;
	public boolean kill;
	public int bulletId; //uint
	public int objectId;
	
	
	public DamagePacket(DataInput read) {
		this.type = Packet.DAMAGE;
		try {
			parseFromDataInput(read);
		} catch (IOException e) {
			
		}
	}
	
	public DamagePacket() {
		type = Packet.DAMAGE;
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeInt(targetId);
		out.writeByte(effects.length);
		for (int i : effects) {
			out.writeByte(i);
		}
		out.writeShort(damageAmount);
		out.writeBoolean(kill);
		out.writeByte(bulletId);
		out.writeInt(objectId);
	}

	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		targetId = in.readInt();
		int recs = in.readUnsignedByte();
		effects = new int[recs];
		for (int i = 0; i < recs; i++) {
			effects[i] = in.readUnsignedByte();
		}
		damageAmount = in.readUnsignedShort();
		kill = in.readBoolean();
		bulletId = in.readUnsignedByte();
		objectId = in.readInt();
	}
	
	@Override
	public String toString() {
		return "DAMAGE " + targetId + " " + effects + " " + damageAmount + " " + kill + " " + bulletId + " " + objectId;
	}

}
