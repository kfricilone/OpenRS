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
package net.openrs.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.openrs.cache.Cache;
import net.openrs.cache.Container;
import net.openrs.cache.ReferenceTable;
import net.openrs.cache.ReferenceTable.Entry;
import net.openrs.cache.type.CacheIndex;
import net.openrs.util.Preconditions;

/**
 * @author Kyle Friz
 * 
 * @since May 26, 2015
 */
public class ModelList {

	private Logger logger = Logger.getLogger(ModelList.class.getName());

	private Model[] models;

	public void initialize(Cache cache) {
		int count = 0;
		try {
			ReferenceTable table = cache.getReferenceTable(CacheIndex.MODELS);
			models = new Model[table.capacity()];
			for (int id = 0; id < table.capacity(); id++) {
				Entry entry = table.getEntry(id);
				if (entry == null)
					continue;

				Container mContainer = cache.read(CacheIndex.MODELS, entry.index());
				ByteBuffer buffer = mContainer.getData();
				
				Model model = new Model(id);
				model.decode(buffer);
				models[id] = model;
				count++;
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error Loading Model(s)!", e);
		}
		logger.info("Loaded " + count + " Model(s)!");
	}

	public Model list(int id) {
		Preconditions.checkArgument(id >= 0, "ID can't be negative!");
		Preconditions.checkArgument(id < models.length, "ID can't be greater than the max model id!");
		return models[id];
	}

}
