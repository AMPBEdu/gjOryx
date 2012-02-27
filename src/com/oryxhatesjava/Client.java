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

package com.oryxhatesjava;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.clarenceho.crypto.RC4;

import com.oryxhatesjava.net.ByteArrayDataOutput;
import com.oryxhatesjava.net.Packet;
import com.oryxhatesjava.proxy.Proxy;

/**
 * <p>
 * TODO document this type
 * </p>
 * <p>
 * Started Feb 27, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class Client implements Runnable {
    
	public static final int PORT = 2050;
	private InetAddress address;
	private Socket socket;
	private RC4 cipherOut;
	private RC4 cipherIn;
	private DataOutputStream write;
	private DataInputStream read;
	
	private List<ClientListener> listeners;
	private Map<ClientListener, PacketFilter> filters;
	
	public Client(InetAddress address) {
		this.address = address;
		listeners = new Vector<ClientListener>();
		filters = new HashMap<ClientListener, PacketFilter>();
		cipherOut = new RC4(Proxy.CLIENTKEY);
		cipherIn = new RC4(Proxy.SERVERKEY);
	}
	
    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // Connect to the server
    	try {
			socket = new Socket(address, PORT);
			write = new DataOutputStream(socket.getOutputStream());
			read = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
    	
    	while (true) {
    		Packet pkt = null;
    		try {
    			int length = read.readInt();
    			int type = read.readByte();
    			byte[] buf = new byte[length-5];
    			read.readFully(buf);
    			pkt = Packet.parse(type, cipherIn.rc4(buf));
    		} catch (IOException e) {
    			e.printStackTrace();
    			break;
    		} finally {
    			for (ClientListener l : listeners) {
    				PacketFilter f = filters.get(l);
    				if (f.select(pkt)) {
    					l.packetReceived(pkt);
    				}
    			}
    		}
    	}
    }
    
    public synchronized void addListener(ClientListener l, PacketFilter filter) {
    	listeners.add(l);
    	filters.put(l, filter);
    }
    
    public synchronized void addListener(ClientListener l, boolean accept, int ... filter) {
    	listeners.add(l);
    	PacketFilter f = new PacketFilter(accept, filter);
    	filters.put(l, f);
    }
    
    public synchronized void removeListener(ClientListener l) {
    	listeners.remove(l);
    	filters.remove(l);
    }
    
    public synchronized void sendPacket(Packet pkt) throws IOException {
    	ByteArrayDataOutput b = new ByteArrayDataOutput(4096);
    	pkt.writeToDataOutput(b);
    	byte[] buf = b.getArray();
    	write.writeInt(buf.length+5);
    	write.writeByte(pkt.type);
    	write.write(cipherOut.rc4(buf));
    }
}
