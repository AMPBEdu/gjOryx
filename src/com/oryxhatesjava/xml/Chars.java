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
			return;
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
}
