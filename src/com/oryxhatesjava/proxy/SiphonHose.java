/*
 * Copyright (C) 2011 Furyhunter
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.CharBuffer;

import net.clarenceho.crypto.RC4;

public class SiphonHose implements Runnable {
    
    public static byte[] KEY = new byte[] { 0x7a, 0x43, 0x56, 0x32, 0x74, 0x73,
            0x59, 0x30, 0x5d, 0x73, 0x3b, 0x7c, 0x5d };
    // public static byte[] KEY = new byte[] { 0x68, 0x50, 0x76, 0x4a, 0x28,
    // 0x52,
    // 0x7d, 0x4d, 0x70, 0x24, 0x2d, 0x63, 0x67 };
    public PrintWriter replyTo;
    public BufferedReader recv;
    
    public SiphonHose(InputStream recv, OutputStream replyTo) {
        this.recv = new BufferedReader(new InputStreamReader(recv));
        this.replyTo = new PrintWriter(replyTo);
    }
    
    @Override
    public void run() {
        int size;
        CharBuffer buf = CharBuffer.allocate(8192);
        RC4 rc = new RC4(KEY);
        try {
            while ((size = recv.read(buf)) > -1) {
                String str;
                byte[] dec = rc.rc4((str = new String(buf.array())));
                // Make decoded string
                String decstr = Thread.currentThread().getName();
                for (int i = 0; i < dec.length; i++) {
                    decstr += Short.toString(dec[i]) + " ";
                }
                buf.clear();
                System.out.println(decstr);
                replyTo.print(str);
                replyTo.flush();
            }
        } catch (IOException e) {
            System.err.println("Error on SiphonHose "
                    + Thread.currentThread().getName() + ": " + e.getMessage());
        }
        
        System.out
                .println("End SiphonHose " + Thread.currentThread().getName());
        
        try {
            recv.close();
            replyTo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
