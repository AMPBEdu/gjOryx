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

package com.oryxhatesjava.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;

/**
 * A utility for retrieving information from the Realm of the Mad God Google
 * AppEngine service. Use this to get information about accounts, characters,
 * servers, et cetera.
 * @author furyhunter
 *
 */
public class AppEngineClient {

	private String urlbase;
	private BlockingQueue<Request> reqs;
	private Thread thread;
	
	enum RequestType {
		PlainText,
		XML,
		Account,
		CharList,
		GuildMemberList,
		FameList,
		CharFame,
		SuccessFail
	}
	class Request {
		public RequestType type = RequestType.XML;
		public URL url;
		public RequestListener listener;
		public String fullRequest;
		
		public Request(URL url, String fullRequest, RequestType type, RequestListener listener) {
			this.url = url;
			this.type = type;
			this.listener = listener;
			this.fullRequest = fullRequest;
		}
	}
	
	/**
	 * Create an AppEngineClient that retrieves from the official production
	 * servers.
	 */
	public AppEngineClient() {
		this("http://realmofthemadgod.appspot.com");//80.241.222.17:8888
	}
	
	/**
	 * Create an AppEngineClient that retrieves from the given server. The
	 * string is used as a base for the full request URL, so include http.
	 * @param urlBase
	 */
	public AppEngineClient(String urlBase) {
		urlbase = urlBase;
		reqs = new LinkedBlockingQueue<Request>();
		thread = new Thread(new Runnable() {
			public void run() {
				run();
			}
		});
	}
	
	/**
	 * Asynchronously retrieve the contents of a request in XML format.
	 * <p>
	 * Does nothing if the service thread is not started.
	 * @param service the service to call
	 * @param request the request to make
	 * @param postdata the URL string of parameters
	 * @param callback the listener to inform of reception
	 */
	public void getXMLRequestAsync(String service, String request, String postdata, RequestListener callback) {
		if (!thread.isAlive()) {
			return;
		}
		try {
			URL url = new URL(urlbase + service + request + "?" + postdata);
			reqs.add(new Request(url, service+request, RequestType.XML, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Asynchronously retrieve the contents of a request in plain text.
	 * <p>
	 * Does nothing if the service thread is not started.
	 * @param service the service to call
	 * @param request the request to make
	 * @param postdata the URL string of parameters, if any
	 * @param callback the listener to inform of reception
	 */
	public void getTextRequestAsync(String service, String request, String postdata, RequestListener callback) {
		if (!thread.isAlive()) {
			return;
		}
		try {
			URL url = new URL(urlbase + service + request + "?" + postdata);
			reqs.add(new Request(url, service+request, RequestType.PlainText, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Asynchronously retrieve information on a Web Account.
	 * <p>
	 * Does nothing if the service thread is not started.
	 * @param guid the guid of the account
	 * @param password the password of the account
	 * @param callback the listener to inform of reception
	 */
	public void getAccountAsync(String guid, String password, RequestListener callback) {
		if (!thread.isAlive()) {
			return;
		}
		try {
			URL url = new URL(urlbase + "/account/verify?" + "guid=" + URLEncoder.encode(guid, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));
			reqs.add(new Request(url, "/account/verify", RequestType.Account, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Asynchronously retrieve login information, or Chars. This includes a
	 * list of servers to connect to and the list of characters on the account,
	 * with their classes unlocked, their vault, and their high scores.
	 * <p>
	 * Does nothing if the service thread is not started.
	 * @param guid the guid of the account
	 * @param password the password of the account
	 * @param callback the listener to inform of reception
	 */
	public void getCharsAsync(String guid, String password, RequestListener callback) {
		if (!thread.isAlive()) {
			return;
		}
		try {
			URL url = new URL(urlbase + "/char/list?guid=" + URLEncoder.encode(guid, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));
			reqs.add(new Request(url, "/char/list", RequestType.CharList, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Asynchronously retrieve a list of guild members.
	 * <p>
	 * Does nothing if the service thread is not started.
	 * @param num the number of guild members to list
	 * @param offset the index of the first guild member to list
	 * @param guid the guid of the account whose guild to parse
	 * @param password the password of the account whose guild to parse
	 * @param callback the listener to inform of reception
	 */
	public void getGuildMemberListAsync(int num, int offset, String guid, String password, RequestListener callback) {
		if (!thread.isAlive()) {
			return;
		}
		try {
			URL url = new URL(urlbase + "/guild/listMembers?num=" + num + "&offset=" + offset + "&guid=" + URLEncoder.encode(guid, "UTF-8") + "&password=" + URLEncoder.encode(guid, "UTF-8"));
			reqs.add(new Request(url, "/guild/list", RequestType.GuildMemberList, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Asynchronously retrieve the Legends fame list, with an entry for the
	 * given (dead) character.
	 * <p>
	 * Does nothing if the service thread is not started.
	 * @param timespan "week", "month" or "all"
	 * @param accountId id of the character's account
	 * @param charId id of the character on the account
	 * @param callback the listener to inform of reception
	 */
	public void getFameListAsync(String timespan, int accountId, int charId, RequestListener callback) {
		if (!thread.isAlive()) {
			return;
		}
		try {
			URL url = new URL(urlbase + "/fame/list?timespan=" + timespan + "&accountId=" + accountId + "&charId=" + charId);
			reqs.add(new Request(url, "/fame/list", RequestType.FameList, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Asynchronously retrieve a character's fame information.
	 * <p>
	 * Does nothing if the service thread is not started.
	 * @param accountId
	 * @param charId
	 * @param callback
	 */
	public void getCharFameAsync(int accountId, int charId, RequestListener callback) {
		if (!thread.isAlive()) {
			return;
		}
		try {
			URL url = new URL(urlbase + "/char/fame?accountId=" + accountId + "&charId=" + charId);
			reqs.add(new Request(url, "/char/fame", RequestType.CharFame, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Asynchronously retrieve a boolean result. Use this method for requests
	 * that either return Success or Fail.
	 * <p>
	 * Does nothing if the service thread is not started.
	 * @param service
	 * @param request
	 * @param postdata
	 * @param callback
	 */
	public void getBooleanAsync(String service, String request, String postdata, RequestListener callback) {
		if (!thread.isAlive()) {
			return;
		}
		try {
			URL url = new URL(urlbase + service + request + "?" + postdata);
			reqs.add(new Request(url, service+request, RequestType.SuccessFail, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void runThread() {
		Request r;
		try {
			while ((r = reqs.take()) != null) {
				Document d;
				switch (r.type) {
				case Account:
					try {
						d = new SAXBuilder().build(r.url);
						r.listener.accountReceived(r.fullRequest, new Account(d.getRootElement()));
					} catch (IOException e) {
						r.listener.requestFailed(r.fullRequest, "Couldn't connect to account service.");
					} catch (IllegalArgumentException e) {
						r.listener.requestFailed(r.fullRequest, "Invalid credentials.");
					} catch (JDOMException e) {
						r.listener.requestFailed(r.fullRequest, "Bad XML");
					}
					break;
				case PlainText:
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(r.url.openStream()));
						String line;
						StringBuilder full = new StringBuilder();
						while ((line = br.readLine()) != null) {
							full.append(line+"\n");
						}
						r.listener.textReceived(r.fullRequest, full.toString());
					} catch (IOException e) {
						r.listener.requestFailed(r.fullRequest, "Couldn't connect to account service.");
					}
					break;
				case XML:
					try {
						d = new SAXBuilder().build(r.url);
						r.listener.xmlReceived(r.fullRequest, d);
					} catch (IOException e) {
						r.listener.requestFailed(r.fullRequest, "Couldn't connect to account service.");
					} catch (Exception e) {
						r.listener.requestFailed(r.fullRequest, "Bad XML");
					}
					break;
				case CharList:
					try {
						d = new SAXBuilder().build(r.url);
						r.listener.charsReceived(r.fullRequest, new Chars(d.getRootElement()));
					} catch (IOException e) {
						r.listener.requestFailed(r.fullRequest, "Couldn't connect to account service.");
					} catch (IllegalArgumentException e) {
						r.listener.requestFailed(r.fullRequest, "Invalid credentials.");
					} catch (JDOMException e) {
						r.listener.requestFailed(r.fullRequest, "Bad XML");
					}
					break;
				case GuildMemberList:
					try {
						d = new SAXBuilder().build(r.url);
						r.listener.guildMemberListReceived(r.fullRequest, new GuildMembers(d.getRootElement()));
					} catch (IOException e) {
						r.listener.requestFailed(r.fullRequest, "Couldn't connect to account service.");
					} catch (IllegalArgumentException e) {
						r.listener.requestFailed(r.fullRequest, "Invalid credentials.");
					} catch (JDOMException e) {
						r.listener.requestFailed(r.fullRequest, "Bad XML");
					}
					break;
				case FameList:
					try {
						d = new SAXBuilder().build(r.url);
						r.listener.fameListReceived(r.fullRequest, new FameList(d.getRootElement()));
					} catch (IOException e) {
						r.listener.requestFailed(r.fullRequest, "Couldn't connect to account service.");
					} catch (IllegalArgumentException e) {
						r.listener.requestFailed(r.fullRequest, "Invalid credentials.");
					} catch (JDOMException e) {
						r.listener.requestFailed(r.fullRequest, "Bad XML");
					}
					break;
				case CharFame:
					try {
						d = new SAXBuilder().build(r.url);
						r.listener.charFameReceived(r.fullRequest, new CharFame(d.getRootElement()));
					} catch (IOException e) {
						r.listener.requestFailed(r.fullRequest, "Couldn't connect to account service.");
					} catch (IllegalArgumentException e) {
						r.listener.requestFailed(r.fullRequest, "Invalid credentials.");
					} catch (JDOMException e) {
						r.listener.requestFailed(r.fullRequest, "Bad XML");
					}
					break;
				case SuccessFail:
					try {
						d = new SAXBuilder().build(r.url);
						boolean success = (d.getRootElement().getChild("Success") != null ? true : false);
						r.listener.booleanReceived(r.fullRequest, success);
						break;
					} catch (IOException e) {
						r.listener.requestFailed(r.fullRequest, "Couldn't connect to account service.");
					} catch (JDOMException e) {
						r.listener.requestFailed(r.fullRequest, "Bad XML");
					}
					break;
				}
			
			}
		} catch (InterruptedException e) {
			return;
		}
	}
	
	/**
	 * Start the service thread for asynchronous requests, or do nothing if
	 * already started.
	 */
	public void start() {
		if (thread.isAlive()) {
			return;
		}
		thread = new Thread(new Runnable() {
			public void run() {
				runThread();
			}
		}, "AppEngine Service Thread");
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * Stop the service thread for asynchronous requests, or do nothing if
	 * it is not started.
	 */
	public void stop() {
		if (thread.isAlive()) {
			thread.interrupt();
		}
	}
}
