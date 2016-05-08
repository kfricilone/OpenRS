/**
 * Copyright (c) 2014 RSE Studios
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
package net.openrs.cache.sprite;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.openrs.util.ByteBufferUtils;

/**
 * Represents a {@link Sprite} which may contain one or more frames.
 * 
 * @author Graham
 * @author `Discardedx2
 */
public final class Sprite {

	/**
	 * This flag indicates that the pixels should be read vertically instead of
	 * horizontally.
	 */
	public static final int FLAG_VERTICAL = 0x01;

	/**
	 * This flag indicates that every pixel has an alpha, as well as red, green
	 * and blue, component.
	 */
	public static final int FLAG_ALPHA = 0x02;

	/**
	 * Decodes the {@link Sprite} from the specified {@link ByteBuffer}.
	 * 
	 * @param buffer
	 *            The buffer.
	 * @return The sprite.
	 */
	public static Sprite decode(ByteBuffer buffer) {
		/* find the size of this sprite set */
		buffer.position(buffer.limit() - 2);
		int size = buffer.getShort() & 0xFFFF;

		/* allocate arrays to store info */
		int[] offsetsX = new int[size];
		int[] offsetsY = new int[size];
		int[] subWidths = new int[size];
		int[] subHeights = new int[size];

		/* read the width, height and palette size */
		buffer.position(buffer.limit() - size * 8 - 7);
		int width = buffer.getShort() & 0xFFFF;
		int height = buffer.getShort() & 0xFFFF;
		int[] palette = new int[(buffer.get() & 0xFF) + 1];

		/* and allocate an object for this sprite set */
		Sprite set = new Sprite(width, height, size);

		/* read the offsets and dimensions of the individual sprites */
		for (int i = 0; i < size; i++) {
			offsetsX[i] = buffer.getShort() & 0xFFFF;
		}
		for (int i = 0; i < size; i++) {
			offsetsY[i] = buffer.getShort() & 0xFFFF;
		}
		for (int i = 0; i < size; i++) {
			subWidths[i] = buffer.getShort() & 0xFFFF;
		}
		for (int i = 0; i < size; i++) {
			subHeights[i] = buffer.getShort() & 0xFFFF;
		}

		/* read the palette */
		buffer.position(buffer.limit() - size * 8 - 7 - (palette.length - 1) * 3);
		palette[0] = 0; /* transparent colour (black) */
		for (int index = 1; index < palette.length; index++) {
			palette[index] = ByteBufferUtils.getMedium(buffer);
			if (palette[index] == 0)
				palette[index] = 1;
		}

		/* read the pixels themselves */
		buffer.position(0);
		for (int id = 0; id < size; id++) {
			/* grab some frequently used values */
			int subWidth = subWidths[id], subHeight = subHeights[id];
			int offsetX = offsetsX[id], offsetY = offsetsY[id];

			/* create a BufferedImage to store the resulting image */
			BufferedImage image = set.frames[id] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			/* allocate an array for the palette indices */
			int[][] indices = new int[subWidth][subHeight];

			/*
			 * read the flags so we know whether to read horizontally or
			 * vertically
			 */
			int flags = buffer.get() & 0xFF;

			/* now read the image */
			if (image != null) {
				/* read the palette indices */
				if ((flags & FLAG_VERTICAL) != 0) {
					for (int x = 0; x < subWidth; x++) {
						for (int y = 0; y < subHeight; y++) {
							indices[x][y] = buffer.get() & 0xFF;
						}
					}
				} else {
					for (int y = 0; y < subHeight; y++) {
						for (int x = 0; x < subWidth; x++) {
							indices[x][y] = buffer.get() & 0xFF;
						}
					}
				}

				/*
				 * read the alpha (if there is alpha) and convert values to ARGB
				 */
				if ((flags & FLAG_ALPHA) != 0) {
					if ((flags & FLAG_VERTICAL) != 0) {
						for (int x = 0; x < subWidth; x++) {
							for (int y = 0; y < subHeight; y++) {
								int alpha = buffer.get() & 0xFF;
								image.setRGB(x + offsetX, y + offsetY, alpha << 24 | palette[indices[x][y]]);
							}
						}
					} else {
						for (int y = 0; y < subHeight; y++) {
							for (int x = 0; x < subWidth; x++) {
								int alpha = buffer.get() & 0xFF;
								image.setRGB(x + offsetX, y + offsetY, alpha << 24 | palette[indices[x][y]]);
							}
						}
					}
				} else {
					for (int x = 0; x < subWidth; x++) {
						for (int y = 0; y < subHeight; y++) {
							int index = indices[x][y];
							if (index == 0) {
								image.setRGB(x + offsetX, y + offsetY, 0);
							} else {
								image.setRGB(x + offsetX, y + offsetY, 0xFF000000 | palette[index]);
							}
						}
					}
				}
			}
		}
		return set;
	}

	/**
	 * The width of this sprite.
	 */
	private final int width;

	/**
	 * The height of this sprite.
	 */
	private final int height;

	/**
	 * The array of animation frames in this sprite.
	 */
	private final BufferedImage[] frames;

	/**
	 * Creates a new sprite with one frame.
	 * 
	 * @param width
	 *            The width of the sprite in pixels.
	 * @param height
	 *            The height of the sprite in pixels.
	 */
	public Sprite(int width, int height) {
		this(width, height, 1);
	}

	/**
	 * Creates a new sprite with the specified number of frames.
	 * 
	 * @param width
	 *            The width of the sprite in pixels.
	 * @param height
	 *            The height of the sprite in pixels.
	 * @param size
	 *            The number of animation frames.
	 */
	public Sprite(int width, int height, int size) {
		if (size < 1)
			throw new IllegalArgumentException();

		this.width = width;
		this.height = height;
		this.frames = new BufferedImage[size];
	}

	/**
	 * Encodes this {@link Sprite} into a {@link ByteBuffer}.
	 * <p />
	 * Please note that this is a fairly simple implementation which only
	 * supports vertical encoding. It does not attempt to use the offsets to
	 * save space.
	 * 
	 * @return The buffer.
	 * @throws IOException
	 *             if an I/O exception occurs.
	 */
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream os = new DataOutputStream(bout);
		try {
			/* set up some variables */
			List<Integer> palette = new ArrayList<Integer>();
			palette.add(0); /* transparent colour */

			/* write the sprites */
			for (BufferedImage image : frames) {
				/* check if we can encode this */
				if (image.getWidth() != width || image.getHeight() != height)
					throw new IOException("All frames must be the same size.");

				/* loop through all the pixels constructing a palette */
				int flags = FLAG_VERTICAL; // TODO: do we need to support
											// horizontal encoding?
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						/* grab the colour of this pixel */
						int argb = image.getRGB(x, y);
						int alpha = (argb >> 24) & 0xFF;
						int rgb = argb & 0xFFFFFF;
						if (rgb == 0)
							rgb = 1;

						/* we need an alpha channel to encode this image */
						if (alpha != 0 && alpha != 255)
							flags |= FLAG_ALPHA;

						/*
						 * add the colour to the palette if it isn't already in
						 * the palette
						 */
						if (!palette.contains(rgb)) {
							if (palette.size() >= 256)
								throw new IOException("Too many colours in this sprite!");
							palette.add(rgb);
						}
					}
				}

				/* write this sprite */
				os.write(flags);
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						int argb = image.getRGB(x, y);
						int alpha = (argb >> 24) & 0xFF;
						int rgb = argb & 0xFFFFFF;
						if (rgb == 0)
							rgb = 1;

						if ((flags & FLAG_ALPHA) == 0 && alpha == 0) {
							os.write(0);
						} else {
							os.write(palette.indexOf(rgb));
						}
					}
				}

				/* write the alpha channel if this sprite has one */
				if ((flags & FLAG_ALPHA) != 0) {
					for (int x = 0; x < width; x++) {
						for (int y = 0; y < height; y++) {
							int argb = image.getRGB(x, y);
							int alpha = (argb >> 24) & 0xFF;
							os.write(alpha);
						}
					}
				}
			}

			/* write the palette */
			for (int i = 1; i < palette.size(); i++) {
				int rgb = palette.get(i);
				os.write((byte) (rgb >> 16));
				os.write((byte) (rgb >> 8));
				os.write((byte) rgb);
			}

			/* write the max width, height and palette size */
			os.writeShort(width);
			os.writeShort(height);
			os.write(palette.size() - 1);

			/* write the individual offsets and dimensions */
			for (int i = 0; i < frames.length; i++) {
				os.writeShort(0); // offset X
				os.writeShort(0); // offset Y
				os.writeShort(width);
				os.writeShort(height);
			}

			/* write the number of frames */
			os.writeShort(frames.length);

			/* convert the stream to a byte array and then wrap a buffer */
			byte[] bytes = bout.toByteArray();
			return ByteBuffer.wrap(bytes);
		} finally {
			os.close();
		}
	}

	/**
	 * Gets the frame with the specified id.
	 * 
	 * @param id
	 *            The id.
	 * @return The frame.
	 */
	public BufferedImage getFrame(int id) {
		return frames[id];
	}

	/**
	 * Gets the height of this sprite.
	 * 
	 * @return The height of this sprite.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the width of this sprite.
	 * 
	 * @return The width of this sprite.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the frame with the specified id.
	 * 
	 * @param id
	 *            The id.
	 * @param frame
	 *            The frame.
	 */
	public void setFrame(int id, BufferedImage frame) {
		if (frame.getWidth() != width || frame.getHeight() != height)
			throw new IllegalArgumentException("The frame's dimensions do not match with the sprite's dimensions.");

		frames[id] = frame;
	}

	/**
	 * Gets the number of frames in this set.
	 * 
	 * @return The number of frames.
	 */
	public int size() {
		return frames.length;
	}

}
