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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class TextureSizeFixer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String folder = "chars16x16dEncounters";
		
		File[] files = new File("assets/Textures/" + folder + "/").listFiles();
		
		for (File f : files) {
			BufferedImage in = ImageIO.read(f);
			BufferedImage res = new BufferedImage(128, 16, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = res.getGraphics();
			
			for (int ix = 0; ix < in.getWidth() && ix < res.getWidth(); ix++) {
				for (int iy = 0; iy < in.getHeight() && iy < res.getHeight(); iy++) {
					int[] src = in.getData().getPixel(ix, iy, (int[])null);
					Color realCol;
					realCol = new Color(src[0], src[1], src[2], src[3]);
					g.setColor(realCol);
					g.fillRect(ix, iy, 1, 1);
				}
			}
			
			ImageIO.write(res, "png", f);
		}
	}

}
