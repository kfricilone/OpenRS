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
package net.openrs.cache.type.identkits;

import java.nio.ByteBuffer;

import net.openrs.cache.type.Type;

/**
 * @author Kyle Friz
 * 
 * @since Jun 11, 2015
 */
public class IdentkitType implements Type {

	private final int id;
	private int[] modelIds;
	private short[] retextureToFind;
	private short[] recolorToReplace;
	private short[] recolorToFind;
	private int bodyPartId = -1;
	private short[] retextureToReplace;
	private int[] models = new int[] { -1, -1, -1, -1, -1 };
	private boolean nonSelectable = false;

	public IdentkitType(int id) {
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
				this.bodyPartId = buffer.get() & 0xFF;
			} else if (opcode == 2) {
				int length = buffer.get() & 0xFF;
				this.modelIds = new int[length];

				for (int index = 0; index < length; ++index) {
					this.modelIds[index] = buffer.getShort() & 0xFFFF;
				}
			} else if (opcode == 3) {
				this.nonSelectable = true;
			} else if (40 == opcode) {
				int length = buffer.get() & 0xFFFF;
				this.recolorToFind = new short[length];
				this.recolorToReplace = new short[length];

				for (int index = 0; index < length; ++index) {
					this.recolorToFind[index] = (short) (buffer.getShort() & 0xFFFF);
					this.recolorToReplace[index] = (short) (buffer.getShort() & 0xFFFF);
				}
			} else if (41 == opcode) {
				int length = buffer.get() & 0xFF;
				this.retextureToFind = new short[length];
				this.retextureToReplace = new short[length];

				for (int index = 0; index < length; ++index) {
					this.retextureToFind[index] = (short) (buffer.getShort() & 0xFFFF);
					this.retextureToReplace[index] = (short) (buffer.getShort());
				}
			} else if (opcode >= 60 && opcode < 70) {
				this.models[opcode - 60] = buffer.getShort() & 0xFFFF;
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
	 * @return the modelIds
	 */
	public int[] getModelIds() {
		return modelIds;
	}

	/**
	 * @return the retextureToFind
	 */
	public short[] getRetextureToFind() {
		return retextureToFind;
	}

	/**
	 * @return the recolorToReplace
	 */
	public short[] getRecolorToReplace() {
		return recolorToReplace;
	}

	/**
	 * @return the recolorToFind
	 */
	public short[] getRecolorToFind() {
		return recolorToFind;
	}

	/**
	 * @return the bodyPartId
	 */
	public int getBodyPartId() {
		return bodyPartId;
	}

	/**
	 * @return the retextureToReplace
	 */
	public short[] getRetextureToReplace() {
		return retextureToReplace;
	}

	/**
	 * @return the models
	 */
	public int[] getModels() {
		return models;
	}

	/**
	 * @return the nonSelectable
	 */
	public boolean isNonSelectable() {
		return nonSelectable;
	}

}
