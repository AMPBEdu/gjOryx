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

public class BuyResultPacket extends Packet implements Parsable {

	public int result; //int
	public String resultString; //UTF
    public static final int UNKNOWN_NEG1 = -1;
    public static final int UNKNOWN_0 = 0;
    public static final int UNKNOWN_1 = 1;
    public static final int UNKNOWN_2 = 2;
    public static final int UNKNOWN_3 = 3;
    public static final int UNKNOWN_4 = 4;
    public static final int UNKNOWN_5 = 5;
    public static final int UNKNOWN_6 = 6;
	
	
	public BuyResultPacket(DataInput in) {
		try {
			type = Packet.BUYRESULT;
			parseFromDataInput(in);
		} catch (IOException e) {
			
		}
	}
	
	public BuyResultPacket() {
		type = Packet.BUYRESULT;
	}
	
	@Override
	public void parseFromDataInput(DataInput in) throws IOException {
		result = in.readInt();
		resultString = in.readUTF();
	}
	
	@Override
	public void writeToDataOutput(DataOutput out) throws IOException {
		out.writeInt(result);
		out.writeUTF(resultString);
	}
	
	@Override
	public String toString() {
		return "BUYRESULT " + result + " " + resultString;
	}
}
