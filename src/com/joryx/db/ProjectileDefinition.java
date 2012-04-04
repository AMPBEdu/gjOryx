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

import org.jdom.Attribute;
import org.jdom.Element;

import com.oryxhatesjava.util.HexValueParser;

public class ProjectileDefinition {

	public int id = 0;
	public String containerId;
	public int speed;
	public boolean varyingDamage;
	public int minDamage;
	public int damage;
	public int maxDamage;
	public float scale = 1;
	public float lifetimeSeconds;
	public float frequency = 0;
	public float amplitude = 0;
	public boolean multiHit;
	public boolean passesCover;
	public boolean particleTrail;
	
	public ProjectileDefinition(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		containerId = e.getChildTextTrim("ObjectId");
		speed = HexValueParser.parseInt(e.getChildTextTrim("Speed"));
		Element t = e.getChild("Damage");
		if (t != null) {
			damage = HexValueParser.parseInt(e.getChildTextTrim("Damage"));
		} else {
			minDamage = HexValueParser.parseInt(e.getChildTextTrim("MinDamage"));
			maxDamage = HexValueParser.parseInt(e.getChildTextTrim("MaxDamage"));
		}
		lifetimeSeconds = (float)HexValueParser.parseInt(e.getChildTextTrim("LifetimeMS")) / 1000f;
		
		multiHit = (e.getChild("MultiHit") != null ? true : false);
		passesCover = (e.getChild("PassesCover") != null ? true : false);
		particleTrail = (e.getChild("ParticleTrail") != null ? true : false);
		
		t = e.getChild("Size");
		if (t != null) {
			scale = (float)HexValueParser.parseInt(t.getTextTrim()) / 120f;
		}
		
		t = e.getChild("Frequency");
		if (t != null) {
			frequency = Float.parseFloat(t.getTextTrim());
		}
		t = e.getChild("Amplitude");
		if (t != null) {
			amplitude = Float.parseFloat(t.getTextTrim());
		}
		
		Attribute a = e.getAttribute("id");
		if (a != null) {
			id = HexValueParser.parseInt(a.getValue());
		}
	}
}
