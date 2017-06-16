/*
 * Copyright (C) 2016 Kyle Fricilone
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
package net.openrs.cache.sprite;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.openrs.cache.Cache;
import net.openrs.cache.Container;
import net.openrs.cache.Identifiers;
import net.openrs.cache.ReferenceTable;
import net.openrs.cache.ReferenceTable.Entry;
import net.openrs.cache.type.CacheIndex;
import net.openrs.util.crypto.Djb2;

/**
 * Created by Kyle Fricilone on Sep 15, 2016.
 */
public class Sprites {

	private static Logger logger = Logger.getLogger(Sprites.class.getName());
	
	private static Sprite[] sprites;
	
	private static Identifiers identifiers;
	
	public static void initialize(Cache cache)
	{
		int count = 0;
		try
		{
			ReferenceTable table = cache.getReferenceTable(8);
			
			sprites = new Sprite[table.capacity()];
			
			for (int i = 0; i < table.capacity(); i++)
			{
				Entry e = table.getEntry(i);
				if (e == null)
					continue;
				
				Container c = cache.read(CacheIndex.SPRITES, i);
				Sprite sprite = Sprite.decode(c.getData());
				sprites[i] = sprite;
				count++;
			}
			
			identifiers = table.getIdentifiers();
			
		}
		
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "Error Loading Sprite(s)!", e);
		}
		logger.info("Loaded " + count + " Sprite(s)!");
	}
	
	public static final Sprite getSprite(int id)
	{
		return sprites[id];
	}
	
	public static final Sprite getSprite(String name)
	{
		int id = identifiers.getFile(Djb2.hash(name));
		if (id == -1) {
			return null;
		}
		
		return sprites[id];
	}
	
}
