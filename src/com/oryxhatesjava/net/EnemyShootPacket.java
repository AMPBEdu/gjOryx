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

import com.oryxhatesjava.net.data.Location;
import com.oryxhatesjava.net.data.Parsable;


/**
 * @author Furyhunter
 */
public class EnemyShootPacket extends Packet implements Parsable {
    
    public int bulletId;
    public int ownerId;
    public int containerType;
    public Location startingPos;
    public float angle;
    public int damage;
    public int numShots;
    public float angleInc;
    
    public EnemyShootPacket(int bulletId, int ownerId, int containerType,
            Location startingPos, float angle, int damage) {
    	type = Packet.ENEMYSHOOT;
        this.bulletId = bulletId;
        this.ownerId = ownerId;
        this.containerType = containerType;
        this.startingPos = startingPos.clone();
        this.angle = angle;
        this.damage = damage;
    }
    
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
        containerType = read.readShort();
        startingPos = new Location(read);
        angle = read.readFloat();
        damage = read.readShort();
        numShots = read.readInt();
        angleInc = read.readFloat();
    }
    
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
        write.writeByte(bulletId);
        write.writeInt(ownerId);
        write.writeShort(containerType);
        startingPos.writeToDataOutput(write);
        write.writeFloat(angle);
        write.writeShort(damage);
        write.writeInt(numShots);
        write.writeFloat(angleInc);
    }
    
    @Override
    public String toString() {
        return "ENEMYSHOOT " + bulletId + " " + ownerId + " " + containerType + " " + startingPos + " " + angle + " " + damage + " " + numShots + " " + angleInc;
    }
}
