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
public class HelloPacket extends Packet {
    
    public String buildVersion;
    public int gameId;
    public String guid;
    public String password;
    
    public HelloPacket(String buildVersion, int gameId, String guid,
            String password) {
        type = Packet.HELLO;
        this.buildVersion = buildVersion;
        this.gameId = gameId;
        this.guid = guid;
        this.password = password;
    }
    
    public HelloPacket(DataInput read) {
        try {
            parse(read);
            type = Packet.HELLO;
        } catch (IOException e) {
            
        }
    }
    
    @Override
    public void parse(DataInput read) throws IOException {
        this.buildVersion = read.readUTF();
        this.gameId = read.readInt();
        this.guid = read.readUTF();
        this.password = read.readUTF();
    }
    
    @Override
    public String toString() {
        return "HELLO " + buildVersion + " " + gameId + " " + guid + " "
                + password;
    }
    
    @Override
    public void write(DataOutput write) throws IOException {
        write.writeUTF(buildVersion);
        write.writeInt(gameId);
        write.writeUTF(guid);
        write.writeUTF(password);
    }
}
