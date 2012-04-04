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

package com.oryxhatesjava;

import com.oryxhatesjava.net.Packet;

/**
 * A listener for packet events. These methods are called on the {@link Client}
 * receptor thread.
 * @author furyhunter
 *
 */
public interface PacketListener {
	/**
	 * Callback on packet reception. Only called if <code>filter()</code>
	 * returned true on the same packet.
	 * @param client the client that received the packet
	 * @param pkt the packet received
	 */
	public void packetReceived(Client client, Packet pkt);
	
	/**
	 * Filter a packet. 
	 * @param pkt the packet check for filtering
	 * @return true to handle, false to ignore
	 */
	public boolean filter(Packet pkt);
}
