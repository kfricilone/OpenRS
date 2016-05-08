/**
 * Copyright (c) 2015 Kyle Friz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.openrs.cache.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.imageio.ImageIO;

import net.openrs.cache.Cache;
import net.openrs.cache.Constants;
import net.openrs.cache.Container;
import net.openrs.cache.FileStore;
import net.openrs.cache.region.Region;
import net.openrs.cache.sprite.Textures;
import net.openrs.cache.type.TypeListManager;
import net.openrs.cache.type.overlays.OverlayType;
import net.openrs.cache.type.underlays.UnderlayType;
import net.openrs.cache.util.XTEAManager;
import net.openrs.util.ImageFlip;

/**
 * @author Kyle Friz
 * @since Jan 23, 2016
 */
public class MapImageDumper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedImage underlayImage = new BufferedImage(8193, 16385, BufferedImage.TYPE_INT_RGB);
		BufferedImage image = new BufferedImage(8193, 16385, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();

		try (Cache cache = new Cache(FileStore.open(Constants.CACHE_PATH))) {
			TypeListManager.initialize(cache);
			Textures.initialize(cache);

			graphics.setColor(Color.RED);
			for (int i = 0; i < 32768; i++) {
				Region region = new Region(i);
				int baseX = region.getBaseX();
				int baseY = region.getBaseY();

				int map = cache.getFileId(5, "m" + (i >> 8) + "_" + (i & 0xFF));
				if (map != -1) {
					region.loadTerrain(cache.read(5, map).getData());

					for (int x = 0; x < 64; x++) {
						for (int y = 0; y < 64; y++) {
							int overlayId = region.getOverlayId(0, x, y) - 1;
							int underlayId = region.getUnderlayId(0, x, y) - 1;
							if (overlayId > -1) {
								OverlayType overlay = TypeListManager.lookupOver(overlayId);
								if (!overlay.isHideUnderlay() && (underlayId > -1)) {
									UnderlayType underlay = TypeListManager.lookupUnder(underlayId);
									int rgb = underlay.getRgbColor();
									underlayImage.setRGB(baseX + x, baseY + y, rgb);
								} else {
									underlayImage.setRGB(baseX + x, baseY + y, Color.CYAN.getRGB());
								}
							} else if (underlayId > -1) {
								UnderlayType underlay = TypeListManager.lookupUnder(underlayId);
								int rgb = underlay.getRgbColor();
								underlayImage.setRGB(baseX + x, baseY + y, rgb);
							} else {
								underlayImage.setRGB(baseX + x, baseY + y, Color.CYAN.getRGB());
							}
						}
					}
				}
			}

			for (int i = 0; i < 32768; i++) {
				Region region = new Region(i);
				int baseX = region.getBaseX();
				int baseY = region.getBaseY();

				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						int dx = baseX + x;
						int dy = baseY + y;

						Color c = new Color(underlayImage.getRGB(dx, dy));

						if (Color.CYAN.equals(c))
							continue;

						int tRed = 0, tGreen = 0, tBlue = 0;
						int count = 0;
						for (int oy = Math.max(0, dy - 5); oy < Math.min(underlayImage.getHeight(), dy + 5); oy++) {
							for (int ox = Math.max(0, dx - 5); ox < Math.min(underlayImage.getWidth(), dx + 5); ox++) {
								c = new Color(underlayImage.getRGB(ox, oy));

								if (Color.CYAN.equals(c))
									continue;

								tRed += c.getRed();
								tGreen += c.getGreen();
								tBlue += c.getBlue();
								count++;
							}
						}
						if (count > 0) {
							c = new Color(tRed / count, tGreen / count, tBlue / count);
							image.setRGB(dx, dy, c.getRGB());
						}
					}
				}
			}

			for (int i = 0; i < 32768; i++) {
				Region region = new Region(i);
				int baseX = region.getBaseX();
				int baseY = region.getBaseY();

				int map = cache.getFileId(5, "m" + (i >> 8) + "_" + (i & 0xFF));
				if (map != -1) {
					region.loadTerrain(cache.read(5, map).getData());

					for (int x = 0; x < 64; x++) {
						for (int y = 0; y < 64; y++) {
							int overlayId = region.getOverlayId(0, x, y) - 1;
							if (overlayId > -1) {
								OverlayType overlay = TypeListManager.lookupOver(overlayId);

								int rgb = 0;
								if (overlay.isHideUnderlay())
									rgb = overlay.getRgbColor();

								if (overlay.getSecondaryRgbColor() > -1)
									rgb = overlay.getSecondaryRgbColor();

								if (overlay.getTexture() > -1)
									rgb = Textures.getColors(overlay.getTexture());

								image.setRGB(baseX + x, baseY + y, rgb);
							}
						}
					}
				}
				graphics.drawRect(baseX, baseY, 64, 64);
			}

			graphics.setColor(new Color(255, 0, 0, 100));
			for (int i = 0; i < 32768; i++) {
				Region region = new Region(i);
				int baseX = region.getBaseX();
				int baseY = region.getBaseY();

				int[] keys = XTEAManager.lookupMap(i);

				int land = cache.getFileId(5, "l" + (i >> 8) + "_" + (i & 0xFF));
				if (land != -1) {
					ByteBuffer buffer = cache.getStore().read(5, land);
					if (buffer.remaining() == 32)
						continue;

					try {
						region.loadLocations(Container.decode(buffer, keys).getData());
					} catch (Exception e) {
						graphics.fillRect(baseX, baseY, 64, 64);
						System.out.println(region.getRegionID() + ", " + Arrays.toString(keys));
					}
				}
			}

			BufferedImage flipped = ImageFlip.verticalFlip(image);
			Graphics flippedGraphics = flipped.getGraphics();
			flippedGraphics.setColor(Color.WHITE);
			for (int i = 0; i < 32768; i++) {
				Region region = new Region(i);
				int baseX = region.getBaseX();
				int baseY = ((16385 - 64) - region.getBaseY());

				String text = String.valueOf(i);
				int width = graphics.getFontMetrics().stringWidth(text) / 2;
				flippedGraphics.drawString(text, ((baseX + 31) - width), (baseY + 36));
			}
			ImageIO.write(flipped, "png", new File("map_image.png"));
			ImageIO.write(ImageFlip.verticalFlip(underlayImage), "png", new File("underlay_image.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
