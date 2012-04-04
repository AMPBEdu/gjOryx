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

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.joryx.appstate.DatabaseState;
import com.joryx.control.ProjectileControl;
import com.joryx.db.GroundType;
import com.joryx.db.ObjectType;
import com.joryx.db.ProjectileDefinition;
import com.joryx.db.TextureDefinition;
import com.joryx.mesh.IndexedSpriteQuad;
import com.joryx.util.ImageData;

public final class EntityFactory {

	private static Material lofiEnvMat;
	private static Material lofiEnv2Mat;
	private static Material lofiEnv3Mat;
	
	public static void loadGroundMaterials(AssetManager assetManager) {
		lofiEnvMat = new Material(assetManager, "MatDefs/RealmTile.j3md");
		Texture t = ImageData.loadTexture(assetManager, "lofiEnvironment");
		t.setMagFilter(MagFilter.Nearest);
		lofiEnvMat.setTexture("ColorMap", t);
		lofiEnv2Mat = new Material(assetManager, "MatDefs/RealmTile.j3md");
		t = ImageData.loadTexture(assetManager, "lofiEnvironment2");
		t.setMagFilter(MagFilter.Nearest);
		lofiEnv2Mat.setTexture("ColorMap", t);
		
		lofiEnv3Mat = new Material(assetManager, "MatDefs/RealmTile.j3md");
		t = ImageData.loadTexture(assetManager, "lofiEnvironment3");
		t.setMagFilter(MagFilter.Nearest);
		lofiEnv3Mat.setTexture("ColorMap", t);
	}
	
	public static Node makeProjectile(AssetManager assetManager, Spatial owner, DatabaseState db, int bulletId, ProjectileDefinition def, ObjectType container, Vector3f location, float angle, int damage, boolean ally, boolean player) {
		final Node nn = new Node("Projectile " + bulletId);
		
		ObjectType projType = db.getObjectType(db.getObjectType(def.containerId).type);
		
		nn.setLocalTranslation(location);
		nn.move(0, 0, 0);
		nn.setLocalScale(def.scale);
		nn.rotate(0, 0, -angle - (projType.projectileAngleCorrection*(FastMath.HALF_PI/2)));
		ProjectileControl pc = new ProjectileControl(def, owner, container, ally, bulletId, angle, player);
		nn.addControl(pc);
		
		IndexedSpriteQuad iq = new IndexedSpriteQuad(projType.texture.file);
		iq.setIndex(projType.texture.index);
		iq.updateGeometry();
		Geometry g = new Geometry("Projectile Geometry", iq);
		g.setLocalTranslation(-0.5f, -0.5f, 0.5f);
		
		Material m = new Material(assetManager, "MatDefs/RealmObject.j3md");
		Texture t = ImageData.loadTexture(assetManager, projType.texture.file);
		t.setMagFilter(MagFilter.Nearest);
		m.setTexture("ColorMap", t);
		g.setMaterial(m);
		nn.attachChild(g);
		
		return nn;
	}
	
	public static Node makeTile(AssetManager assetManager, GroundType type, int x, int y) {
		final Node ret = new Node("Tile " + x + " " + y);
		ret.setLocalTranslation(x, y, 0);
		
		IndexedSpriteQuad iq = new IndexedSpriteQuad();
		Geometry g = new Geometry("Tile Geometry", iq);
		g.move(0, -1, 0);
		
		Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		m.setColor("Color", ColorRGBA.Red);
		
		if (type.textureType == GroundType.TEXTURE) {
			if (type.texture.file.equals("lofiEnvironment")) {
				m = lofiEnvMat;
			}
			if (type.texture.file.equals("lofiEnvironment2")) {
				m = lofiEnv2Mat;
			}
			if (type.texture.file.equals("lofiEnvironment3")) {
				m = lofiEnv3Mat;
			}
			iq.setKey(type.texture.file);
			iq.setIndex(type.texture.index);
			iq.updateGeometry();
		} else if (type.textureType == GroundType.RANDOM) {
			int random = Math.round(FastMath.rand.nextFloat() * (type.randomTexture.textures.size()));
			if (random == type.randomTexture.textures.size()) {
				random -= 1;
			}
			TextureDefinition tex = type.randomTexture.textures.get(random);
			if (tex.file.equals("lofiEnvironment")) {
				m = lofiEnvMat;
			}
			if (tex.file.equals("lofiEnvironment2")) {
				m = lofiEnv2Mat;
			}
			if (tex.file.equals("lofiEnvironment3")) {
				m = lofiEnv3Mat;
			}
			iq.setKey(tex.file);
			iq.setIndex(tex.index);
			iq.updateGeometry();
		}
		
		g.setMaterial(m);
		
		ret.attachChild(g);
		
		return ret;
	}
}
