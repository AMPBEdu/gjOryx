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
import java.util.List;
import java.util.Vector;

import com.oryxhatesjava.util.Serializer;

public class ObjectStatusData implements Parsable {
	
	public int objectId; //int
	public Location pos;
	public List<StatData> stats;
	
	public ObjectStatusData() {
		this(0, new Location(), new Vector<StatData>());
	}
	
	public ObjectStatusData(int id, Location l, List<StatData> stat) {
		objectId = id;
		pos = l.clone();
		stats = stat;
	}
	
	public ObjectStatusData(DataInput in) {
		try {
			parseFromDataInput(in);
			
		} catch (IOException e) {
			objectId = 0;
			pos = new Location();
			stats = new Vector<StatData>();
		}
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeInt(objectId);
		pos.writeToDataOutput(out);
		Serializer.writeArray(out, stats.toArray(new Parsable[stats.size()]));
	}

	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		objectId = in.readInt();
		pos = new Location(in);
		
		int size = in.readShort();
		stats = new Vector<StatData>();
		for (int i = 0; i < size; i++) {
			StatData inp = new StatData(in);
			stats.add(inp);
		}
	}
	
	public void updateStat(StatData in) {
		for (StatData myStat : stats) {
			if (in.type == myStat.type) {
				myStat.value = in.value;
				myStat.valueString = in.valueString;
				return;
			}
		}
		stats.add(in);
	}
	
	public StatData getStat(int type) {
		StatData ret = null;
		for (StatData s : stats) {
			if (s.type == type) {
				ret = s;
			}
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return "ObjectStatusData {" + objectId + " " + pos + " " + stats + "}";
	}

}
