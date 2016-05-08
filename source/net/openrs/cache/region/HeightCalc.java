/**
 * Copyright (c) 2015 Kyle Friz
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
 * @since Feb 20, 2016
 */
public class HeightCalc {

	private static final int JAGEX_CIRCULAR_ANGLE = 2048;
	private static final double ANGULAR_RATIO = 360D / JAGEX_CIRCULAR_ANGLE;
	private static final double JAGEX_RADIAN = Math.toRadians(ANGULAR_RATIO);

	private static final int[] SIN = new int[JAGEX_CIRCULAR_ANGLE];
	private static final int[] COS = new int[JAGEX_CIRCULAR_ANGLE];

	static {
		for (int i = 0; i < JAGEX_CIRCULAR_ANGLE; i++) {
			SIN[i] = (int) (65536.0D * Math.sin((double) i * JAGEX_RADIAN));
			COS[i] = (int) (65536.0D * Math.cos((double) i * JAGEX_RADIAN));
		}
	}

	static final int calculate(int baseX, int baseY, int x, int y) {
		int xc = (baseX >> 3) + 932638 + x;
		int yc = (baseY >> 3) + 556190 + y;
		int var2 = method494(xc + '\ub135', yc + 91923, 4) - 128
				+ (method494(10294 + xc, yc + '\u93bd', 2) - 128 >> 1) + (method494(xc, yc, 1) - 128 >> 2);
		var2 = 35 + (int) ((double) var2 * 0.3D);
		if (var2 >= 10) {
			if (var2 > 60) {
				var2 = 60;
			}
		} else {
			var2 = 10;
		}

		return var2;
	}

	static final int method494(int var0, int var1, int var2) {
		int var3 = var0 / var2;
		int var8 = var0 & var2 - 1;
		int var4 = var1 / var2;
		int var6 = var1 & var2 - 1;
		int var11 = method197(var3, var4);
		int var10 = method197(var3 + 1, var4);
		int var7 = method197(var3, var4 + 1);
		int var5 = method197(1 + var3, 1 + var4);
		int var12 = method578(var11, var10, var8, var2);
		int var9 = method578(var7, var5, var8, var2);
		return method578(var12, var9, var6, var2);
	}

	static final int method197(int var0, int var1) {
		int var2 = method721(var0 - 1, var1 - 1) + method721(var0 + 1, var1 - 1) + method721(var0 - 1, 1 + var1)
				+ method721(var0 + 1, var1 + 1);
		int var4 = method721(var0 - 1, var1) + method721(1 + var0, var1) + method721(var0, var1 - 1)
				+ method721(var0, 1 + var1);
		int var3 = method721(var0, var1);
		return var3 / 4 + var4 / 8 + var2 / 16;
	}

	static final int method721(int var0, int var1) {
		int var2 = var1 * 57 + var0;
		var2 ^= var2 << 13;
		int var3 = var2 * (789221 + var2 * var2 * 15731) + 1376312589 & Integer.MAX_VALUE;
		return var3 >> 19 & 255;
	}

	static final int method578(int var0, int var1, int var2, int var3) {
		int var4 = 65536 - COS[1024 * var2 / var3] >> 1;
		return (var4 * var1 >> 16) + (var0 * (65536 - var4) >> 16);
	}

}
