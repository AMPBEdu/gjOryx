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
public class TextPacket extends Packet implements Parsable {
    
    public String name;
    public int objectId;
    public int numStars;
    public int bubbleTime;
    public String recipient;
    public String text;
    public String cleanText;
    
    public TextPacket(String name, int objectId, int numStars, int bubbleTime, String recipient, String text) {
        this.name = new String(name);
        this.objectId = objectId;
        this.numStars = numStars;
        this.bubbleTime = bubbleTime;
        this.recipient = new String(recipient);
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
        numStars = read.readInt();
        bubbleTime = read.readUnsignedByte();
        recipient = read.readUTF();
        text = read.readUTF();
        cleanText = read.readUTF();
    }
    
    /*
     * (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#writeToDataOutput(java.io.DataOutput)
     */
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
        write.writeUTF(name);
        write.writeInt(objectId);
        write.writeInt(numStars);
        write.writeByte(bubbleTime);
        write.writeUTF(recipient);
        write.writeUTF(text);
        write.writeUTF(cleanText);
    }
    
    /*
     * (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#toString()
     */
    @Override
    public String toString() {
        return "TEXT " + name + " " + objectId + " " + numStars + " "
                + bubbleTime + " " + recipient + " " + text + " " + cleanText;
    }
}
