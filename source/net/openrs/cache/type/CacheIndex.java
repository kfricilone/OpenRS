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
package net.openrs.cache.type;

/**
 * @author Kyle Friz
 * 
 * @since May 27, 2015
 */
public enum CacheIndex {

	BINARY(10), 
	CLIENTSCRIPT(12), 
	CONFIGS(2), 
	FONTS(13), 
	INSTRUMENTS(15), 
	INTERFACES(3), 
	LANDSCAPES(5), 
	MODELS(7), 
	REFERENCE(255), 
	SKELETONS(0), 
	SKINS(1),
	SOUNDEFFECTS(4), 
	SPRITES(8), 
	TEXTURES(9), 
	TRACK1(6), 
	TRACK2(11), 
	VORBIS(14);

	private final int id;

	CacheIndex(int id) {
		this.id = id;
	}

	public final int getID() {
		return id;
	}

}
