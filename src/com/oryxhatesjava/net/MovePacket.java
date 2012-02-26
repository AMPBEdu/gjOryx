/*
 * Copyright (C) 2011 Furyhunter <furyhunter600@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the creator nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
    
    public int messageId;
    public int tickId;
    public int time;
    public Location newPosition;
    public List<HistoricalLocation> records;
    
    public MovePacket(int messageId, int time, Location newPosition) {
        this.type = Packet.MOVE;
        
        this.messageId = messageId;
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
    
    /*
     * (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#parseFromDataInput(java.io.DataInput)
     */
    @Override
    public void parseFromDataInput(DataInput read) throws IOException {
        messageId = read.readInt();
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
        write.writeInt(messageId);
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
        return "MOVE " + messageId + " " + time + " " + newPosition + " (" + records.size() + " records)";
    }
}
