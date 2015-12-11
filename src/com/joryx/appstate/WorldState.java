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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.joryx.EntityFactory;
import com.joryx.JOryx;
import com.joryx.appstate.screens.LoginScreen;
import com.joryx.appstate.screens.WorldScreen;
import com.joryx.control.ChaseCameraControl;
import com.joryx.control.NotificationControl;
import com.joryx.db.GroundType;
import com.joryx.db.ObjectType;
import com.joryx.object.CharacterControl;
import com.joryx.object.ObjectControl;
import com.joryx.object.PlayerControl;
import com.joryx.object.PortalControl;
import com.joryx.object.WallControl;
import com.oryxhatesjava.Client;
import com.oryxhatesjava.ClientListener;
import com.oryxhatesjava.DataListener;
import com.oryxhatesjava.PacketListener;
import com.oryxhatesjava.net.AllyShootPacket;
import com.oryxhatesjava.net.DamagePacket;
import com.oryxhatesjava.net.EnemyShootPacket;
import com.oryxhatesjava.net.FailurePacket;
import com.oryxhatesjava.net.HelloPacket;
import com.oryxhatesjava.net.LoadPacket;
import com.oryxhatesjava.net.MapInfoPacket;
import com.oryxhatesjava.net.MovePacket;
import com.oryxhatesjava.net.NewTickPacket;
import com.oryxhatesjava.net.NotificationPacket;
import com.oryxhatesjava.net.Packet;
import com.oryxhatesjava.net.ReconnectPacket;
import com.oryxhatesjava.net.ShootAckPacket;
import com.oryxhatesjava.net.ShootPacket;
import com.oryxhatesjava.net.TextPacket;
import com.oryxhatesjava.net.UpdatePacket;
import com.oryxhatesjava.net.data.Location;
import com.oryxhatesjava.net.data.ObjectStatus;
import com.oryxhatesjava.net.data.Tile;
import com.oryxhatesjava.util.GUID;

public class WorldState extends AbstractAppState implements ClientListener, DataListener, PacketListener {

	private Client client;
	private String serverName = "";
	private String serverHost = "";
	private int serverPort = 2050;
	private int gameId = 0;
	private int keyTime = -1;
	private byte[] key;
	
	private PlayerControl playerControl;
	
	private static final int TDIST_X = 13;
	private static final int TDIST_Y = 9;
	
	private Application app;
	
	private int mapWidth;
	private int mapHeight;
	private byte map[];
	private Spatial tileSpatials[];
	private boolean firstTileBatchReceived = false;
	
	private Node worldNode = new Node("World");
	private Node tileNode = new Node("Tiles");
	
	private Vector3f playerLastLocation;
	
	private int currentTime;
	
	private boolean changingGame;
	
	private List<Spatial> projectilesToAdd = new LinkedList<Spatial>();
	
	public WorldState(String address) {
		serverHost = "127.0.0.1";
	}
	
	public WorldState(String name, String address, int port, int gameId, int keyTime, byte[] key) {
		serverName = "Proxy";
		serverHost = "127.0.0.1";
		serverPort = 2050;
		this.gameId = gameId;
		this.keyTime = keyTime;
		System.out.println(key);
		this.key = key;
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		
		this.app = app;
		
		client = new Client();
		RootNodeState root = stateManager.getState(RootNodeState.class);
		root.getRootNode().attachChild(tileNode);
		root.getRootNode().attachChild(worldNode);
		
		WorldScreen.getSingleton().hidePortalPanel();
		
		try {
			client.addClientListener(this);
			client.addDataListener(this);
			client.addPacketListener(this);
			client.connect(InetAddress.getByName(serverHost), serverPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stateDetached(AppStateManager stateManager) {
		super.stateDetached(stateManager);
		RootNodeState root = stateManager.getState(RootNodeState.class);
		root.getRootNode().detachChild(tileNode);
		root.getRootNode().detachChild(worldNode);
		app.getInputManager().removeListener(playerControl);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		if (client.isConnected()) {
			currentTime = client.getTime();
		}
		
		if (firstTileBatchReceived && playerControl != null && playerLastLocation != null) {
			
			if (playerLastLocation.distance(playerControl.getSpatial().getLocalTranslation()) > 1) {
				// recalculate the scene tiles
				playerLastLocation = playerControl.getSpatial().getLocalTranslation().clone();
				final int x = (int)Math.floor(playerLastLocation.x);
				final int y = (int)Math.ceil(playerLastLocation.y);
				
				final DatabaseState db = app.getStateManager().getState(DatabaseState.class);
				
				refreshTiles(db, x, y, tileNode);
				
				GroundType under = db.getGroundType(getTile(x, mapHeight-y));
				if (under != null && under.speedModifier != 0) {
					playerControl.setSpeedMult(under.speedModifier);
				}
			}
		}
		
		for (Spatial s : projectilesToAdd) {
			worldNode.attachChild(s);
		}
		
		projectilesToAdd.clear();
	}
	
	public void refreshTiles(DatabaseState db, int x, int y, final Node toAddTo) {
		// Removal step
		List<Spatial> children = tileNode.getChildren();
		
		for (Spatial s : children) {
			int sx = Math.round(s.getLocalTranslation().x);
			int sy = Math.round(s.getLocalTranslation().y);
			
			if (sx >= (x + TDIST_X) || sx < (x - TDIST_X) || sy < (y - TDIST_Y) || sy >= (y + TDIST_Y)) {
				setTileSpatial(sx, sy, null);
			}
		}
		
		// Addition step
		tileNode.detachAllChildren();
		for (int ix = x-TDIST_X; ix < x + TDIST_X; ix++) {
			for (int iy = y-TDIST_Y; iy < y + TDIST_Y; iy++) {
				int type = getTile(ix, mapHeight-iy) & 0xFF;
				
				if (type == 0xB4) {
					continue;
				}
				
				Spatial tile = getTileSpatial(ix, iy);
				if (tile == null) {
					tile = EntityFactory.makeTile(app.getAssetManager(), db.getGroundType(type), ix, iy);
					setTileSpatial(ix, iy, tile);
				}
				tileNode.attachChild(tile);
			}
		}
	}
	
	@Override
	public DataListener objectAdded(final Client client, final ObjectStatus object) {
		if (!isEnabled()) {
			return null;
		}
		
		final Spatial nn = createObject(client, object);
		if (nn == null) {
			return null;
		}
		PlayerControl pc = nn.getControl(PlayerControl.class);
		if (pc != null) {
			if (client.getPlayerObject() == pc.getStatus()) {
				playerControl = pc;
				pc.setPlayer(true);
				app.getInputManager().addListener(playerControl, "PlayerStrafeLeft", "PlayerStrafeRight", "PlayerMoveForward", "PlayerMoveBackward", "PlayerTurnLeft", "PlayerTurnRight", "PlayerShoot");
				
				ChaseCameraControl cc = new ChaseCameraControl(app.getCamera());
				nn.addControl(cc);
				
				playerLastLocation = new Vector3f();
			}
		}
		
		app.enqueue(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				if (nn != null) {
					worldNode.attachChild(nn);
				}
				
				return true;
			}
			
		});
		return nn.getControl(ObjectControl.class);
	}

	@Override
	public DataListener objectRemoved(final Client client, final ObjectStatus object, final int id) {
		if (id == playerControl.getStatus().data.objectId) {
			JOryx.getSingleton().getInputManager().removeListener(playerControl);
		}
		
		return null;
	}

	@Override
	public void objectUpdated(final Client client, final ObjectStatus object) {
		
	}

	@Override
	public void connected(Client client) {
		HelloPacket hp = new HelloPacket();
		hp.buildVersion = "27.7.0";
		hp.gameId = gameId;
		hp.guid = GUID.encrypt(SettingsState.getSingleton().username);
		hp.password = GUID.encrypt(SettingsState.getSingleton().password);
		hp.keyTime = keyTime;
		hp.key = new byte[0];
		hp.secret = ""; //unused... only for kongregate/steam
		
		try {
			client.sendSyncPacket(hp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnected(Client client) {
		final WorldState ws = this;
		if (changingGame) {
			return;
		}
		app.enqueue(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				app.getStateManager().attach(LoginScreen.getSingleton());
				app.getStateManager().detach(ws);
				app.getStateManager().detach(WorldScreen.getSingleton());
				RootNodeState root = app.getStateManager().getState(RootNodeState.class);
				root.getNiftyDisplay().getNifty().gotoScreen("screenLogin");
				return true;
			}
		});
	}

	@Override
	public void packetReceived(Client client, Packet pkt) {
		if (pkt.type == Packet.UPDATE) {
			UpdatePacket up = (UpdatePacket)pkt;
			if (!firstTileBatchReceived && up.tiles != null) {
				firstTileBatchReceived = true;
			}
		}
		
		if (pkt.type == Packet.FAILURE) {
			String desc = ((FailurePacket)pkt).errorDescription;
			WorldScreen.getSingleton().addChatLine("", "", desc);
		}
		
		if (pkt.type == Packet.MAPINFO) {
			LoadPacket lp = new LoadPacket();
			lp.charId = SettingsState.getSingleton().charId;
			
			MapInfoPacket mip = (MapInfoPacket) pkt;
			mapHeight = mip.height;
			mapWidth = mip.width;
			
			map = new byte[mapWidth*mapHeight];
			tileSpatials = new Spatial[mapWidth*mapHeight];
			
			Arrays.fill(map, (byte)0xB4);
			
			try {
				client.sendSyncPacket(lp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (pkt.type == Packet.NEW_TICK) {
			NewTickPacket ntp = (NewTickPacket)pkt;
			MovePacket mp = new MovePacket();
			Location nl = new Location();
			nl.x = playerControl.getSpatial().getLocalTranslation().x;
			nl.y = mapHeight-playerControl.getSpatial().getLocalTranslation().y;
			mp.newPosition = nl;
			mp.tickId = ntp.tickId;
			mp.time = client.getTime();
			
			try {
				client.sendSyncPacket(mp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (pkt.type == Packet.NOTIFICATION) {
			NotificationPacket np = (NotificationPacket)pkt;
			makeNotification(np.objectId, np.text, np.color);
			return;
		}
		
		if (pkt.type == Packet.ALLYSHOOT) {
			final AllyShootPacket as = (AllyShootPacket)pkt;
			DatabaseState db = app.getStateManager().getState(DatabaseState.class);
			ObjectType containerType = db.getObjectType(as.containerType);
			PlayerControl oc = null;
			for (Spatial s : worldNode.getChildren()) {
				oc = s.getControl(PlayerControl.class);
				if (oc != null && oc.getStatus().data.objectId != as.ownerId) {
					oc = null;
				}
				if (oc != null) {
					break;
				}
			}
			
			if (oc != null) {
				final PlayerControl pc = oc;
				app.enqueue(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						pc.shoot(as.bulletId, 0, as.angle);
						return true;
					}
				});
			}
			return;
		}
		
		if (pkt.type == Packet.TEXT) {
			final TextPacket tp = (TextPacket)pkt;
			app.enqueue(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					WorldScreen.getSingleton().addChatLine(tp.name, tp.recipient, tp.text);
					return true;
				}
			});
		}
		
		if (pkt.type == Packet.ENEMYSHOOT) {
			final EnemyShootPacket esp = (EnemyShootPacket)pkt;
			
			ObjectControl oc = null;
			for (Spatial s : worldNode.getChildren()) {
				oc = s.getControl(ObjectControl.class);
				if (oc != null && oc.getStatus().data.objectId != esp.ownerId) {
					oc = null;
				}
				if (oc != null) {
					break;
				}
			}
			
			if (oc != null) {
				final CharacterControl cc = (CharacterControl)oc;
				app.enqueue(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						if (!esp.hasMultiProjectiles) {
							cc.shoot(esp.bulletId, esp.bulletType, esp.angle, 1, 0);
						} else {
							cc.shoot(esp.bulletId, esp.bulletType, esp.angle, esp.numShots, esp.angleInc);
						}
						return true;
					}
				});
			}
			
			ShootAckPacket sap = new ShootAckPacket();
			sap.time = currentTime;
			try {
				client.sendSyncPacket(sap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (pkt.type == Packet.SHOOT) {
			final ShootPacket sp = (ShootPacket)pkt;
			ObjectControl oc = null;
			for (Spatial s : worldNode.getChildren()) {
				oc = s.getControl(ObjectControl.class);
				if (oc != null && oc.getStatus().data.objectId != sp.ownerId) {
					oc = null;
				}
				if (oc != null) {
					break;
				}
			}
			
			PlayerControl pc = null;
			CharacterControl cc = null;
			if (oc instanceof PlayerControl) {
				pc = (PlayerControl)oc;
				// do not respond with ack
			}
			
			if (oc instanceof CharacterControl) {
				cc = (CharacterControl)oc;
				
				ShootAckPacket sap = new ShootAckPacket();
				sap.time = currentTime;
				try {
					client.sendSyncPacket(sap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			final PlayerControl fpc = pc;
			final CharacterControl fcc = cc;
			app.enqueue(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					if (fpc != null) {
						fpc.shoot(sp.bulletId, 1, sp.angle);
					}
					if (fcc != null) {
						fcc.shoot(sp.bulletId, 0, sp.angle, 0, 0);
					}
					return true;
				}
			});
			return;
		}
		
		if (pkt.type == Packet.DAMAGE) {
			DamagePacket dp = (DamagePacket)pkt;
			makeNotification(dp.targetId, "-" + dp.damageAmount, ColorRGBA.Red.asIntARGB());
		}
		
		if (pkt.type == Packet.RECONNECT) {
			ReconnectPacket rp = (ReconnectPacket)pkt;
			if (rp.host.length() == 0) {
				rp.host = serverHost;
			}
			if (rp.port == -1) {
				rp.port = serverPort;
			}
			reconnect(rp.name, rp.host, rp.port, rp.gameId, rp.keyTime, rp.key);
		}
	}
	
	private void reconnect(String name, String host, int port, int gameId, int keyTime, byte[] key) {
		changingGame = true;
		if (client.isConnected()) {
			client.disconnect();
		}
		final WorldState ns = new WorldState(name, host, port, gameId, keyTime, key);
		final WorldState ws = this;
		app.enqueue(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				app.getStateManager().detach(ws);
				app.getStateManager().attach(ns);
				return true;
			}
		});
	}

	public void makeNotification(int objectId, final String text, int color) {
		final RootNodeState root = app.getStateManager().getState(RootNodeState.class);
		List<Spatial> children = worldNode.getChildren();
		
		for (final Spatial s : children) {
			ObjectControl oc = s.getControl(ObjectControl.class);
			if (oc == null) {
				continue;
			}
			if (oc.getStatus().data.objectId == objectId) {
				final Node notifNode = new Node("Notification");
				final ColorRGBA colorr = new ColorRGBA();
				colorr.a = (float)((color & 0xFF000000) >> 24) / (float)0xFF;
				colorr.r = (float)((color & 0x00FF0000) >> 16) / (float)0xFF;
				colorr.g = (float)((color & 0x0000FF00) >> 8) / (float)0xFF;
				colorr.b = (float)((color & 0x000000FF)) / (float)0xFF;
				final NotificationControl control = new NotificationControl();
				notifNode.addControl(control);
				notifNode.move(0, 0, 1);
				app.enqueue(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						((Node)s).attachChild(notifNode);
						WorldScreen.getSingleton().addNotification(text, colorr, control);
						return true;
					}
				});
				break;
			}
		}
	}

	@Override
	public boolean filter(Packet pkt) {
		return true;
	}

	@Override
	public void tileAdded(Tile tile) {
		setTile(tile.x, tile.y, (byte)tile.type);
	}
	
	public Spatial createObject(Client client, ObjectStatus status) {
		DatabaseState db = app.getStateManager().getState(DatabaseState.class);

		ObjectType type = db.getObjectType(status.objectType);
		
		if (type == null) {
			return null;
		}
		if (type.className.equals("Player")) {
			return PlayerControl.create(app.getAssetManager(), type, status, mapWidth, mapHeight);
		} else if (type.className.equals("Character")) {
			return CharacterControl.create(app.getAssetManager(), type, status, mapWidth, mapHeight);
		} else if (type.className.equals("Wall")) {
			return WallControl.create(app.getAssetManager(), type, status, mapWidth, mapHeight);
		} else if (type.className.equals("ConnectedWall")) {
			return WallControl.create(app.getAssetManager(), type, status, mapWidth, mapHeight); // TODO connected wall
		} else if (type.className.equals("Portal")) {
			return PortalControl.create(app.getAssetManager(), type, status, mapWidth, mapHeight, playerControl.getSpatial());
		} else if (type.className.equals("GuildHallPortal")) {
			return PortalControl.create(app.getAssetManager(), type, status, mapWidth, mapHeight, playerControl.getSpatial());
		} else {
			return ObjectControl.create(app.getAssetManager(), type, status, mapWidth, mapHeight);
		}
	}
	
	public byte getTile(int x, int y) {
		return map[x + (y*mapWidth)];
	}
	
	public void setTile(int x, int y, byte value) {
		//map[x + (y*mapWidth)] = value;
	}
	
	public Spatial getTileSpatial(int x, int y) {
		return tileSpatials[x + (y*mapWidth)];
	}
	
	public void setTileSpatial(int x, int y, Spatial s) {
		tileSpatials[x + (y*mapHeight)] = s;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Node getWorldNode() {
		return worldNode;
	}

	public Node getTileNode() {
		return tileNode;
	}
}
