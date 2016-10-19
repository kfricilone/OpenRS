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
package net.openrs.cache.type.objects;

import java.nio.ByteBuffer;

import net.openrs.cache.type.Type;
import net.openrs.util.ByteBufferUtils;

/**
 * @author Kyle Friz
 * @since Oct 18, 2015
 */
public class ObjectType implements Type {

	private final int id;
	private short[] retextureToFind;
	private int anInt2069 = -1652411312;
	private boolean isSolid = false;
	private String name = "null";
	private int[] objectModels;
	private int[] objectTypes;
	private short[] recolorToFind;
	private int mapFunctionID = -1;
	private short[] textureToReplace;
	private int sizeX = 160821065;
	private int sizeY = 1617841409;
	private int anInt2083 = 0;
	private int[] anIntArray2084;
	private int offsetX = 0;
	private boolean nonFlatShading = false;
	private int anInt2088 = -1212747031;
	private int animationID = 706875959;
	private int varpID = -155117661;
	private int ambient = 0;
	private int contrast = 0;
	private String[] actions = new String[5];
	private int anInt2094 = 385819046;
	private int mapSceneID = -1;
	private short[] recolorToReplace;
	private boolean aBool2097 = true;
	private int modelSizeX = 1552764032;
	private int modelSizeHeight = 636925568;
	private int modelSizeY = 408014720;
	private int objectID;
	private int offsetHeight = 0;
	private int offsetY = 0;
	private boolean aBool2104 = false;
	private int anInt2105 = 359794785;
	private int anInt2106 = -1443463903;
	private int[] configChangeDest;
	private boolean aBool2108 = false;
	private int configId = -1070845109;
	private int anInt2110 = 2019882883;
	private boolean aBool2111 = false;
	private int anInt2112 = 0;
	private int anInt2113 = 0;
	private boolean aBool2114 = true;

	public ObjectType(int id) {
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
				int length = buffer.get() & 0xFF;
				if (length > 0) {
					this.objectTypes = new int[length];
					this.objectModels = new int[length];

					for (int index = 0; index < length; ++index) {
						this.objectModels[index] = buffer.getShort() & 0xFFFF;
						this.objectTypes[index] = buffer.get() & 0xFF;
					}
				}
			} else if (opcode == 2) {
				this.name = ByteBufferUtils.getString(buffer);
			} else if (opcode == 5) {
				int length = buffer.get() & 0xFF;
				if (length > 0) {
					this.objectTypes = null;
					this.objectModels = new int[length];

					for (int index = 0; index < length; ++index) {
						this.objectModels[index] = buffer.getShort() & 0xFFFF;
					}
				}
			} else if (opcode == 14) {
				this.sizeX = buffer.get() & 0xFF;
			} else if (15 == opcode) {
				this.sizeY = buffer.get() & 0xFF;
			} else if (opcode == 17) {
				this.anInt2094 = 0;
				this.aBool2114 = false;
			} else if (opcode == 18) {
				this.aBool2114 = false;
			} else if (opcode == 19) {
				this.anInt2088 = buffer.get() & 0xFF;
			} else if (opcode == 21) {
				this.anInt2105 = 0;
			} else if (22 == opcode) {
				this.nonFlatShading = true;
			} else if (opcode == 23) {
				this.aBool2111 = true;
			} else if (24 == opcode) {
				this.animationID = buffer.getShort() & 0xFFFF;
				if (this.animationID == 0xFFFF) {
					this.animationID = -1;
				}
			} else if (opcode == 27) {
				this.anInt2094 = 192909523;
			} else if (opcode == 28) {
				this.anInt2069 = buffer.get() & 0xFF;
			} else if (opcode == 29) {
				this.ambient = buffer.get();
			} else if (opcode == 39) {
				this.contrast = buffer.get();
			} else if (opcode >= 30 && opcode < 35) {
				this.actions[opcode - 30] = ByteBufferUtils.getString(buffer);
				if (this.actions[opcode - 30].equalsIgnoreCase("Hidden")) {
					this.actions[opcode - 30] = null;
				}
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
				this.textureToReplace = new short[length];

				for (int index = 0; index < length; ++index) {
					this.retextureToFind[index] = (short) (buffer.getShort() & 0xFFFF);
					this.textureToReplace[index] = (short) (buffer.getShort() & 0xFFFF);
				}

			} else if (opcode == 60) {
				this.mapFunctionID = buffer.getShort() & 0xFFFF;
			} else if (62 == opcode) {
				this.aBool2108 = true;
			} else if (opcode == 64) {
				this.aBool2097 = false;
			} else if (opcode == 65) {
				this.modelSizeX = buffer.getShort() & 0xFFFF;
			} else if (opcode == 66) {
				this.modelSizeHeight = buffer.getShort() & 0xFFFF;
			} else if (67 == opcode) {
				this.modelSizeY = buffer.getShort() & 0xFFFF;
			} else if (opcode == 68) {
				this.mapSceneID = buffer.getShort() & 0xFFFF;
			} else if (opcode == 69) {
				buffer.get();
			} else if (70 == opcode) {
				this.offsetX = buffer.getShort() & 0xFFFF;
			} else if (opcode == 71) {
				this.offsetHeight = buffer.getShort() & 0xFFFF;
			} else if (opcode == 72) {
				this.offsetY = buffer.getShort() & 0xFFFF;
			} else if (73 == opcode) {
				this.aBool2104 = true;
			} else if (74 == opcode) {
				this.isSolid = true;
			} else if (opcode == 75) {
				this.anInt2106 = buffer.get() & 0xFF;
			} else if (opcode == 77) {
				this.varpID = buffer.getShort() & 0xFFFF;
				if (this.varpID == 0xFFFF) {
					this.varpID = -1;
				}

				this.configId = buffer.getShort() & 0xFFFF;
				if (this.configId == 0xFFFF) {
					this.configId = -1;
				}

				int length = buffer.get() & 0xFF;
				this.configChangeDest = new int[length + 1];

				for (int index = 0; index <= length; ++index) {
					this.configChangeDest[index] = buffer.getShort() & 0xFFFF;
					if (0xFFFF == this.configChangeDest[index]) {
						this.configChangeDest[index] = -1;
					}
				}

			} else if (opcode == 78) {
				this.anInt2110 = buffer.getShort() & 0xFFFF;
				this.anInt2083 = buffer.get() & 0xFF;
			} else if (opcode == 79) {
				this.anInt2112 = buffer.getShort() & 0xFFFF;
				this.anInt2113 = buffer.getShort() & 0xFFFF;
				this.anInt2083 = buffer.get() & 0xFF;
				int length = buffer.get() & 0xFF;
				this.anIntArray2084 = new int[length];

				for (int index = 0; index < length; ++index) {
					this.anIntArray2084[index] = buffer.getShort() & 0xFFFF;
				}
			} else if (opcode == 81) {
				this.anInt2105 = buffer.get() & 0xFF;
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
	 * @return the anInt2069
	 */
	public int getAnInt2069() {
		return anInt2069;
	}

	/**
	 * @param anInt2069
	 *            the anInt2069 to set
	 */
	public void setAnInt2069(int anInt2069) {
		this.anInt2069 = anInt2069;
	}

	/**
	 * @return the isSolid
	 */
	public boolean isSolid() {
		return isSolid;
	}

	/**
	 * @param isSolid
	 *            the isSolid to set
	 */
	public void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the objectModels
	 */
	public int[] getObjectModels() {
		return objectModels;
	}

	/**
	 * @param objectModels
	 *            the objectModels to set
	 */
	public void setObjectModels(int[] objectModels) {
		this.objectModels = objectModels;
	}

	/**
	 * @return the objectTypes
	 */
	public int[] getObjectTypes() {
		return objectTypes;
	}

	/**
	 * @param objectTypes
	 *            the objectTypes to set
	 */
	public void setObjectTypes(int[] objectTypes) {
		this.objectTypes = objectTypes;
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
	 * @return the mapIconID
	 */
	public int getMapFunctionID() {
		return mapFunctionID;
	}

	/**
	 * @param mapFunctionID
	 *            the mapFunctionID to set
	 */
	public void setMapFunctionID(int mapFunctionID) {
		this.mapFunctionID = mapFunctionID;
	}

	/**
	 * @return the textureToReplace
	 */
	public short[] getTextureToReplace() {
		return textureToReplace;
	}

	/**
	 * @param textureToReplace
	 *            the textureToReplace to set
	 */
	public void setTextureToReplace(short[] textureToReplace) {
		this.textureToReplace = textureToReplace;
	}

	/**
	 * @return the sizeX
	 */
	public int getSizeX() {
		return sizeX;
	}

	/**
	 * @param sizeX
	 *            the sizeX to set
	 */
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	/**
	 * @return the sizeY
	 */
	public int getSizeY() {
		return sizeY;
	}

	/**
	 * @param sizeY
	 *            the sizeY to set
	 */
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	/**
	 * @return the anInt2083
	 */
	public int getAnInt2083() {
		return anInt2083;
	}

	/**
	 * @param anInt2083
	 *            the anInt2083 to set
	 */
	public void setAnInt2083(int anInt2083) {
		this.anInt2083 = anInt2083;
	}

	/**
	 * @return the anIntArray2084
	 */
	public int[] getAnIntArray2084() {
		return anIntArray2084;
	}

	/**
	 * @param anIntArray2084
	 *            the anIntArray2084 to set
	 */
	public void setAnIntArray2084(int[] anIntArray2084) {
		this.anIntArray2084 = anIntArray2084;
	}

	/**
	 * @return the offsetX
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * @param offsetX
	 *            the offsetX to set
	 */
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * @return the nonFlatShading
	 */
	public boolean isNonFlatShading() {
		return nonFlatShading;
	}

	/**
	 * @param nonFlatShading
	 *            the nonFlatShading to set
	 */
	public void setNonFlatShading(boolean nonFlatShading) {
		this.nonFlatShading = nonFlatShading;
	}

	/**
	 * @return the anInt2088
	 */
	public int getAnInt2088() {
		return anInt2088;
	}

	/**
	 * @param anInt2088
	 *            the anInt2088 to set
	 */
	public void setAnInt2088(int anInt2088) {
		this.anInt2088 = anInt2088;
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
	 * @return the varpID
	 */
	public int getVarpID() {
		return varpID;
	}

	/**
	 * @param varpID
	 *            the varpID to set
	 */
	public void setVarpID(int varpID) {
		this.varpID = varpID;
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

	/**
	 * @return the actions
	 */
	public String[] getActions() {
		return actions;
	}

	/**
	 * @param actions
	 *            the actions to set
	 */
	public void setActions(String[] actions) {
		this.actions = actions;
	}

	/**
	 * @return the anInt2094
	 */
	public int getAnInt2094() {
		return anInt2094;
	}

	/**
	 * @param anInt2094
	 *            the anInt2094 to set
	 */
	public void setAnInt2094(int anInt2094) {
		this.anInt2094 = anInt2094;
	}

	/**
	 * @return the mapSceneID
	 */
	public int getMapSceneID() {
		return mapSceneID;
	}

	/**
	 * @param mapSceneID
	 *            the mapSceneID to set
	 */
	public void setMapSceneID(int mapSceneID) {
		this.mapSceneID = mapSceneID;
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
	 * @return the aBool2097
	 */
	public boolean isaBool2097() {
		return aBool2097;
	}

	/**
	 * @param aBool2097
	 *            the aBool2097 to set
	 */
	public void setaBool2097(boolean aBool2097) {
		this.aBool2097 = aBool2097;
	}

	/**
	 * @return the modelSizeX
	 */
	public int getModelSizeX() {
		return modelSizeX;
	}

	/**
	 * @param modelSizeX
	 *            the modelSizeX to set
	 */
	public void setModelSizeX(int modelSizeX) {
		this.modelSizeX = modelSizeX;
	}

	/**
	 * @return the modelSizeHeight
	 */
	public int getModelSizeHeight() {
		return modelSizeHeight;
	}

	/**
	 * @param modelSizeHeight
	 *            the modelSizeHeight to set
	 */
	public void setModelSizeHeight(int modelSizeHeight) {
		this.modelSizeHeight = modelSizeHeight;
	}

	/**
	 * @return the modelSizeY
	 */
	public int getModelSizeY() {
		return modelSizeY;
	}

	/**
	 * @param modelSizeY
	 *            the modelSizeY to set
	 */
	public void setModelSizeY(int modelSizeY) {
		this.modelSizeY = modelSizeY;
	}

	/**
	 * @return the objectID
	 */
	public int getObjectID() {
		return objectID;
	}

	/**
	 * @param objectID
	 *            the objectID to set
	 */
	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}

	/**
	 * @return the offsetHeight
	 */
	public int getOffsetHeight() {
		return offsetHeight;
	}

	/**
	 * @param offsetHeight
	 *            the offsetHeight to set
	 */
	public void setOffsetHeight(int offsetHeight) {
		this.offsetHeight = offsetHeight;
	}

	/**
	 * @return the offsetY
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * @param offsetY
	 *            the offsetY to set
	 */
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	/**
	 * @return the aBool2104
	 */
	public boolean isaBool2104() {
		return aBool2104;
	}

	/**
	 * @param aBool2104
	 *            the aBool2104 to set
	 */
	public void setaBool2104(boolean aBool2104) {
		this.aBool2104 = aBool2104;
	}

	/**
	 * @return the anInt2105
	 */
	public int getAnInt2105() {
		return anInt2105;
	}

	/**
	 * @param anInt2105
	 *            the anInt2105 to set
	 */
	public void setAnInt2105(int anInt2105) {
		this.anInt2105 = anInt2105;
	}

	/**
	 * @return the anInt2106
	 */
	public int getAnInt2106() {
		return anInt2106;
	}

	/**
	 * @param anInt2106
	 *            the anInt2106 to set
	 */
	public void setAnInt2106(int anInt2106) {
		this.anInt2106 = anInt2106;
	}

	/**
	 * @return the configChangeDest
	 */
	public int[] getConfigChangeDest() {
		return configChangeDest;
	}

	/**
	 * @param configChangeDest
	 *            the configChangeDest to set
	 */
	public void setConfigChangeDest(int[] configChangeDest) {
		this.configChangeDest = configChangeDest;
	}

	/**
	 * @return the aBool2108
	 */
	public boolean isaBool2108() {
		return aBool2108;
	}

	/**
	 * @param aBool2108
	 *            the aBool2108 to set
	 */
	public void setaBool2108(boolean aBool2108) {
		this.aBool2108 = aBool2108;
	}

	/**
	 * @return the configId
	 */
	public int getConfigId() {
		return configId;
	}

	/**
	 * @param configId
	 *            the configId to set
	 */
	public void setConfigId(int configId) {
		this.configId = configId;
	}

	/**
	 * @return the anInt2110
	 */
	public int getAnInt2110() {
		return anInt2110;
	}

	/**
	 * @param anInt2110
	 *            the anInt2110 to set
	 */
	public void setAnInt2110(int anInt2110) {
		this.anInt2110 = anInt2110;
	}

	/**
	 * @return the aBool2111
	 */
	public boolean isaBool2111() {
		return aBool2111;
	}

	/**
	 * @param aBool2111
	 *            the aBool2111 to set
	 */
	public void setaBool2111(boolean aBool2111) {
		this.aBool2111 = aBool2111;
	}

	/**
	 * @return the anInt2112
	 */
	public int getAnInt2112() {
		return anInt2112;
	}

	/**
	 * @param anInt2112
	 *            the anInt2112 to set
	 */
	public void setAnInt2112(int anInt2112) {
		this.anInt2112 = anInt2112;
	}

	/**
	 * @return the anInt2113
	 */
	public int getAnInt2113() {
		return anInt2113;
	}

	/**
	 * @param anInt2113
	 *            the anInt2113 to set
	 */
	public void setAnInt2113(int anInt2113) {
		this.anInt2113 = anInt2113;
	}

	/**
	 * @return the aBool2114
	 */
	public boolean isaBool2114() {
		return aBool2114;
	}

	/**
	 * @param aBool2114
	 *            the aBool2114 to set
	 */
	public void setaBool2114(boolean aBool2114) {
		this.aBool2114 = aBool2114;
	}

}
