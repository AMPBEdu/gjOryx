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

package com.joryx.util;

import java.util.HashMap;
import java.util.Map;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.scene.plugins.blender.BlenderContext.LoadedFeatureDataType;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;

public class ImageData {
	private static Map<String, Info> images;
	
	static class Info {
		int cellWidthPixels;
		int cellHeightPixels;
		int widthCells;
		int heightCells;
		String path;

		int sourceWidthPixels;
		int sourceHeightPixels;
		int loadedWidthPixels;
		int loadedHeightPixels;
	}
	
	static {
		images = new HashMap<String, Info>();
		
		setValue("assItem", 16, 5, 8, 8, 128, 40, "Textures/assItem.png"); // 128x40
		setValue("lofiChar8x8", 16, 31, 8, 8, 128, 248, "Textures/lofiChar.png"); // 128x248
		setValue("lofiChar16x16", 8, 15, 16, 16, 128, 248, "Textures/lofiChar.png");
		setValue("lofiChar216x16", 8, 8, 16, 16, 128, 128, "Textures/lofiChar2.png"); // 128x128
		setValue("lofiChar216x8", 8, 16, 16, 8, 128, 128, "Textures/lofiChar2.png");
		setValue("lofiChar28x8", 8, 8, 16, 16, 128, 128, "Textures/lofiChar2.png");
		setValue("lofiEnvironment", 16, 16, 8, 8, 128, 128, "Textures/lofiEnvironment.png"); // 128x128
		setValue("lofiEnvironment2", 16, 16, 8, 8, 128, 128, "Textures/lofiEnvironment2.png");
		setValue("lofiEnvironment3", 16, 16, 8, 8, 128, 128, "Textures/lofiEnvironment3.png");
		setValue("lofiObj", 16, 16, 8, 8, 128, 128, "Textures/lofiObj.png"); // 128x128
		setValue("lofiObj2", 16, 16, 8, 8, 128, 128, "Textures/lofiObj2.png");
		setValue("lofiObj3", 16, 16, 8, 8, 128, 128, "Textures/lofiObj3.png");
		setValue("lofiObj4", 16, 16, 8, 8, 128, 128, "Textures/lofiObj4.png");
		setValue("lofiObj5", 16, 16, 8, 8, 128, 128, "Textures/lofiObj5.png");
		setValue("lofiObj6", 16, 16, 8, 8, 128, 128, "Textures/lofiObj6.png");
		
		// animated
		setValue("chars8x8dBeach", 7, 16, 8, 8, 56, 128, "Textures/chars8x8dBeach.png");
		setValue("chars8x8dEncounters", 7, 24, 8, 8, 56, 192, "Textures/chars8x8dEncounters.png");
		setValue("chars8x8dHero1", 7, 24, 8, 8, 56, 192, "Textures/chars8x8dHero1.png");
		setValue("chars8x8rBeach", 7, 16, 8, 8, 56, 128, "Textures/chars8x8rBeach.png");
		setValue("chars8x8rEncounters", 7, 48, 8, 8, 56, 384, "Textures/chars8x8rEncounters.png");
		setValue("chars8x8rHero1", 7, 24, 8, 8, 56, 192, "Textures/chars8x8rHero1.png");
		setValue("chars8x8rHero2", 7, 24, 8, 8, 56, 192, "Textures/chars8x8rHero2.png");
		setValue("chars8x8rHigh", 7, 24, 8, 8, 56, 192, "Textures/chars8x8rHigh.png");
		setValue("chars8x8rLow1", 7, 24, 8, 8, 56, 192, "Textures/chars8x8rLow1.png");
		setValue("chars8x8rLow2", 7, 24, 8, 8, 56, 192, "Textures/chars8x8rLow2.png");
		setValue("chars8x8rMid", 7, 30, 8, 8, 56, 240, "Textures/chars8x8rMid.png");
		setValue("chars8x8rMid2", 7, 30, 8, 8, 56, 240, "Textures/chars8x8rMid2.png");
		setValue("chars8x8rPets1", 7, 24, 8, 8, 56, 192, "Textures/chars8x8rPets1.png");
		setValue("chars8x8rPets1Mask", 7, 24, 8, 8, 56, 192, "Textures/chars8x8rPets1Mask.png");
		setValue("chars16x8dEncounters", 7, 12, 16, 8, 112, 192, "Textures/chars16x8dEncounters.png");
		setValue("chars16x8rEncounters", 7, 12, 16, 8, 112, 192, "Textures/chars16x8rEncounters.png");
		setValue("chars16x16dEncounters", 7, 24, 16, 16, 112, 384, "Textures/chars16x8dEncounters.png");
		setValue("chars16x16dEncounters2", 7, 48, 16, 16, 112, 768, "Textures/chars16x8dEncounters2.png");
		setValue("chars16x16dMountains1", 7, 24, 16, 16, 112, 384, "Textures/chars16x8dMountains1.png");
		setValue("chars16x16dMountains2", 7, 24, 16, 16, 112, 384, "Textures/chars16x8dMountains2.png");
		setValue("chars16x16rEncounters", 7, 24, 16, 16, 112, 384, "Textures/chars16x8rEncounters.png");
		
		// players
		setValue("players", 7, 45, 8, 8, 56, 360, "Textures/players.png");
	}
	
	private static void setValue(String val, int widthCells, int heightCells, int cellWidthPixels, int cellHeightPixels, int sourceWidthPixels, int sourceHeightPixels, String path) {
		Info i = new Info();
		i.cellHeightPixels = cellHeightPixels;
		i.cellWidthPixels = cellWidthPixels;
		i.widthCells = widthCells;
		i.heightCells = heightCells;
		i.path = path;
		i.sourceWidthPixels = sourceWidthPixels;
		i.sourceHeightPixels = sourceHeightPixels;
		
		images.put(val, i);
	}
	
	public static int getWidthCells(String file) {
		Info i = images.get(file);
		if (i == null) {
			return 0;
		} else {
			return i.widthCells;
		}
	}
	
	public static int getHeightCells(String file) {
		Info i = images.get(file);
		if (i == null) {
			return 0;
		} else {
			return i.heightCells;
		}
	}
	
	public static int getCellWidthPixels(String file) {
		Info i = images.get(file);
		if (i == null) {
			return 0;
		} else {
			return i.cellWidthPixels;
		}
	}
	
	public static int getCellHeightPixels(String file) {
		Info i = images.get(file);
		if (i == null) {
			return 0;
		} else {
			return i.cellHeightPixels;
		}
	}
	
	public static int getWidthPixels(String file) {
		Info i = images.get(file);
		if (i == null) {
			return 0;
		} else {
			return i.sourceWidthPixels;
		}
	}
	
	public static int getHeightPixels(String file) {
		Info i = images.get(file);
		if (i == null) {
			return 0;
		} else {
			return i.sourceHeightPixels;
		}
	}
	
	public static String getLocalFilePath(String file) {
		Info i = images.get(file);
		if (i == null) {
			return null;
		} else {
			return i.path;
		}
	}
	
	public static float getCellWidth(String file) {
		Info i = images.get(file);
		if (i == null) {
			return 1;
		} else {
			int usedWidth = i.widthCells * i.cellWidthPixels;
			
			float sc = (float)i.loadedWidthPixels / (float)usedWidth;
			float r = ((1f/sc) / (float)i.widthCells);
			return r;
		}
	}
	
	public static float getCellHeight(String file) {
		Info i = images.get(file);
		if (i == null) {
			return 1;
		} else {
			int usedHeight = i.heightCells * i.cellHeightPixels;
			
			float sc = (float)i.loadedHeightPixels / (float)usedHeight;
			float r = ((1f/sc) / (float)i.heightCells);
			return r;
		}
	}
	
	public static float getScaleMultiplier(String file) {
		Info i = images.get(file);
		if (i == null) {
			return 1;
		} else {
			return (float)i.cellHeightPixels / (float)8;
		}
	}
	
	public static Texture loadTexture(AssetManager assetManager, String key) {
		Info i = images.get(key);
		
		try {
			Texture t = assetManager.loadTexture(i.path);
			Image im = t.getImage();
			i.loadedWidthPixels = im.getWidth();
			i.loadedHeightPixels = im.getHeight();
			return t;
		} catch (AssetNotFoundException e) {
			return assetManager.loadTexture("Textures/Missing.png");
		}
	}
}
