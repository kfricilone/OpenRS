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

/**
 * Created by Kyle Fricilone on Jun 11, 2017.
 */
public class Identifiers {

	int[] table;

	public int getFile(int identifier) {
		int mask = (this.table.length >> 1) - 1;
		int i = identifier & mask;

		while (true) {
			int ident = this.table[i + i + 1];
			if (ident == -1) {
				return -1;
			}

			if (this.table[i + i] == identifier) {
				return ident;
			}

			i = i + 1 & mask;
		}
	}

	public Identifiers(int[] idents) {
		int size;
		for (size = 1; size <= idents.length + (idents.length >> 1); size <<= 1) {
			;
		}

		this.table = new int[size + size];

		int i;
		for (i = 0; i < size + size; i++) {
			this.table[i] = -1;
		}

		int j;
		for (i = 0; i < idents.length; this.table[j + j + 1] = i++) {
			for (j = idents[i] & size - 1; this.table[j + j + 1] != -1; j = j + 1 & size - 1) {
				;
			}

			this.table[j + j] = idents[i];
		}

	}

}