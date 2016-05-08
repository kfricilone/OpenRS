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
 * @author Graham
 * @author `Discardedx2
 */
public final class Sector {

	/**
	 * The size of the header within a sector in bytes.
	 */
	public static final int HEADER_SIZE = 8;

	/**
	 * The size of the data within a sector in bytes.
	 */
	public static final int DATA_SIZE = 512;

	/**
	 * The extended data size
	 */
	public static final int EXTENDED_DATA_SIZE = 510;

	/**
	 * The extended header size
	 */
	public static final int EXTENDED_HEADER_SIZE = 10;

	/**
	 * The total size of a sector in bytes.
	 */
	public static final int SIZE = HEADER_SIZE + DATA_SIZE;

	/**
	 * Decodes the specified {@link ByteBuffer} into a {@link Sector} object.
	 * 
	 * @param buf
	 *            The buffer.
	 * @return The sector.
	 */
	public static Sector decode(ByteBuffer buf) {
		if (buf.remaining() != SIZE)
			throw new IllegalArgumentException();

		int id = buf.getShort() & 0xFFFF;
		int chunk = buf.getShort() & 0xFFFF;
		int nextSector = ByteBufferUtils.getMedium(buf);
		int type = buf.get() & 0xFF;
		byte[] data = new byte[DATA_SIZE];
		buf.get(data);

		return new Sector(type, id, chunk, nextSector, data);
	}

	/**
	 * Decodes the specified {@link ByteBuffer} into a {@link Sector} object.
	 * 
	 * @param buf
	 *            The buffer.
	 * @return The sector.
	 */
	public static Sector decodeExtended(ByteBuffer buf) {
		if (buf.remaining() != SIZE)
			throw new IllegalArgumentException();

		int id = buf.getInt();
		int chunk = buf.getShort() & 0xFFFF;
		int nextSector = ByteBufferUtils.getMedium(buf);
		int type = buf.get() & 0xFF;
		byte[] data = new byte[EXTENDED_DATA_SIZE];
		buf.get(data);

		return new Sector(type, id, chunk, nextSector, data);
	}

	/**
	 * The type of file this sector contains.
	 */
	private final int type;

	/**
	 * The id of the file this sector contains.
	 */
	private final int id;

	/**
	 * The chunk within the file that this sector contains.
	 */
	private final int chunk;

	/**
	 * The next sector.
	 */
	private final int nextSector;

	/**
	 * The data in this sector.
	 */
	private final byte[] data;

	/**
	 * Creates a new sector.
	 * 
	 * @param type
	 *            The type of the file.
	 * @param id
	 *            The file's id.
	 * @param chunk
	 *            The chunk of the file this sector contains.
	 * @param nextSector
	 *            The sector containing the next chunk.
	 * @param data
	 *            The data in this sector.
	 */
	public Sector(int type, int id, int chunk, int nextSector, byte[] data) {
		this.type = type;
		this.id = id;
		this.chunk = chunk;
		this.nextSector = nextSector;
		this.data = data;
	}

	/**
	 * Encodes this sector into a {@link ByteBuffer}.
	 * 
	 * @return The encoded buffer.
	 */
	public ByteBuffer encode() {
		ByteBuffer buf = ByteBuffer.allocate(SIZE);
		if (id > 0xFFFF) {
			buf.putInt(id);
		} else {
			buf.putShort((short) id);
		}
		buf.putShort((short) chunk);
		ByteBufferUtils.putMedium(buf, nextSector);
		buf.put((byte) type);
		buf.put(data);

		return (ByteBuffer) buf.flip();
	}

	/**
	 * Gets the chunk of the file this sector contains.
	 * 
	 * @return The chunk of the file this sector contains.
	 */
	public int getChunk() {
		return chunk;
	}

	/**
	 * Gets this sector's data.
	 * 
	 * @return The data within this sector.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Gets the id of the file within this sector.
	 * 
	 * @return The id of the file in this sector.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the next sector.
	 * 
	 * @return The next sector.
	 */
	public int getNextSector() {
		return nextSector;
	}

	/**
	 * Gets the type of file in this sector.
	 * 
	 * @return The type of file in this sector.
	 */
	public int getType() {
		return type;
	}

}
