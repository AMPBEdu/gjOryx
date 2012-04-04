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

package com.joryx.appstate.screens;

import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.joryx.appstate.DatabaseState;
import com.joryx.appstate.SettingsState;
import com.joryx.appstate.WorldState;
import com.oryxhatesjava.xml.Char;
import com.oryxhatesjava.xml.Chars;
import com.oryxhatesjava.xml.Server;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class CharsScreen extends AbstractAppState implements ScreenController {
	private static CharsScreen singleton = new CharsScreen();
	
	public static CharsScreen getSingleton() {
		return singleton;
	}
	
	class CharEntry {
		public Char c;
		public boolean newChar;
		
		public CharEntry() {
			
		}
		
		public CharEntry(Char c) {
			this.c = c;
		}
		
		@Override
		public String toString() {
			if (c != null) {
				return "Level " + c.level + " " + DatabaseState.getSingleton().getObjectType(c.objectType).id;
			} else if (newChar) {
				return "Create New Character";
			} else {
				return "Buy Character Slot";
			}
		}
	}
	
	private Application app;
	private Nifty nifty;
	private Screen screen;
	
	private boolean serverSelected;
	private Element popupServers;
	private Element popupError;
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = app;
	}
	
	@Override
	public void update(float tpf) {
		
	}
	
	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEndScreen() {
		ListBox<CharEntry> list = (ListBox<CharEntry>) screen.findNiftyControl("listboxCharacters", ListBox.class);
		list.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onStartScreen() {
		Chars cs = SettingsState.getSingleton().chars;
		ListBox<CharEntry> list = (ListBox<CharEntry>) screen.findNiftyControl("listboxCharacters", ListBox.class);
		if (cs != null) {
			for (Char c : cs.chars) {
				list.addItem(new CharEntry(c));
			}
		}
		
		CharEntry ce = new CharEntry();
		if (cs.maxNumChars > cs.chars.size()) {
			ce.newChar = true;
		}
		list.addItem(ce);
		list.selectItemByIndex(0);
		
		Label accountName = screen.findNiftyControl("labelAccountName", Label.class);
		accountName.setText(cs.account.name);
		accountName.setWidth(null);
		
		serverSelected = (SettingsState.getSingleton().server != null ? true : false);
		
		if (serverSelected) {
			refreshServerLabel();
		} else {
			List<Server> servers = cs.servers;
			showServerPopup(servers);
		}
	}
	
	@NiftyEventSubscriber(id="listboxCharacters")
	public void onListboxSelect(String id, ListBoxSelectionChangedEvent<CharEntry> event) {
		Label level = screen.findNiftyControl("labelLevel", Label.class);
		Char selection = event.getSelection().get(0).c;
		level.setText(event.getSelection().get(0).toString());
	}
	
	@NiftyEventSubscriber(id="buttonChangeServer")
	public void onChangeServerClick(String id, ButtonClickedEvent event) {
		Chars cs = SettingsState.getSingleton().chars;
		List<Server> servers = cs.servers;
		showServerPopup(servers);
	}

	@SuppressWarnings("unchecked")
	public void showServerPopup(List<Server> servers) {
		popupServers = nifty.createPopup("popupServers");
		ListBox<Server> list = popupServers.findNiftyControl("listboxServers", ListBox.class);
		list.addAllItems(servers);
		list.selectItemByIndex(0);
		nifty.showPopup(screen, popupServers.getId(), null);
	}
	
	public void closeServerPopup() {
		if (popupServers != null) {
			screen.closePopup(popupServers, null);
			refreshServerLabel();
			popupServers = null;
		}
	}
	
	public void showErrorPopup(String text) {
		popupError = nifty.createPopup("popupError");
		Label error = popupError.findNiftyControl("labelError", Label.class);
		error.setText(text);
		error.setWidth(null);
		nifty.showPopup(screen, popupError.getId(), null);
	}
	
	public void closeErrorPopup() {
		if (popupError != null) {
			screen.closePopup(popupError, null);
			popupError = null;
		}
	}
	
	@NiftyEventSubscriber(id="buttonOkay")
	public void errorOkay(String next, ButtonClickedEvent event) {
		closeErrorPopup();
	}
	
	@SuppressWarnings("unchecked")
	@NiftyEventSubscriber(id="buttonSelectServer")
	public void onSelectServerClick(String id, ButtonClickedEvent event) {
		ListBox<Server> list = popupServers.findNiftyControl("listboxServers", ListBox.class);
		SettingsState.getSingleton().server = list.getSelection().get(0);
		closeServerPopup();
	}
	
	public void refreshServerLabel() {
		Label server = screen.findNiftyControl("labelServer", Label.class);
		server.setText(SettingsState.getSingleton().server.name);
	}
	
	@NiftyEventSubscriber(id="buttonStart")
	public void onStartClick(String id, ButtonClickedEvent event) {
		CharEntry selection = (CharEntry) screen.findNiftyControl("listboxCharacters", ListBox.class).getSelection().get(0);
		if (selection.c == null) {
			showErrorPopup("Not yet implemented.");
			return;
		}
		
		SettingsState.getSingleton().charId = selection.c.id;
		WorldState ws = new WorldState(SettingsState.getSingleton().server.host);
		app.getStateManager().attach(ws);
		app.getStateManager().attach(WorldScreen.getSingleton());
		nifty.gotoScreen("screenWorld");
	}
}
