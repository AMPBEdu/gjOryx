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
import java.nio.charset.Charset;
import java.util.Arrays;

import com.oryxhatesjava.net.data.Parsable;

/**
 * <p>
 * Sent by client to ensure it is up to date.
 * </p>
 * <p>
 * Started Mar 2, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class HelloPacket extends Packet implements Parsable {
    
    public String buildVersion;
    public int gameId; //int
    public String guid;
    public String password;
    public String secret;
    public int keyTime; //int
    public byte[] key;
    public String unkStr;
    
    public HelloPacket(DataInput read) {
        try {
            parseFromDataInput(read);
            type = Packet.HELLO;
        } catch (IOException e) {
            
        }
    }
    
    @Override
    public void parseFromDataInput(DataInput read) throws IOException {
        this.buildVersion = read.readUTF();
        this.gameId = read.readInt();
        this.guid = read.readUTF();
        this.password = read.readUTF();
        secret = read.readUTF();
        keyTime = read.readInt();
        int size = read.readUnsignedShort();
        if (size > 0) {
        	key = new byte[size];
        	read.readFully(key);
        }
        
        size = read.readInt();
        if (size > 0) {
        	byte[] buf = new byte[size];
        	read.readFully(buf);
        	unkStr = new String(buf, Charset.forName("UTF-8"));
        }
        
    }
    
    @Override
    public String toString() {
        return "HELLO " + buildVersion + " " + gameId + " " + guid + " "
                + password + " " + secret + " " + keyTime + " " + Arrays.toString(key) + " " + unkStr;
    }
    
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
        write.writeUTF(buildVersion);
        write.writeInt(gameId);
        write.writeUTF(guid);
        write.writeUTF(password);
        write.writeUTF(secret);
        write.writeInt(keyTime);
        
        if (key != null) {
            write.writeShort(key.length);
            write.write(key);
        } else {
        	write.writeShort(0);
        }
        
        if (unkStr != null) {
        	byte[] buf = unkStr.getBytes("UTF-8");
        	write.writeInt(buf.length);
        	write.write(buf);
        } else {
        	write.writeInt(0);
        }
    }
}
