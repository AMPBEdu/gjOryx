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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 * Implements a basic cross-domain policy server for Flash.
 * </p>
 * <p>
 * Started Feb 27, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class FlashPolicyServer implements Runnable {
    
    public ServerSocket server;
    
    public static final String POLICY = new String(new byte[] { (byte) 0x3C,
            (byte) 0x3F, (byte) 0x78, (byte) 0x6D, (byte) 0x6C, (byte) 0x20,
            (byte) 0x76, (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x69,
            (byte) 0x6F, (byte) 0x6E, (byte) 0x3D, (byte) 0x22, (byte) 0x31,
            (byte) 0x2E, (byte) 0x30, (byte) 0x22, (byte) 0x3F, (byte) 0x3E,
            (byte) 0x3C, (byte) 0x21, (byte) 0x44, (byte) 0x4F, (byte) 0x43,
            (byte) 0x54, (byte) 0x59, (byte) 0x50, (byte) 0x45, (byte) 0x20,
            (byte) 0x63, (byte) 0x72, (byte) 0x6F, (byte) 0x73, (byte) 0x73,
            (byte) 0x2D, (byte) 0x64, (byte) 0x6F, (byte) 0x6D, (byte) 0x61,
            (byte) 0x69, (byte) 0x6E, (byte) 0x2D, (byte) 0x70, (byte) 0x6F,
            (byte) 0x6C, (byte) 0x69, (byte) 0x63, (byte) 0x79, (byte) 0x20,
            (byte) 0x53, (byte) 0x59, (byte) 0x53, (byte) 0x54, (byte) 0x45,
            (byte) 0x4D, (byte) 0x20, (byte) 0x22, (byte) 0x2F, (byte) 0x78,
            (byte) 0x6D, (byte) 0x6C, (byte) 0x2F, (byte) 0x64, (byte) 0x74,
            (byte) 0x64, (byte) 0x73, (byte) 0x2F, (byte) 0x63, (byte) 0x72,
            (byte) 0x6F, (byte) 0x73, (byte) 0x73, (byte) 0x2D, (byte) 0x64,
            (byte) 0x6F, (byte) 0x6D, (byte) 0x61, (byte) 0x69, (byte) 0x6E,
            (byte) 0x2D, (byte) 0x70, (byte) 0x6F, (byte) 0x6C, (byte) 0x69,
            (byte) 0x63, (byte) 0x79, (byte) 0x2E, (byte) 0x64, (byte) 0x74,
            (byte) 0x64, (byte) 0x22, (byte) 0x3E, (byte) 0x20, (byte) 0x20,
            (byte) 0x3C, (byte) 0x63, (byte) 0x72, (byte) 0x6F, (byte) 0x73,
            (byte) 0x73, (byte) 0x2D, (byte) 0x64, (byte) 0x6F, (byte) 0x6D,
            (byte) 0x61, (byte) 0x69, (byte) 0x6E, (byte) 0x2D, (byte) 0x70,
            (byte) 0x6F, (byte) 0x6C, (byte) 0x69, (byte) 0x63, (byte) 0x79,
            (byte) 0x3E, (byte) 0x20, (byte) 0x20, (byte) 0x3C, (byte) 0x73,
            (byte) 0x69, (byte) 0x74, (byte) 0x65, (byte) 0x2D, (byte) 0x63,
            (byte) 0x6F, (byte) 0x6E, (byte) 0x74, (byte) 0x72, (byte) 0x6F,
            (byte) 0x6C, (byte) 0x20, (byte) 0x70, (byte) 0x65, (byte) 0x72,
            (byte) 0x6D, (byte) 0x69, (byte) 0x74, (byte) 0x74, (byte) 0x65,
            (byte) 0x64, (byte) 0x2D, (byte) 0x63, (byte) 0x72, (byte) 0x6F,
            (byte) 0x73, (byte) 0x73, (byte) 0x2D, (byte) 0x64, (byte) 0x6F,
            (byte) 0x6D, (byte) 0x61, (byte) 0x69, (byte) 0x6E, (byte) 0x2D,
            (byte) 0x70, (byte) 0x6F, (byte) 0x6C, (byte) 0x69, (byte) 0x63,
            (byte) 0x69, (byte) 0x65, (byte) 0x73, (byte) 0x3D, (byte) 0x22,
            (byte) 0x6D, (byte) 0x61, (byte) 0x73, (byte) 0x74, (byte) 0x65,
            (byte) 0x72, (byte) 0x2D, (byte) 0x6F, (byte) 0x6E, (byte) 0x6C,
            (byte) 0x79, (byte) 0x22, (byte) 0x2F, (byte) 0x3E, (byte) 0x20,
            (byte) 0x20, (byte) 0x3C, (byte) 0x61, (byte) 0x6C, (byte) 0x6C,
            (byte) 0x6F, (byte) 0x77, (byte) 0x2D, (byte) 0x61, (byte) 0x63,
            (byte) 0x63, (byte) 0x65, (byte) 0x73, (byte) 0x73, (byte) 0x2D,
            (byte) 0x66, (byte) 0x72, (byte) 0x6F, (byte) 0x6D, (byte) 0x20,
            (byte) 0x64, (byte) 0x6F, (byte) 0x6D, (byte) 0x61, (byte) 0x69,
            (byte) 0x6E, (byte) 0x3D, (byte) 0x22, (byte) 0x2A, (byte) 0x22,
            (byte) 0x20, (byte) 0x74, (byte) 0x6F, (byte) 0x2D, (byte) 0x70,
            (byte) 0x6F, (byte) 0x72, (byte) 0x74, (byte) 0x73, (byte) 0x3D,
            (byte) 0x22, (byte) 0x2A, (byte) 0x22, (byte) 0x20, (byte) 0x2F,
            (byte) 0x3E, (byte) 0x3C, (byte) 0x2F, (byte) 0x63, (byte) 0x72,
            (byte) 0x6F, (byte) 0x73, (byte) 0x73, (byte) 0x2D, (byte) 0x64,
            (byte) 0x6F, (byte) 0x6D, (byte) 0x61, (byte) 0x69, (byte) 0x6E,
            (byte) 0x2D, (byte) 0x70, (byte) 0x6F, (byte) 0x6C, (byte) 0x69,
            (byte) 0x63, (byte) 0x79, (byte) 0x3E, (byte) 0x0A, (byte) 0x00 });
    
    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            server = new ServerSocket(843);
            System.out.println("Policy server init.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        try {
            while (true) {
                Socket client = server.accept();
                System.out.println("Policy server got connection");
                InputStream clIs = client.getInputStream();
                OutputStream clOs = client.getOutputStream();
                PrintWriter write = new PrintWriter(clOs);
                byte[] buf = new byte[4096];
                int ret;
                if ((ret = clIs.read(buf)) > -1) {
                    String str = new String(buf);
                    System.out.println(str);
                    if (str.contains("policy-file-request")) {
                        System.out.println("Sending policy xml");
                        write.print(POLICY);
                        write.flush();
                    }
                }
                write.close();
                client.close();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
