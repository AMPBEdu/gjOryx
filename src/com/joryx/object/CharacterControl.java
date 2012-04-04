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

package com.joryx.object;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.joryx.EntityFactory;
import com.joryx.JOryx;
import com.joryx.appstate.DatabaseState;
import com.joryx.appstate.screens.WorldScreen;
import com.joryx.db.ObjectType;
import com.joryx.db.ProjectileDefinition;
import com.joryx.mesh.IndexedSpriteQuad;
import com.joryx.util.ImageData;
import com.oryxhatesjava.Client;
import com.oryxhatesjava.DataListener;
import com.oryxhatesjava.net.data.ObjectStatus;

public class CharacterControl extends ObjectControl {

	private ObjectType type;
	private IndexedSpriteQuad quad;
	
	private boolean animated;
	private float timeToFrame;
	private int frame;
	private boolean shooting;
	private boolean dead;
	
	public static Node create(AssetManager assetManager, ObjectType type, ObjectStatus status, float mapWidth, float mapHeight) {
		Node ret = new Node("Object");
		IndexedSpriteQuad iq = new IndexedSpriteQuad();
		CharacterControl c = new CharacterControl(ret, status, mapWidth, mapHeight, type, iq);
		Geometry g = new Geometry("Geometry", iq);
		Material m = new Material(assetManager, "MatDefs/RealmObject.j3md");
		
		boolean animated = false;
		if (type.textureType == ObjectType.TEXTURE) {
			Texture t = ImageData.loadTexture(assetManager, type.texture.file);
			m.setTexture("ColorMap", t);
			t.setMagFilter(MagFilter.Nearest);
			iq.setIndex(type.texture.index);
			iq.setKey(type.texture.file);
			//iq.setScale(type.size * (ImageData.getCellWidthPixels(type.texture.file) / 8));
			//c.setSize(iq.getScale());
			iq.updateGeometry();
		} else if (type.textureType == ObjectType.ANIMATED) {
			Texture t = ImageData.loadTexture(assetManager, type.animatedTexture.file);
			iq.setKey(type.animatedTexture.file);
			m.setTexture("ColorMap", t);
			t.setMagFilter(MagFilter.Nearest);
			//iq.setScale(type.size);
			c.setSize(type.size);
			animated = true;
		}
		g.setMaterial(m);
		g.setLocalTranslation(-0.5f, -0.5f, 0.01f);
		g.rotate(0.5f, 0, 0);
		ret.attachChild(g);
		c.setAnimated(animated);
		ret.addControl(c);
		return ret;
	}
	public CharacterControl(Spatial spatial, ObjectStatus status,
			float mapWidth, float mapHeight, ObjectType type, IndexedSpriteQuad iq) {
		super(spatial, status, mapWidth, mapHeight, iq);
		this.type = type;
		quad = iq;
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		super.controlUpdate(tpf);
		
		if (animated) {
			if (isMoving()) {
				timeToFrame += tpf * 5;
				if (timeToFrame >= 1) {
					frame = (frame != 2 ? 2 : 1);
					timeToFrame = 0;
				}
			} else {
				frame = 0;
			}
			animateQuad();
		}
	}
	
	public void shoot(int bulletId, int slotId, float angle, int numShots, float angleInc) {
		// get the projectile definition
		ProjectileDefinition def = null;
		for (ProjectileDefinition d : type.projectiles) {
			if (d.id == slotId) {
				def = d;
				break;
			}
		}
		
		for (int i = 0; i < numShots; i++) {
			Node proj = EntityFactory.makeProjectile(JOryx.getSingleton().getAssetManager(), getSpatial(), DatabaseState.getSingleton(), bulletId, def, type, spatial.getLocalTranslation(), angle + (angleInc * (float)i), 0, false, false);
			
			spatial.getParent().attachChild(proj);
		}
	}

	private void animateQuad() {
		quad.setIndex(frame + (type.animatedTexture.index * 7));
		switch (getDirection()) {
		case ObjectControl.UP:
			quad.setFlipHorizontally(false);
			break;
		case ObjectControl.DOWN:
			quad.setFlipHorizontally(false);
			break;
		case ObjectControl.LEFT:
			quad.setFlipHorizontally(true);
			break;
		case ObjectControl.RIGHT:
			quad.setFlipHorizontally(false);
			break;
		}
		
		quad.updateGeometry();
	}
	public boolean isAnimated() {
		return animated;
	}
	public void setAnimated(boolean animated) {
		this.animated = animated;
	}
	public boolean isEnemy() {
		return type.enemy;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	@Override
	public DataListener objectRemoved(Client client, ObjectStatus object, int id) {
		if (id == getStatus().data.objectId && dead) {
			WorldScreen.getSingleton().addChatLine("", "", "Object killed");
		}
		return super.objectRemoved(client, object, id);
	}
}
