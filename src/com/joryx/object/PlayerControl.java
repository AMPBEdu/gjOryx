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

import java.util.List;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.joryx.EntityFactory;
import com.joryx.JOryx;
import com.joryx.appstate.DatabaseState;
import com.joryx.appstate.WorldState;
import com.joryx.appstate.screens.WorldScreen;
import com.joryx.control.NoMoveControl;
import com.joryx.db.ObjectType;
import com.joryx.db.ProjectileDefinition;
import com.joryx.mesh.IndexedSpriteQuad;
import com.joryx.util.ImageData;
import com.oryxhatesjava.net.PlayerShootPacket;
import com.oryxhatesjava.net.data.Location;
import com.oryxhatesjava.net.data.ObjectStatus;
import com.oryxhatesjava.net.data.StatData;

public class PlayerControl extends ObjectControl implements ActionListener {
	private boolean moveForward;
	private boolean moveBackward;
	private boolean strafeLeft;
	private boolean strafeRight;
	
	private float speedMult = 1.0f;
	
	private boolean shooting;
	
	private boolean buttonShooting;
	private float shootDelay;
	private int bulletId;
	
	private float timeToFrame;
	private int frame;
	
	public ObjectType type;
	
	public static Node create(AssetManager assetManager, ObjectType type, ObjectStatus status, float mapWidth, float mapHeight) {
		Node ret = new Node("Object " + status.data.objectId);
		IndexedSpriteQuad iq = new IndexedSpriteQuad(type.playerTexture.file);
		Geometry g = new Geometry("Geometry", iq);
		Material m = new Material(assetManager, "MatDefs/RealmObject.j3md");
		g.setMaterial(m);
		Texture t = ImageData.loadTexture(assetManager, type.playerTexture.file);
		m.setTexture("ColorMap", t);
		t.setMagFilter(MagFilter.Nearest);
		
		ret.attachChild(g);
		g.setLocalTranslation(-0.5f, -0.5f, 0.01f);
		g.rotate(0.5f, 0, 0);
		PlayerControl c = new PlayerControl(ret, status, mapWidth, mapHeight, iq);
		c.type = type;
		ret.addControl(c);
		return ret;
	}
	
	public PlayerControl(Spatial spatial, ObjectStatus status, float mapWidth,
			float mapHeight, IndexedSpriteQuad quad) {
		super(spatial, status, mapWidth, mapHeight, quad);
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		super.controlUpdate(tpf);
		
		if (isPlayer()) {
			Vector3f strafe = new Vector3f(0, 0, 0);
			Vector3f straight = new Vector3f(0, 0, 0);
			
			if (moveForward) {
				straight.y += 1;
			}
			
			if (moveBackward) {
				straight.y -= 1;
			}
			
			if (strafeLeft) {
				strafe.x -= 1;
			}
			
			if (strafeRight) {
				strafe.x += 1;
			}
			
			if (strafe.length() == 0 && straight.length() == 0) {
				setMoving(false);
			} else {
				setMoving(true);
				// do movement
				strafe.normalizeLocal();
				straight.normalizeLocal();
				Vector3f fin = strafe.add(straight).normalizeLocal();
				spatial.getLocalRotation().multLocal(fin);
				
				float dist = 4f + 5.6f*(((float)(getStatus().data.getStat(StatData.SPD).value + getStatus().data.getStat(StatData.SPDBONUS).value)/75f) - 0.1f);
				dist *= speedMult;
				fin.multLocal(dist);
				fin.multLocal(tpf);
				
				// Check maximum distance in each direction
				Vector3f origin = spatial.getWorldTranslation();
				Ray rayUpLeft = new Ray(origin.add(-0.4f, 0f, 0), Vector3f.UNIT_Y);
				Ray rayUpRight = new Ray(origin.add(0.4f, 0f, 0), Vector3f.UNIT_Y);
				Ray rayLeftTop = new Ray(origin.add(0f, 0.4f, 0), Vector3f.UNIT_X.negate());
				Ray rayLeftBottom = new Ray(origin.add(0f, -0.4f, 0), Vector3f.UNIT_X.negate());
				Ray rayRightTop = new Ray(origin.add(0f, 0.4f, 0), Vector3f.UNIT_X);
				Ray rayRightBottom = new Ray(origin.add(0f, -0.4f, 0), Vector3f.UNIT_X);
				Ray rayDownLeft = new Ray(origin.add(-0.4f, 0f, 0), Vector3f.UNIT_Y.negate());
				Ray rayDownRight = new Ray(origin.add(0.4f, 0f, 0), Vector3f.UNIT_Y.negate());
				
				float maxUp = 1000;
				float maxRight = 1000;
				float maxDown = -1000;
				float maxLeft = -1000;
				
				Node parent = spatial.getParent(); // world node
				List<Spatial> children = parent.getChildren();
				CollisionResults rs = new CollisionResults();
				PortalControl setPortal = null;
				for (Spatial s : children) {
					if (s == spatial) {
						continue;
					}
					
					if (s.getWorldTranslation().distance(spatial.getWorldTranslation()) > 5) {
						continue;
					}
					
					if (s.getControl(PortalControl.class) != null) {
						PortalControl pc = s.getControl(PortalControl.class);
						float distance = spatial.getLocalTranslation().distance(s.getLocalTranslation());
						if (distance < 1f) {
							setPortal = pc;
						}
					}
					
					if (s.getControl(NoMoveControl.class) != null) {
						float distance = 0;
						int ret = 0;

						// Down
						if (moveBackward) {
							ret = s.collideWith(rayDownLeft, rs);
							if (ret > 0) {
								distance = rs.getClosestCollision().getDistance();
								if (distance <= -maxDown) {
									maxDown = -distance;
								}
							}
							ret = s.collideWith(rayDownRight, rs);
							if (ret > 0) {
								distance = rs.getClosestCollision().getDistance();
								if (distance <= -maxDown) {
									maxDown = -distance;
								}
							}
						}

						// Up
						if (moveForward) {
							ret = s.collideWith(rayUpLeft, rs);
							if (ret > 0) {
								distance = rs.getClosestCollision().getDistance();
								if (distance <= maxUp) {
									maxUp = distance;
								}
							}
							ret = s.collideWith(rayUpRight, rs);
							if (ret > 0) {
								distance = rs.getClosestCollision().getDistance();
								if (distance <= maxUp) {
									maxUp = distance;
								}
							}
						}
						
						// Left
						if (strafeLeft) {
							ret = s.collideWith(rayLeftTop, rs);
							if (ret > 0) {
								distance = rs.getClosestCollision().getDistance();
								if (distance <= -maxLeft) {
									maxLeft = -distance;
								}
							}
							ret = s.collideWith(rayLeftBottom, rs);
							if (ret > 0) {
								distance = rs.getClosestCollision().getDistance();
								if (distance <= -maxLeft) {
									maxLeft = -distance;
								}
							}
						}

						// Right
						if (strafeRight) {
							ret = s.collideWith(rayRightTop, rs);
							if (ret > 0) {
								distance = rs.getClosestCollision().getDistance();
								if (distance <= maxRight) {
									maxRight = distance;
								}
							}
							ret = s.collideWith(rayRightBottom, rs);
							if (ret > 0) {
								distance = rs.getClosestCollision().getDistance();
								if (distance <= maxRight) {
									maxRight = distance;
								}
							}
						}
					}
				}
				
				if (setPortal != null) {
					WorldScreen.getSingleton().showPortalPanel(setPortal.getStatus());
				} else {
					WorldScreen.getSingleton().hidePortalPanel();
				}
				
				maxLeft += 0.5f;
				maxRight -= 0.5f;
				maxUp -= 0.5f;
				maxDown += 0.5f;
				
				if (fin.x > 0 && maxRight < fin.x) {
					fin.x = maxRight;
				} else if (fin.x < 0 && maxLeft > fin.x) {
					fin.x = maxLeft;
				}
				
				if (fin.y > 0 && maxUp < fin.y) {
					fin.y = maxUp;
				} else if (fin.y < 0 && maxDown > fin.y) {
					fin.y = maxDown;
				}
				
				if (strafeRight) {
					setDirection(ObjectControl.RIGHT);
				}
				if (strafeLeft) {
					setDirection(ObjectControl.LEFT);
				}
				if (moveForward) {
					setDirection(ObjectControl.UP);
				}
				if (moveBackward) {
					setDirection(ObjectControl.DOWN);
				}
				
				spatial.move(fin);
			} //end movement block
			
			// shooting code
			if (buttonShooting && shootDelay <= 0) {
				Vector2f mp = JOryx.getSingleton().getInputManager().getCursorPosition();
				Vector3f wp = JOryx.getSingleton().getCamera().getWorldCoordinates(mp, 0);
				Vector3f dir = JOryx.getSingleton().getCamera().getWorldCoordinates(mp, 1).subtractLocal(wp);
				Ray ray = new Ray(wp, dir);
				Geometry plane = new Geometry("Hit plane", new Quad(256, 256));
				plane.setLocalTranslation(-128, -128, 0);
				plane.move(spatial.getLocalTranslation());
				CollisionResults cr = new CollisionResults();
				ray.collideWith(plane.getWorldBound(), cr);
				CollisionResult r = cr.getClosestCollision();
				float angle = 0;
				if (r != null) { // should never be null anyway
					Vector3f loc = r.getContactPoint().subtract(spatial.getLocalTranslation());

					if (loc.x > 0) {
						angle = -FastMath.atan(loc.y/loc.x);
					}
					if (loc.x < 0) {
						angle = FastMath.atan(loc.y/-loc.x) - FastMath.PI;
					}
				}
				
				shoot(-1, 0, angle);
				shooting = true;
				float dex = getStatus().data.getStat(StatData.DEX).value + getStatus().data.getStat(StatData.DEXBONUS).value; 
				shootDelay = 1f / (1.5f + (6.5f * (dex / 75f)));
			} else if (shootDelay > 0) {
				shootDelay -= tpf;
			}
		} //end player only block
		
		animate(tpf);
	}

	public void shoot(int bulletId, int slotId, float angle) {
		// need to get the weapon item
		setShooting(true);
		StatData item = getStatus().data.getStat(StatData.INVENTORY0 + slotId);
		if (item != null) {
			int weaponItem = item.value;
			ObjectType type = DatabaseState.getSingleton().getObjectType(weaponItem);
			shootDelay *= type.projectileRateOfFire;
			ProjectileDefinition def = type.projectiles.get(0);
			WorldState ws = JOryx.getSingleton().getStateManager().getState(WorldState.class);
			int time = ws.getClient().getTime();
			for (int i = 0; i < type.numProjectiles; i++) {
				boolean sendPacket = false;
				if (!isPlayer() && i > 0) {
					break;
				} else if (isPlayer()) {
					this.bulletId += 1;
					if (this.bulletId == 256) {
						this.bulletId = 0;
					}
					bulletId = this.bulletId;
					sendPacket = true;
				}
				Node proj = EntityFactory.makeProjectile(JOryx.getSingleton().getAssetManager(), getSpatial(), DatabaseState.getSingleton(), bulletId, def, type, spatial.getLocalTranslation(), angle, 0, true, isPlayer());

				spatial.getParent().attachChild(proj);

				if (sendPacket) {
					// send the packet
					PlayerShootPacket psp = new PlayerShootPacket();
					psp.angle = angle;
					psp.startingPos = new Location(spatial.getLocalTranslation().x, getMapHeight()-spatial.getLocalTranslation().y);
					psp.containerType = (short) type.type;
					psp.time = time;
					psp.bulletId = (byte) bulletId++;
					try {
						ws.getClient().sendSyncPacket(psp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void animate(float tpf) {
		if (shooting) {
			timeToFrame += tpf * 5;
			if (timeToFrame >= 1) {
				if (frame == 5) {
					shooting = false;
					frame = 0;
				} else {
					frame = 5;
				}
				timeToFrame = 0;
			}
		} else if (isMoving()) {
			int dir = getDirection();
			timeToFrame += tpf * 5 * speedMult;
			if (timeToFrame >= 1) {
				if (dir == ObjectControl.LEFT || dir == ObjectControl.RIGHT) {
					frame = (frame != 0 ? 0 : 1);
				} else {
					frame = (frame != 1 ? 1 : 2);
				}
				timeToFrame = 0;
			}
		} else {
			timeToFrame = 0;
			frame = 0;
		}
		
		animateQuad();
	}
	
	private void animateQuad() {
		quad.setDoubleWidth(frame == 5);
		switch (getDirection()) {
		case ObjectControl.UP:
			quad.setIndex(frame + 14 + (type.playerTexture.index * 21));
			quad.setFlipHorizontally(false);
			break;
		case ObjectControl.DOWN:
			quad.setIndex(frame + 7 + (type.playerTexture.index * 21));
			quad.setFlipHorizontally(false);
			break;
		case ObjectControl.LEFT:
			quad.setIndex(frame + (type.playerTexture.index * 21));
			quad.setFlipHorizontally(true);
			break;
		case ObjectControl.RIGHT:
			quad.setIndex(frame + (type.playerTexture.index * 21));
			quad.setFlipHorizontally(false);
			break;
		}
		
		quad.updateGeometry();
	}

	public float getSpeedMult() {
		return speedMult;
	}

	public void setSpeedMult(float speedMult) {
		this.speedMult = speedMult;
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("PlayerStrafeLeft")) {
			strafeLeft = isPressed;
		}
		
		if (name.equals("PlayerStrafeRight")) {
			strafeRight = isPressed;
		}
		
		if (name.equals("PlayerMoveForward")) {
			moveForward = isPressed;
		}
		
		if (name.equals("PlayerMoveBackward")) {
			moveBackward = isPressed;
		}
		
		if (name.equals("PlayerShoot")) {
			buttonShooting = isPressed;
		}
	}

	public boolean isShooting() {
		return shooting;
	}

	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}
}
