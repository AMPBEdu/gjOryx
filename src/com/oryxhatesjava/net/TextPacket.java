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
 * TODO document this type
 * </p>
 * <p>
 * Started Mar 5, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class TextPacket extends Packet implements Writable, Parsable {
    
    public String name;
    public int objectId;
    public short objectType;
    public int tex1ld;
    public int tex2ld;
    public int totalFame;
    public String text;
    
    public TextPacket(String name, int objectId, short objectType, int tex1ld,
            int tex2ld, int totalFame, String text) {
        this.name = new String(name);
        this.objectId = objectId;
        this.objectType = objectType;
        this.tex1ld = tex1ld;
        this.tex2ld = tex2ld;
        this.totalFame = totalFame;
        this.text = new String(text);
    }
    
    public TextPacket(DataInput read) {
        try {
            this.type = Packet.TEXT;
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
        name = read.readUTF();
        objectId = read.readInt();
        objectType = read.readShort();
        tex1ld = read.readInt();
        tex2ld = read.readInt();
        totalFame = read.readInt();
        text = read.readUTF();
    }
    
    /*
     * (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#writeToDataOutput(java.io.DataOutput)
     */
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
        write.writeUTF(name);
        write.writeInt(objectId);
        write.writeShort(objectType);
        write.writeInt(tex1ld);
        write.writeInt(tex2ld);
        write.writeInt(totalFame);
        write.writeUTF(text);
    }
    
    /*
     * (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#toString()
     */
    @Override
    public String toString() {
        return "TEXT " + name + " " + objectId + " " + objectType + " "
                + tex1ld + " " + tex2ld + " " + totalFame + " " + text;
    }
}
