package net.openrs.model;

import java.nio.ByteBuffer;

import net.openrs.util.ByteBufferUtils;

public class Model {

	int id;
	
	int vertexCount = 0;
	int faceCount = 0;
	int anInt1745;
	int[] vertexX;
	int[] vertexY;
	int[] vertexZ;
	int[] triangleX;
	int[] triangleY;
	int[] triangleZ;
	byte[] textureRenderTypes;
	short[] texTriX;
	short[] texTriY;
	short[] texTriZ;
	int[] vertexSkins;
	byte[] faceType;
	byte[] texturePos;
	short[] faceTextures;
	byte priority = 0;
	byte[] facePriorities;
	byte[] faceAlphas;
	int[] skinValues;
	short[] faceColor;
	boolean aBool1775 = false;
	short[] aShortArray1750;
	short[] aShortArray1751;
	short[] aShortArray1767;
	short[] aShortArray1768;
	byte[] aByteArray1735;
	short[] aShortArray1754;
	short[] texPrimaryColor;
	int[][] anIntArrayArray1759;
	int[][] anIntArrayArray1760;
	public short aShort1764;
	public short aShort1766;
	static int anInt1774;
	int anInt1757;
	int anInt1742;
	int anInt1769;
	int anInt1765;
	int anInt1771;

	public Model(final int id) {
		this.id = id;
	}
	
	public void decode(final ByteBuffer buf) {
		if (buf.getShort(buf.limit() - 2) == -1) {
			this.decodeNewFormat(buf);
		} else {
			this.decodeOldFormat(buf);
		}
	}
	
	void decodeOldFormat(final ByteBuffer buf) {
		boolean var2 = false;
		boolean var9 = false;
		
		ByteBuffer var5 = ByteBufferUtils.clone(buf);
		ByteBuffer var6 = ByteBufferUtils.clone(buf);
		ByteBuffer var7 = ByteBufferUtils.clone(buf);
		ByteBuffer var8 = ByteBufferUtils.clone(buf);
		ByteBuffer var38 = ByteBufferUtils.clone(buf);
		
		var5.position(buf.limit() - 18);
		
		int var10 = var5.getShort() & 0xFFFF;
		int var11 = var5.getShort() & 0xFFFF;
		int var12 = var5.get() & 0xFF;
		int var42 = var5.get() & 0xFF;
		int var15 = var5.get() & 0xFF;
		int var16 = var5.get() & 0xFF;
		int var25 = var5.get() & 0xFF;
		int var17 = var5.get() & 0xFF;
		int var18 = var5.getShort() & 0xFFFF;
		int var19 = var5.getShort() & 0xFFFF;
		var5.getShort();
		int var21 = var5.getShort() & 0xFFFF;
		
		byte var13 = 0;
		int var46 = var13 + var10;
		int var24 = var46;
		var46 += var11;
		int var14 = var46;
		if (var15 == 255) {
			var46 += var11;
		}

		int var26 = var46;
		if (var25 == 1) {
			var46 += var11;
		}

		int var22 = var46;
		if (var42 == 1) {
			var46 += var11;
		}

		int var28 = var46;
		if (var17 == 1) {
			var46 += var10;
		}

		int var30 = var46;
		if (var16 == 1) {
			var46 += var11;
		}

		int var39 = var46;
		var46 += var21;
		int var34 = var46;
		var46 += var11 * 2;
		int var35 = var46;
		var46 += var12 * 6;
		int var36 = var46;
		var46 += var18;
		int var41 = var46;
		var46 += var19;
		
		this.vertexCount = var10;
		this.faceCount = var11;
		this.anInt1745 = var12;
		this.vertexX = new int[var10];
		this.vertexY = new int[var10];
		this.vertexZ = new int[var10];
		this.triangleX = new int[var11];
		this.triangleY = new int[var11];
		this.triangleZ = new int[var11];
		
		if (var12 > 0) {
			this.textureRenderTypes = new byte[var12];
			this.texTriX = new short[var12];
			this.texTriY = new short[var12];
			this.texTriZ = new short[var12];
		}

		if (var17 == 1) {
			this.vertexSkins = new int[var10];
		}

		if (var42 == 1) {
			this.faceType = new byte[var11];
			this.texturePos = new byte[var11];
			this.faceTextures = new short[var11];
		}

		if (var15 == 255) {
			this.facePriorities = new byte[var11];
		} else {
			this.priority = (byte) var15;
		}

		if (var16 == 1) {
			this.faceAlphas = new byte[var11];
		}

		if (var25 == 1) {
			this.skinValues = new int[var11];
		}

		this.faceColor = new short[var11];
		
		var5.position(var13);
		var6.position(var36);
		var7.position(var41);
		var8.position(var46);
		var38.position(var28);
		
		int var29 = 0;
		int var32 = 0;
		int var43 = 0;

		int var3;
		int var4;
		int var23;
		int var27;
		int var33;
		for (var23 = 0; var23 < var10; var23++) {
			var27 = var5.get() & 0xFF;
			var4 = 0;
			if ((var27 & 0x1) != 0) {
				var4 = ByteBufferUtils.getSignedSmart(var6);
			}

			var33 = 0;
			if ((var27 & 0x2) != 0) {
				var33 = ByteBufferUtils.getSignedSmart(var7);
			}

			var3 = 0;
			if ((var27 & 0x4) != 0) {
				var3 = ByteBufferUtils.getSignedSmart(var8);
			}

			this.vertexX[var23] = var29 + var4;
			this.vertexY[var23] = var32 + var33;
			this.vertexZ[var23] = var43 + var3;
			var29 = this.vertexX[var23];
			var32 = this.vertexY[var23];
			var43 = this.vertexZ[var23];
			if (var17 == 1) {
				this.vertexSkins[var23] = var38.get() & 0xFF;
			}
		}

		var5.position(var34);
		var6.position(var22);
		var7.position(var14);
		var8.position(var30);
		var38.position(var26);

		for (var23 = 0; var23 < var11; var23++) {
			this.faceColor[var23] = (short) (var5.getShort() & 0xFFFF);
			if (var42 == 1) {
				var27 = var6.get() & 0xFF;
				if ((var27 & 0x1) == 1) {
					this.faceType[var23] = 1;
					var2 = true;
				} else {
					this.faceType[var23] = 0;
				}

				if ((var27 & 0x2) == 2) {
					this.texturePos[var23] = (byte) (var27 >> 2);
					this.faceTextures[var23] = this.faceColor[var23];
					this.faceColor[var23] = 127;
					if (this.faceTextures[var23] != -1) {
						var9 = true;
					}
				} else {
					this.texturePos[var23] = -1;
					this.faceTextures[var23] = -1;
				}
			}

			if (var15 == 255) {
				this.facePriorities[var23] = var7.get();
			}

			if (var16 == 1) {
				this.faceAlphas[var23] = var8.get();
			}

			if (var25 == 1) {
				this.skinValues[var23] = var38.get() & 0xFF;
			}
		}

		var5.position(var39);
		var6.position(var24);
		
		var23 = 0;
		var27 = 0;
		var4 = 0;
		var33 = 0;

		int var31;
		int var44;
		for (var3 = 0; var3 < var11; var3++) {
			var31 = var6.get() & 0xFF;
			if (var31 == 1) {
				var23 = ByteBufferUtils.getSignedSmart(var5) + var33;
				var27 = ByteBufferUtils.getSignedSmart(var5) + var23;
				var4 = ByteBufferUtils.getSignedSmart(var5) + var27;
				var33 = var4;
				this.triangleX[var3] = var23;
				this.triangleY[var3] = var27;
				this.triangleZ[var3] = var4;
			}

			if (var31 == 2) {
				var27 = var4;
				var4 = ByteBufferUtils.getSignedSmart(var5) + var33;
				var33 = var4;
				this.triangleX[var3] = var23;
				this.triangleY[var3] = var27;
				this.triangleZ[var3] = var4;
			}

			if (var31 == 3) {
				var23 = var4;
				var4 = ByteBufferUtils.getSignedSmart(var5) + var33;
				var33 = var4;
				this.triangleX[var3] = var23;
				this.triangleY[var3] = var27;
				this.triangleZ[var3] = var4;
			}

			if (var31 == 4) {
				var44 = var23;
				var23 = var27;
				var27 = var44;
				var4 = ByteBufferUtils.getSignedSmart(var5) + var33;
				var33 = var4;
				this.triangleX[var3] = var23;
				this.triangleY[var3] = var44;
				this.triangleZ[var3] = var4;
			}
		}

		var5.position(var35);

		for (var3 = 0; var3 < var12; var3++) {
			this.textureRenderTypes[var3] = 0;
			this.texTriX[var3] = (short) (var5.getShort() & 0xFFFF);
			this.texTriY[var3] = (short) (var5.getShort() & 0xFFFF);
			this.texTriZ[var3] = (short) (var5.getShort() & 0xFFFF);
		}

		if (this.texturePos != null) {
			boolean var45 = false;

			for (var31 = 0; var31 < var11; var31++) {
				var44 = this.texturePos[var31] & 0xff;
				if (var44 != 255) {
					if ((this.texTriX[var44] & 0xffff) == this.triangleX[var31]
							&& (this.texTriY[var44] & 0xffff) == this.triangleY[var31]
							&& (this.texTriZ[var44] & 0xffff) == this.triangleZ[var31]) {
						this.texturePos[var31] = -1;
					} else {
						var45 = true;
					}
				}
			}

			if (!var45) {
				this.texturePos = null;
			}
		}

		if (!var9) {
			this.faceTextures = null;
		}

		if (!var2) {
			this.faceType = null;
		}

	}

	void decodeNewFormat(final ByteBuffer buf) {
		ByteBuffer var2 = ByteBufferUtils.clone(buf);
		ByteBuffer var9 = ByteBufferUtils.clone(buf);
		ByteBuffer var33 = ByteBufferUtils.clone(buf);
		ByteBuffer var49 = ByteBufferUtils.clone(buf);
		ByteBuffer var7 = ByteBufferUtils.clone(buf);
		ByteBuffer var8 = ByteBufferUtils.clone(buf);
		ByteBuffer var46 = ByteBufferUtils.clone(buf);
		
		var2.position(buf.limit() - 23);
		
		int numVertices = var2.getShort() & 0xFFFF;
		int numTriangles = var2.getShort() & 0xFFFF;
		int numTextureTriangles = var2.get() & 0xFF;
		int var13 = var2.get() & 0xFF;
		int modelPriority = var2.get() & 0xFF;
		int var16 = var2.get() & 0xFF;
		int var4 = var2.get() & 0xFF;
		int texture = var2.get() & 0xFF;
		int modelSkins = var2.get() & 0xFF;
		int var20 = var2.getShort() & 0xFFFF;
		int var21 = var2.getShort() & 0xFFFF;
		int var22 = var2.getShort() & 0xFFFF;
		int var23 = var2.getShort() & 0xFFFF;
		int var24 = var2.getShort() & 0xFFFF;
		
		int textureCount = 0;
		int var3 = 0;
		int var26 = 0;
		int var5;
		if (numTextureTriangles > 0) {
			this.textureRenderTypes = new byte[numTextureTriangles];
			var2.position(0);

			for (var5 = 0; var5 < numTextureTriangles; var5++) {
				byte type = this.textureRenderTypes[var5] = var2.get();
				if (type == 0) {
					++textureCount;
				}

				if (type >= 1 && type <= 3) {
					++var3;
				}

				if (type == 2) {
					++var26;
				}
			}
		}

		var5 = numTextureTriangles + numVertices;
		int var561 = var5;
		if (var13 == 1) {
			var5 += numTriangles;
		}

		int var35 = var5;
		var5 += numTriangles;
		int var56 = var5;
		if (modelPriority == 255) {
			var5 += numTriangles;
		}

		int var37 = var5;
		if (var4 == 1) {
			var5 += numTriangles;
		}

		int var48 = var5;
		if (modelSkins == 1) {
			var5 += numVertices;
		}

		int var39 = var5;
		if (var16 == 1) {
			var5 += numTriangles;
		}

		int var40 = var5;
		var5 += var23;
		int var57 = var5;
		if (texture == 1) {
			var5 += numTriangles * 2;
		}

		int var42 = var5;
		var5 += var24;
		int var50 = var5;
		var5 += numTriangles * 2;
		int var10 = var5;
		var5 += var20;
		int var51 = var5;
		var5 += var21;
		int var6 = var5;
		var5 += var22;
		int var43 = var5;
		var5 += textureCount * 6;
		int var52 = var5;
		var5 += var3 * 6;
		int var44 = var5;
		var5 += var3 * 6;
		int var54 = var5;
		var5 += var3 * 2;
		int var45 = var5;
		var5 += var3;
		int var47 = var5;
		var5 += var3 * 2 + var26 * 2;
		
		this.vertexCount = numVertices;
		this.faceCount = numTriangles;
		this.anInt1745 = numTextureTriangles;
		this.vertexX = new int[numVertices];
		this.vertexY = new int[numVertices];
		this.vertexZ = new int[numVertices];
		this.triangleX = new int[numTriangles];
		this.triangleY = new int[numTriangles];
		this.triangleZ = new int[numTriangles];
		
		if (modelSkins == 1) {
			this.vertexSkins = new int[numVertices];
		}

		if (var13 == 1) {
			this.faceType = new byte[numTriangles];
		}

		if (modelPriority == 255) {
			this.facePriorities = new byte[numTriangles];
		} else {
			this.priority = (byte) modelPriority;
		}

		if (var16 == 1) {
			this.faceAlphas = new byte[numTriangles];
		}

		if (var4 == 1) {
			this.skinValues = new int[numTriangles];
		}

		if (texture == 1) {
			this.faceTextures = new short[numTriangles];
		}

		if (texture == 1 && numTextureTriangles > 0) {
			this.texturePos = new byte[numTriangles];
		}

		this.faceColor = new short[numTriangles];
		if (numTextureTriangles > 0) {
			this.texTriX = new short[numTextureTriangles];
			this.texTriY = new short[numTextureTriangles];
			this.texTriZ = new short[numTextureTriangles];
			if (var3 > 0) {
				this.aShortArray1750 = new short[var3];
				this.aShortArray1751 = new short[var3];
				this.aShortArray1767 = new short[var3];
				this.aShortArray1768 = new short[var3];
				this.aByteArray1735 = new byte[var3];
				this.aShortArray1754 = new short[var3];
			}

			if (var26 > 0) {
				this.texPrimaryColor = new short[var26];
			}
		}

		var2.position(numTextureTriangles);
		var9.position(var10);
		var33.position(var51);
		var49.position(var6);
		var7.position(var48);
		
		int var53 = 0;
		int var28 = 0;
		int var34 = 0;

		int vertetxOffsetZ;
		int var15;
		int vertextOffsetY;
		int flags;
		int vertextOffsetX;
		for (var15 = 0; var15 < numVertices; var15++) {
			flags = var2.get() & 0xFF;
			vertextOffsetX = 0;
			if ((flags & 0x1) != 0) {
				vertextOffsetX = ByteBufferUtils.getSignedSmart(var9);
			}

			vertextOffsetY = 0;
			if ((flags & 0x2) != 0) {
				vertextOffsetY = ByteBufferUtils.getSignedSmart(var33);
			}

			vertetxOffsetZ = 0;
			if ((flags & 0x4) != 0) {
				vertetxOffsetZ = ByteBufferUtils.getSignedSmart(var49);
			}

			this.vertexX[var15] = var53 + vertextOffsetX;
			this.vertexY[var15] = var28 + vertextOffsetY;
			this.vertexZ[var15] = var34 + vertetxOffsetZ;
			var53 = this.vertexX[var15];
			var28 = this.vertexY[var15];
			var34 = this.vertexZ[var15];
			if (modelSkins == 1) {
				this.vertexSkins[var15] = var7.get() & 0xFF;
			}
		}

		var2.position(var50);
		var9.position(var561);
		var33.position(var56);
		var49.position(var39);
		var7.position(var37);
		var8.position(var57);
		var46.position(var42);

		for (var15 = 0; var15 < numTriangles; var15++) {
			this.faceColor[var15] = (short) (var2.getShort() & 0xFFFF);
			if (var13 == 1) {
				this.faceType[var15] = var9.get();
			}

			if (modelPriority == 255) {
				this.facePriorities[var15] = var33.get();
			}

			if (var16 == 1) {
				this.faceAlphas[var15] = var49.get();
			}

			if (var4 == 1) {
				this.skinValues[var15] = var7.get() & 0xFF;
			}

			if (texture == 1) {
				this.faceTextures[var15] = (short) (var8.getShort() & 0xFFFF - 1);
			}

			if (this.texturePos != null && this.faceTextures[var15] != -1) {
				this.texturePos[var15] = (byte) (var46.get() & 0xFF - 1);
			}
		}

		var2.position(var40);
		var9.position(var35);
		var15 = 0;
		flags = 0;
		vertextOffsetX = 0;
		vertextOffsetY = 0;

		int var32;
		for (vertetxOffsetZ = 0; vertetxOffsetZ < numTriangles; vertetxOffsetZ++) {
			var32 = var9.get() & 0xFF;
			if (var32 == 1) {
				var15 = ByteBufferUtils.getSignedSmart(var2) + vertextOffsetY;
				flags = ByteBufferUtils.getSignedSmart(var2) + var15;
				vertextOffsetX = ByteBufferUtils.getSignedSmart(var2) + flags;
				vertextOffsetY = vertextOffsetX;
				this.triangleX[vertetxOffsetZ] = var15;
				this.triangleY[vertetxOffsetZ] = flags;
				this.triangleZ[vertetxOffsetZ] = vertextOffsetX;
			}

			if (var32 == 2) {
				flags = vertextOffsetX;
				vertextOffsetX = ByteBufferUtils.getSignedSmart(var2) + vertextOffsetY;
				vertextOffsetY = vertextOffsetX;
				this.triangleX[vertetxOffsetZ] = var15;
				this.triangleY[vertetxOffsetZ] = flags;
				this.triangleZ[vertetxOffsetZ] = vertextOffsetX;
			}

			if (var32 == 3) {
				var15 = vertextOffsetX;
				vertextOffsetX = ByteBufferUtils.getSignedSmart(var2) + vertextOffsetY;
				vertextOffsetY = vertextOffsetX;
				this.triangleX[vertetxOffsetZ] = var15;
				this.triangleY[vertetxOffsetZ] = flags;
				this.triangleZ[vertetxOffsetZ] = vertextOffsetX;
			}

			if (var32 == 4) {
				int var41 = var15;
				var15 = flags;
				flags = var41;
				vertextOffsetX = ByteBufferUtils.getSignedSmart(var2) + vertextOffsetY;
				vertextOffsetY = vertextOffsetX;
				this.triangleX[vertetxOffsetZ] = var15;
				this.triangleY[vertetxOffsetZ] = var41;
				this.triangleZ[vertetxOffsetZ] = vertextOffsetX;
			}
		}

		var2.position(var43);
		var9.position(var52);
		var33.position(var44);
		var49.position(var54);
		var7.position(var45);
		var8.position(var47);

		for (vertetxOffsetZ = 0; vertetxOffsetZ < numTextureTriangles; vertetxOffsetZ++) {
			var32 = this.textureRenderTypes[vertetxOffsetZ] & 0xff;
			if (var32 == 0) {
				this.texTriX[vertetxOffsetZ] = (short) (var2.getShort() & 0xFFFF);
				this.texTriY[vertetxOffsetZ] = (short) (var2.getShort() & 0xFFFF);
				this.texTriZ[vertetxOffsetZ] = (short) (var2.getShort() & 0xFFFF);
			}

			if (var32 == 1) {
				this.texTriX[vertetxOffsetZ] = (short) (var9.getShort() & 0xFFFF);
				this.texTriY[vertetxOffsetZ] = (short) (var9.getShort() & 0xFFFF);
				this.texTriZ[vertetxOffsetZ] = (short) (var9.getShort() & 0xFFFF);
				this.aShortArray1750[vertetxOffsetZ] = (short) (var33.getShort() & 0xFFFF);
				this.aShortArray1751[vertetxOffsetZ] = (short) (var33.getShort() & 0xFFFF);
				this.aShortArray1767[vertetxOffsetZ] = (short) (var33.getShort() & 0xFFFF);
				this.aShortArray1768[vertetxOffsetZ] = (short) (var49.getShort() & 0xFFFF);
				this.aByteArray1735[vertetxOffsetZ] = var7.get();
				this.aShortArray1754[vertetxOffsetZ] = (short) (var8.getShort() & 0xFFFF);
			}

			if (var32 == 2) {
				this.texTriX[vertetxOffsetZ] = (short) (var9.getShort() & 0xFFFF);
				this.texTriY[vertetxOffsetZ] = (short) (var9.getShort() & 0xFFFF);
				this.texTriZ[vertetxOffsetZ] = (short) (var9.getShort() & 0xFFFF);
				this.aShortArray1750[vertetxOffsetZ] = (short) (var33.getShort() & 0xFFFF);
				this.aShortArray1751[vertetxOffsetZ] = (short) (var33.getShort() & 0xFFFF);
				this.aShortArray1767[vertetxOffsetZ] = (short) (var33.getShort() & 0xFFFF);
				this.aShortArray1768[vertetxOffsetZ] = (short) (var49.getShort() & 0xFFFF);
				this.aByteArray1735[vertetxOffsetZ] = var7.get();
				this.aShortArray1754[vertetxOffsetZ] = (short) (var8.getShort() & 0xFFFF);
				this.texPrimaryColor[vertetxOffsetZ] = (short) (var8.getShort() & 0xFFFF);
			}

			if (var32 == 3) {
				this.texTriX[vertetxOffsetZ] = (short) (var9.getShort() & 0xFFFF);
				this.texTriY[vertetxOffsetZ] = (short) (var9.getShort() & 0xFFFF);
				this.texTriZ[vertetxOffsetZ] = (short) (var9.getShort() & 0xFFFF);
				this.aShortArray1750[vertetxOffsetZ] = (short) (var33.getShort() & 0xFFFF);
				this.aShortArray1751[vertetxOffsetZ] = (short) (var33.getShort() & 0xFFFF);
				this.aShortArray1767[vertetxOffsetZ] = (short) (var33.getShort() & 0xFFFF);
				this.aShortArray1768[vertetxOffsetZ] = (short) (var49.getShort() & 0xFFFF);
				this.aByteArray1735[vertetxOffsetZ] = var7.get();
				this.aShortArray1754[vertetxOffsetZ] = (short) (var8.getShort() & 0xFFFF);
			}
		}

		var2.position(var5);
		vertetxOffsetZ = var2.get() & 0xFF;
		if (vertetxOffsetZ != 0) {
			//new Class25();
			var2.getShort();
			var2.getShort();
			var2.getShort();
			var2.getInt();
		}

	}

}
