/* oryx-hates-java
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

package com.oryxhatesjava;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.clarenceho.crypto.RC4;

import com.oryxhatesjava.net.ByteArrayDataOutput;
import com.oryxhatesjava.net.CreateSuccessPacket;
import com.oryxhatesjava.net.GotoAckPacket;
import com.oryxhatesjava.net.GotoPacket;
import com.oryxhatesjava.net.HelloPacket;
import com.oryxhatesjava.net.NewTickPacket;
import com.oryxhatesjava.net.Packet;
import com.oryxhatesjava.net.PingPacket;
import com.oryxhatesjava.net.PongPacket;
import com.oryxhatesjava.net.UpdateAckPacket;
import com.oryxhatesjava.net.UpdatePacket;
import com.oryxhatesjava.net.data.ObjectStatus;
import com.oryxhatesjava.net.data.ObjectStatusData;
import com.oryxhatesjava.net.data.Tile;
import com.oryxhatesjava.proxy.Proxy;

/**
 * A Realm of the Mad God client. Provides facilities for hooking packet
 * reception, network events and data updates from the game server.
 * <p>
 * Create an instance of this and use the <code>connect()</code> method to
 * connect to a game server.
 * 
 * @author Furyhunter
 */
public class Client {
    
	private int port = 2050;
	private InetAddress address;
	private Socket socket;
	private RC4 cipherOut;
	private RC4 cipherIn;
	private DataOutputStream write;
	
	private DataInputStream read;
	
	private long startTime;
	
	private Thread clientThread;
	private Thread eventThread;
	
	private boolean running = true;
	private boolean connected = false;
	private BlockingQueue<Runnable> eventQueue;
	
	private List<PacketListener> packetListeners;
	private List<ClientListener> clientListeners;
	private List<DataListener> dataListeners;
	
	private List<ObjectStatus> gameObjects;
	
	private boolean automaticallyHandling = true;
	private int tickLengthMs;
	
	private ObjectStatus playerObject;
	private int playerObjectId;
	
	/**
	 * Create a Client.
	 */
	public Client() {
		packetListeners = new LinkedList<PacketListener>();
		clientListeners = new LinkedList<ClientListener>();
		dataListeners = new LinkedList<DataListener>();
	}
	
	private void runThread() {
        // Connect to the server
		cipherOut = new RC4(Proxy.CLIENTKEY);
		cipherIn = new RC4(Proxy.SERVERKEY);
    	try {
			socket = new Socket(address, port);
    		startTime = System.currentTimeMillis();
			OutputStream ist = socket.getOutputStream();
			InputStream ost = socket.getInputStream();
			write = new DataOutputStream(ist);
			read = new DataInputStream(ost);
			
		} catch (IOException e) {
			e.printStackTrace();
			for (ClientListener l : clientListeners) {
    			l.disconnected(this);
    		}
			return;
		}
    	
    	connected = true;
    	
    	gameObjects = new LinkedList<ObjectStatus>();
    	
    	for (ClientListener l : clientListeners) {
    		l.connected(this);
    	}
    	
    	//TODO: Tries to read a null packet???
    	/*while (running) {
    		Packet pkt = null;
    		try {
    			int length = read.readInt(); //actually the socket closes for no reason
    			int type = read.readByte();
    			byte[] buf = new byte[length-5];
    			read.readFully(buf);
    			pkt = Packet.parse(type, cipherIn.rc4(buf));
    			if (isAutomaticallyHandling()) {
    				automaticHandling(pkt);
    			}
    		} catch (EOFException e) {
    			e.printStackTrace();
    			break;
    		} catch (IOException e) {
    			break;
    		}
    		
    		for (PacketListener l : packetListeners) {
    			if (l.filter(pkt)) {
    				l.packetReceived(this, pkt);
    			}
    			if (pkt.type == Packet.FAILURE) {
    				running = false;
    			}
    		}
    	}*/
    	
    	try {
    		write.close();
    		read.close();
    		socket.close();
    		
    		for (ClientListener l : clientListeners) {
    			l.disconnected(this);
    		}
    	} catch (Exception e) {
    		
    	}
    	connected = false;
    }
    
    /**
     * Add a packet listener.
     * @param l the listener to add
     */
    public synchronized void addPacketListener(PacketListener l) {
    	if (l == null) {
    		throw new NullPointerException();
    	}
    	
    	packetListeners.add(l);
    }
    
    /**
     * Remove a packet listener.
     * @param l listener to remove
     */
    public synchronized void removePacketListener(PacketListener l) {
    	packetListeners.remove(l);
    }
    
    /**
     * Add a client listener.
     * @param l
     */
    public synchronized void addClientListener(ClientListener l) {
    	if (l == null) {
    		throw new NullPointerException();
    	}
    	
    	clientListeners.add(l);
    }
    
    /**
     * Remove a client listener.
     * @param l
     */
    public synchronized void removeClientListener(ClientListener l) {
    	clientListeners.remove(l);
    }
    
    /**
     * Add a data listener.
     * @param l
     */
    public synchronized void addDataListener(DataListener l) {
    	if (l == null) {
    		throw new NullPointerException();
    	}
    	
    	dataListeners.add(l);
    }
    
    /**
     * Remove a data listener.
     * @param l
     */
    public synchronized void removeDataListener(DataListener l) {
    	dataListeners.remove(l);
    }
    
    /**
     * <em>Deprecated. Replaced by <code>syncSendPacket()</code>. To be removed
     * by version 0.2 release.
     * <p>
     * Send a packet.
     * @param pkt the packet to send
     * @throws IOException couldn't write packet
     */
    @Deprecated
    public void sendPacket(Packet pkt) throws IOException {
    	sendSyncPacket(pkt);
    }
    
    /**
     * Send a packet synchronously. The current thread will not continue until
     * the packet has been sent.
     * @param pkt the packet to send
     * @throws IOException couldn't write packet
     */
    public void sendSyncPacket(Packet pkt) throws IOException {
    	if (!connected) {
    		throw new IllegalStateException("Not connected to a server.");
    	}
    	
    	ByteArrayDataOutput b = new ByteArrayDataOutput(20000);
    	pkt.writeToDataOutput(b);
    	byte[] buf = b.getArray();
    	write.writeInt(buf.length+5);
    	write.writeByte((byte)pkt.type);
    	write.write(cipherOut.rc4(buf));
    }
    
    /**
     * Sends a packet asynchronously, allowing execution on the current thread
     * to continue.
     * @param pkt the packet to send
     */
    public void sendAsyncPacket(final Packet pkt) {
    	if (!connected) {
    		throw new IllegalStateException("Not connected to a server.");
    	}
    	
    	eventQueue.add(new Runnable() {
			public void run() {
				try {
					sendSyncPacket(pkt);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
    }
    
    /**
     * Get the current client time.
     * @return
     */
    public int getTime() {
    	return (int) (System.currentTimeMillis() - startTime);
    }
    
    private void automaticHandling(Packet pkt) throws IOException {
		List<DataListener> copyList = new LinkedList<DataListener>(dataListeners);
		
		// Catch ping/pong because this should be same across any client
		if (pkt instanceof PingPacket) {
			PingPacket pp = (PingPacket)pkt;
			PongPacket pop = new PongPacket();
			pop.serial = pp.serial;
			pop.time = (int) (System.currentTimeMillis() - startTime);
			sendSyncPacket(pop);
		}
		
		// Catch UPDATE since you ALWAYS need to respond with an ack
		if (pkt instanceof UpdatePacket) {
			UpdatePacket up = (UpdatePacket)pkt;
			
			// Add tiles
			if (up.tiles != null) {
				for (Tile i : up.tiles) {
					for (DataListener dl : copyList) {
						dl.tileAdded(i);
					}
				}
			}
			
			// Remove objects dropped
			if (up.drops != null) {
				for (int i : up.drops) {
					ObjectStatus del = null;
					for (ObjectStatus lo : gameObjects) {
						if (lo.data.objectId == i) {
							del = lo;
						}
					}
					if (del != null) {
						gameObjects.remove(del);
						for (DataListener dl : copyList) {
							if (dataListeners.contains(dl)) {
								DataListener ret = dl.objectRemoved(this, del, i);
								if (ret != null) {
									dataListeners.remove(ret);
								}
							}
						}
					}
				}
			}
			
			// Add and update new objects
			if (up.newobjs != null) {
				for (ObjectStatus o : up.newobjs) {
					gameObjects.add(o);
					
					if (o.data.objectId == playerObjectId) {
						playerObject = o;
					}
					
					for (DataListener dl : copyList) {
						if (dataListeners.contains(dl)) {
							DataListener ret = dl.objectAdded(this, o);
							if (ret != null) {
								dataListeners.add(ret);
							}
						}
					}
						
				}
			}
			
			
			UpdateAckPacket ump = new UpdateAckPacket();
			sendSyncPacket(ump);
		}
		
		if (pkt instanceof CreateSuccessPacket) {
			CreateSuccessPacket csp = (CreateSuccessPacket)pkt;
			playerObjectId = csp.objectId;
		}
		
		if (pkt instanceof GotoPacket) {
			GotoPacket gtp = (GotoPacket)pkt;
			ObjectStatus o = null;
			for (ObjectStatus ob : gameObjects) {
				if (ob.data.objectId == gtp.objectId) {
					o = ob;
					o.data.pos = gtp.pos;
					break;
				}
			}
			
			for (DataListener dl : copyList) {
				dl.objectUpdated(this, o);
			}
			
			GotoAckPacket gtap = new GotoAckPacket();
			gtap.time = getTime();
			sendSyncPacket(gtap);
		}
		
		if (pkt instanceof NewTickPacket) {
			NewTickPacket ntp = (NewTickPacket)pkt;
			tickLengthMs = ntp.tickTime;
			
			for (ObjectStatusData o : ntp.statuses) {
				for (ObjectStatus lo : gameObjects) {
					if (o.objectId == lo.data.objectId && o.objectId != playerObjectId) {
						lo.data.pos = o.pos;
						lo.update(o);
						
						for (DataListener dl : copyList) {
							if (dataListeners.contains(dl)) {
								dl.objectUpdated(this, lo);
							}
						}
						break;
					}
				}
			}
		}
    }

    /**
     * Whether or not the client automatically handles basic procedures, such
     * as ping/pong and object updating.
     * @return
     */
	public boolean isAutomaticallyHandling() {
		return automaticallyHandling;
	}

	/**
	 * Set whether the client automatically handles basic procedures, such as
	 * ping/pong and object updating.
	 * @param automaticallyHandling
	 */
	public void setAutomaticallyHandling(boolean automaticallyHandling) {
		this.automaticallyHandling = automaticallyHandling;
	}

	/**
	 * Get the player object.
	 * @return
	 */
	public ObjectStatus getPlayerObject() {
		return playerObject;
	}
	
	/**
	 * Starts the client thread and connects to the given server address. This
	 * method is asynchronous; use a ClientListener to check for connection
	 * events.
	 * @param address the address to connect to
	 * @param port the port to connect on
	 */
	public void connect(InetAddress address, int port) {
		this.port = port;
		if (connected) {
			throw new IllegalStateException("Currently connected to a server.");
		}
		
		this.address = address;
		running = true;
		eventQueue = new LinkedBlockingQueue<Runnable>();
		clientThread = new Thread(new Runnable() {
			@Override
			public void run() {
				runThread();
			}
		}, "Client Thread");
		clientThread.setDaemon(true);
		clientThread.start();
		
		eventThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					Runnable run = null;
					try {
						run = eventQueue.take();
						run.run();
					} catch (InterruptedException ie) {
						break;
					}
				}
			}
		}, "Client Event Thread");
		eventThread.setDaemon(true);
		eventThread.start();
	}
	
	/**
	 * Disconnect the client.
	 */
	public void disconnect() {
		if (!connected) {
			throw new IllegalStateException("Not connected to a server.");
		}
		running = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		eventThread.interrupt();
	}
	
	/**
	 * Whether or not the client is connected.
	 * @return
	 */
	public boolean isConnected() {
		return connected;
	}
	
	/**
	 * Retrieve the length of time in milliseconds for the last received tick.
	 * @return
	 */
	public int getTickLengthMs() {
		return tickLengthMs;
	}
}
