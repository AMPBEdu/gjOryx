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

package com.oryxhatesjava.net;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

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
    
    public ByteArrayDataOutput() {
        data = ByteBuffer.allocate(4096);
    }
    
    public ByteArrayDataOutput(int size) {
    	data = ByteBuffer.allocate(size);
    }
    
    public ByteArrayDataOutput(byte[] dst) {
        data = ByteBuffer.wrap(dst);
    }
    
    public byte[] getArray() {
        return Arrays.copyOf(data.array(), data.position());
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#write(int)
     */
    @Override
    public void write(int b) throws IOException {
        writeByte(b);
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
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeByte(int)
     */
    @Override
    public void writeByte(int v) throws IOException {
        data.put((byte) v);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeShort(int)
     */
    @Override
    public void writeShort(int v) throws IOException {
        data.putShort((short) v);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeChar(int)
     */
    @Override
    public void writeChar(int v) throws IOException {
        data.putChar((char) v);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeInt(int)
     */
    @Override
    public void writeInt(int v) throws IOException {
        data.putInt(v);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeLong(long)
     */
    @Override
    public void writeLong(long v) throws IOException {
        data.putLong(v);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeFloat(float)
     */
    @Override
    public void writeFloat(float v) throws IOException {
        data.putFloat(v);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataOutput#writeDouble(double)
     */
    @Override
    public void writeDouble(double v) throws IOException {
        data.putDouble(v);
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
