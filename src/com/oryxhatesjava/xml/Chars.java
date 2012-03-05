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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

public class Chars {

	public int nextCharId;
	public int maxNumChars;
	public Account account;
	public List<Char> chars;
	public List<NewsItem> news;
	public List<Server> servers;
	
	public Chars() {
		
	}
	
	public Chars(Element e) {
		parseElement(e);
	}
	
	@SuppressWarnings("unchecked")
	public void parseElement(Element e) {
		if (!e.getName().equals("Chars")) {
			throw new IllegalArgumentException();
		}
		
		nextCharId = Integer.parseInt(e.getAttributeValue("nextCharId"));
		maxNumChars = Integer.parseInt(e.getAttributeValue("maxNumChars"));
		
		chars = new LinkedList<Char>();
		Iterator<Element> itr = e.getChildren("Char").iterator();
		while (itr.hasNext()) {
			Element ce = itr.next();
			Char c = new Char(ce);
			chars.add(c);
		}
		
		account = new Account(e.getChild("Account"));
		
		news = new LinkedList<NewsItem>();
		itr = e.getChild("News").getChildren("Item").iterator();
		while (itr.hasNext()) {
			Element ie = itr.next();
			NewsItem i = new NewsItem(ie);
			news.add(i);
		}
		
		servers = new LinkedList<Server>();
		itr = e.getChild("Servers").getChildren("Server").iterator();
		while (itr.hasNext()) {
			Element se = itr.next();
			Server s = new Server(se);
			servers.add(s);
		}
		
	}
	
	@Override
	public String toString() {
		return "Chars max " + maxNumChars + ": " + chars;
	}
}
