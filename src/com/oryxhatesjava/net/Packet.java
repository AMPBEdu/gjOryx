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

import com.oryxhatesjava.data.Parsable;
import com.oryxhatesjava.data.Writable;

/**
 * <p>
 * A generic packet.
 * </p>
 * <p>
 * Started Mar 2, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class Packet implements Writable, Parsable {
    
    public static final int FAILURE = 0;
    public static final int CREATE_SUCCESS = 1;
    public static final int CREATE = 2;
    public static final int PLAYERSHOOT = 3;
    public static final int MOVE = 4;
    public static final int PLAYERTEXT = 5;
    public static final int TEXT = 6;
    public static final int SHOOT = 7;
    public static final int DAMAGE = 8;
    public static final int UNKNOWN1 = 9;
    public static final int UPDATE = 10;
    public static final int NOTIFICATION = 12;
    public static final int OBJECTS = 13;
    public static final int INVSWAP = 14;
    public static final int USEITEM = 15;
    public static final int EFFECT = 16;
    public static final int HELLO = 17;
    public static final int GOTO = 18;
    public static final int INVDROP = 19;
    public static final int INVRESULT = 20;
    public static final int RECONNECT = 21;
    public static final int PING = 22;
    public static final int PONG = 23;
    public static final int MAPINFO = 24;
    public static final int LOAD = 25;
    public static final int PIC = 26;
    public static final int SETCONDITION = 27;
    public static final int TELEPORT = 28;
    public static final int USEPORTAL = 30;
    public static final int DEATH = 31;
    public static final int BUY = 33;
    public static final int BUYRESULT = 34;
    public static final int AOE = 35;
    public static final int GROUNDDAMAGE = 36;
    public static final int PLAYERHIT = 37;
    public static final int ENEMYHIT = 38;
    public static final int AOEHIT = 39;
    public static final int SHOOTACK = 40;
    public static final int OTHERHIT = 41;
    public static final int SQUAREHIT = 42;
    public static final int UNKNOWN2 = 43;
    public static final int GOTOACK = 44;
    public static final int STAR = 45;
    public static final int STARLIST = 46;
    public static final int QUESTOBJID = 47;
    
    public int type;
    
    private byte[] data;
    
    protected Packet() {
        
    }
    
    protected Packet(int type, byte[] data) {
        this.type = type;
        this.data = data.clone();
    }
    
    public String toString() {
        return "Unknown " + data;
    }
    
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
        write.write(data);
    }
    
    @Override
    public void parseFromDataInput(DataInput read) throws IOException {
        read.readFully(data);
    }
    
    public static Packet parse(int type, byte[] data) {
        DataInput read = new ByteArrayDataInput(data);
        switch (type) {
            case FAILURE:
                return new FailurePacket(read);
            case CREATE_SUCCESS:
                return new CreateSuccessPacket(read);
            case CREATE:
                return new CreatePacket(read);
            case PLAYERSHOOT:
                return new PlayerShootPacket(read);
            case MOVE:
                return new MovePacket(read);
            case PLAYERTEXT:
                return new PlayerTextPacket(read);
            case HELLO:
                return new HelloPacket(read);
            default:
                return new Packet(type, data);
        }
    }
}
