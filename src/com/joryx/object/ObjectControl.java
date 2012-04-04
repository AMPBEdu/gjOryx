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

import java.util.concurrent.Callable;

import org.jdom.Element;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.joryx.JOryx;
import com.joryx.db.ObjectType;
import com.joryx.mesh.IndexedSpriteQuad;
import com.joryx.util.ImageData;
import com.oryxhatesjava.Client;
import com.oryxhatesjava.DataListener;
import com.oryxhatesjava.net.data.ObjectStatus;
import com.oryxhatesjava.net.data.Tile;

public class ObjectControl extends AbstractControl implements DataListener {

	private Element typeDefinition;
	private ObjectStatus status;
	private Vector3f oldLocation;
	private Vector3f targetLocation;
	private boolean moving;
	private float scale;
	private float timeToDestination;
	private boolean player;
	private float size = 1.0f;
	
	private int direction = DOWN;
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	
	protected IndexedSpriteQuad quad;
	
	private float mapWidth;
	private float mapHeight;
	
	public static Node create(AssetManager assetManager, ObjectType type, ObjectStatus status, float mapWidth, float mapHeight) {
		Node ret = new Node("Object");
		
		IndexedSpriteQuad iq = new IndexedSpriteQuad();
		Geometry g = generateStaticQuad(assetManager, iq, type);
		
		ret.attachChild(g);
		
		ObjectControl oc = new ObjectControl(ret, status, mapWidth, mapHeight, iq);
		ret.addControl(oc);
		
		return ret;
	}
	
	public static Geometry generateStaticQuad(AssetManager assetManager, IndexedSpriteQuad iq, ObjectType type) {
		Material m = new Material(assetManager, "MatDefs/RealmTexture.j3md");
		Geometry g = new Geometry("Geometry", iq);
		g.setLocalTranslation(-0.5f, -0.5f, 0.01f);
		g.setLocalScale(type.size);
		if (!type.drawOnGround) {
			g.rotate(0.5f, 0, 0);
		}
		if (type.textureType == ObjectType.TEXTURE) { 
			iq.setIndex(type.texture.index);
			iq.setKey(type.texture.file);
			g.setLocalScale(type.size * (ImageData.getCellWidthPixels(type.texture.file) / 8));
			iq.updateGeometry();
			Texture t = ImageData.loadTexture(assetManager, type.texture.file);
			t.setMagFilter(MagFilter.Nearest);
			m.setTexture("ColorMap", t);
		} else if (type.textureType == ObjectType.ANIMATED) {
			iq.setIndex(0);
			iq.setKey(type.animatedTexture.file);
			//iq.setScale(type.size);
			iq.updateGeometry();
			Texture t = ImageData.loadTexture(assetManager, type.animatedTexture.file);
			t.setMagFilter(MagFilter.Nearest);
			m.setTexture("ColorMap", t);
		}
		g.setMaterial(m);
		return g;
	}
	
	public ObjectControl(Spatial spatial, ObjectStatus status, float mapWidth, float mapHeight, IndexedSpriteQuad quad) {
		this.status = status;
		
		spatial.setLocalTranslation(status.data.pos.x, mapHeight-status.data.pos.y, 0);
		moving = false;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.quad = quad;
	}
	
	private ObjectControl(Spatial spatial, ObjectControl clone) {
		this.spatial = spatial;
		this.status = clone.status;
		this.targetLocation = new Vector3f(clone.targetLocation);
		this.moving = clone.moving;
		this.scale = clone.scale;
		this.typeDefinition = clone.typeDefinition;
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return new ObjectControl(this.spatial, this);
	}

	@Override
	protected void controlUpdate(float tpf) {
		if (moving && !player) {
			scale += tpf*timeToDestination;
			if (scale > 1) {
				scale = 1;
				moving = false;
			}
			spatial.setLocalTranslation(FastMath.interpolateLinear(scale, oldLocation, targetLocation));
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		
	}
	
	public void goToLocation(Vector3f targetLocation, int time) {
		oldLocation = spatial.getLocalTranslation().clone();
		this.targetLocation = targetLocation.clone();
		Vector3f diffVec = targetLocation.subtract(oldLocation).normalizeLocal();
		
		if (diffVec.y > 0 && diffVec.y > diffVec.x) {
			direction = UP;
		}
		
		if (diffVec.y < 0 && diffVec.y < diffVec.x) {
			direction = DOWN;
		}
		
		if (diffVec.x > 0 && diffVec.x > diffVec.y) {
			direction = RIGHT;
		}
		
		if (diffVec.x < 0 && diffVec.x < diffVec.y) {
			direction = LEFT;
		}
		
		if (this.targetLocation.equals(oldLocation)) {
			scale = 1;
		} else if (time > 0) {
			scale = 0;
			timeToDestination = (float)time/100f;
			moving = true;
		} else {
			scale = 1;
		}
	}
	
	public ObjectStatus getStatus() {
		return status;
	}

	@Override
	public DataListener objectAdded(Client client, ObjectStatus object) {
		return null;
	}

	@Override
	public DataListener objectRemoved(Client client, ObjectStatus object, int id) {
		if (id == status.data.objectId) {
			JOryx.getSingleton().enqueue(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					getSpatial().removeFromParent();
					return true;
				}
			});
			return this;
		}
		return null;
	}

	@Override
	public void objectUpdated(final Client client, final ObjectStatus object) {
		if (object.data.objectId == status.data.objectId) {
			goToLocation(new Vector3f(object.data.pos.x, mapHeight-object.data.pos.y, 0), client.getTickLengthMs());
		}
	}

	@Override
	public void tileAdded(Tile tile) {
		
	}
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean isMoving() {
		return moving;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public float getMapWidth() {
		return mapWidth;
	}
	
	public float getMapHeight() {
		return mapHeight;
	}

	public boolean isPlayer() {
		return player;
	}

	public void setPlayer(boolean player) {
		this.player = player;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}
}
