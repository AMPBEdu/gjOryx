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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.clarenceho.crypto.RC4;

/**
 * <p>
 * A filter stream that encrypts all written bytes with the given key under the
 * RC4 stream cipher.
 * </p>
 * <p>
 * Started Mar 2, 2011
 * </p>
 * 
 * @author Furyhunter
 */
public class ArcFourOutputStream extends FilterOutputStream {
    
    private RC4 cipher;
    
    protected ArcFourOutputStream(OutputStream output) {
        this(output, (String) null);
    }
    
    public ArcFourOutputStream(OutputStream output, RC4 cipher) {
        super(output);
        this.cipher = cipher;
    }
    
    public ArcFourOutputStream(OutputStream output, byte[] key) {
        this(output, new RC4(key.clone()));
    }
    
    public ArcFourOutputStream(OutputStream output, String key) {
        this(output, new RC4(new String(key)));
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.FilterOutputStream#write(int)
     */
    @Override
    public void write(int b) throws IOException {
        super.write((int)cipher.rc4(new byte[]{(byte) b})[0]);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.FilterOutputStream#write(byte[])
     */
    @Override
    public void write(byte[] b) throws IOException {
        super.write(cipher.rc4(b.clone()));
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.FilterOutputStream#write(byte[], int, int)
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(cipher.rc4(b.clone()), off, len);
    }
}
