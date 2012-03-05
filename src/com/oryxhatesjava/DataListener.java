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

package com.oryxhatesjava;

import com.oryxhatesjava.net.data.ObjectStatus;
import com.oryxhatesjava.net.data.Tile;

/**
 * A listener for data updates. Only called upon if the {@link Client} is
 * automatically handling packets.
 * @author furyhunter
 *
 */
public interface DataListener {

	/**
	 * An object was added
	 * @param client the client who had an object added
	 * @param object the status of the object added
	 * @return DataListener to add
	 */
	public DataListener objectAdded(Client client, ObjectStatus object);
	
	/**
	 * An object was removed
	 * @param client the client whose object was removed
	 * @param object the last status of the object removed
	 * @param id the object id of the object removed
	 * @return DataListener to remove
	 */
	public DataListener objectRemoved(Client client, ObjectStatus object, int id);
	
	/**
	 * An object was updated.
	 * @param client the client whose object was updated
	 * @param object the status of the object
	 */
	public void objectUpdated(Client client, ObjectStatus object);
	
	/**
	 * A tile was added.
	 * @param tile
	 */
	public void tileAdded(Tile tile);
}
