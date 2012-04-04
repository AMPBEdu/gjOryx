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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.joryx.appstate.DatabaseState;
import com.joryx.appstate.RootNodeState;
import com.joryx.appstate.WorldState;
import com.joryx.control.NotificationControl;
import com.joryx.db.ObjectType;
import com.joryx.object.ObjectControl;
import com.oryxhatesjava.net.PlayerTextPacket;
import com.oryxhatesjava.net.UsePortalPacket;
import com.oryxhatesjava.net.data.ObjectStatus;
import com.oryxhatesjava.net.data.StatData;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

public class WorldScreen extends AbstractAppState implements ScreenController, ActionListener {

	private static int counter = 0;
	private static final WorldScreen singleton = new WorldScreen();
	
	class Message {
		public String text;
		public float timeAlive;
		public Element parent;
		public Element label;
		public int id;
		
		public Message(String t, float i) {
			text = t;
			timeAlive = i;
			id = counter++;
		}
		
		public void makeLabel() {
			parent = screen.findElementByName("panelChat");
			label = new PanelBuilder() {{
				childLayoutHorizontal();
				width("100%");
				backgroundColor("#0004");
				control(new LabelBuilder() {{
					text(text);
				}});
				
			}}.build(nifty, screen, parent);
		}
		
		public void remove() {
			recursiveElementRemoval(nifty, screen, label);
			nifty.removeElement(screen, label);
			parent.resetLayout();
		}
	}
	
	public static WorldScreen getSingleton() {
		return singleton;
	}
	
	public static void recursiveElementRemoval(Nifty nifty, Screen screen, Element element) {
		for (Element e : element.getElements()) {
			if (e.getElements().size() > 1) {
				recursiveElementRemoval(nifty, screen, e);
			}
			nifty.removeElement(screen, e);
		}
		nifty.removeElement(screen, element);
	}
	
	private Application app;
	private Nifty nifty;
	private Screen screen;
	private List<Message> messages = new LinkedList<Message>();
	private TextField textfieldChat;
	
	private List<Element> notificationElements = new LinkedList<Element>();
	private Map<Element, NotificationControl> notificationControls = new HashMap<Element, NotificationControl>();
	
	private Element popupError;
	private Element panelToolTip;
	private Element layerHUD;
	
	private Element panelPortal;
	private ObjectStatus portalStatus;
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = app;
		
		app.getInputManager().addListener(this, "SendChat");
		//app.getInputManager().addRawInputListener(this);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		// Handle chat
		Iterator<Message> itr = messages.iterator();
		while (itr.hasNext()) {
			Message m = itr.next();
			m.timeAlive -= tpf;
			if (m.timeAlive <= 0) {
				m.remove();
				itr.remove();
			}
		}
		
		// Handle tooltip
		Vector3f loc = app.getCamera().getWorldCoordinates(app.getInputManager().getCursorPosition(), 0);
		Vector3f dir = app.getCamera().getWorldCoordinates(app.getInputManager().getCursorPosition(), 1).subtractLocal(loc);
		Ray ray = new Ray(loc, dir);
		
		RootNodeState root = app.getStateManager().getState(RootNodeState.class);
		Node world = (Node) root.getRootNode().getChild("World");
		boolean pointed = false;
		for (Spatial s : world.getChildren()) {
			ObjectControl oc = s.getControl(ObjectControl.class);
			if (oc == null) {
				continue;
			}
			
			if (s.collideWith(ray, new CollisionResults()) > 0) {
				// we got an object pointed at
				panelToolTip.setVisible(true);
				ObjectType type = DatabaseState.getSingleton().getObjectType(oc.getStatus().objectType);
				StatData name = oc.getStatus().data.getStat(StatData.NAME);
				if (name != null && name.valueString.length() > 0) {
					panelToolTip.findNiftyControl("labelToolTipName", Label.class).setText(name.valueString);
				} else {
					panelToolTip.findNiftyControl("labelToolTipName", Label.class).setText(type.id);
				}
				panelToolTip.findNiftyControl("labelToolTipObjType", Label.class).setText(type.type + "");
				StatData hps = oc.getStatus().data.getStat(StatData.HP);
				StatData mhps = oc.getStatus().data.getStat(StatData.MAXHP);
				if (hps != null && mhps != null) {
					panelToolTip.findNiftyControl("labelToolTipHealth", Label.class).setText(hps.value + "/" + mhps.value);
				} else if (hps != null) {
					panelToolTip.findNiftyControl("labelToolTipHealth", Label.class).setText(hps.value + "");
				} else {
					panelToolTip.findNiftyControl("labelToolTipHealth", Label.class).setText("");
				}
				pointed = true;
				break;
			}
		}
		
		if (!pointed) {
			panelToolTip.setVisible(false);
		}
		
		// Handle notifications
		Iterator<Element> elitr = notificationElements.iterator();
		while (elitr.hasNext()) {
			Element e = elitr.next();
			NotificationControl control = notificationControls.get(e);
			WorldState ws = app.getStateManager().getState(WorldState.class);
			if (!control.alive || !control.getSpatial().hasAncestor(ws.getWorldNode())) {
				notificationControls.remove(e);
				recursiveElementRemoval(nifty, screen, e);
				elitr.remove();
				continue;
			}
			
			Vector3f sloc = app.getCamera().getScreenCoordinates(control.getSpatial().getWorldTranslation());
			int x = Math.round(sloc.x);
			int y = nifty.getRenderEngine().getHeight() - Math.round(sloc.y);
			x = x - (e.getWidth() / 2);
			
			e.setConstraintX(new SizeValue(x + ""));
			e.setConstraintY(new SizeValue(y + ""));
		}
		layerHUD.resetLayout();
		layerHUD.layoutElements();
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
		textfieldChat = screen.findNiftyControl("textfieldChat", TextField.class);
		panelToolTip = screen.findElementByName("panelToolTip");
		layerHUD = screen.findElementByName("layerHUD");
		panelPortal = screen.findElementByName("panelPortal");
		
		panelPortal.hide();
		
		for (Element e : notificationElements) {
			recursiveElementRemoval(nifty, screen, e);
		}
		
		notificationElements.clear();
		notificationControls.clear();
		
		Iterator<Message> itr = messages.iterator();
		while (itr.hasNext()) {
			Message m = itr.next();
			m.remove();
			itr.remove();
		}
		
		screen.getFocusHandler().setKeyFocus(null);
	}
	
	public void addChatLine(String player, String recipient, String line) {
		String lineIn = "";
		if (recipient != null && recipient.length() > 0) {
			if (recipient.equals("*Guild*")) {
				lineIn = "[G] <" + player + "> " + line;
			} else {
				lineIn = player + " to " + recipient + ": " + line;
			}
		} else if (player.length() > 0) {
			lineIn = "<" + player + "> " + line;
		} else {
			lineIn = line;
		}
		Message m = new Message(lineIn, 20);
		m.makeLabel();
		messages.add(m);
	}
	
	public void addNotification(final String text, final ColorRGBA color, NotificationControl control) {
		final Color niftyColor = new Color(color.r, color.g, color.b, color.a);
		Element e = new PanelBuilder() {{
			childLayoutCenter();
			control(new LabelBuilder() {{
				text(text);
				color(niftyColor);
			}});
		}}.build(nifty, screen, layerHUD);
		layerHUD.resetLayout();
		notificationElements.add(e);
		notificationControls.put(e, control);
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
	
	public void showPortalPanel(ObjectStatus status) {
		portalStatus = status;
		panelPortal.show();
		StatData nameStat = status.data.getStat(StatData.NAME);
		if (nameStat != null) {
			panelPortal.findNiftyControl("panelPortalLabel", Label.class).setText(nameStat.valueString);
		} else {
			String id = DatabaseState.getSingleton().getObjectType(status.objectType).id;
			panelPortal.findNiftyControl("panelPortalLabel", Label.class).setText(id);
		}
		panelPortal.resetLayout();
	}
	
	public void hidePortalPanel() {
		panelPortal.hide();
		screen.getFocusHandler().setKeyFocus(null);
	}
	
	@NiftyEventSubscriber(id="panelPortalEnter")
	public void onPanelPortalEnterClick(String st, ButtonClickedEvent evt) {
		String text = panelPortal.findNiftyControl("panelPortalLabel", Label.class).getText();
		WorldState ws = app.getStateManager().getState(WorldState.class);
		UsePortalPacket pkt = new UsePortalPacket();
		pkt.objectId = portalStatus.data.objectId;
		try {
			ws.getClient().sendSyncPacket(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("SendChat")) {
			if (isPressed) {
				if (screen.getFocusHandler().getKeyboardFocusElement() == textfieldChat.getElement()) {
					if (textfieldChat.getText().length() == 0) {
						screen.getFocusHandler().setKeyFocus(null);
						return;
					}
					WorldState cs = app.getStateManager().getState(WorldState.class);
					PlayerTextPacket pkt = new PlayerTextPacket();
					pkt.text = textfieldChat.getText();
					try {
						cs.getClient().sendSyncPacket(pkt);
					} catch (IOException e) {
						e.printStackTrace();
					}
					textfieldChat.setText("");
					screen.getFocusHandler().setKeyFocus(null);
					return;
				} else {
					screen.getFocusHandler().setKeyFocus(textfieldChat.getElement());
					return;
				}
			}
		}
	}
}
