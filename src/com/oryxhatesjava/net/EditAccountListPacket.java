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

public class EditAccountListPacket extends Packet implements Parsable {

	public int accountListId; //int
	public boolean add;
	public int objectId; //int
	
	public EditAccountListPacket(DataInput in) {
		try {
			type = Packet.EDITACCOUNTLIST;
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	public EditAccountListPacket() {
		type = Packet.EDITACCOUNTLIST;
	}
	
	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		accountListId = in.readInt();
		add = in.readBoolean();
		objectId = in.readInt();
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeInt(accountListId);
		out.writeBoolean(add);
		out.writeInt(objectId);
	}
	
	@Override
	public String toString() {
		return "EDITACCOUNTLIST " + accountListId + " " + add + " " + objectId;
	}
}
