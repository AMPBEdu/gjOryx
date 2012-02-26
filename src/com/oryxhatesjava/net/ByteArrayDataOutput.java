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

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * <p>
 * Implements data output on a byte array, very similar to the ByteBuffer class.
 * </p>
 * <p>
 * Started Mar 4, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class ByteArrayDataOutput implements DataOutput {
    
    private ByteBuffer data;
    private int length;
    
    public ByteArrayDataOutput() {
        data = ByteBuffer.allocate(4096);
    }
    
    public ByteArrayDataOutput(byte[] dst) {
        data = ByteBuffer.wrap(dst);
    }
    
    public byte[] getArray() {
        byte[] ret = data.array();
        byte[] pack = new byte[length];
        for (int i = 0; i < length; i++) {
            pack[i] = ret[i];
        }
        return pack;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#write(int)
     */
    @Override
    public void write(int b) throws IOException {
        writeByte(b);
        length += 1;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#write(byte[])
     */
    @Override
    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#write(byte[], int, int)
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        data.put(b, off, len);
        length += len;
        
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeBoolean(boolean)
     */
    @Override
    public void writeBoolean(boolean v) throws IOException {
        if (v) {
            data.put((byte) 0x01);
        } else {
            data.put((byte) 0x00);
        }
        length += 1;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeByte(int)
     */
    @Override
    public void writeByte(int v) throws IOException {
        data.put((byte) v);
        length += 1;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeShort(int)
     */
    @Override
    public void writeShort(int v) throws IOException {
        data.putShort((short) v);
        length += 2;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeChar(int)
     */
    @Override
    public void writeChar(int v) throws IOException {
        data.putChar((char) v);
        length += 2;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeInt(int)
     */
    @Override
    public void writeInt(int v) throws IOException {
        data.putInt(v);
        length += 4;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeLong(long)
     */
    @Override
    public void writeLong(long v) throws IOException {
        data.putLong(v);
        length += 8;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeFloat(float)
     */
    @Override
    public void writeFloat(float v) throws IOException {
        data.putFloat(v);
        length += 4;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeDouble(double)
     */
    @Override
    public void writeDouble(double v) throws IOException {
        data.putDouble(v);
        length += 8;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeBytes(java.lang.String)
     */
    @Override
    public void writeBytes(String s) throws IOException {
    	if (s.length() == 0) {
    		return;
    	}
    	char[] c = s.toCharArray();
    	for (char cc : c) {
    		writeByte(cc);
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeChars(java.lang.String)
     */
    @Override
    public void writeChars(String s) throws IOException {
    	if (s.length() == 0) {
    		return;
    	}
        char[] c = s.toCharArray();
        for (char ch : c) {
            writeChar(ch);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeUTF(java.lang.String)
     */
    @Override
    public void writeUTF(String s) throws IOException {
        byte[] bytes = s.getBytes("UTF-8");
        writeShort(bytes.length);
        write(bytes);
    }
    
}
