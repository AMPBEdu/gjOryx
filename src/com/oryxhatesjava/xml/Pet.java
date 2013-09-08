package com.oryxhatesjava.xml;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

public class Pet {

	public String name;
	public int type;
	public int instanceId;
	public int rarity;
	public int maxAbilityPower;
	public int skin;
	public PetAbility[] abilities;
	
	public Pet(Element e){
		parseElement(e);
	}
	
	public void parseElement(Element e){
		try{
		if(e.getName() != "Pet")
			return;
		
		this.name = e.getAttributeValue("name");
		this.type = Integer.parseInt(e.getAttributeValue("type"));
		this.instanceId = Integer.parseInt(e.getAttributeValue("instanceId"));
		this.rarity = Integer.parseInt(e.getAttributeValue("rarity"));
		this.maxAbilityPower = Integer.parseInt(e.getAttributeValue("maxAbilityPower"));
		this.skin = Integer.parseInt(e.getAttributeValue("skin"));
		this.abilities = new PetAbility[3];
		Element b = e.getChild("Abilities");
		Iterator itr = b.getChildren().iterator();
		if(itr.hasNext() == true){
		this.abilities[0] = new PetAbility((Element)itr.next());
		this.abilities[1] = new PetAbility((Element)itr.next());
		this.abilities[2] = new PetAbility((Element)itr.next());
		}
		}
		catch(Exception ee)
		{
		
		}
	}
	
}
