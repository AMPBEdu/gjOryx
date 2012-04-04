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

package com.joryx;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.app.Application;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.system.AppSettings;
import com.joryx.appstate.AppEngineState;
import com.joryx.appstate.DatabaseState;
import com.joryx.appstate.RootNodeState;
import com.joryx.appstate.SettingsState;

public class JOryx extends Application {

	private static JOryx singleton;
	public ScheduledThreadPoolExecutor threadpool = new ScheduledThreadPoolExecutor(4);
	
	@Override
	public void initialize() {
		Logger.getLogger("com.jme3").setLevel(Level.WARNING);
		super.initialize();
		
		getStateManager().attach(DatabaseState.getSingleton());
		
		getStateManager().attach(SettingsState.getSingleton());
		
		RootNodeState rns = new RootNodeState();
		rns.setEnabled(true);
		getStateManager().attach(rns);
		
		AppEngineState cs = new AppEngineState();
		getStateManager().attach(cs);
		
		inputManager.addMapping("PlayerStrafeLeft", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("PlayerStrafeRight", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("PlayerMoveForward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("PlayerMoveBackward", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("SendChat", new KeyTrigger(KeyInput.KEY_RETURN));
		inputManager.addMapping("PlayerShoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	}
	
	@Override
	public void update() {
		super.update();
		
		// Implement delta timing
		if (speed == 0 || paused) {
			return;
		}

		float tpf = timer.getTimePerFrame() * speed;

		stateManager.update(tpf);

		stateManager.render(renderManager);
		renderManager.render(tpf, context.isRenderable());
		stateManager.postRender();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		threadpool.shutdown();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JOryx oryx = new JOryx();
		singleton = oryx;
		AppSettings a = new AppSettings(true);
		SettingsState ss = SettingsState.getSingleton();
		ss.loadSettings();
		a.setTitle("Realm of the Mad God");
		a.setWidth(ss.width);
		a.setHeight(ss.height);
		a.setFullscreen(ss.fullscreen);
		a.setRenderer(AppSettings.LWJGL_OPENGL1);
		oryx.setSettings(a);
		oryx.setPauseOnLostFocus(false);
		oryx.start();
	}
	
	public static JOryx getSingleton() {
		return singleton;
	}

}
