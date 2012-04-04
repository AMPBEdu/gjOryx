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

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class LoginScreenState extends AbstractAppState implements ScreenController {

	private Application app;
	private Nifty nifty;
	private Screen screen;
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = app;
		
		RootNodeState root = (RootNodeState) stateManager.getState(RootNodeState.class);
		nifty = root.getNiftyDisplay().getNifty();
		nifty.fromXml("Interface/Nifty/Login.xml", "login", this);
		System.out.println("initialized");
	}
	
	@Override
	public void update(float tpf) {
		// TODO Auto-generated method stub
		super.update(tpf);
	}
	
	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void onEndScreen() {
		
	}

	@Override
	public void onStartScreen() {
		System.out.println("start");
	}
	
	public void login(String nextScreen) {
		System.out.println("Mother of god");
	}

}
