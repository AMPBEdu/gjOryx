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

package com.oryxhatesjava;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.clarenceho.crypto.RC4;

/**
 * <p>
 * A filter stream that decrypts all read bytes with the given key under the RC4
 * stream cipher.
 * </p>
 * <p>
 * Started Mar 2, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class ArcFourInputStream extends FilterInputStream {
    
    private RC4 cipher;
    
    protected ArcFourInputStream(InputStream encryptedStream) {
        this(encryptedStream, (String) null);
    }
    
    /**
     * <p>
     * Creates an ArcFourInputStream using the given RC4 object.
     * </p>
     * 
     * @param encryptedStream
     * @param cipher
     */
    public ArcFourInputStream(InputStream encryptedStream, RC4 cipher) {
        super(encryptedStream);
        this.cipher = cipher;
    }
    
    /**
     * <p>
     * Creates an ArcFourInputStream using the given byte array as the key. A
     * duplicate of the array is passed to the RC4 instance.
     * </p>
     * 
     * @param encryptedStream
     * @param key
     */
    public ArcFourInputStream(InputStream encryptedStream, byte[] key) {
        this(encryptedStream, new RC4(key.clone()));
    }
    
    /**
     * <p>
     * Creates an ArcFourInputStream using the given String as the key. A
     * duplicate of the String object is passed to the RC4 instance.
     * </p>
     * 
     * @param encryptedStream
     * @param key
     */
    public ArcFourInputStream(InputStream encryptedStream, String key) {
        this(encryptedStream, new RC4(new String(key)));
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        byte[] r = new byte[1];
        r = cipher.rc4(r);
        return r[0];
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.FilterInputStream#read(byte[])
     */
    @Override
    public int read(byte[] b) throws IOException {
        in.read(b);
        byte[] res = cipher.rc4(b);
        for (int i=0; i<b.length; i++) {
            b[i] = res[i];
        }
        return b.length;
    }
}
