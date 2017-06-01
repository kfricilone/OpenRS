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
package net.openrs.cache.type.items;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import net.openrs.cache.type.Type;
import net.openrs.util.ByteBufferUtils;

/**
 * @author Kyle Friz
 * 
 * @since May 27, 2015
 */
public class ItemType implements Type {

	private int ambient;
	private int boughtLink;
	private int boughtTemplate;
	private short[] colorFind;
	private short[] colorReplace;
	private int contrast;
	private int cost = 1;
	private int[] countCo;
	private int[] countObj;
	private int femaleHeadModel;
	private int femaleHeadModel2;
	private int femaleModel0;
	private int femaleModel1;
	private int femaleModel2;
	private int femaleOffset;
	private final int id;
	private String[] interfaceOptions = new String[5];
	private int inventoryModel;
	private int maleHeadModel;
	private int maleHeadModel2;
	private int maleModel0 = -1;
	private int maleModel1;
	private int maleModel2;
	private int maleOffset;
	private boolean members = false;
	private String name = "null";
	private int notedID;
	private int notedTemplate;
	private String[] options = new String[5];
	private int resizeX = 128;
	private int resizeY = 128;
	private int resizeZ = 128;
	private int stackable = 0;
	private boolean stockMarket;
	private int team;
	private short[] textureFind;
	private short[] textureReplace;
	private int xan2d = 0;
	private int xOffset2d = 0;
	private int yan2d = 0;
	private int yOffset2d = 0;
	private int zan2d = 0;
	private int zoom2d = 2000;
	private int anInt1879;
	private int anInt1833;
	private int anInt2173 = -2;
	private Map<Integer, Object> params = null;
	
	public ItemType(int id) {
		this.id = id;
	}

	@Override
	public void decode(ByteBuffer buffer) {
		while (true) {
			int opcode = buffer.get() & 0xFF;
			if (opcode == 0)
				return;

			if (opcode == 1) {
				inventoryModel = buffer.getShort() & 0xFFFF;
			} else if (opcode == 2) {
				name = ByteBufferUtils.getString(buffer);
			} else if (opcode == 4) {
				zoom2d = buffer.getShort() & 0xFFFF;
			} else if (opcode == 5) {
				xan2d = buffer.getShort() & 0xFFFF;
			} else if (opcode == 6) {
				yan2d = buffer.getShort() & 0xFFFF;
			} else if (7 == opcode) {
				xOffset2d = buffer.getShort() & 0xFFFF;
				if (xOffset2d > 32767) {
					xOffset2d -= 65536;
				}
			} else if (8 == opcode) {
				yOffset2d = buffer.getShort() & 0xFFFF;
				if (yOffset2d > 32767) {
					yOffset2d -= 65536;
				}
			} else if (11 == opcode) {
				stackable = 1;
			} else if (opcode == 12) {
				cost = buffer.getInt();
			} else if (16 == opcode) {
				members = true;
			} else if (opcode == 23) {
				maleModel0 = buffer.getShort() & 0xFFFF;
				maleOffset = buffer.get() & 0xFF;
			} else if (opcode == 24) {
				maleModel1 = buffer.getShort() & 0xFFFF;
			} else if (25 == opcode) {
				femaleModel0 = buffer.getShort() & 0xFFFF;
				femaleOffset = buffer.get() & 0xFF;
			} else if (26 == opcode) {
				femaleModel1 = buffer.getShort() & 0xFFFF;
			} else if (opcode >= 30 && opcode < 35) {
				options[opcode - 30] = ByteBufferUtils.getString(buffer);
				if (options[opcode - 30].equalsIgnoreCase("Hidden")) {
					options[opcode - 30] = null;
				}
			} else if (opcode >= 35 && opcode < 40) {
				interfaceOptions[opcode - 35] = ByteBufferUtils.getString(buffer);
			} else if (opcode == 40) {
				int length = buffer.get() & 0xFF;
				colorFind = new short[length];
				colorReplace = new short[length];

				for (int idx = 0; idx < length; ++idx) {
					colorFind[idx] = (short) (buffer.getShort() & 0xFFFF);
					colorReplace[idx] = (short) (buffer.getShort() & 0xFFFF);
				}

			} else if (opcode == 41) {
				int length = buffer.get() & 0xFF;
				textureFind = new short[length];
				textureReplace = new short[length];

				for (int idx = 0; idx < length; ++idx) {
					textureFind[idx] = (short) (buffer.getShort() & 0xFFFF);
					textureReplace[idx] = (short) (buffer.getShort() & 0xFFFF);
				}
			} else if(opcode == 42) {
				anInt2173 = buffer.get() & 0xFF;
			} else if (opcode == 65) {
				stockMarket = true;
			} else if (opcode == 78) {
				maleModel2 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 79) {
				femaleModel2 = buffer.getShort() & 0xFFFF;
			} else if (90 == opcode) {
				maleHeadModel = buffer.getShort() & 0xFFFF;
			} else if (91 == opcode) {
				femaleHeadModel = buffer.getShort() & 0xFFFF;
			} else if (92 == opcode) {
				maleHeadModel2 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 93) {
				femaleHeadModel2 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 95) {
				zan2d = buffer.getShort() & 0xFFFF;
			} else if (97 == opcode) {
				notedID = buffer.getShort() & 0xFFFF;
			} else if (98 == opcode) {
				notedTemplate = buffer.getShort() & 0xFFFF;
			} else if (opcode >= 100 && opcode < 110) {
				if (countObj == null) {
					countObj = new int[10];
					countCo = new int[10];
				}

				countObj[opcode - 100] = buffer.getShort() & 0xFFFF;
				countCo[opcode - 100] = buffer.getShort() & 0xFFFF;
			} else if (110 == opcode) {
				resizeX = buffer.getShort() & 0xFFFF;
			} else if (opcode == 111) {
				resizeY = buffer.getShort() & 0xFFFF;
			} else if (opcode == 112) {
				resizeZ = buffer.getShort() & 0xFFFF;
			} else if (opcode == 113) {
				ambient = buffer.get();
			} else if (114 == opcode) {
				contrast = buffer.get();
			} else if (115 == opcode) {
				team = buffer.get() & 0xFF;
			} else if (opcode == 139) {
				boughtLink = buffer.getShort() & 0xFFFF;
			} else if (opcode == 140) {
				boughtTemplate = buffer.getShort() & 0xFFFF;
			} else if (opcode == 148) {
				anInt1879 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 149) {
				anInt1833 = buffer.getShort() & 0xFFFF;
			} else if (opcode == 249) {
				int length = buffer.get() & 0xFF;

				params = new HashMap<>(length);
				for (int i = 0; i < length; i++) {
					boolean isString = (buffer.get() & 0xFF) == 1;
					int key = ByteBufferUtils.getMedium(buffer);
					Object value;

					if (isString) {
						value = ByteBufferUtils.getString(buffer);
					}

					else {
						value = buffer.getInt();
					}

					params.put(key, value);
				}
			}
		}
	}

	@Override
	public ByteBuffer encode() {
		ByteBuffer buffer = ByteBuffer.allocate(1132);
		return (ByteBuffer) buffer.flip();
	}

	@Override
	public ByteBuffer encode317() {
		ByteBuffer buffer = ByteBuffer.allocate(1132);
		return (ByteBuffer) buffer.flip();
	}

	/**
	 * @return the ambient
	 */
	public int getAmbient() {
		return ambient;
	}

	/**
	 * @return the boughtlink
	 */
	public int getBoughtLink() {
		return boughtLink;
	}

	/**
	 * @return the boughttemplate
	 */
	public int getBoughtTemplate() {
		return boughtTemplate;
	}

	/**
	 * @return the colorFind
	 */
	public short[] getColorFind() {
		return colorFind;
	}

	/**
	 * @return the colorReplace
	 */
	public short[] getColorReplace() {
		return colorReplace;
	}

	/**
	 * @return the contrast
	 */
	public int getContrast() {
		return contrast;
	}

	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @return the countCo
	 */
	public int[] getCountCo() {
		return countCo;
	}

	/**
	 * @return the countObj
	 */
	public int[] getCountObj() {
		return countObj;
	}

	/**
	 * @return the femaleHeadModel
	 */
	public int getFemaleHeadModel() {
		return femaleHeadModel;
	}

	/**
	 * @return the femaleHeadModel2
	 */
	public int getFemaleHeadModel2() {
		return femaleHeadModel2;
	}

	/**
	 * @return the femaleModel0
	 */
	public int getFemaleModel0() {
		return femaleModel0;
	}

	/**
	 * @return the femaleModel1
	 */
	public int getFemaleModel1() {
		return femaleModel1;
	}

	/**
	 * @return the femaleModel2
	 */
	public int getFemaleModel2() {
		return femaleModel2;
	}

	/**
	 * @return the femaleOffset
	 */
	public int getFemaleOffset() {
		return femaleOffset;
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
	 * @return the interfaceOptions
	 */
	public String[] getInterfaceOptions() {
		return interfaceOptions;
	}

	/**
	 * @return the inventoryModel
	 */
	public int getInventoryModel() {
		return inventoryModel;
	}

	/**
	 * @return the maleHeadModel
	 */
	public int getMaleHeadModel() {
		return maleHeadModel;
	}

	/**
	 * @return the maleHeadModel2
	 */
	public int getMaleHeadModel2() {
		return maleHeadModel2;
	}

	/**
	 * @return the maleModel0
	 */
	public int getMaleModel0() {
		return maleModel0;
	}

	/**
	 * @return the maleModel1
	 */
	public int getMaleModel1() {
		return maleModel1;
	}

	/**
	 * @return the maleModel2
	 */
	public int getMaleModel2() {
		return maleModel2;
	}

	/**
	 * @return the maleOffset
	 */
	public int getMaleOffset() {
		return maleOffset;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the notedID
	 */
	public int getNotedID() {
		return notedID;
	}

	/**
	 * @return the notedTemplate
	 */
	public int getNotedTemplate() {
		return notedTemplate;
	}

	/**
	 * @return the options
	 */
	public String[] getOptions() {
		return options;
	}

	/**
	 * @return the resizeX
	 */
	public int getResizeX() {
		return resizeX;
	}

	/**
	 * @return the resizeY
	 */
	public int getResizeY() {
		return resizeY;
	}

	/**
	 * @return the resizeZ
	 */
	public int getResizeZ() {
		return resizeZ;
	}

	/**
	 * @return the team
	 */
	public int getTeam() {
		return team;
	}

	/**
	 * @return the textureFind
	 */
	public short[] getTextureFind() {
		return textureFind;
	}

	/**
	 * @return the textureReplace
	 */
	public short[] getTextureReplace() {
		return textureReplace;
	}

	/**
	 * @return the xan2d
	 */
	public int getXan2d() {
		return xan2d;
	}

	/**
	 * @return the xOffset2d
	 */
	public int getxOffset2d() {
		return xOffset2d;
	}

	/**
	 * @return the yan2d
	 */
	public int getYan2d() {
		return yan2d;
	}

	/**
	 * @return the yOffset2d
	 */
	public int getyOffset2d() {
		return yOffset2d;
	}

	/**
	 * @return the zan2d
	 */
	public int getZan2d() {
		return zan2d;
	}

	/**
	 * @return the zoom2d
	 */
	public int getZoom2d() {
		return zoom2d;
	}

	/**
	 * @return the members
	 */
	public boolean isMembers() {
		return members;
	}

	/**
	 * @return the stackable
	 */
	public boolean isStackable() {
		return stackable == 1;
	}

	/**
	 * @return the stockMarket
	 */
	public boolean isStockMarket() {
		return stockMarket;
	}

	public int getId() {
		return id;
	}

	public int getStackable() {
		return stackable;
	}

	public int getAnInt1879() {
		return anInt1879;
	}

	public int getAnInt1833() {
		return anInt1833;
	}
}
