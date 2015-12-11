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
import java.util.List;
import java.util.Vector;

import com.oryxhatesjava.net.data.ObjectStatus;
import com.oryxhatesjava.net.data.Parsable;
import com.oryxhatesjava.net.data.Tile;
import com.oryxhatesjava.util.Serializer;

public class UpdatePacket extends Packet implements Parsable {

	public List<Tile> tiles;
	public List<ObjectStatus> newobjs;
	public int[] drops; //int array
	
	public UpdatePacket(DataInput in) {
		try {
			this.type = Packet.UPDATE;
			parseFromDataInput(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public UpdatePacket() {
		type = Packet.UPDATE;
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		
		if (tiles != null && tiles.size() > 0) {
			Serializer.writeArray(out, tiles.toArray(new Parsable[tiles.size()]));
		} else {
			out.writeShort(0);
		}
		
		if (newobjs != null && newobjs.size() > 0) {
			Serializer.writeArray(out, newobjs.toArray(new Parsable[newobjs.size()]));
		} else {
			out.writeShort(0);
		}
		
		if (drops != null) {
			Serializer.writeArray(out, drops, Serializer.AS_INT);
		} else {
			out.writeShort(0);
		}
	}

	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		short size = in.readShort();
		System.out.println(size);
		System.out.println("Tiles in size: " + size);
		tiles = new Vector<Tile>(size);
		
		if (size > 0) {
			tiles = new Vector<Tile>();
			for (int i = 0; i < size; i++) {
				tiles.add(new Tile(in));
			}
		} else {
			tiles = null;
		}
		
		size = in.readShort();
		if (size > 0) {
			newobjs = new Vector<ObjectStatus>(size);
			for (int i = 0; i < size; i++) {
				newobjs.add(new ObjectStatus(in));
			}
		} else {
			newobjs = null;
		}
		
		size = in.readShort();
		if (size > 0) {
			drops = new int[size];
			for (int i = 0; i < size; i++) {
				drops[i] = in.readInt();
			}
		} else {
			drops = null;
		}
	}
	
	@Override
	public String toString() {
		return "UPDATE " + tiles + " " + newobjs + " " + Arrays.toString(drops);
	}

}
