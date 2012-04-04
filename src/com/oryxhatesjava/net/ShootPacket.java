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
 * <p>TODO document this type</p>
 *
 * <p>Started Mar 10, 2011</p>
 *
 * @author Furyhunter
 */
public class ShootPacket extends Packet implements Parsable {
    
    public int bulletId;
    public int ownerId;
    public int containerType;
    public Location startingPos;
    public float angle;
    public int damage;
    
    public ShootPacket(int bulletId, int ownerId, int containerType,
            Location startingPos, float angle, int damage) {
    	type = Packet.SHOOT;
        this.bulletId = bulletId;
        this.ownerId = ownerId;
        this.containerType = containerType;
        this.startingPos = startingPos.clone();
        this.angle = angle;
        this.damage = damage;
    }
    
    public ShootPacket(DataInput read) {
        try {
            type = Packet.SHOOT;
            parseFromDataInput(read);
        } catch (IOException e) {
            
        }
    }
    
    public ShootPacket() {
    	type = Packet.SHOOT;
    }
    
    @Override
    public void parseFromDataInput(DataInput read) throws IOException {
        bulletId = read.readUnsignedByte();
        ownerId = read.readInt();
        containerType = read.readShort();
        startingPos = new Location(read);
        angle = read.readFloat();
        damage = read.readShort();
    }
    
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
        write.writeByte(bulletId);
        write.writeInt(ownerId);
        write.writeShort(containerType);
        startingPos.writeToDataOutput(write);
        write.writeFloat(angle);
        write.writeShort(damage);
    }
    
    @Override
    public String toString() {
        return "SHOOT " + bulletId + " " + ownerId + " " + containerType + " " + startingPos + " " + angle + " " + damage;
    }
}
