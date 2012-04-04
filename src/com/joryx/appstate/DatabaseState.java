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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.joryx.EntityFactory;
import com.joryx.db.GroundType;
import com.joryx.db.ObjectType;

public class DatabaseState extends AbstractAppState {

	private static final DatabaseState singleton = new DatabaseState();
	
	public static DatabaseState getSingleton() {
		return singleton;
	}
	
	public static final Logger log = Logger.getLogger(DatabaseState.class.getName());
	private List<GroundType> groundTypes;
	
	private List<ObjectType> objectTypes;
	
	private static final String[] objectxmls = { "bosses.xml",
		"clothtextures.xml", "drakes.xml", "dyes.xml", "encounters.xml",
		"enemies-beach.xml", "enemies-low.xml", "enemies-mid.xml",
		"enemies-high.xml", "enemies-gods.xml", "enemies-dungeons.xml",
		"gameobjects.xml", "items.xml", "otherobjects.xml", "pets.xml",
		"playerclasses.xml", "projectiles.xml" };
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		
		loadGroundTypes();
		loadObjectTypes();
		EntityFactory.loadGroundMaterials(app.getAssetManager());
	}
	
	@SuppressWarnings("unchecked")
	public void loadGroundTypes() {

		Document d = null;
		try {
			d = new SAXBuilder().build(getClass().getResourceAsStream("/Data/groundtypes.xml"));
		} catch (JDOMException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Element gt = d.getRootElement();
		
		Iterator<Element> itr = gt.getChildren("Ground").iterator();
		groundTypes = new LinkedList<GroundType>();
		
		while (itr.hasNext()) {
			GroundType g = new GroundType(itr.next());
			groundTypes.add(g);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadObjectTypes() {
		objectTypes = new LinkedList<ObjectType>();
		for (String xml : objectxmls) {
			Document d = null;
			try {
				d = new SAXBuilder().build(getClass().getResourceAsStream("/Data/" + xml));
			} catch (JDOMException e) {
				e.printStackTrace();
				continue;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			
			Element root = d.getRootElement();
			Iterator<Element> itr = root.getChildren("Object").iterator();
			while (itr.hasNext()) {
				ObjectType t = new ObjectType(itr.next());
				objectTypes.add(t);
			}
		}
	}
	
	public GroundType getGroundType(int type) {
		for (GroundType t : groundTypes) {
			if (t.type == type) {
				return t;
			}
		}
		return null;
	}
	
	public ObjectType getObjectType(int type) {
		for (ObjectType t : objectTypes) {
			if (t.type == type) {
				return t;
			}
		}
		return null;
	}
	
	public ObjectType getObjectType(String id) {
		for (ObjectType t : objectTypes) {
			if (t.id.equals(id)) {
				return t;
			}
		}
		return null;
	}
	
	public Iterator<ObjectType> objectIterator() {
		return objectTypes.iterator();
	}
}
