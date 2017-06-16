/**
 * Copyright (c) OpenRS
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
package net.openrs.util.crypto;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * An implementation of the RSA algorithm.
 * 
 * @author Graham
 * @author `Discardedx2
 */
public final class Rsa {

	/**
	 * Encrypts/decrypts the specified buffer with the key and modulus.
	 * 
	 * @param buffer
	 *            The input buffer.
	 * @param modulus
	 *            The modulus.
	 * @param key
	 *            The key.
	 * @return The output buffer.
	 */
	public static ByteBuffer crypt(ByteBuffer buffer, BigInteger modulus, BigInteger key) {
		byte[] bytes = new byte[buffer.limit()];
		buffer.get(bytes);

		BigInteger in = new BigInteger(bytes);
		BigInteger out = in.modPow(key, modulus);

		return ByteBuffer.wrap(out.toByteArray());
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private Rsa() {

	}

}
