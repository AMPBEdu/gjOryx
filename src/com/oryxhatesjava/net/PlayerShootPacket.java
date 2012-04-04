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

import com.oryxhatesjava.net.data.Location;
import com.oryxhatesjava.net.data.Parsable;

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
public class PlayerShootPacket extends Packet implements Parsable {
    
    public int time;
    public int bulletId; // uint
    public short containerType;
    public Location startingPos;
    public float angle;
    
    public PlayerShootPacket(int time, byte bulletId, short containerType,
            Location startingPos, float angle) {
        this.type = Packet.PLAYERSHOOT;
        this.time = time;
        this.bulletId = bulletId;
        this.containerType = containerType;
        this.startingPos = startingPos.clone();
        this.angle = angle;
    }
    
    public PlayerShootPacket(DataInput read) {
        try {
            this.type = Packet.PLAYERSHOOT;
            parseFromDataInput(read);
        } catch (IOException e) {
            
        }
    }
    
    public PlayerShootPacket() {
    	type = Packet.PLAYERSHOOT;
    }
    
    @Override
    public void parseFromDataInput(DataInput read) throws IOException {
        time = read.readInt();
        bulletId = read.readUnsignedByte();
        containerType = read.readShort();
        startingPos = new Location(read.readFloat(), read.readFloat());
        angle = read.readFloat();
    }
    
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
        write.writeInt(time);
        write.writeByte(bulletId);
        write.writeShort(containerType);
        startingPos.writeToDataOutput(write);
        write.writeFloat(angle);
    }
    
    /*
     * (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#toString()
     */
    @Override
    public String toString() {
        return "PLAYERSHOOT " + time + " " + bulletId + " " + containerType
                + " " + startingPos + " " + angle;
    }
}
