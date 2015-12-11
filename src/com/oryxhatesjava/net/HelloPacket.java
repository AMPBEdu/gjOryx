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
    
	public int Random1 = 0;
	public int Random2 = 0;
	public String buildVersion = "";
	//public int obf0;
	public int gameId = 0;
	public String guid = "";
	public String password = "";
	public String secret = "";
	public int keyTime;
	public byte[] key = new byte[0];
	public byte[] obf1 = new byte[0];
	public String obf2 = "";
	public String obf3 = "";
	public String obf4 = "";
	public String obf5 = "";
	public String obf6 = "";
	public int obf7 = 0;
    
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
		this.Random1 = read.readInt();
		this.password = read.readUTF();
		this.Random2 = read.readInt();
		this.secret = read.readUTF();
		this.keyTime = read.readInt();
		this.key = new byte[read.readShort()];
		read.readFully(this.key);
		this.obf1 = new byte[read.readInt()];
		read.readFully(this.obf1);
		this.obf2 = read.readUTF();
		this.obf3 = read.readUTF();
		this.obf4 = read.readUTF();
		this.obf5 = read.readUTF();
		this.obf6 = read.readUTF();
        
    }
    
    @Override
    public String toString() {
        return "HELLO " + buildVersion + " " + gameId + " guid=" + guid + " pw="
                + password + " secret=" + secret + " " + keyTime + " " + Arrays.toString(key);
    }
    
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
    	write.writeUTF(this.buildVersion);
		//write.writeInt(this.obf0);
		write.writeInt(this.gameId);
		write.writeUTF(this.guid);
		write.writeInt(Random1);
		write.writeUTF(this.password);
		write.writeInt(Random2);
		write.writeUTF(this.secret);
		write.writeInt(this.keyTime);
		write.writeShort(this.key.length);
		write.write(this.key);
		write.writeInt(this.obf1.length);
		write.write(this.obf1);
		write.writeUTF(this.obf2);
		write.writeUTF(this.obf3);
		write.writeUTF(this.obf4);
		write.writeUTF(this.obf5);
		write.writeUTF(this.obf6);
		System.out.println(write.toString());
    }
}
