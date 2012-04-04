/* jOryx - A Realm of the Mad God client.
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

package com.joryx.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;

import com.joryx.appstate.DatabaseState;
import com.joryx.db.ObjectType;

public class SoundDownloader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseState.getSingleton().loadObjectTypes();
		
		Iterator<ObjectType> itr = DatabaseState.getSingleton().objectIterator();
		
		while (itr.hasNext()) {
			ObjectType i = itr.next();
			
			if (i.hitSound != null && !downloadSound(i.hitSound)) {
				System.out.println("Breaking");
				break;
			}
			
			if (i.deathSound != null && !downloadSound(i.deathSound)) {
				System.out.println("Breaking");
				break;
			}
			
			if (i.sound != null && !downloadSound(i.sound)) {
				System.out.println("Breaking");
				break;
			}
		}
	}
	
	public static boolean downloadSound(String filename) {
		File file = new File("assets/Sounds/" + filename + ".mp3");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				System.out.println("Creating file " + filename + " failed");
				e1.printStackTrace();
				return false;
			}
			// download
			URL url = null;
			FileOutputStream fos = null;
			try {
				url = new URL("http://realmofthemadgod.appspot.com/sfx/" + filename + ".mp3");
				fos = new FileOutputStream(file);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			
			try {
				ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				fos.getChannel().transferFrom(rbc, 0, 1 << 24);
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

}
