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

package com.oryxhatesjava.proxy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import net.clarenceho.crypto.RC4;

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
    
    public SiphonHose(InputStream recv, OutputStream replyTo, byte[] key) {
        this.recv = new DataInputStream(recv);
        this.replyTo = new DataOutputStream(replyTo);
        this.cipher = new RC4(key);
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                int length = recv.readInt();
                int type = recv.readByte();
                byte[] buf = new byte[length - 5];
                recv.readFully(buf);
                Packet pkt = Packet.parse(type, cipher.rc4(buf));
                if (fileOut != null) {
                    fileOut.println(pkt);
                }
                replyTo.writeInt(length);
                replyTo.writeByte(type);
                replyTo.write(buf);
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
            fileOut.close();
            recv.close();
            replyTo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void openOutputFile(File output) throws FileNotFoundException {
        fileOut = new PrintWriter(output);
    }
}
