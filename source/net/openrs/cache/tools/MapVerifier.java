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

import java.util.Arrays;

import net.openrs.cache.Cache;
import net.openrs.cache.Constants;
import net.openrs.cache.FileStore;
import net.openrs.cache.util.XTEAManager;

/**
 * @author Kyle Friz
 * 
 * @since Jun 30, 2015
 */
public class MapVerifier {

	public static void main(String[] args) {
		int count = 0;

		try {
			Cache cache = new Cache(FileStore.open(Constants.CACHE_PATH));
			for (int i = 0; i < 32_768; i++) {
				int[] keys = XTEAManager.lookupMap(i);
				int land = cache.getFileId(5, "l" + (i >> 8) + "_" + (i & 0xFF));

				if (land != -1) {
					try {
						cache.read(5, land, keys).getData();
					} catch (Exception e) {
						if (keys[0] != 0) {
							System.out.println("Region ID: " + i + ", Coords: (" + ((i >> 8) << 6) + ", "
									+ ((i & 0xFF) << 6) + "), File: (5, " + land + "), Keys: " + Arrays.toString(keys));
							count++;

							try {
								cache.read(5, land).getData();
								System.out.println("Region ID: " + i + " no longer encrypted");
							} catch (Exception ee) {
							}
						}
					}
				}
			}
			cache.close();

			System.out.println("Incorrect: " + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
