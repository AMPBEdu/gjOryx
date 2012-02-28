package com.oryxhatesjava.xml;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;


public class Database {

	private ArrayList<ObjectType> types;
	
	public Database() {
		types = new ArrayList<ObjectType>(5000);
	}
	
	@SuppressWarnings("unchecked")
	public void loadObjectTypeXMLs(File folder) {
		if (!folder.isDirectory()) {
			return;
		}
		
		File[] files = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".xml")) {
					return true;
				}
				return false;
			}
		});
		
		for (File f : files) {
			Document d;
			try {
				d = new SAXBuilder().build(f);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			Element root = d.getRootElement();
			Element objects = root.getChild("Objects");
			
			if (objects == null) {
				System.out.println("xml " + f.getName() + " is not an object list");
				continue;
			}
			
			Iterator<Element> itr = objects.getChildren("Object").iterator();
			while (itr.hasNext()) {
				ObjectType o = new ObjectType(itr.next());
				types.set(o.type, o);
			}
		}
	}
	
	public ObjectType getObjectType(int type) {
		return types.get(type);
	}
	
	public ObjectType getObjectType(String id) {
		for (ObjectType t : types) {
			if (t.id.equals(id)) {
				return t;
			}
		}
		return null;
	}
}
