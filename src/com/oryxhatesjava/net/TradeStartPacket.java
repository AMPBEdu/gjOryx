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
import java.util.List;
import java.util.Vector;

import com.oryxhatesjava.net.data.Item;
import com.oryxhatesjava.net.data.Parsable;
import com.oryxhatesjava.util.Serializer;

public class TradeStartPacket extends Packet implements Parsable {

	public List<Item> myItems;
	public String yourName;
	public List<Item> yourItems;
	
	public TradeStartPacket(DataInput in) {
		try {
			type = Packet.TRADESTART;
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		int size = in.readShort();
		myItems = new Vector<Item>(size);
		for (int i = 0; i < size; i++) {
			myItems.add(new Item(in));
		}
		yourName = in.readUTF();
		size = in.readShort();
		yourItems = new Vector<Item>(size);
		for (int i = 0; i < size; i++) {
			yourItems.add(new Item(in));
		}
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		Serializer.writeArray(out, myItems.toArray(new Parsable[myItems.size()]));
		out.writeUTF(yourName);
		Serializer.writeArray(out, yourItems.toArray(new Parsable[yourItems.size()]));
	}
	
	@Override
	public String toString() {
		return "TRADESTART " + myItems + " " + yourName + " " + yourItems;
	}
}
