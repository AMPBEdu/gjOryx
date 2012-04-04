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
import java.io.EOFException;
import java.io.IOException;

import com.oryxhatesjava.net.data.Location;
import com.oryxhatesjava.net.data.Parsable;


/**
 * @author Furyhunter
 */
public class EnemyShootPacket extends Packet implements Parsable {
    
    public int bulletId;
    public int ownerId;
    public int bulletType;
    public Location startingPos;
    public float angle;
    public int damage;
    public boolean hasMultiProjectiles;
    public int numShots;
    public float angleInc;
    
    public EnemyShootPacket(DataInput read) {
        try {
            type = Packet.ENEMYSHOOT;
            parseFromDataInput(read);
        } catch (IOException e) {
            
        }
    }
    
    public EnemyShootPacket() {
    	type = Packet.ENEMYSHOOT;
    }
    
    @Override
    public void parseFromDataInput(DataInput read) throws IOException {
        bulletId = read.readUnsignedByte();
        ownerId = read.readInt();
        bulletType = read.readUnsignedByte();
        startingPos = new Location(read);
        angle = read.readFloat();
        damage = read.readShort();
        try {
        	numShots = read.readUnsignedByte();
        	angleInc = read.readFloat();
        	hasMultiProjectiles = true;
        } catch (EOFException ex) {
        	hasMultiProjectiles = false;
        }
    }
    
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
        write.writeByte(bulletId);
        write.writeInt(ownerId);
        write.writeByte(bulletType);
        startingPos.writeToDataOutput(write);
        write.writeFloat(angle);
        write.writeShort(damage);
        if (hasMultiProjectiles) {
        	write.writeByte(numShots);
        	write.writeFloat(angleInc);
        }
    }
    
    @Override
    public String toString() {
        return "ENEMYSHOOT " + bulletId + " " + ownerId + " " + bulletType + " " + startingPos + " " + angle + " " + damage + " " + numShots + " " + angleInc;
    }
}
