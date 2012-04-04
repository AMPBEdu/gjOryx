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

package com.joryx.mesh;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.joryx.util.ImageData;

public class IndexedSpriteQuad extends Mesh {
	private int index;
	private boolean doubleWidth;
	private boolean flipHorizontally;
	private String key;
	
	public IndexedSpriteQuad() {
		updateGeometry();
	}
	
	public IndexedSpriteQuad(String key) {
		this.key = key;
		updateGeometry();
	}
	
	public void updateGeometry() {
		// Create the vertex buffers
		// origin is at the center of the quad
		setStreamed();
		
		if (doubleWidth) {
			if (flipHorizontally) {
				// make assumption flip Hori = extend leftward
				setBuffer(Type.Position, 3, new float[] {
						-1, 0, 0,
						1, 0, 0,
						1, 1, 0,
						-1, 1, 0
				});
			} else {
				setBuffer(Type.Position, 3, new float[] {
						0, 0, 0,
						2, 0, 0,
						2, 1, 0,
						0, 1, 0
				});
			}
		} else {
			setBuffer(Type.Position, 3, new float[] {
					0, 0, 0,
					1, 0, 0,
					1, 1, 0,
					0, 1, 0
			});
		}
		
		setBuffer(Type.Normal, 3, new float[] {
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1
		});
		
		updateTexCoords();
		
		setBuffer(Type.Index, 3, new short[] {
				0, 1, 2,
				0, 2, 3
		});
		
		updateBound();
	}
	
	public void updateTexCoords() {
		if (key == null) {
			setBuffer(Type.TexCoord, 2, new float[] {
					0, 0,
					1, 0,
					1, 1,
					0, 1
			});
			return;
		}
		float cellWidth = ImageData.getCellWidth(key);
		float cellHeight = ImageData.getCellHeight(key);
		int hIn = index % ImageData.getWidthCells(key);
		int vIn = (ImageData.getHeightCells(key)-1) - (index / ImageData.getWidthCells(key));
		
		if (doubleWidth) {
			if (flipHorizontally) {
				setBuffer(Type.TexCoord, 2, new float[] {
						(hIn + 2) * cellWidth - 0.001f, vIn * cellHeight,
						hIn * cellWidth, vIn * cellHeight,
						hIn * cellWidth, (vIn + 1) * cellHeight - 0.001f,
						(hIn + 2) * cellWidth - 0.001f, (vIn + 1) * cellHeight - 0.001f
				});
			} else {
				setBuffer(Type.TexCoord, 2, new float[] {
						hIn * cellWidth, vIn * cellHeight,
						(hIn + 2) * cellWidth - 0.001f, vIn * cellHeight,
						(hIn + 2) * cellWidth - 0.001f, (vIn + 1) * cellHeight - 0.001f,
						hIn * cellWidth, (vIn + 1) * cellHeight - 0.001f
				});
			}
			
		} else {
			if (flipHorizontally) {
				setBuffer(Type.TexCoord, 2, new float[] {
						(hIn + 1) * cellWidth - 0.001f, vIn * cellHeight,
						hIn * cellWidth, vIn * cellHeight,
						hIn * cellWidth, (vIn + 1) * cellHeight - 0.001f,
						(hIn + 1) * cellWidth - 0.001f, (vIn + 1) * cellHeight - 0.001f
				});
			} else {
				setBuffer(Type.TexCoord, 2, new float[] {
						hIn * cellWidth, vIn * cellHeight,
						(hIn + 1) * cellWidth - 0.001f, vIn * cellHeight,
						(hIn + 1) * cellWidth - 0.001f, (vIn + 1) * cellHeight - 0.001f,
						hIn * cellWidth, (vIn + 1) * cellHeight - 0.001f
				});
			}
			
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isDoubleWidth() {
		return doubleWidth;
	}

	public void setDoubleWidth(boolean doubleWidth) {
		this.doubleWidth = doubleWidth;
	}

	public boolean isFlipHorizontally() {
		return flipHorizontally;
	}

	public void setFlipHorizontally(boolean flipHorizontally) {
		this.flipHorizontally = flipHorizontally;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}
