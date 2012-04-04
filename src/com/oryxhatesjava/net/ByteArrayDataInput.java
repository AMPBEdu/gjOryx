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

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * <p>
 * Implements data input on a byte array.
 * </p>
 * <p>
 * Started Mar 3, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class ByteArrayDataInput implements DataInput {
    
    private int position;
    private byte[] data;
    private ByteBuffer buffer;
    
    public ByteArrayDataInput(byte[] src) {
        if (src == null) {
            throw new NullPointerException();
        }
        data = src;
        position = 0;
        buffer = ByteBuffer.wrap(data);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readFully(byte[])
     */
    @Override
    public void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readFully(byte[], int, int)
     */
    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
//        if (b == null) {
//            throw new NullPointerException();
//        }
//        if (off < 0 || len < 0 || b.length < off+len) {
//            throw new ArrayIndexOutOfBoundsException();
//        }
//        try {
//            for (int i = 0; i < len; i++) {
//                b[off+i] = data[position];
//                position += 1;
//            }
//            buffer.get(b, 0, len);
//        } catch (ArrayIndexOutOfBoundsException e) {
//            throw new EOFException();
//        }
        
        try {
        	buffer.get(b, 0, len);
        } catch (BufferUnderflowException e) {
        	throw new EOFException();
        } catch (IndexOutOfBoundsException e) {
        	throw new IllegalArgumentException();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#skipBytes(int)
     */
    @Override
    public int skipBytes(int n) throws EOFException {
//        if (position + n > data.length - 1) {
//            int overage = -(data.length - (position + n));
//            position = data.length - 1;
//            return n - overage;
//        }
        buffer.position(buffer.position() + n);
        return n;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readBoolean()
     */
    @Override
    public boolean readBoolean() throws EOFException {
//        try {
//            int value = data[position] & 0xFF;
//            position += 1;
//            if (value > 0)
//                return true;
//            else
//                return false;
//        } catch (ArrayIndexOutOfBoundsException e) {
//            throw new EOFException();
//        }
        
        try {
        	int value = buffer.get();
        	if (value > 0)
        		return true;
        	else
        		return false;
        } catch (BufferUnderflowException e) {
        	throw new EOFException();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readByte()
     */
    @Override
    public byte readByte() throws EOFException {
//        position += 1;
//        try {
//            return data[position - 1];
//        } catch (ArrayIndexOutOfBoundsException e) {
//            throw new EOFException();
//        }
        
        try {
        	return buffer.get();
        } catch (BufferUnderflowException e) {
        	throw new EOFException();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readUnsignedByte()
     */
    @Override
    public int readUnsignedByte() throws EOFException {
//        try {
//            position += 1;
//            return data[position - 1] & 0xFF;
//        } catch (ArrayIndexOutOfBoundsException e) {
//            throw new EOFException();
//        }
        
        try {
        	int value = buffer.get();
        	return value & 0xFF;
        } catch (BufferUnderflowException e) {
        	throw new EOFException();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readShort()
     */
    @Override
    public short readShort() throws EOFException {
//        try {
//            int b1 = data[position];
//            position += 1;
//            int b2 = data[position];
//            position += 1;
//            return (short) ((b1 << 8) + (b2 << 0));
//        } catch (ArrayIndexOutOfBoundsException e) {
//            throw new EOFException();
//        }
        
        try {
        	return buffer.getShort();
        } catch (BufferUnderflowException e) {
        	throw new EOFException();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readUnsignedShort()
     */
    @Override
    public int readUnsignedShort() throws EOFException {
//        try {
//            int b1 = data[position];
//            position += 1;
//            int b2 = data[position];
//            position += 1;
//            return (((b1 & 0xff) << 8) | (b2 & 0xff));
//        } catch (ArrayIndexOutOfBoundsException e) {
//            throw new EOFException();
//        }
        
        try {
        	int value = buffer.getShort();
        	return value & 0xFFFF;
        } catch (BufferUnderflowException e) {
        	throw new EOFException();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readChar()
     */
    @Override
    public char readChar() throws EOFException {
//        try {
//            int b1 = data[position];
//            position += 1;
//            int b2 = data[position];
//            position += 1;
//            return (char) ((b1 << 8) + (b2 << 0));
//        } catch (ArrayIndexOutOfBoundsException e) {
//            throw new EOFException();
//        }
        
        try {
        	return buffer.getChar();
        } catch (BufferUnderflowException e) {
        	throw new EOFException();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readInt()
     */
    @Override
    public int readInt() throws EOFException {
//        try {
//            int b1 = data[position];
//            position += 1;
//            int b2 = data[position];
//            position += 1;
//            int b3 = data[position];
//            position += 1;
//            int b4 = data[position];
//            position += 1;
//            return ((b1 << 24) + (b2 << 16) + (b3 << 8) + (b4 << 0));
//        } catch (ArrayIndexOutOfBoundsException e) {
//            throw new EOFException();
//        }
        
        try {
        	return buffer.getInt();
        } catch (BufferUnderflowException e) {
        	throw new EOFException();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readLong()
     */
    @Override
    public long readLong() throws EOFException {
//        try {
//            // TODO this is the ugliest code I've ever read in my life. fix it.
//            int b1 = data[position];
//            position += 1;
//            int b2 = data[position];
//            position += 1;
//            int b3 = data[position];
//            position += 1;
//            int b4 = data[position];
//            position += 1;
//            int b5 = data[position];
//            position += 1;
//            int b6 = data[position];
//            position += 1;
//            int b7 = data[position];
//            position += 1;
//            int b8 = data[position];
//            position += 1;
//            return ((b1 << 56) + (b2 << 48) + (b3 << 40) + (b4 << 32)
//                    + (b5 << 24) + (b6 << 16) + (b7 << 8) + (b8 << 0));
//        } catch (ArrayIndexOutOfBoundsException e) {
//            throw new EOFException();
//        }
        
        try {
        	return buffer.getLong();
        } catch (BufferUnderflowException e) {
        	throw new EOFException();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readFloat()
     */
    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readDouble()
     */
    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readUTF()
     */
    @Override
    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.DataInput#readLine()
     */
    @Override
    public String readLine() throws IOException {
        throw new UnsupportedOperationException(
                "don't you ever let me catch you using this again");
    }
    
}
