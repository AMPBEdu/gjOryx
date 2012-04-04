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
import java.io.DataOutput;
import java.io.IOException;

import com.oryxhatesjava.net.data.Parsable;


/**
 * <p>TODO document this type</p>
 *
 * <p>Started Mar 5, 2011</p>
 *
 * @author Furyhunter
 */
public class PlayerTextPacket extends Packet implements Parsable {
    public String text;
    
    public PlayerTextPacket(String text) {
        this.text = new String(text);
    }
    
    public PlayerTextPacket(DataInput read) {
        try {
            this.type = Packet.PLAYERTEXT;
            parseFromDataInput(read);
        } catch (IOException e) {
            
        }
    }
    
    public PlayerTextPacket() {
		type = Packet.PLAYERTEXT;
	}

	/* (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#parseFromDataInput(java.io.DataInput)
     */
    @Override
    public void parseFromDataInput(DataInput read) throws IOException {
        text = read.readUTF();
    }
    
    /* (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#writeToDataOutput(java.io.DataOutput)
     */
    @Override
    public void writeToDataOutput(DataOutput write) throws IOException {
        write.writeUTF(text);
    }
    
    /* (non-Javadoc)
     * @see com.oryxhatesjava.net.Packet#toString()
     */
    @Override
    public String toString() {
        return "PLAYERTEXT " + text;
    }
}
