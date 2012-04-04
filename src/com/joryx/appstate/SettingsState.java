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

package com.joryx.appstate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.oryxhatesjava.xml.Chars;
import com.oryxhatesjava.xml.Server;

public class SettingsState extends AbstractAppState {

	private static SettingsState singleton = new SettingsState();
	
	public static SettingsState getSingleton() {
		return singleton;
	}
	
	private Application app;
	
	public String username;
	public String password;
	public int charId;
	public Server server;
	public Chars chars;
	
	// saveable block
	public boolean saveUsername;
	public int width;
	public int height;
	public boolean fullscreen;
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = app;
	}
	
	public void saveSettings() {
		Properties props = new Properties();
		props.setProperty("width", width+"");
		props.setProperty("height", height+"");
		props.setProperty("fullscreen", fullscreen+"");
		if (saveUsername) {
			props.setProperty("username", username);
		}
		try {
			props.store(new FileWriter("joryx.properties"), "JOryx Settings");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadSettings() {
		Properties props = new Properties();
		try {
			props.load(new FileReader("joryx.properties"));
			width = Integer.parseInt(props.getProperty("width"));
			height = Integer.parseInt(props.getProperty("height"));
			fullscreen = Boolean.parseBoolean(props.getProperty("fullscreen"));
			username = props.getProperty("username", "");
			if (username.length() > 0) {
				saveUsername = true;
			}
		} catch (Exception e) {
			width = 1024;
			height = 768;
			fullscreen = false;
			e.printStackTrace();
		}
	}
}
