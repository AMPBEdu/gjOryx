/*
 * Copyright (C) 2011 Furyhunter <furyhunter600@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the creator nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.oryxhatesjava.xml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

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
		this("http://realmofthemadgod.appspot.com");
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
			URL url = new URL(urlbase + "/account/verify?" + "guid=" + guid + "&password=" + password);
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
			URL url = new URL(urlbase + "/char/list?guid=" + guid + "&password=" + password);
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
			URL url = new URL(urlbase + "/guild/listMembers?num=" + num + "&offset=" + offset + "&guid=" + guid + "&password=" + password);
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
				try {
					Document d;
					switch (r.type) {
					case Account:
						d = new SAXBuilder().build(r.url);
						r.listener.accountReceived(r.fullRequest, new Account(d.getRootElement()));
						break;
					case PlainText:
						BufferedReader br = new BufferedReader(new InputStreamReader(r.url.openStream()));
						String line;
						StringBuilder full = new StringBuilder();
						while ((line = br.readLine()) != null) {
							full.append(line+"\n");
						}
						r.listener.textReceived(r.fullRequest, full.toString());
						break;
					case XML:
						d = new SAXBuilder().build(r.url);
						r.listener.xmlReceived(r.fullRequest, d);
						break;
					case CharList:
						d = new SAXBuilder().build(r.url);
						r.listener.charsReceived(r.fullRequest, new Chars(d.getRootElement()));
						break;
					case GuildMemberList:
						d = new SAXBuilder().build(r.url);
						r.listener.guildMemberListReceived(r.fullRequest, new GuildMembers(d.getRootElement()));
						break;
					case FameList:
						d = new SAXBuilder().build(r.url);
						r.listener.fameListReceived(r.fullRequest, new FameList(d.getRootElement()));
						break;
					case CharFame:
						d = new SAXBuilder().build(r.url);
						r.listener.charFameReceived(r.fullRequest, new CharFame(d.getRootElement()));
						break;
					case SuccessFail:
					{
						d = new SAXBuilder().build(r.url);
						boolean success = (d.getRootElement().getChild("Success") != null ? true : false);
						r.listener.booleanReceived(r.fullRequest, success);
						break;
					}
					}
				} catch (Exception e) {
					e.printStackTrace();
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
