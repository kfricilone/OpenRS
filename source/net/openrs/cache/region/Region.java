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
package net.openrs.cache.region;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.openrs.util.ByteBufferUtils;

/**
 * 
 * @author Kyle Friz
 * @since  Jun 30, 2015
 */
public class Region {

	private final int regionID;
	private final int baseX;
	private final int baseY;

	private final int[][][] tileHeights = new int[4][104][104];
	private final byte[][][] renderRules = new byte[4][104][104];
	private final byte[][][] overlayIds = new byte[4][104][104];
	private final byte[][][] overlayPaths = new byte[4][104][104];
	private final byte[][][] overlayRotations = new byte[4][104][104];
	private final byte[][][] underlayIds = new byte[4][104][104];
	
	private final List<Location> locations = new ArrayList<>();

	public Region(int id) {
		this.regionID = id;
		this.baseX = (id >> 8 & 0xFF) << 6;
		this.baseY = (id & 0xFF) << 6;
	}

	/**
	 * Decodes terrain data stored in the specified {@link ByteBuffer}.
	 *
	 * @param buffer
	 *            The ByteBuffer.
	 */
	public void loadTerrain(ByteBuffer buf) {
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++) {
					while (true) {
						int attribute = buf.get() & 0xFF;
						if (attribute == 0) {
							if (z == 0) {
								//TODO Verify the height calculation was correctly ripped from client
								tileHeights[0][x][y] = HeightCalc.calculate(baseX, baseY, x, y) << 3;
							} else
								tileHeights[z][x][y] = tileHeights[z - 1][x][y] - 240;

							break;
						} else if (attribute == 1) {
							int height = buf.get() & 0xFF;
							if (height == 1)
								height = 0;

							if (z == 0)
								tileHeights[0][x][y] = -height << 3;
							else
								tileHeights[z][x][y] = tileHeights[z - 1][x][y] - height << 3;

							break;
						} else if (attribute <= 49) {
							overlayIds[z][x][y] = buf.get();
							overlayPaths[z][x][y] = (byte) ((attribute - 2) / 4);
							overlayRotations[z][x][y] = (byte) (attribute - 2 & 0x3);
						} else if (attribute <= 81) {
							renderRules[z][x][y] = (byte) (attribute - 49);
						} else {
							underlayIds[z][x][y] = (byte) (attribute - 81);
						}
					}
				}
			}
		}
	}

	/**
	 * Decodes location data stored in the specified {@link ByteBuffer}.
	 *
	 * @param buffer
	 *            The ByteBuffer.
	 */
	public void loadLocations(ByteBuffer buf) {
		int id = -1;
		int idOffset;

		while ((idOffset = ByteBufferUtils.getUnsignedSmart(buf)) != 0) {
			id += idOffset;

			int position = 0;
			int positionOffset;

			while ((positionOffset = ByteBufferUtils.getUnsignedSmart(buf)) != 0) {
				position += positionOffset - 1;

				int localY = position & 0x3F;
				int localX = position >> 6 & 0x3F;
				int height = position >> 12 & 0x3;

				int attributes = buf.get() & 0xFF;
				int type = attributes >> 2;
				int orientation = attributes & 0x3;

				locations.add(new Location(id, type, orientation, new Position(baseX + localX, baseY + localY, height)));
			}
		}
	}
	
	/**
	 * @return the regionID
	 */
	public final int getRegionID() {
		return regionID;
	}

	/**
	 * @return the baseX
	 */
	public final int getBaseX() {
		return baseX;
	}

	/**
	 * @return the baseY
	 */
	public final int getBaseY() {
		return baseY;
	}

	/**
	 * @return the tileHeights
	 */
	public final int getTileHeight(final int z, final int x, final int y) {
		return tileHeights[z][x][y];
	}

	/**
	 * @return the renderRules
	 */
	public final byte getRenderRule(final int z, final int x, final int y) {
		return renderRules[z][x][y];
	}

	/**
	 * @return the overlayIds
	 */
	public final int getOverlayId(final int z, final int x, final int y) {
		return overlayIds[z][x][y] & 0xFF;
	}

	/**
	 * @return the overlayPaths
	 */
	public final byte getOverlayPath(final int z, final int x, final int y) {
		return overlayPaths[z][x][y];
	}

	/**
	 * @return the overlayRotations
	 */
	public final byte getOverlayRotation(final int z, final int x, final int y) {
		return overlayRotations[z][x][y];
	}

	/**
	 * @return the underlayIds
	 */
	public final int getUnderlayId(final int z, final int x, final int y) {
		return underlayIds[z][x][y] & 0xFF;
	}
	
	/**
	 * @return the locations
	 */
	public final List<Location> getLocations() {
		return locations;
	}
	
	public final String getLocationsIdentifier()
	{
		return "l" + (regionID >> 8) + "_" + (regionID & 0xFF);
	}
	
	public final String getTerrainIdentifier()
	{
		return "m" + (regionID >> 8) + "_" + (regionID & 0xFF);
	}
}
