/**
* Copyright (c) Kyle Fricilone
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import net.openrs.cache.Cache;
import net.openrs.cache.Constants;
import net.openrs.cache.Container;
import net.openrs.cache.FileStore;
import net.openrs.cache.ReferenceTable;
import net.openrs.cache.sprite.Sprite;

/**
 * @author Kyle Friz
 * @since Dec 30, 2015
 */
public class SpriteDumper {

	public static void main(String[] args) throws IOException {
		try (Cache cache = new Cache(FileStore.open(Constants.CACHE_PATH))) {
			ReferenceTable table = ReferenceTable.decode(Container.decode(cache.getStore().read(255, 8)).getData());
			for (int i = 0; i < table.capacity(); i++) {
				if (table.getEntry(i) == null)
					continue;

				Container container = cache.read(8, i);
				Sprite sprite = Sprite.decode(container.getData());
				
        File dir = new File(Constants.SPRITE_PATH);
        
        if (!dir.exists()) {
              dir.mkdir();
        }
				
				for (int frame = 0; frame < sprite.size(); frame++) {
					File file = new File(Constants.SPRITE_PATH, i + "_" + frame + ".png");
					BufferedImage image = sprite.getFrame(frame);

					ImageIO.write(image, "png", file);
				}
			}
			
			Container container = cache.read(10, cache.getFileId(10, "title.jpg"));
			byte[] bytes = new byte[container.getData().remaining()];
			container.getData().get(bytes);
			Files.write(Paths.get(Constants.SPRITE_PATH).resolve("title.jpg"), bytes);
		}
	}

}
