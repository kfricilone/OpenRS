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
package net.openrs.cache.type.areas;

import java.nio.ByteBuffer;

import net.openrs.cache.type.Type;
import net.openrs.util.ByteBufferUtils;

/**
 * 
 * Created by Kyle Fricilone on Jun 1, 2017.
 */
public class AreaType implements Type {

	private final int id;

	private int spriteId = -1;
	private int anInt1967 = -1;
	private String name;
	private int anInt1959;
	private int anInt1968 = 0;
	private int[] anIntArray1982;
	private String aString1970;
	private int[] anIntArray1981;
	private int anInt1980;
	private byte[] aByteArray1979;
	private String[] aStringArray1969 = new String[5];

	public AreaType(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#decode(java.nio.ByteBuffer)
	 */
	@Override
	public void decode(ByteBuffer buffer) {
		while (true) {
			int opcode = buffer.get() & 0xFF;
			if (opcode == 0)
				break;

			if (opcode == 1) {
				spriteId = ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 2) {
				anInt1967 = ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 3) {
				name = ByteBufferUtils.getString(buffer);
			} else if (opcode == 4) {
				anInt1959 = ByteBufferUtils.getMedium(buffer);
			} else if (opcode == 5) {
				ByteBufferUtils.getMedium(buffer);
			} else if (opcode == 6) {
				anInt1968 = buffer.get() & 0xFF;
			} else if (opcode == 7) {
				int flags = buffer.get() & 0xFF;
				if ((flags & 1) == 0) {
				}
				if ((flags & 2) == 2) {
				}
			} else if (opcode == 8) {
				buffer.get();
			} else if (opcode >= 10 && opcode <= 14) {
				aStringArray1969[opcode - 10] = ByteBufferUtils.getString(buffer);
			} else if (opcode == 15) {
				int size = buffer.get() & 0xFF;
				anIntArray1982 = new int[size * 2];

				for (int i = 0; i < size * 2; ++i) {
					anIntArray1982[i] = buffer.getShort();
				}

				buffer.getInt();
				int size2 = buffer.get() & 0xFF;
				anIntArray1981 = new int[size2];

				for (int i = 0; i < anIntArray1981.length; ++i) {
					anIntArray1981[i] = buffer.getInt();
				}

				aByteArray1979 = new byte[size];

				for (int i = 0; i < size; ++i) {
					aByteArray1979[i] = buffer.get();
				}
			} else if (opcode == 17) {
				aString1970 = ByteBufferUtils.getString(buffer);
			} else if (opcode == 18) {
				ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 19) {
				anInt1980 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 21) {
				buffer.getInt();
			} else if (opcode == 22) {
				buffer.getInt();
			} else if (opcode == 23) {
				buffer.get();
				buffer.get();
				buffer.get();
			} else if (opcode == 24) {
				buffer.getShort();
				buffer.getShort();
			} else if (opcode == 25) {
				ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 28) {
				buffer.get();
			} else if (opcode == 29) {
				buffer.get();
			} else if (opcode == 30) {
				buffer.get();
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
		ByteBuffer buffer = ByteBuffer.allocate(1132);
		return (ByteBuffer) buffer.flip();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#encode317()
	 */
	@Override
	public ByteBuffer encode317() {
		ByteBuffer buffer = ByteBuffer.allocate(1132);
		return (ByteBuffer) buffer.flip();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#getID()
	 */
	@Override
	public int getID() {
		return id;
	}

	/**
	 * @return the spriteId
	 */
	public int getSpriteId() {
		return spriteId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
