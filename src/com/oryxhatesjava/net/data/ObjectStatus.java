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

public class ObjectStatus implements Parsable {

	public int objectType; //short
	public ObjectStatusData data;
	
	public ObjectStatus() {
		this(0, new ObjectStatusData());
	}
	
	public ObjectStatus(int type, ObjectStatusData data) {
		objectType = type;
		this.data = data;
		
	}
	
	public ObjectStatus(DataInput in) {
		try {
			parseFromDataInput(in);
		} catch (IOException e) {
			e.printStackTrace();
			objectType = 0;
			data = new ObjectStatusData();
		}
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeShort(objectType);
		data.writeToDataOutput(out);
	}

	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		objectType = in.readShort();
		data = new ObjectStatusData(in);
	}
	
	public void update(ObjectStatus status) {
		update(status.data);
	}
	
	public void update(ObjectStatusData data) {
		this.data.pos = data.pos;
		for (StatData stat : data.stats) {
			this.data.updateStat(stat);
		}
	}
	
	@Override
	public String toString() {
		return "GameObject {" + objectType + " " + data + "}";
	}

}
