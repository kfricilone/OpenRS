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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import net.openrs.cache.Cache;
import net.openrs.cache.Constants;
import net.openrs.cache.Container;
import net.openrs.cache.FileStore;
import net.openrs.cache.util.XTEAManager;

/**
 * @author Kyle Friz
 * @since Dec 30, 2015
 */
public class MapDumper {

	public static void main(String[] args) throws IOException {
		try (Cache cache = new Cache(FileStore.open(Constants.CACHE_PATH))) {
			for (int i = 0; i < 32768; i++) {			
				int[] keys = XTEAManager.lookupMap(i);

				int x = (i >> 8);
				int y = (i & 0xFF);

				int map = cache.getFileId(5, "m" + x + "_" + y);
				int land = cache.getFileId(5, "l" + x + "_" + y);

				if (map != -1) {
					Container container = cache.read(5, map);
					byte[] bytes = new byte[container.getData().limit()];
					container.getData().get(bytes);
					
	         File dir = new File(Constants.MAP_PATH);
	          
	          if (!dir.exists()) {
	                dir.mkdir();
	          }

					File file = new File(Constants.MAP_PATH, "m" + x + "_" + y + ".dat");

					DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
					dos.write(bytes);
					dos.close();
				}

				if (land != -1) {
					try {
						Container container = cache.read(5, land, keys);
						byte[] bytes = new byte[container.getData().limit()];
						container.getData().get(bytes);

						File file = new File(Constants.MAP_PATH, "l" + x + "_" + y + ".dat");

						DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
						dos.write(bytes);
						dos.close();
					} catch (Exception e) {
						System.out.println(i + ", " + Arrays.toString(keys));
					}
				}
			}
		}
	}

}
