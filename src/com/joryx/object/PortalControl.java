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
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.joryx.appstate.screens.WorldScreen;
import com.joryx.db.ObjectType;
import com.joryx.mesh.IndexedSpriteQuad;
import com.oryxhatesjava.net.data.ObjectStatus;

public class PortalControl extends ObjectControl {

	public static Node create(AssetManager assetManager, ObjectType type, ObjectStatus status, float mapWidth, float mapHeight, Spatial target) {
		Node ret = new Node("Object");
		
		IndexedSpriteQuad iq = new IndexedSpriteQuad();
		PortalControl c = new PortalControl(ret, status, mapWidth, mapHeight, target, iq);
		Geometry g = ObjectControl.generateStaticQuad(assetManager, iq, type);
		ret.attachChild(g);
		ret.addControl(c);
		return ret;
	}
	
	private Spatial target;
	
	public PortalControl(Spatial spatial, ObjectStatus status, float mapWidth,
			float mapHeight, Spatial target, IndexedSpriteQuad quad) {
		super(spatial, status, mapWidth, mapHeight, quad);
		this.target = target;
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		super.controlUpdate(tpf);
		
		Vector3f loc = spatial.getLocalTranslation();
		float dist = loc.distance(target.getLocalTranslation());
		
		if (dist < 1f) {
			WorldScreen.getSingleton().showPortalPanel(getStatus());
		}
	}

}
