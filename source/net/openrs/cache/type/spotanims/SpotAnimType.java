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
package net.openrs.cache.type.spotanims;

import java.nio.ByteBuffer;

import net.openrs.cache.type.Type;

/**
 * @author Kyle Friz
 * @since Oct 18, 2015
 */
public class SpotAnimType implements Type {

	private final int id;
	private int resizeX = 1063384192;
	private short[] retextureToFind;
	private short[] recolorToReplace;
	private int animationID = -2018612393;
	private short[] recolorToFind;
	private int ambient = 0;
	private short[] retextureToReplace;
	private int gfxID;
	private int resizeY = 1782475648;
	private int rotation = 0;
	private int modelID;
	private int contrast = 0;

	public SpotAnimType(int id) {
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

			if (1 == opcode) {
				this.modelID = buffer.getShort() & 0xFFFF;
			} else if (opcode == 2) {
				this.animationID = buffer.getShort() & 0xFFFF;
			} else if (opcode == 4) {
				this.resizeX = buffer.getShort() & 0xFFFF;
			} else if (opcode == 5) {
				this.resizeY = buffer.getShort() & 0xFFFF;
			} else if (opcode == 6) {
				this.rotation = buffer.getShort() & 0xFFFF;
			} else if (7 == opcode) {
				this.ambient = buffer.get() & 0xFF;
			} else if (8 == opcode) {
				this.contrast = buffer.get() & 0xFF;
			} else if (opcode == 40) {
				int length = buffer.get() & 0xFF;
				this.recolorToFind = new short[length];
				this.recolorToReplace = new short[length];

				for (int index = 0; index < length; ++index) {
					this.recolorToFind[index] = (short) (buffer.getShort() & 0xFFFF);
					this.recolorToReplace[index] = (short) (buffer.getShort() & 0xFFFF);
				}

			} else if (opcode == 41) {
				int length = buffer.get() & 0xFF;
				this.retextureToFind = new short[length];
				this.retextureToReplace = new short[length];

				for (int index = 0; index < length; ++index) {
					this.retextureToFind[index] = (short) (buffer.getShort() & 0xFFFF);
					this.retextureToReplace[index] = (short) (buffer.getShort() & 0xFFFF);
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
		ByteBuffer buffer = ByteBuffer.allocate(39);
		return (ByteBuffer) buffer.flip();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#encode317()
	 */
	@Override
	public ByteBuffer encode317() {
		ByteBuffer buffer = ByteBuffer.allocate(39);
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
	 * @return the resizeX
	 */
	public int getResizeX() {
		return resizeX;
	}

	/**
	 * @param resizeX
	 *            the resizeX to set
	 */
	public void setResizeX(int resizeX) {
		this.resizeX = resizeX;
	}

	/**
	 * @return the retextureToFind
	 */
	public short[] getRetextureToFind() {
		return retextureToFind;
	}

	/**
	 * @param retextureToFind
	 *            the retextureToFind to set
	 */
	public void setRetextureToFind(short[] retextureToFind) {
		this.retextureToFind = retextureToFind;
	}

	/**
	 * @return the recolorToReplace
	 */
	public short[] getRecolorToReplace() {
		return recolorToReplace;
	}

	/**
	 * @param recolorToReplace
	 *            the recolorToReplace to set
	 */
	public void setRecolorToReplace(short[] recolorToReplace) {
		this.recolorToReplace = recolorToReplace;
	}

	/**
	 * @return the animationID
	 */
	public int getAnimationID() {
		return animationID;
	}

	/**
	 * @param animationID
	 *            the animationID to set
	 */
	public void setAnimationID(int animationID) {
		this.animationID = animationID;
	}

	/**
	 * @return the recolorToFind
	 */
	public short[] getRecolorToFind() {
		return recolorToFind;
	}

	/**
	 * @param recolorToFind
	 *            the recolorToFind to set
	 */
	public void setRecolorToFind(short[] recolorToFind) {
		this.recolorToFind = recolorToFind;
	}

	/**
	 * @return the ambient
	 */
	public int getAmbient() {
		return ambient;
	}

	/**
	 * @param ambient
	 *            the ambient to set
	 */
	public void setAmbient(int ambient) {
		this.ambient = ambient;
	}

	/**
	 * @return the retextureToReplace
	 */
	public short[] getRetextureToReplace() {
		return retextureToReplace;
	}

	/**
	 * @param retextureToReplace
	 *            the retextureToReplace to set
	 */
	public void setRetextureToReplace(short[] retextureToReplace) {
		this.retextureToReplace = retextureToReplace;
	}

	/**
	 * @return the gfxID
	 */
	public int getGfxID() {
		return gfxID;
	}

	/**
	 * @param gfxID
	 *            the gfxID to set
	 */
	public void setGfxID(int gfxID) {
		this.gfxID = gfxID;
	}

	/**
	 * @return the resizeY
	 */
	public int getResizeY() {
		return resizeY;
	}

	/**
	 * @param resizeY
	 *            the resizeY to set
	 */
	public void setResizeY(int resizeY) {
		this.resizeY = resizeY;
	}

	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the modelID
	 */
	public int getModelID() {
		return modelID;
	}

	/**
	 * @param modelID
	 *            the modelID to set
	 */
	public void setModelID(int modelID) {
		this.modelID = modelID;
	}

	/**
	 * @return the contrast
	 */
	public int getContrast() {
		return contrast;
	}

	/**
	 * @param contrast
	 *            the contrast to set
	 */
	public void setContrast(int contrast) {
		this.contrast = contrast;
	}

}
