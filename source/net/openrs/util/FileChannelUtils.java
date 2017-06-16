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
package net.openrs.util;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Contains {@link FileChannel}-related utility methods.
 * 
 * @author Graham
 * @author `Discardedx2
 */
public final class FileChannelUtils {

	/**
	 * Reads as much as possible from the channel into the buffer.
	 * 
	 * @param channel
	 *            The channel.
	 * @param buffer
	 *            The buffer.
	 * @param ptr
	 *            The initial position in the channel.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static void readFully(FileChannel channel, ByteBuffer buffer, long ptr) throws IOException {
		while (buffer.remaining() > 0) {
			long read = channel.read(buffer, ptr);
			if (read < -1) {
				throw new EOFException();
			} else {
				ptr += read;
			}
		}
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private FileChannelUtils() {

	}

}
