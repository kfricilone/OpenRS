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
package net.openrs.cache.type.enums;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import net.openrs.cache.type.Type;
import net.openrs.util.ByteBufferUtils;

/**
 * @author Kyle Friz
 * @since Oct 17, 2015
 */
public class EnumType implements Type {

	private final int id;
	private char keyType;
	private char valType;
	private String defaultString = "null";
	private int defaultInt;
	private int size = 0;
	private Map<Integer, Object> params = null;

	public EnumType(int id) {
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
				keyType = (char) (buffer.get() & 0xFF);
			} else if (opcode == 2) {
				valType = (char) (buffer.get() & 0xFF);
			} else if (opcode == 3) {
				defaultString = ByteBufferUtils.getString(buffer);
			} else if (opcode == 4) {
				defaultInt = buffer.getInt();
			} else if (opcode == 5) {
				size = buffer.getShort() & 0xFFFF;
				params = new HashMap<>(size);

				for (int index = 0; index < size; ++index) {
					int key = buffer.getInt();
					String value = ByteBufferUtils.getString(buffer);
					params.put(key, value);
				}
			} else if (opcode == 6) {
				size = buffer.getShort() & 0xFFFF;
				params = new HashMap<>(size);

				for (int index = 0; index < size; ++index) {
					int key = buffer.getInt();
					int value = buffer.getInt();
					params.put(key, value);
				}
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
	 * @return the keyType
	 */
	public char getKeyType() {
		return keyType;
	}

	/**
	 * @param keyType
	 *            the keyType to set
	 */
	public void setKeyType(char keyType) {
		this.keyType = keyType;
	}

	/**
	 * @return the valType
	 */
	public char getValType() {
		return valType;
	}

	/**
	 * @param valType
	 *            the valType to set
	 */
	public void setValType(char valType) {
		this.valType = valType;
	}

	/**
	 * @return the defaultString
	 */
	public String getDefaultString() {
		return defaultString;
	}

	/**
	 * @param defaultString
	 *            the defaultString to set
	 */
	public void setDefaultString(String defaultString) {
		this.defaultString = defaultString;
	}

	/**
	 * @return the defaultInt
	 */
	public int getDefaultInt() {
		return defaultInt;
	}

	/**
	 * @param defaultInt
	 *            the defaultInt to set
	 */
	public void setDefaultInt(int defaultInt) {
		this.defaultInt = defaultInt;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

}
