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

/**
 * 
 * @author Kyle Friz
 * @since  Apr 2, 2016
 */
public final class Position {

	public static enum RegionSize {
		DEFAULT(104), LARGE(120), XLARGE(136), XXLARGE(168);

		private final int size;

		RegionSize(int size) {
			this.size = size;
		}

		public int getSize() {
			return size;
		}
	}

	private final RegionSize mapSize;

	private final int x;

	private final int y;

	private final int height;

	public Position(int x, int y, int height) {
		this(x, y, height, RegionSize.DEFAULT);
	}
	
	public Position(int x, int y, int height, RegionSize mapSize) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.mapSize = mapSize;
	}

	public Position(int localX, int localY, int height, int regionId, RegionSize mapSize) {
		this(localX + (((regionId >> 8) & 0xFF) << 6), localX
				+ ((regionId & 0xff) << 6), height, mapSize);
	}

	public int getXInRegion() {
		return x & 0x3F;
	}

	public int getYInRegion() {
		return y & 0x3F;
	}

	public int getLocalX() {
		return x - 8 * (getChunkX() - (mapSize.getSize() >> 4));
	}

	public int getLocalY() {
		return y - 8 * (getChunkY() - (mapSize.getSize() >> 4));
	}

	public int getLocalX(Position pos) {
		return x - 8 * (pos.getChunkX() - (mapSize.getSize() >> 4));
	}

	public int getLocalY(Position pos) {
		return y - 8 * (pos.getChunkY() - (mapSize.getSize() >> 4));
	}

	public int getChunkX() {
		return (x >> 3);
	}

	public int getChunkY() {
		return (y >> 3);
	}
	
	public int getRegionX() {
		return (x >> 6);
	}

	public int getRegionY() {
		return (y >> 6);
	}
	
	public int getRegionID() {
		return ((getRegionX() << 8) + getRegionY());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}

	public RegionSize getMapSize() {
		return mapSize;
	}
	
	public int toRegionPacked() {
		return getRegionY() + (getRegionX() << 8) + (height << 16);
	}

	public int toPositionPacked() {
		return y + (x << 14) + (height << 28);
	}
	
	public Position toAbsolute() {
		int xOff = x % 8;
		int yOff = y % 8;
		return new Position(x - xOff, y - yOff, height);
	}
	
	@Override
	public String toString() {
		return new String("X: " + getX() + ", Y: " + getY() + ", Height: " + getHeight());
	}
}
