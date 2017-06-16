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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import net.openrs.cache.Cache;
import net.openrs.cache.Constants;
import net.openrs.cache.Container;
import net.openrs.cache.FileStore;

/**
 * @author Kyle Friz
 * @since  Apr 16, 2016
 */
public class TablesEncryptor {

	public static void main(String[] args) {
		try (Cache cache = new Cache(FileStore.open(Constants.CACHE_PATH))) {
			for (int type = 0; type < cache.getTypeCount(); type++) {
				ByteBuffer buf = cache.getStore().read(255, type);
				if (buf != null && buf.limit() > 0) {
					BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Constants.XTABLE_PATH, type + ".txt")));
					SecureRandom random = new SecureRandom();
					int[] keys = new int[4];
					for (int i = 0; i < keys.length; i++) {
						keys[i] = random.nextInt();
						
						writer.write(String.valueOf(keys[i]));
						writer.newLine();
					}
					writer.flush();
					writer.close();
					
					Container container = Container.decode(buf);
					ByteBuffer buffer = container.encode(keys);
					cache.getStore().write(255, type, buffer);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
