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

/**
 * <p>
 * TODO document this type
 * </p>
 * <p>
 * Started Mar 5, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class Location implements Parsable {
    
    public float x;
    public float y;
    
    public Location(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Location() {
        this.x = 0;
        this.y = 0;
    }
    
    public Location(DataInput in) {
		try {
			parseFromDataInput(in);
		} catch (IOException e) {
			x = 0;
			y = 0;
		}
	}

	@Override
    public Location clone() {
        return new Location(this.x, this.y);
    }
    
    @Override
    public void writeToDataOutput(DataOutput out) throws IOException {
        out.writeFloat(x);
        out.writeFloat(y);
    }
    
    @Override
    public void parseFromDataInput(DataInput in) throws IOException {
        this.x = in.readFloat();
        this.y = in.readFloat();
    }
    
    @Override
    public String toString() {
        return "Location {" + x + ", " + y + "}";
    }
}
