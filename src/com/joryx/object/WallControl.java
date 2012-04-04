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
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.joryx.control.NoMoveControl;
import com.joryx.db.ObjectType;
import com.joryx.db.TextureDefinition;
import com.joryx.mesh.IndexedSpriteQuad;
import com.joryx.util.ImageData;
import com.oryxhatesjava.net.data.ObjectStatus;

public class WallControl extends ObjectControl {

	public static Node create(AssetManager assetManager, ObjectType type, ObjectStatus status, float mapWidth, float mapHeight) {
		Node ret = new Node("Object");
		
		Material tm = new Material(assetManager, "MatDefs/RealmTexture.j3md");
		Texture t = null;
		IndexedSpriteQuad iq = new IndexedSpriteQuad();
		if (type.topTextureType == ObjectType.TEXTURE) {
			t = assetManager.loadTexture("Textures/" + type.topTexture.file + ".png");
			iq.setIndex(type.topTexture.index);
			iq.setKey(type.topTexture.file);
		} else if (type.topTextureType == ObjectType.RANDOM) {
			int rand = Math.round(FastMath.rand.nextFloat() * type.topRandomTexture.textures.size());
			if (rand == type.topRandomTexture.textures.size()) {
				rand -= 1;
			}
			TextureDefinition d = type.topRandomTexture.textures.get(rand);
			t = assetManager.loadTexture("Textures/" + d.file + ".png");
			iq.setIndex(d.index);
			iq.setKey(d.file);
		} else {
			t = assetManager.loadTexture("Textures/Missing.png");
		}
		t.setMagFilter(MagFilter.Nearest);
		tm.setTexture("ColorMap", t);
		iq.updateGeometry();
		Geometry g = new Geometry("Top Geometry", iq);
		g.setMaterial(tm);
		g.setLocalTranslation(-0.5f, -0.5f, 1);
		ret.attachChild(g);
		
		WallControl c = new WallControl(ret, status, mapWidth, mapHeight, iq);
		ret.addControl(c);
		
		int index = 0;
		Material m = new Material(assetManager, "MatDefs/RealmTexture.j3md");
		if (type.textureType == ObjectType.TEXTURE) {
			t = ImageData.loadTexture(assetManager, type.texture.file);
			index = type.texture.index;
		} else if (type.textureType == ObjectType.RANDOM) {
			int rand = Math.round(FastMath.rand.nextFloat() * type.randomTexture.textures.size());
			if (rand == type.randomTexture.textures.size()) {
				rand -= 1;
			}
			TextureDefinition d = type.randomTexture.textures.get(rand);
			t = ImageData.loadTexture(assetManager, d.file);
			index = d.index;
		}
		t.setMagFilter(MagFilter.Nearest);
		m.setTexture("ColorMap", t);
		
		// front side
		iq = new IndexedSpriteQuad();
		iq.setKey(type.texture.file);
		iq.setIndex(index);
		iq.updateGeometry();
		g = new Geometry("Wall", iq);
		g.setLocalTranslation(-0.5f, -0.5f, 0);
		g.setMaterial(m);
		g.rotate(FastMath.HALF_PI, 0, 0);
		ret.attachChild(g);
		
		// right side
		iq = new IndexedSpriteQuad();
		iq.setKey(type.texture.file);
		iq.setIndex(index);
		iq.updateGeometry();
		g = new Geometry("Wall", iq);
		g.setLocalTranslation(0.5f, -0.5f, 0);
		g.setMaterial(m);
		g.rotate(FastMath.HALF_PI, 0, FastMath.HALF_PI);
		ret.attachChild(g);
		
		// left side
		iq = new IndexedSpriteQuad();
		iq.setKey(type.texture.file);
		iq.setIndex(index);
		iq.updateGeometry();
		g = new Geometry("Wall", iq);
		g.setLocalTranslation(-0.5f, 0.5f, 0);
		g.setMaterial(m);
		g.rotate(FastMath.HALF_PI, 0, -FastMath.HALF_PI);
		ret.attachChild(g);
		
		// back side (facing away)
		iq = new IndexedSpriteQuad();
		iq.setKey(type.texture.file);
		iq.setIndex(index);
		iq.updateGeometry();
		g = new Geometry("Wall", iq);
		g.setLocalTranslation(-0.5f, 0.5f, 0);
		g.setMaterial(m);
		g.rotate(FastMath.HALF_PI, 0, 0);
		ret.attachChild(g);
		
		ret.addControl(new NoMoveControl());
		return ret;
	}
	public WallControl(Spatial spatial, ObjectStatus status, float mapWidth,
			float mapHeight, IndexedSpriteQuad quad) {
		super(spatial, status, mapWidth, mapHeight, quad);
	}

}
