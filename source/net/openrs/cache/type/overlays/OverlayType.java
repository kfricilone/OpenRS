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
package net.openrs.cache.type.overlays;

import java.nio.ByteBuffer;

import net.openrs.cache.type.Type;
import net.openrs.util.ByteBufferUtils;

/**
 * @author Kyle Friz
 * 
 * @since Jun 13, 2015
 */
public class OverlayType implements Type {

	private final int id;
	private int rgbColor = 0;
	private int texture = -1;
	private int secondaryRgbColor = -1;
	private boolean hideUnderlay = true;

	public OverlayType(int id) {
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

			if (opcode == 1)
				rgbColor = ByteBufferUtils.getMedium(buffer);

			if (opcode == 2)
				texture = buffer.get() & 0xFF;

			if (opcode == 5)
				hideUnderlay = false;

			if (opcode == 7)
				secondaryRgbColor = ByteBufferUtils.getMedium(buffer);

			if (opcode == 8)
				;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#encode()
	 */
	@Override
	public ByteBuffer encode() {
		ByteBuffer buffer = ByteBuffer.allocate(10);

		if (rgbColor != -1) {
			buffer.put((byte) 1);
			ByteBufferUtils.putMedium(buffer, rgbColor);
		}

		if (texture != -1) {
			buffer.put((byte) 2);
			buffer.put((byte) texture);
		}

		if (!hideUnderlay)
			buffer.put((byte) 5);

		if (secondaryRgbColor != -1) {
			buffer.put((byte) 7);
			ByteBufferUtils.putMedium(buffer, secondaryRgbColor);
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
		ByteBuffer buffer = ByteBuffer.allocate(10);

		if (rgbColor != -1) {
			buffer.put((byte) 1);
			ByteBufferUtils.putMedium(buffer, rgbColor);
		}

		if (texture != -1) {
			buffer.put((byte) 2);
			buffer.put((byte) texture);
		}

		if (!hideUnderlay)
			buffer.put((byte) 5);

		if (secondaryRgbColor != -1) {
			buffer.put((byte) 7);
			ByteBufferUtils.putMedium(buffer, secondaryRgbColor);
		}

		buffer.put((byte) 0);
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
	 * @return the rgbColor
	 */
	public int getRgbColor() {
		return rgbColor;
	}

	/**
	 * @return the texture
	 */
	public int getTexture() {
		return texture;
	}

	/**
	 * @return the secondaryRgbColor
	 */
	public int getSecondaryRgbColor() {
		return secondaryRgbColor;
	}

	/**
	 * @return the hideUnderlay
	 */
	public boolean isHideUnderlay() {
		return hideUnderlay;
	}

}
