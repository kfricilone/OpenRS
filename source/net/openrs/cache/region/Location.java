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
 * @author Kyle Friz
 * @since  Apr 2, 2016
 */
public class Location {

	private final int id;
	private final int type;
	private final int orientation;
	private final Position position;
	
	public Location(int id, int type, int ori, Position pos) {
		this.id = id;
		this.type = type;
		this.orientation = ori;
		this.position = pos;
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	public final int getType() {
		return type;
	}

	/**
	 * @return the orientation
	 */
	public final int getOrientation() {
		return orientation;
	}

	/**
	 * @return the position
	 */
	public final Position getPosition() {
		return position;
	}
	
}
