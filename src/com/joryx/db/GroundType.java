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

import org.jdom.Element;

import com.oryxhatesjava.util.HexValueParser;

public class GroundType {
	
	public int type; // number sent by server
	public String id; // string name, probably unused except for editors
	
	public int textureType;
	
	public static final int TEXTURE = 0;
	public static final int RANDOM = 1;
	
	public TextureDefinition texture;
	public RandomTextureDefinition randomTexture;
	
	public boolean animated; // use animation function
	public float animateDx; // delta x
	public float animateDy; // delta y
	public String animationFunction; // function
	
	public boolean topAnimated; // top texture is animated?
	public float topAnimateDx;
	public float topAnimateDy;
	public String topAnimationFunction;
	
	public boolean noWalk; // collision here
	public boolean sink; // sinks objects by a few pixels when inside
	public boolean sinking; // ???
	public boolean push;
	
	public float speedModifier = 1;
	
	public boolean compositePriority;
	public int compositePriorityValue;
	
	public boolean blendPriority;
	public int blendPriorityValue;
	
	public float xOffset;
	public float yOffset;
	
	public GroundType(Element e) {
		parseElement(e);
	}
	
	public void parseElement(Element e) {
		type = HexValueParser.parseInt(e.getAttributeValue("type"));
		id = e.getAttributeValue("id");
		Element temp;
		
		temp = e.getChild("Texture");
		
		if (temp != null) {
			textureType = TEXTURE;
			texture = new TextureDefinition(temp);
		}
		
		temp = e.getChild("RandomTexture");
		
		if (temp != null) {
			textureType = RANDOM;
			randomTexture = new RandomTextureDefinition(temp);
		}
		
		temp = e.getChild("Animate");
		
		if (temp != null) {
			animated = true;
			if (temp.getAttributeValue("dx") != null) {
				animateDx = Float.parseFloat(temp.getAttributeValue("dx"));
			}
			if (temp.getAttributeValue("dy") != null) {
				animateDy = Float.parseFloat(temp.getAttributeValue("dy"));
			}
			animationFunction = e.getTextTrim();
		}
		
		temp = e.getChild("TopAnimate");
		
		if (temp != null) {
			topAnimated = true;
			topAnimateDx = Float.parseFloat(temp.getAttributeValue("dx"));
			topAnimateDy = Float.parseFloat(temp.getAttributeValue("dy"));
			topAnimationFunction = e.getTextTrim();
		}
		
		temp = e.getChild("CompositePriority");
		if (temp != null) {
			compositePriority = true;
			compositePriorityValue = HexValueParser.parseInt(temp.getTextTrim());
		}
		
		temp = e.getChild("BlendPriority");
		if (temp != null) {
			blendPriority = true;
			blendPriorityValue = HexValueParser.parseInt(temp.getTextTrim());
		}
		
		temp = e.getChild("Speed");
		if (temp != null) {
			speedModifier = Float.parseFloat(temp.getTextTrim());
		}
		
		temp = e.getChild("XOffset");
		if (temp != null) {
			xOffset = Float.parseFloat(temp.getTextTrim());
		}
		
		temp = e.getChild("YOffset");
		if (temp != null) {
			yOffset = Float.parseFloat(temp.getTextTrim());
		}
		
		noWalk = (e.getChild("NoWalk") != null ? true : false);
		push = (e.getChild("Push") != null ? true : false);
		sink = (e.getChild("Sink") != null ? true : false);
		sinking = (e.getChild("Sinking") != null ? true : false);
	}
}
