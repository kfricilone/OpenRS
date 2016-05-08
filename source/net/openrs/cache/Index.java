/**
 * Copyright (c) 2014 RSE Studios
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
package net.openrs.cache;

import java.nio.ByteBuffer;

import net.openrs.util.ByteBufferUtils;

/**
 * An {@link Index} points to a file inside a {@link FileStore}.
 * 
 * @author Graham
 * @author `Discardedx2
 */
public final class Index {

	/**
	 * The size of an index, in bytes.
	 */
	public static final int SIZE = 6;

	/**
	 * Decodes the specified {@link ByteBuffer} into an {@link Index} object.
	 * 
	 * @param buf
	 *            The buffer.
	 * @return The index.
	 */
	public static Index decode(ByteBuffer buf) {
		if (buf.remaining() != SIZE)
			throw new IllegalArgumentException();

		int size = ByteBufferUtils.getMedium(buf);
		int sector = ByteBufferUtils.getMedium(buf);
		return new Index(size, sector);
	}

	/**
	 * The size of the file in bytes.
	 */
	private int size;

	/**
	 * The number of the first sector that contains the file.
	 */
	private int sector;

	/**
	 * Creates a new index.
	 * 
	 * @param size
	 *            The size of the file in bytes.
	 * @param sector
	 *            The number of the first sector that contains the file.
	 */
	public Index(int size, int sector) {
		this.size = size;
		this.sector = sector;
	}

	/**
	 * Encodes this index into a byte buffer.
	 * 
	 * @return The buffer.
	 */
	public ByteBuffer encode() {
		ByteBuffer buf = ByteBuffer.allocate(Index.SIZE);
		ByteBufferUtils.putMedium(buf, size);
		ByteBufferUtils.putMedium(buf, sector);
		return (ByteBuffer) buf.flip();
	}

	/**
	 * Gets the number of the first sector that contains the file.
	 * 
	 * @return The number of the first sector that contains the file.
	 */
	public int getSector() {
		return sector;
	}

	/**
	 * Gets the size of the file.
	 * 
	 * @return The size of the file in bytes.
	 */
	public int getSize() {
		return size;
	}

}