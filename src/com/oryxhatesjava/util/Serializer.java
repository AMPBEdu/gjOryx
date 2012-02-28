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

package com.oryxhatesjava.util;

import java.io.DataOutput;
import java.io.IOException;

import com.oryxhatesjava.net.data.Parsable;

public class Serializer {
	
	public static final int AS_INT = 0;
	public static final int AS_BYTE = 1;
	public static final int AS_SHORT = 2;
	
	public static void writeArray(DataOutput out, Parsable[] array) throws IOException {
		int size = array.length;
		out.writeShort(size);
		if (size != 0) {
			for (Parsable w : array) {
				w.writeToDataOutput(out);
			}
		}
	}
	
	public static void writeArray(DataOutput out, int[] array, int cast) throws IOException {
		int size = array.length;
		out.writeShort(size);
		if (size != 0) {
			for (int i : array) {
				switch (cast) {
				case AS_INT:
					out.writeInt(i);
					break;
				case AS_BYTE:
					out.writeByte(i);
					break;
				case AS_SHORT:
					out.writeShort(i);
					break;
				default:
					throw new IllegalArgumentException("invalid cast operation");
				}
			}
		}
	}
	
	public static void writeArray(DataOutput out, boolean[] array) throws IOException {
		int size = array.length;
		out.writeShort(size);
		if (size != 0) {
			for (boolean i : array) {
				out.writeBoolean(i);
			}
		}
	}
}
