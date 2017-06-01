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
package net.openrs.cache.type.hitmarks;

import java.nio.ByteBuffer;

import net.openrs.cache.type.Type;
import net.openrs.util.ByteBufferUtils;

/**
 * 
 * Created by Kyle Fricilone on Jun 1, 2017.
 */
public class HitMarkType implements Type {

	private final int id;

	private int anInt2125 = -1;
	private int anInt2126 = 16777215;
	private int anInt2127 = -1;
	private int anInt2133 = -1;
	private int spriteId = -1;
	private int anInt2131 = -1;
	private int anInt2123 = 0;
	private String aString2134 = "";
	private int anInt2139 = 70;
	private int anInt2122 = 0;
	private int anInt2119 = -1;
	private int anInt2135 = -1;
	private int anInt2136 = 0;
	private int anInt2138 = -1;
	private int anInt2132 = -1;
	private int[] anIntArray2137;

	public HitMarkType(int id) {
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
				anInt2125 = ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 2) {
				anInt2126 = ByteBufferUtils.getMedium(buffer);
			} else if (opcode == 3) {
				anInt2127 = ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 4) {
				anInt2133 = ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 5) {
				spriteId = ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 6) {
				anInt2131 = ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 7) {
				anInt2123 = buffer.getShort();
			} else if (opcode == 8) {
				aString2134 = ByteBufferUtils.getPrefixedString(buffer);
			} else if (opcode == 9) {
				anInt2139 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 10) {
				anInt2122 = buffer.getShort();
			} else if (opcode == 11) {
				anInt2119 = 0;
			} else if (opcode == 12) {
				anInt2135 = buffer.get() & 0xFF;
			} else if (opcode == 13) {
				anInt2136 = buffer.getShort();
			} else if (opcode == 14) {
				anInt2119 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 17) {
				anInt2138 = buffer.getShort() & 0xFFFF;
				if (anInt2138 == '\uffff') {
					anInt2138 = -1;
				}

				anInt2132 = buffer.getShort() & 0xFFFF;
				if (anInt2132 == '\uffff') {
					anInt2132 = -1;
				}

				int length = buffer.get() & 0xFFFF;
				anIntArray2137 = new int[length + 2];

				for (int var3 = 0; var3 <= length; ++var3) {
					anIntArray2137[var3] = buffer.getShort() & 0xFFFF;
					if (anIntArray2137[var3] == '\uffff') {
						anIntArray2137[var3] = -1;
					}
				}

				anIntArray2137[length + 1] = -1;
			} else if (opcode == 18) {
				anInt2138 = buffer.getShort() & 0xFFFF;
				if (anInt2138 == '\uffff') {
					anInt2138 = -1;
				}

				anInt2132 = buffer.getShort() & 0xFFFF;
				if (anInt2132 == '\uffff') {
					anInt2132 = -1;
				}

				int var = var = buffer.getShort() & 0xFFFF;
				if (var == '\uffff') {
					var = -1;
				}

				int length = buffer.get() & 0xFF;
				anIntArray2137 = new int[length + 2];

				for (int var3 = 0; var3 <= length; ++var3) {
					anIntArray2137[var3] = buffer.getShort() & 0xFFFF;
					if (anIntArray2137[var3] == '\uffff') {
						anIntArray2137[var3] = -1;
					}
				}

				anIntArray2137[length + 1] = var;
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

}
