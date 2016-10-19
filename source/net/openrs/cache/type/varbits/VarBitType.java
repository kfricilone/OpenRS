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
package net.openrs.cache.type.varbits;

import java.nio.ByteBuffer;

import net.openrs.cache.type.Type;
import net.openrs.util.Masks;

/**
 * @author Kyle Friz
 * 
 * @since May 26, 2015
 */
public class VarBitType implements Type {

	private final int id;
	private int configID;
	private int leastSigBit;
	private int mostSigBit;

	public VarBitType(int id) {
		this.id = id;
		this.configID = -1;
		this.leastSigBit = -1;
		this.mostSigBit = -1;
	}

	@Override
	public void decode(ByteBuffer buffer) {
		while (true) {
			int opcode = buffer.get() & 0xFF;

			if (opcode == 0)
				return;

			if (opcode == 1) {
				configID = buffer.getShort() & 0xFFFF;
				leastSigBit = buffer.get() & 0xFF;
				mostSigBit = buffer.get() & 0xFF;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#encode()
	 */
	@Override
	public ByteBuffer encode() {
		ByteBuffer buffer = ByteBuffer.allocate(6);
		if (configID != -1 || leastSigBit != -1 || mostSigBit != -1) {
			buffer.put((byte) 1);
			buffer.putShort((short) configID);
			buffer.put((byte) leastSigBit);
			buffer.put((byte) mostSigBit);
		}

		buffer.put((byte) 0);
		return (ByteBuffer) buffer.flip();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#encode317()
	 */
	@Override
	public ByteBuffer encode317() {
		ByteBuffer buffer = ByteBuffer.allocate(6);
		if (configID != -1 || leastSigBit != -1 || mostSigBit != -1) {
			buffer.put((byte) 1);
			buffer.putShort((short) configID);
			buffer.put((byte) leastSigBit);
			buffer.put((byte) mostSigBit);
		}

		buffer.put((byte) 0);
		return (ByteBuffer) buffer.flip();
	}

	public int getID() {
		return id;
	}

	public int getConfigID() {
		return configID;
	}

	public int getLeastSigBit() {
		return leastSigBit;
	}

	public int getMostSigBit() {
		return mostSigBit;
	}

	public int getBitCount() {
		return (getMostSigBit() - getLeastSigBit()) + 1;
	}

	public boolean isBooleanType() {
		return getBitCount() == 1;
	}

	public int setBoolean(int oVal, boolean nVal) {
		if (!isBooleanType())
			throw new Error();
		return set(oVal, nVal ? 1 : 0);
	}

	public int set(int oVal, int nVal) {
		int least = getLeastSigBit();
		int most = getMostSigBit();
		int mask = Masks.getMask(most - least);
		if (nVal < 0 || nVal > mask) {
			throw new Error("Value out of bit range:" + nVal + ", MAX:" + mask);
		}
		mask <<= least;
		return oVal & ~mask | nVal << least & mask;
	}

	public int get(int val) {
		int least = getLeastSigBit();
		int most = getMostSigBit();
		int mask = Masks.getMask(most - least);
		return val >> least & mask;
	}

	public boolean getBoolean(int val) {
		if (!isBooleanType())
			throw new Error();
		return get(val) == 1;
	}

}
