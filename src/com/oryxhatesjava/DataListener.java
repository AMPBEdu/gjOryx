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
