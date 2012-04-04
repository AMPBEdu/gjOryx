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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.joryx.appstate.screens.CharsScreen;
import com.joryx.appstate.screens.LoginScreen;
import com.joryx.appstate.screens.WorldScreen;

import de.lessvoid.nifty.Nifty;

public class RootNodeState extends AbstractAppState {

	private Node rootNode = new Node("Root Node");
	private NiftyJmeDisplay niftyDisplay;
	
	private Application app;
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		app.getViewPort().attachScene(rootNode);
        
        niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        Nifty nifty = niftyDisplay.getNifty();
        
        stateManager.attach(LoginScreen.getSingleton());
        nifty.fromXml("Interface/Nifty/Main.xml", "screenLogin", LoginScreen.getSingleton(), CharsScreen.getSingleton(), WorldScreen.getSingleton());
		
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE);
        Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);
        app.getGuiViewPort().addProcessor(niftyDisplay);
		this.app = app;
		rootNode.setCullHint(CullHint.Dynamic);
	}
	
	@Override
	public void stateAttached(AppStateManager stateManager) {
		if (isInitialized()) {
			app.getViewPort().attachScene(rootNode);
		}
	}
	
	@Override
	public void stateDetached(AppStateManager stateManager) {
		app.getViewPort().detachScene(rootNode);
		app.getViewPort().removeProcessor(niftyDisplay);
	}
	
	@Override
	public void update(float tpf) {
		rootNode.updateLogicalState(tpf);
	}
	
	@Override
	public void render(RenderManager rm) {
		super.render(rm);
		rootNode.updateGeometricState();
	}
	
	public Node getRootNode() {
		return rootNode;
	}
	
	public NiftyJmeDisplay getNiftyDisplay() {
		return niftyDisplay;
	}
}
