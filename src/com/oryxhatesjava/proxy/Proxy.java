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

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

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
public class Proxy implements Runnable {
    
    private ServerSocket proxyServerSocket = null;
    private Socket clientSocket = null;
    private Socket serverSocket = null;
    
    public static byte[] CLIENTKEY = new byte[] { 0x7a, 0x43, 0x56, 0x32, 0x74,
            0x73, 0x59, 0x30, 0x5d, 0x73, 0x3b, 0x7c, 0x5d };
    public static byte[] SERVERKEY = new byte[] { 0x68, 0x50, 0x76, 0x4a, 0x28,
            0x52, 0x7d, 0x4d, 0x70, 0x24, 0x2d, 0x63, 0x67 };
    
    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        /*
         * An httpd server needs to be running and hosts needs to point to it in
         * order to fake the server list and make it connect to the localhost;
         * the game connects to the proxy and the proxy connects to the actual
         * game server
         */

        try {
            // Set up server socket
            proxyServerSocket = new ServerSocket(2050);
            System.out.println("Socket opened");
        } catch (UnknownHostException e) {
            System.err.println("This should absolutely NEVER happen. WTF???");
            return;
        } catch (IOException e) {
            System.err.println("Failed to bind 2050: " + e.getMessage());
            return;
        }
        
        // Set up flash policy server
        Thread flashPolicyServer = new Thread(new FlashPolicyServer(),
                "FlashPolicyServer");
        flashPolicyServer.start();
        
        try {
            while (!proxyServerSocket.isClosed()) {
                System.out.println("Waiting for client connection...");
                
                clientSocket = proxyServerSocket.accept();
                String ipString = clientSocket.getInetAddress()
                        .getHostAddress();
                System.out.println("Client connected to proxy: " + ipString);
                
                System.out.println("Connecting to Oryx for " + ipString);
                serverSocket = new Socket("184.72.218.199", 2050); // "Oryx"
                System.out.println("Connected to Oryx for " + ipString);
                
                SiphonHose clientHose = new SiphonHose(
                        clientSocket.getInputStream(),
                        serverSocket.getOutputStream(), CLIENTKEY);
                SiphonHose serverHose = new SiphonHose(
                        serverSocket.getInputStream(),
                        clientSocket.getOutputStream(), SERVERKEY);
                
                Date current = new Date(System.currentTimeMillis());
                clientHose.openOutputFile(new File("c2s-" + current));
                serverHose.openOutputFile(new File("s2c-" + current));
                
                Thread clientThread = new Thread(clientHose, "ClientDaemon");
                Thread serverThread = new Thread(serverHose, "ServerDaemon");
                
                clientThread.start();
                serverThread.start();
                System.out.println("Done setting up siphon hoses, NEEEXT");
            }
        } catch (IOException e) {
            try {
                proxyServerSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    
    /**
     * <p>
     * Starts the proxy as an application.
     * </p>
     * 
     * @param args
     */
    public static void main(String[] args) {
        Proxy p = new Proxy();
        p.run();
    }
    
}
