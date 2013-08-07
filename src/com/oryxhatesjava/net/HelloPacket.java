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
    
    public String pk = "";
    public String Tq = "";
    public String H = "";
    public String playPlatform = "";      
    
    public HelloPacket(DataInput read) {
        try {
            parseFromDataInput(read);
            type = Packet.HELLO;
        } catch (IOException e) {
            
        }
    }
    
    public HelloPacket() {
    	type = Packet.HELLO;
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
        
	    pk = read.readUTF();
	    Tq = read.readUTF();
	    H = read.readUTF();
	    playPlatform = read.readUTF();        
        
        
    }
    
    
@Override

    public String toString() {
        return "HELLO " + buildVersion + " " + gameId + " guid=" + guid + " pw="
                + password + " secret=" + secret + " " + keyTime + " " + Arrays.toString(key) + " " + unkStr;
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

        write.writeUTF(pk);
        write.writeUTF(Tq);
        write.writeUTF(H);
        write.writeUTF(playPlatform);        
        
        
        
    }
}
