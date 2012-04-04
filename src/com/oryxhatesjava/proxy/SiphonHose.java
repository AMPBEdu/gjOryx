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

package com.oryxhatesjava.proxy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import net.clarenceho.crypto.RC4;
import net.clarenceho.crypto.RC4State;

import com.oryxhatesjava.net.ByteArrayDataOutput;
import com.oryxhatesjava.net.Packet;

/**
 * <p>
 * Takes an InputStream and siphons it through an OutputStream after analyzing
 * it for RotMG data.
 * </p>
 * <p>
 * Started Mar 2, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class SiphonHose implements Runnable {
    
    public DataOutputStream replyTo;
    public DataInputStream recv;
    public RC4 cipher;
    private PrintWriter fileOut;
    private String name;
    public static final int[] FILTER = {Packet.NEW_TICK, Packet.MOVE, Packet.UPDATE, Packet.PING, Packet.PONG, Packet.ALLYSHOOT, Packet.SHOOT};
    
    public SiphonHose(InputStream recv, OutputStream replyTo, byte[] key, String name) {
        this.recv = new DataInputStream(recv);
        this.replyTo = new DataOutputStream(replyTo);
        this.cipher = new RC4(key);
        this.name = name;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                int length = recv.readInt();
                int type = recv.readByte();
                byte[] buf = new byte[length - 5];
                recv.readFully(buf);
                RC4State oldState = cipher.getState();
                byte[] decr = cipher.rc4(buf);
                Packet pkt = Packet.parse(type, decr);
                cipher.setState(oldState);
                
                // filter
                boolean print = true;
                for (int i : FILTER) {
                	if (pkt.type == i) {
                		print = false;
                		break;
                	}
                }
                if (print) {
                	System.out.println(name + ": " + pkt);
                }
            	if (fileOut != null) {
                	fileOut.println(pkt);
            	}
                
                byte[] recr;
                ByteArrayDataOutput bado = new ByteArrayDataOutput(buf.length);
                pkt.writeToDataOutput(bado);
                recr = bado.getArray();
                if (Arrays.hashCode(recr) != Arrays.hashCode(decr)) {
                	System.err.println(Arrays.toString(decr));
                	System.err.println(Arrays.toString(recr));
                	System.err.println(decr.length);
                	System.err.println(recr.length);
                	System.err.println("they don't match");
                	return;
                }
                recr = cipher.rc4(recr);
                replyTo.writeInt(length);
                replyTo.writeByte(type);
                replyTo.write(recr);
                replyTo.flush();
            }
        } catch (IOException e) {
            System.err.println("Error on SiphonHose "
                    + Thread.currentThread().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out
                .println("End SiphonHose " + Thread.currentThread().getName());
        
        try {
        	if (fileOut != null) {
        		fileOut.close();
        	}
            recv.close();
            replyTo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void openOutputFile(String output) throws IOException {
        FileWriter writ = new FileWriter(output);
        fileOut = new PrintWriter(writ, true);
    }
}
