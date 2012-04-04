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
import java.util.List;
import java.util.Vector;

import com.oryxhatesjava.net.data.HistoricalLocation;
import com.oryxhatesjava.net.data.Location;
import com.oryxhatesjava.net.data.Parsable;
import com.oryxhatesjava.util.Serializer;

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
public class MovePacket extends Packet implements Parsable {
    
    public int tickId;
    public int time;
    public Location newPosition = new Location();
    public List<HistoricalLocation> records = new Vector<HistoricalLocation>();
    
    public MovePacket(int messageId, int time, Location newPosition) {
        this.type = Packet.MOVE;
        
        this.tickId = messageId;
        this.time = time;
        this.newPosition = newPosition.clone();
    }
    
    public MovePacket(DataInput read) {
        try {
            this.type = Packet.MOVE;
            parseFromDataInput(read);
        } catch (IOException e) {
            
        }
    }
    
    public MovePacket() {
		this.type = Packet.MOVE;
	}

	/*
     * (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#parseFromDataInput(java.io.DataInput)
     */
    @Override
    public void parseFromDataInput(DataInput read) throws IOException {
        tickId = read.readInt();
        time = read.readInt();
        newPosition = new Location();
        newPosition.parseFromDataInput(read);
        records = new Vector<HistoricalLocation>();
        int recs = read.readShort();
        while (records.size() < recs) {
        	HistoricalLocation p = new HistoricalLocation();
        	p.parseFromDataInput(read);
        	records.add(p);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#writeToDataOutput(java.io.DataOutput)
     */
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
        write.writeInt(tickId);
        write.writeInt(time);
        newPosition.writeToDataOutput(write);
        Serializer.writeArray(write, records.toArray(new Parsable[records.size()]));
    }
    
    /*
     * (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#toString()
     */
    @Override
    public String toString() {
        return "MOVE " + tickId + " " + time + " " + newPosition + " (" + records.size() + " records)";
    }
}
