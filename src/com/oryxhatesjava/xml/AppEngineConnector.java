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
import java.net.URLEncoder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

public class AppEngineConnector implements Runnable {

	private String urlbase;
	private BlockingQueue<Request> reqs;
	
	enum RequestType {
		PlainText,
		XML,
		Account
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
	
	public AppEngineConnector() {
		this("http://realmofthemadgod.appspot.com");
	}
	
	public AppEngineConnector(String urlBase) {
		urlbase = urlBase;
		reqs = new LinkedBlockingQueue<Request>();
	}
	
	public synchronized void getXMLRequest(String service, String request, String postdata, RequestListener callback) {
		try {
			URL url = new URL(urlbase + service + request + "?" + postdata);
			reqs.add(new Request(url, service+request, RequestType.XML, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void getTextRequest(String service, String request, String postdata, RequestListener callback) {
		try {
			URL url = new URL(urlbase + service + request + "?" + postdata);
			reqs.add(new Request(url, service+request, RequestType.PlainText, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void getAccount(String guid, String password, RequestListener callback) {
		try {
			URL url = new URL(urlbase + "/account/verify?" + "guid=" + guid + "&password=" + password);
			reqs.add(new Request(url, "/account/verify", RequestType.Account, callback));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		Request r;
		try {
			while ((r = reqs.take()) != null) {
				try {
					Document d;
					switch (r.type) {
					case Account:
						d = new SAXBuilder().build(r.url);
						r.listener.accountReceived(r.fullRequest, new Account(d));
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
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
