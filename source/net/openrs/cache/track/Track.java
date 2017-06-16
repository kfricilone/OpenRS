/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.openrs.cache.track;

import java.nio.ByteBuffer;

import net.openrs.cache.type.Type;
import net.openrs.util.ByteBufferUtils;

/**
 * The class that decodes RuneScape's custom packed music format into raw midi format.
 * 
 * References
 * 
 * https://www.rune-server.ee/runescape-development/rs-503-client-server/snippets/311669-rs-music-file-structure-conversion.html
 * https://github.com/runelite/runelite/blob/master/cache/src/main/java/net/runelite/cache/definitions/loaders/TrackLoader.java
 * 
 * Modified by Freyr
 */
public class Track implements Type {
	
	private byte[] decoded;

	@Override
	public void decode(ByteBuffer buf) {
		buf.position(buf.limit() - 3);
		int tracks = buf.get() & 0xFF;
		int division = buf.getShort() & 0xFFFF;
		int offset = 14 + tracks * 10;
		buf.position(0);
		int tempoOpcodes = 0;
		int ctrlChangeOpcodes = 0;
		int noteOnOpcodes = 0;
		int noteOffOpcodes = 0;
		int wheelChangeOpcodes = 0;
		int chnnlAfterTchOpcodes = 0;
		int keyAfterTchOpcodes = 0;
		int progmChangeOpcodes = 0;

		int var13;
		int opcode;
		int var15;
		for (var13 = 0; var13 < tracks; ++var13) {
			opcode = -1;

			while (true) {
				var15 = buf.get() & 0xFF;
				if (var15 != opcode) {
					++offset;
				}

				opcode = var15 & 15;
				if (var15 == 7) {
					break;
				}

				if (var15 == 23) {
					++tempoOpcodes;
				} else if (opcode == 0) {
					++noteOnOpcodes;
				} else if (opcode == 1) {
					++noteOffOpcodes;
				} else if (opcode == 2) {
					++ctrlChangeOpcodes;
				} else if (opcode == 3) {
					++wheelChangeOpcodes;
				} else if (opcode == 4) {
					++chnnlAfterTchOpcodes;
				} else if (opcode == 5) {
					++keyAfterTchOpcodes;
				} else {
					if (opcode != 6) {
						throw new RuntimeException();
					}

					++progmChangeOpcodes;
				}
			}
		}

		offset += 5 * tempoOpcodes;
		offset += 2 * (noteOnOpcodes + noteOffOpcodes + ctrlChangeOpcodes + wheelChangeOpcodes + keyAfterTchOpcodes);
		offset += chnnlAfterTchOpcodes + progmChangeOpcodes;
		var13 = buf.position();
		opcode = tracks + tempoOpcodes + ctrlChangeOpcodes + noteOnOpcodes + noteOffOpcodes + wheelChangeOpcodes
				+ chnnlAfterTchOpcodes + keyAfterTchOpcodes + progmChangeOpcodes;

		for (var15 = 0; var15 < opcode; ++var15) {
			ByteBufferUtils.getVarInt(buf);
		}

		offset += buf.position() - var13;
		var15 = buf.position();
		int var16 = 0;
		int var17 = 0;
		int var18 = 0;
		int var19 = 0;
		int var20 = 0;
		int var21 = 0;
		int var22 = 0;
		int var23 = 0;
		int var24 = 0;
		int var25 = 0;
		int var26 = 0;
		int var27 = 0;
		int var28 = 0;

		int var29;
		for (var29 = 0; var29 < ctrlChangeOpcodes; ++var29) {
			var28 = var28 + (buf.get() & 0xFF) & 127;
			if (var28 != 0 && var28 != 32) {
				if (var28 == 1) {
					++var16;
				} else if (var28 == 33) {
					++var17;
				} else if (var28 == 7) {
					++var18;
				} else if (var28 == 39) {
					++var19;
				} else if (var28 == 10) {
					++var20;
				} else if (var28 == 42) {
					++var21;
				} else if (var28 == 99) {
					++var22;
				} else if (var28 == 98) {
					++var23;
				} else if (var28 == 101) {
					++var24;
				} else if (var28 == 100) {
					++var25;
				} else if (var28 != 64 && var28 != 65 && var28 != 120 && var28 != 121 && var28 != 123) {
					++var27;
				} else {
					++var26;
				}
			} else {
				++progmChangeOpcodes;
			}
		}

		var29 = 0;

		int var30 = buf.position();
		ByteBufferUtils.skip(buf, var26);

		int var31 = buf.position();
		ByteBufferUtils.skip(buf, keyAfterTchOpcodes);

		int var32 = buf.position();
		ByteBufferUtils.skip(buf, chnnlAfterTchOpcodes);

		int var33 = buf.position();
		ByteBufferUtils.skip(buf, wheelChangeOpcodes);

		int var34 = buf.position();
		ByteBufferUtils.skip(buf, var16);

		int var35 = buf.position();
		ByteBufferUtils.skip(buf, var18);

		int var36 = buf.position();
		ByteBufferUtils.skip(buf, var20);

		int var37 = buf.position();
		ByteBufferUtils.skip(buf, noteOnOpcodes + noteOffOpcodes + keyAfterTchOpcodes);

		int var38 = buf.position();
		ByteBufferUtils.skip(buf, noteOnOpcodes);

		int var39 = buf.position();
		ByteBufferUtils.skip(buf, var27);

		int var40 = buf.position();
		ByteBufferUtils.skip(buf, noteOffOpcodes);

		int var41 = buf.position();
		ByteBufferUtils.skip(buf, var17);

		int var42 = buf.position();
		ByteBufferUtils.skip(buf, var19);

		int var43 = buf.position();
		ByteBufferUtils.skip(buf, var21);

		int var44 = buf.position();
		ByteBufferUtils.skip(buf, progmChangeOpcodes);

		int var45 = buf.position();
		ByteBufferUtils.skip(buf, wheelChangeOpcodes);

		int var46 = buf.position();
		ByteBufferUtils.skip(buf, var22);

		int var47 = buf.position();
		ByteBufferUtils.skip(buf, var23);

		int var48 = buf.position();
		ByteBufferUtils.skip(buf, var24);

		int var49 = buf.position();
		ByteBufferUtils.skip(buf, var25);

		int var50 = buf.position();
		ByteBufferUtils.skip(buf, tempoOpcodes * 3);

		ByteBuffer midiBuff = ByteBuffer.allocate(offset + 1);		

		midiBuff.putInt(1297377380); // MThd header
		midiBuff.putInt(6); // length of header
		midiBuff.putShort((short) (tracks > 1 ? 1 : 0)); // format
		midiBuff.putShort((short) tracks); // tracks
		midiBuff.putShort((short) division); // division
		
		buf.position(var13);	
		
		int var52 = 0;
		int var53 = 0;
		int var54 = 0;
		int var55 = 0;
		int var56 = 0;
		int var57 = 0;
		int var58 = 0;
		int[] var59 = new int[128];
		var28 = 0;

		label361: for (int var60 = 0; var60 < tracks; ++var60) {
			midiBuff.putInt(1297379947); // MTrk
			ByteBufferUtils.skip(midiBuff, 4); // length gets written here later
			int var61 = midiBuff.position();
			int var62 = -1;

			while (true) {
				int var63 = ByteBufferUtils.getVarInt(buf);

				ByteBufferUtils.putVarInt(midiBuff, var63); // delta time

				int var64 = buf.array()[var29++] & 255;
				boolean var65 = var64 != var62;
				var62 = var64 & 15;
				if (var64 == 7) {
					// if (var65) -- client has this if, but it causes broken
					// midi to be produced
					{
						midiBuff.put((byte) 255);
					}

					midiBuff.put((byte) 47); // type - end of track
					midiBuff.put((byte) 0); // length
					ByteBufferUtils.putLengthFromMark(midiBuff, midiBuff.position() - var61);
					continue label361;
				}

				if (var64 == 23) {
					// if (var65) -- client has this if, but it causes broken
					// midi to be produced
					{
						midiBuff.put((byte) 255); // meta event FF
					}

					midiBuff.put((byte) 81); // type - set tempo
					midiBuff.put((byte) 3); // length
					midiBuff.put((byte) buf.array()[var50++]);
					midiBuff.put((byte) buf.array()[var50++]);
					midiBuff.put((byte) buf.array()[var50++]);
				} else {
					var52 ^= var64 >> 4;
					if (var62 == 0) {
						if (var65) {
							midiBuff.put((byte) (144 + var52));
						}

						var53 += buf.array()[var37++];
						var54 += buf.array()[var38++];
						midiBuff.put((byte) (var53 & 127));
						midiBuff.put((byte) (var54 & 127));
					} else if (var62 == 1) {
						if (var65) {
							midiBuff.put((byte) (128 + var52));
						}

						var53 += buf.array()[var37++];
						var55 += buf.array()[var40++];
						midiBuff.put((byte) (var53 & 127));
						midiBuff.put((byte) (var55 & 127));
					} else if (var62 == 2) {
						if (var65) {
							midiBuff.put((byte) (176 + var52));
						}

						var28 = var28 + buf.array()[var15++] & 127;
						midiBuff.put((byte) var28);
						byte var66;
						if (var28 != 0 && var28 != 32) {
							if (var28 == 1) {
								var66 = buf.array()[var34++];
							} else if (var28 == 33) {
								var66 = buf.array()[var41++];
							} else if (var28 == 7) {
								var66 = buf.array()[var35++];
							} else if (var28 == 39) {
								var66 = buf.array()[var42++];
							} else if (var28 == 10) {
								var66 = buf.array()[var36++];
							} else if (var28 == 42) {
								var66 = buf.array()[var43++];
							} else if (var28 == 99) {
								var66 = buf.array()[var46++];
							} else if (var28 == 98) {
								var66 = buf.array()[var47++];
							} else if (var28 == 101) {
								var66 = buf.array()[var48++];
							} else if (var28 == 100) {
								var66 = buf.array()[var49++];
							} else if (var28 != 64 && var28 != 65 && var28 != 120 && var28 != 121 && var28 != 123) {
								var66 = buf.array()[var39++];
							} else {
								var66 = buf.array()[var30++];
							}
						} else {
							var66 = buf.array()[var44++];
						}

						int var67 = var66 + var59[var28];
						var59[var28] = var67;
						midiBuff.put((byte) (var67 & 127));
					} else if (var62 == 3) {
						if (var65) {
							midiBuff.put((byte) (224 + var52));
						}

						var56 += buf.array()[var45++];
						var56 += buf.array()[var33++] << 7;
						midiBuff.put((byte) (var56 & 127));
						midiBuff.put((byte) (var56 >> 7 & 127));
					} else if (var62 == 4) {
						if (var65) {
							midiBuff.put((byte) (208 + var52));
						}

						var57 += buf.array()[var32++];
						midiBuff.put((byte) (var57 & 127));
					} else if (var62 == 5) {
						if (var65) {
							midiBuff.put((byte) (160 + var52));
						}

						var53 += buf.array()[var37++];
						var58 += buf.array()[var31++];
						midiBuff.put((byte) (var53 & 127));
						midiBuff.put((byte) (var58 & 127));
					} else {
						if (var62 != 6) {
							throw new RuntimeException();
						}

						if (var65) {
							midiBuff.put((byte) (192 + var52));
						}

						midiBuff.put((byte) buf.array()[var44++]);
					}
				}
			}
		}

		midiBuff.flip();
		
		this.decoded = midiBuff.array();		
	}

	@Override
	public ByteBuffer encode() {
		throw new UnsupportedOperationException("This has not been added yet.");
	}

	@Override
	public ByteBuffer encode317() {
		throw new UnsupportedOperationException("This has not been added yet.");
	}

	@Override
	public int getID() {
		return 0;
	}

	public byte[] getDecoded() {
		return decoded;
	}

}

