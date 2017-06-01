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
package net.openrs.cache.type.hitbars;

import java.nio.ByteBuffer;

import net.openrs.cache.type.Type;
import net.openrs.util.ByteBufferUtils;

/**
 * 
 * Created by Kyle Fricilone on Jun 1, 2017.
 */
public class HitBarType implements Type {

	private final int id;

	private int anInt2098 = 255;
	private int anInt2099 = 255;
	private int anInt2103 = -1;
	private int anInt2101 = 1;
	private int anInt2100 = 70;
	private int secondarySprite = -1;
	private int primarySprite = -1;
	private int anInt2104 = 30;
	private int anInt2105 = 0;

	public HitBarType(int id) {
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
				buffer.getShort();
			} else if (opcode == 2) {
				anInt2098 = buffer.get() & 0xFF;
			} else if (opcode == 3) {
				anInt2099 = buffer.get() & 0xFF;
			} else if (opcode == 4) {
				anInt2103 = 0;
			} else if (opcode == 5) {
				anInt2100 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 6) {
				buffer.get();
			} else if (opcode == 7) {
				secondarySprite = ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 8) {
				primarySprite = ByteBufferUtils.getSmartInt(buffer);
			} else if (opcode == 11) {
				anInt2103 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 14) {
				anInt2104 = buffer.get() & 0xFF;
			} else if (opcode == 15) {
				anInt2105 = buffer.get() & 0xFF;
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

}
