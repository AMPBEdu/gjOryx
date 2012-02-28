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

import com.oryxhatesjava.net.data.Parsable;

public class FilePacket extends Packet implements Parsable {

	public String filename; //int
	public String file; //UTF
	
	public FilePacket(DataInput in) {
		try {
			type = Packet.FILE;
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	public FilePacket() {
		type = Packet.FILE;
	}
	
	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		filename = in.readUTF();
		
		int size = in.readInt();
		byte[] buf = new byte[size];
		in.readFully(buf);
		file = new String(buf, Charset.forName("UTF-8"));
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeUTF(filename);
		byte[] buf = file.getBytes("UTF-8");
		out.writeInt(buf.length);
		out.write(buf);
	}
	
	@Override
	public String toString() {
		return "FILE " + filename + " " + file;
	}
}
