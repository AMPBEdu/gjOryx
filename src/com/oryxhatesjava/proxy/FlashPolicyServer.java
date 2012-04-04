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
                if (clIs.read(buf) > -1) {
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
