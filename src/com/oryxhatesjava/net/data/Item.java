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

public class Item implements Parsable {

	public int objectId; //int
	public int slotId; //ubyte
	public int itemType; //short
	
	public Item() {
		this(0, 0, 0);
	}
	
	public Item(int objectId, int slotId, int itemType) {
		this.objectId = objectId;
		this.slotId = slotId;
		this.itemType = itemType;
	}
	
	public Item(DataInput in) {
		try {
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		objectId = in.readInt();
		slotId = in.readUnsignedByte();
		itemType = in.readShort();
	}

	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeInt(objectId);
		out.writeByte(slotId);
		out.writeShort(itemType);
	}
	
	@Override
	public String toString() {
		return "Item {" + objectId + " " + slotId + " " + itemType + "}";
	}

}
