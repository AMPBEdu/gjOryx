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

package com.joryx.db;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.jdom.Element;

import com.jme3.math.FastMath;
import com.oryxhatesjava.util.HexValueParser;

public class ObjectType {

	public int type;
	public String id;
	
	public String className;
	public String description;
	public String hitSound;
	public String deathSound;
	public String sound;
	
	public int textureType;
	public static final int NONE = 0;
	public static final int TEXTURE = 1;
	public static final int ANIMATED = 2;
	public static final int PLAYER = 3;
	public static final int RANDOM = 4;
	public TextureDefinition texture;
	public AnimatedTextureDefinition animatedTexture;
	public PlayerTextureDefinition playerTexture;
	public RandomTextureDefinition randomTexture;
	public boolean hasTopTexture;
	public int topTextureType;
	public TextureDefinition topTexture;
	public AnimatedTextureDefinition topAnimatedTexture;
	public RandomTextureDefinition topRandomTexture;
	
	
	public int maxHP;
	public int capHP;
	public int maxMP;
	public int capMP;
	public int att;
	public int capAtt;
	public int def;
	public int capDef;
	public int spd;
	public int capSpd;
	public int dex;
	public int capDex;
	public int vit;
	public int capVit;
	public int wis;
	public int capWis;
	
	public int[] slotTypes;
	public int[] equipment;
	public List<LevelIncreaseDefinition> levelIncreases;
	public List<UnlockLevelDefinition> unlockLevels;
	
	public boolean player;
	public boolean staticType;
	public boolean occupySquare;
	public boolean drawOnGround;
	public boolean enemy;
	
	public List<ProjectileDefinition> projectiles;
	public float projectileRotation;
	public int projectileAngleCorrection;
	public int numProjectiles = 1;
	public float projectileRateOfFire = 1;
	
	public float size = 1f;
	
	public ObjectType(Element e) {
		parseElement(e);
	}
	
	@SuppressWarnings("unchecked")
	public void parseElement(Element e) {
		type = HexValueParser.parseInt(e.getAttributeValue("type"));
		id = e.getAttributeValue("id");
		
		
		// TEXTURE PARSING
		Element t = e.getChild("Texture");
		
		if (t != null) {
			textureType = TEXTURE;
			texture = new TextureDefinition(t);
		}
		
		t = e.getChild("AnimatedTexture");
		player = (e.getChild("Player") != null ? true : false);
		
		if (t != null && !player) {
			textureType = ANIMATED;
			animatedTexture = new AnimatedTextureDefinition(t);
		}
		
		if (t != null && player) {
			textureType = PLAYER;
			playerTexture = new PlayerTextureDefinition(t);
		}
		
		t = e.getChild("RandomTexture");
		
		if (t != null) {
			textureType = RANDOM;
			randomTexture = new RandomTextureDefinition(t);
		}
		
		
		t = e.getChild("Class");
		if (t != null) {
			className = t.getTextTrim();
		}
		

		t = e.getChild("Top");
		if (t != null) {
			hasTopTexture = true;
			Element tt = t.getChild("Texture");
			if (tt != null) {
				topTextureType = TEXTURE;
				topTexture = new TextureDefinition(tt);
			}
			tt = t.getChild("AnimatedTexture");
			if (tt != null) {
				topTextureType = ANIMATED;
				topAnimatedTexture = new AnimatedTextureDefinition(tt);
			}
			tt = t.getChild("RandomTexture");
			if (tt != null) {
				topTextureType = RANDOM;
				topRandomTexture = new RandomTextureDefinition(tt);
			}
			
		}
		
		
		t = e.getChild("Description");
		if (t != null) {
			description = t.getTextTrim();
		}
		
		t = e.getChild("HitSound");
		if (t != null) {
			hitSound = t.getTextTrim();
		}
		
		t = e.getChild("DeathSound");
		if (t != null) {
			deathSound = t.getTextTrim();
		}
		
		t = e.getChild("Sound");
		if (t != null) {
			sound = t.getTextTrim();
		}
		
		if (player) {
			maxHP = HexValueParser.parseInt(e.getChildTextTrim("MaxHitPoints"));
			maxMP = HexValueParser.parseInt(e.getChildTextTrim("MaxMagicPoints"));
			att = HexValueParser.parseInt(e.getChildTextTrim("Attack"));
			def = HexValueParser.parseInt(e.getChildTextTrim("Defense"));
			spd = HexValueParser.parseInt(e.getChildTextTrim("Speed"));
			dex = HexValueParser.parseInt(e.getChildTextTrim("Dexterity"));
			vit = HexValueParser.parseInt(e.getChildTextTrim("HpRegen"));
			wis = HexValueParser.parseInt(e.getChildTextTrim("MpRegen"));
			
			capHP = HexValueParser.parseInt(e.getChild("MaxHitPoints").getAttributeValue("max"));
			capMP = HexValueParser.parseInt(e.getChild("MaxMagicPoints").getAttributeValue("max"));
			capAtt = HexValueParser.parseInt(e.getChild("Attack").getAttributeValue("max"));
			capDef = HexValueParser.parseInt(e.getChild("Defense").getAttributeValue("max"));
			capSpd = HexValueParser.parseInt(e.getChild("Speed").getAttributeValue("max"));
			capDex = HexValueParser.parseInt(e.getChild("Dexterity").getAttributeValue("max"));
			capVit = HexValueParser.parseInt(e.getChild("HpRegen").getAttributeValue("max"));
			capWis = HexValueParser.parseInt(e.getChild("MpRegen").getAttributeValue("max"));
			
			slotTypes = new int[12];
			equipment = new int[12];
			
			Scanner scan = new Scanner(e.getChildTextTrim("SlotTypes"));
			scan.useDelimiter(", ");
			for (int i = 0; i < 12; i++) {
				slotTypes[i] = HexValueParser.parseInt(scan.next());
			}
			
			scan = new Scanner(e.getChildTextTrim("Equipment"));
			scan.useDelimiter(", ");
			for (int i = 0; i < 12; i++) {
				equipment[i] = HexValueParser.parseInt(scan.next());
			}
			
			levelIncreases = new LinkedList<LevelIncreaseDefinition>();
			Iterator<Element> itr = e.getChildren("LevelIncrease").iterator();
			while (itr.hasNext()) {
				levelIncreases.add(new LevelIncreaseDefinition(itr.next()));
			}
			
			unlockLevels = new LinkedList<UnlockLevelDefinition>();
			itr = e.getChildren("UnlockLevel").iterator();
			while (itr.hasNext()) {
				unlockLevels.add(new UnlockLevelDefinition(itr.next()));
			}
			
		}

		
		// Flags
		staticType = (e.getChild("Static") != null ? true : false);
		occupySquare = (e.getChild("OccupySquare") != null ? true : false);
		
		projectiles = new LinkedList<ProjectileDefinition>();
		Iterator<Element> itr = e.getChildren("Projectile").iterator();
		while (itr.hasNext()) {
			projectiles.add(new ProjectileDefinition(itr.next()));
		}
		
		t = e.getChild("AngleCorrection");
		if (t != null) {
			projectileAngleCorrection = HexValueParser.parseInt(t.getTextTrim());
		}
		
		t = e.getChild("Rotation");
		if (t != null) {
			projectileRotation = Float.parseFloat(t.getTextTrim());
			if (projectileRotation > FastMath.PI || projectileRotation < -FastMath.PI) {
				projectileRotation *= FastMath.DEG_TO_RAD;
			}
		}
		
		t = e.getChild("NumProjectiles");
		if (t != null) {
			numProjectiles = HexValueParser.parseInt(t.getTextTrim());
		}
		
		t = e.getChild("RateOfFire");
		if (t != null) {
			projectileRateOfFire = Float.parseFloat(t.getTextTrim());
		}
		
		t = e.getChild("Size");
		if (t != null) {
			size = (float)HexValueParser.parseInt(t.getTextTrim()) / 100f;
		}
		
		enemy = (e.getChild("Enemy") == null ? false : true);
		
		drawOnGround = (e.getChild("DrawOnGround") == null ? false : true);
		
	}
}
