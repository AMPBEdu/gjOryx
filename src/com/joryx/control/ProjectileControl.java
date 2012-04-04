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

package com.joryx.control;

import java.io.IOException;
import java.util.List;

import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.joryx.JOryx;
import com.joryx.appstate.DatabaseState;
import com.joryx.appstate.WorldState;
import com.joryx.db.ObjectType;
import com.joryx.db.ProjectileDefinition;
import com.joryx.object.CharacterControl;
import com.joryx.object.ObjectControl;
import com.joryx.object.PlayerControl;
import com.oryxhatesjava.net.EnemyHitPacket;
import com.oryxhatesjava.net.PlayerHitPacket;
import com.oryxhatesjava.net.SquareHitPacket;
import com.oryxhatesjava.net.data.StatData;

public class ProjectileControl extends AbstractControl {

	private ProjectileDefinition projectile;
	private ObjectType containerType;
	private Spatial owner;
	
	private boolean ally;
	
	private Vector3f incrementVector;
	private Vector3f directionVector;
	private float lifeTime;
	private float life;
	private float angle;
	
	private int bulletId;
	private boolean initialized;
	
	private static boolean reverseOscillation;
	private float phase;
	private float frequency;
	private float amplitude;
	private Vector3f location;
	private boolean player;
	
	private Vector3f amplitudeVector;
	
	public ProjectileControl(ProjectileDefinition projectile, Spatial owner,  ObjectType containerType, boolean ally, int bulletId, float angle, boolean player) {
		this.projectile = projectile;
		this.containerType = containerType;
		this.ally = ally;
		this.bulletId = bulletId;
		this.angle = angle;
		this.player = player;
		this.owner = owner;
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void controlUpdate(float tpf) {
		if (!initialized) {
			incrementVector = new Vector3f(((float)projectile.speed / 10f), 0, 0);
			Quaternion rot = new Quaternion();
			rot.fromAngleNormalAxis(-angle, Vector3f.UNIT_Z);
			incrementVector = rot.multLocal(incrementVector);
			lifeTime = projectile.lifetimeSeconds;
			directionVector = incrementVector.normalize();
			location = spatial.getLocalTranslation().clone();
			
			if (projectile.amplitude != 0) {
				if (!reverseOscillation) {
					amplitudeVector = new Vector3f(0, projectile.amplitude, 0);
					amplitudeVector = rot.multLocal(amplitudeVector);
					reverseOscillation = true;
				} else {
					amplitudeVector = new Vector3f(0, -projectile.amplitude, 0);
					amplitudeVector = rot.multLocal(amplitudeVector);
					reverseOscillation = false;
				}
			} else {
				amplitudeVector = new Vector3f(0, 0, 0);
			}
			if (projectile.frequency != 0) {
				frequency = projectile.frequency;
			} else {
				frequency = 1;
			}
			initialized = true;
		}
		
		life += tpf;
		Vector3f inc = incrementVector.mult(tpf);
		
		location.addLocal(inc);
		location.subtractLocal(amplitudeVector.mult(FastMath.sin(phase)));
		phase += tpf * frequency * 10;
		location.addLocal(amplitudeVector.mult(FastMath.sin(phase)));
		spatial.setLocalTranslation(location);
		
		// collisions
		Ray ray = new Ray(spatial.getWorldTranslation(), directionVector);
		CollisionResults cr = new CollisionResults();
		List<Spatial> children = spatial.getParent().getChildren();
		for (Spatial s : children) {
			if (s == spatial) {
				continue;
			}

			if (s == null) {
				continue;
			}

			if (s == owner) {
				continue;
			}

			if (s.getControl(NoMoveControl.class) == null && s.getControl(PlayerControl.class) == null && s.getControl(CharacterControl.class) == null) {
				continue;
			}

			float distance = s.getLocalTranslation().distance(spatial.getLocalTranslation());
			if (distance < 0.5f) {
				// collided!
				CharacterControl cc = s.getControl(CharacterControl.class);
				PlayerControl pc = s.getControl(PlayerControl.class);
				NoMoveControl nmc = s.getControl(NoMoveControl.class);
				WorldState ws = JOryx.getSingleton().getStateManager().getState(WorldState.class);
				
				// ignore if it's an ally bullet hitting a player
				if (ally && pc != null) {
					continue;
				}
				
				// ignore if it's an enemy bullet hitting a character
				if (!ally && cc != null) {
					continue;
				}
				
				// hit if player and hit is CC
				if (player && cc != null && cc.isEnemy()) {
					int damage = calculateDamage();
					EnemyHitPacket php = new EnemyHitPacket();
					php.bulletId = bulletId;
					php.objectId = cc.getStatus().data.objectId;
					php.time = ws.getClient().getTime();
					
					ObjectType type = DatabaseState.getSingleton().getObjectType(cc.getStatus().objectType);

					int hp = cc.getStatus().data.getStat(StatData.HP).value;
					if (hp - damage <= 0) {
						if (type.deathSound != null) {
							AudioNode an = new AudioNode(JOryx.getSingleton().getAssetManager(), "Sounds/" + type.deathSound + ".wav");
							an.play();
						}
						php.kill = true;
					} else {
						if (type.hitSound != null) {
							AudioNode an = new AudioNode(JOryx.getSingleton().getAssetManager(), "Sounds/" + type.hitSound + ".wav");
							an.play();
						}
						php.kill = false;
					}
					ws.makeNotification(cc.getStatus().data.objectId, "-" + damage, ColorRGBA.Red.asIntABGR());

					try {
						ws.getClient().sendSyncPacket(php);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (pc != null && pc.isPlayer()) {
					// hit if unfriendly and hit local player
					PlayerHitPacket ehp = new PlayerHitPacket();
					ehp.bulletId = bulletId;
					ehp.objectId = owner.getControl(ObjectControl.class).getStatus().data.objectId;

					ObjectType type = DatabaseState.getSingleton().getObjectType(pc.getStatus().objectType);
					
					AudioNode an = new AudioNode(JOryx.getSingleton().getAssetManager(), "Sounds/" + type.hitSound + ".wav");
					an.play();
					try {
						ws.getClient().sendSyncPacket(ehp);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (nmc != null && player) {
					// hit if square
					SquareHitPacket shp = new SquareHitPacket();
					shp.bulletId = bulletId;
					shp.objectId = owner.getControl(ObjectControl.class).getStatus().data.objectId;
					try {
						ws.getClient().sendSyncPacket(shp);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				spatial.removeFromParent();
				return;
			}
		}
		
		if (life > lifeTime) {
			spatial.removeFromParent();
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}
	
	public int getBulletId() {
		return bulletId;
	}
	
	private int calculateDamage() {
		float ret = 0;
		
		if (projectile.damage != 0) {
			return projectile.damage;
		}
		
		ret = FastMath.rand.nextFloat() * (projectile.maxDamage - projectile.minDamage);
		ret += projectile.minDamage;
		
		if (player) {
			ObjectControl oc = owner.getControl(ObjectControl.class);
			int att = oc.getStatus().data.getStat(StatData.ATT).value + oc.getStatus().data.getStat(StatData.ATTBONUS).value;
			float mult = 0.5f + ((float)att / 50f);
			ret *= mult;
		}
		return Math.round(ret);
	}

}
