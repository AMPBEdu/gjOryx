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

import java.util.logging.Logger;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.joryx.appstate.AppEngineState;
import com.joryx.appstate.SettingsState;
import com.oryxhatesjava.util.RequestAdapter;
import com.oryxhatesjava.xml.Chars;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class LoginScreen extends AbstractAppState implements ScreenController {

	private static LoginScreen singleton = new LoginScreen();

	public static LoginScreen getSingleton() {
		return singleton;
	}

	class ResolutionEntry {
		public int width;
		public int height;

		public ResolutionEntry(int width, int height) {
			this.width = width;
			this.height = height;
		}

		@Override
		public String toString() {
			return width + "x" + height;
		}
	}

	private Logger log = Logger.getLogger(getClass().getName());
	private Nifty nifty;
	private Screen screen;
	private Application app;

	private Element popupError;
	private Element popupLoading;
	private Element popupSettings;

	private boolean loginSuccess;
	private boolean loginFail;
	private String errorString;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = app;
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (loginSuccess) {
			loginSuccess = false;
			log.info("Login success");
			closeLoadingPopup();
			nifty.gotoScreen("screenChars");
			app.getStateManager().detach(this);
			app.getStateManager().attach(CharsScreen.getSingleton());
		}

		if (loginFail) {
			loginFail = false;
			log.severe("Login failed");
			closeLoadingPopup();
			showErrorPopup("Login failed: " + errorString);
			errorString = null;
		}
	}

	@Override
	public void onEndScreen() {

	}

	@Override
	public void onStartScreen() {
		TextField email = screen.findNiftyControl("emailField", TextField.class);
		email.setText(SettingsState.getSingleton().username);
		//email.setText("");
		if (email.getText().length() > 0) {
			screen.getFocusHandler().setKeyFocus(screen.findNiftyControl("passwordField", TextField.class).getElement());
		}
		TextField password = screen.findNiftyControl("passwordField", TextField.class);
		//password.setText("");
	}

	@NiftyEventSubscriber(id="buttonLogin")
	public void login(String id, ButtonClickedEvent event) {
		TextField email = screen.findNiftyControl("emailField", TextField.class);
		TextField password = screen.findNiftyControl("passwordField", TextField.class);
		SettingsState ss = app.getStateManager().getState(SettingsState.class);
		AppEngineState cs = app.getStateManager().getState(AppEngineState.class);

		ss.username = email.getText();
		ss.password = password.getText();
		showLoadingPopup("Logging in...");
		cs.getAppEngine().getCharsAsync(ss.username, ss.password, new RequestAdapter() {
			@Override
			public void charsReceived(String fullRequest, Chars chars) {
				SettingsState.getSingleton().chars = chars;
				loginSuccess = true;
			}

			@Override
			public void requestFailed(String fullRequest, String reason) {
				loginFail = true;
				errorString = reason;
			}
		});
	}

	@NiftyEventSubscriber(id="rememberMeCheckbox")
	public void onRememberMeCheck(String id, CheckBoxStateChangedEvent event) {
		SettingsState.getSingleton().saveUsername = event.isChecked();
	}

	public void showLoadingPopup(String text) {
		popupLoading = nifty.createPopup("popupLoading");
		Label loading = popupLoading.findNiftyControl("labelLoading", Label.class);
		loading.setText(text);
		loading.setWidth(null);
		nifty.showPopup(screen, popupLoading.getId(), null);
	}

	public void closeLoadingPopup() {
		if (popupLoading != null) {
			screen.closePopup(popupLoading, null);
			popupLoading = null;
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

	@SuppressWarnings("unchecked")
	public void showSettingsPopup() {
		popupSettings = nifty.createPopup("popupSettings");

		// build the dropdown for resolutions
		DropDown<ResolutionEntry> dropdown = popupSettings.findNiftyControl("popupSettings_dropdownResolution", DropDown.class);
		dropdown.addItem(new ResolutionEntry(640, 480));
		dropdown.addItem(new ResolutionEntry(800, 600));
		dropdown.addItem(new ResolutionEntry(1024, 768));
		dropdown.addItem(new ResolutionEntry(1280, 960));
		dropdown.addItem(new ResolutionEntry(1280, 1024));
		dropdown.addItem(new ResolutionEntry(1280, 720));
		dropdown.addItem(new ResolutionEntry(1280, 800));
		dropdown.addItem(new ResolutionEntry(1680, 1050));
		dropdown.addItem(new ResolutionEntry(1920, 1080));
		dropdown.addItem(new ResolutionEntry(1920, 1200));

		nifty.showPopup(screen, popupSettings.getId(), null);
	}

	public void closeSettingsPopup(boolean save) {
		if (popupSettings != null) {
			screen.closePopup(popupSettings, null);
			if (save) {
				SettingsState.getSingleton().saveSettings();
			}
			popupSettings = null;
		}
	}

	public Element getElement(String id) {
		return screen.findElementByName(id);
	}

	@NiftyEventSubscriber(id="buttonOkay")
	public void errorOkay(String next, ButtonClickedEvent event) {
		closeErrorPopup();
	}

	@NiftyEventSubscriber(id="buttonSettings")
	public void onSettingsClick(String id, ButtonClickedEvent event) {
		showSettingsPopup();
	}

	@NiftyEventSubscriber(id="popupSettings_dropdownResolution")
	public void onResolutionSelect(String id, DropDownSelectionChangedEvent<ResolutionEntry> event) {
		SettingsState.getSingleton().width = event.getSelection().width;
		SettingsState.getSingleton().height = event.getSelection().height;
	}

	@NiftyEventSubscriber(id="popupSettings_checkboxFullscreen")
	public void onFullscreenCheck(String id, CheckBoxStateChangedEvent event) {
		SettingsState.getSingleton().fullscreen = event.isChecked();
	}

	@NiftyEventSubscriber(id="popupSettings_buttonSave")
	public void onSaveClick(String id, ButtonClickedEvent event) {
		closeSettingsPopup(true);
		showErrorPopup("Settings will not take effect until you restart.");
	}

	@NiftyEventSubscriber(id="popupSettings_buttonCancel")
	public void onCancelClick(String id, ButtonClickedEvent event) {
		closeSettingsPopup(false);
	}

}
