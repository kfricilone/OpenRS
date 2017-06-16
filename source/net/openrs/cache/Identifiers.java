/*
 * Copyright (C) 2017 Kyle Fricilone
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
package net.openrs.cache;

import java.util.Arrays;

/**
 * Created by Kyle Fricilone on Jun 11, 2017.
 */
public class Identifiers {

	int[] table;

	public int getFile(int identifier) {
		/* Get mask to wrap around, and initial slot */
		int mask = (table.length >> 1) - 1;
		int i = identifier & mask;

		while (true) {
			/* Get id at current slot */
			int id = table[i + i + 1];
			if (id == -1) {
				return -1;
			}

			/* Return current id, if identifier matches */
			if (table[i + i] == identifier) {
				return id;
			}

			/* Move to next slot */
			i = i + 1 & mask;
		}
	}
	
	public Identifiers(int[] identifiers) {
		/* Initial identifier sizes */
		int length = identifiers.length;
		int halfLength = identifiers.length >> 1;
		
		/* Find maximum power of 2 below array and a half length */
		int size = 1;
		int mask = 1;
		for (int i = 1; i <= length + (halfLength); i <<= 1) {
			mask = i;
			size = i << 1;
		}
		
		/* Increase power over the array length */
		mask <<= 1;
		size <<= 1;
		
		/* Create table array */
		table = new int[size];

		/* Fill table with null values */
		Arrays.fill(table, -1);

		/* Populate table with identifiers followed by their id */
		for (int id = 0; id < identifiers.length; id++) {
			int i;
			for (i = identifiers[id] & mask - 1; table[i + i + 1] != -1; i = i + 1 & mask - 1);

			table[i + i] = identifiers[id];
			table[i + i + 1] = id;
		}

	}

}