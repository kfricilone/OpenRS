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
package net.openrs.cache.type.sequences;

import java.nio.ByteBuffer;

import net.openrs.cache.type.Type;
import net.openrs.util.ByteBufferUtils;

/**
 * @author Kyle Friz
 * @since Oct 18, 2015
 */
public class SequenceType implements Type {

	private final int id;
	private int[] anIntArray2118;
	private int priority = 1109376177;
	private int[] frameIDs;
	private int[] frameLengths;
	private int[] anIntArray2126;
	private int frameStep = -1338171561;
	private int[] animationFlowControl;
	private boolean oneSquareAnimation = false;
	private int forcedPriority = -2058263741;
	private int leftHandItem = 1255064447;
	private int anInt2133 = -646022153;
	private int rightHandItem = -1102454537;
	private int delayType = 1420123926;
	private int resetWhenWalk = 1662613047;

	public SequenceType(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#decode(java.nio.ByteBuffer)
	 */
	@Override
	public void decode(ByteBuffer buffer) {
		while (true) {
			int opcode = buffer.get() & 0xFF;
			if (opcode == 0)
				break;

			if (1 == opcode) {
				int count = buffer.getShort() & 0xFFFF;
				this.frameLengths = new int[count];

				for (int index = 0; index < count; ++index) {
					this.frameLengths[index] = buffer.getShort() & 0xFFFF;
				}

				this.frameIDs = new int[count];

				for (int index = 0; index < count; ++index) {
					this.frameIDs[index] = buffer.getShort() & 0xFFFF;
				}

				for (int index = 0; index < count; ++index) {
					this.frameIDs[index] += (buffer.getShort() & 0xFFFF) << 16;
				}

			} else if (2 == opcode) {
				this.frameStep = buffer.getShort() & 0xFFFF;
			} else if (3 == opcode) {
				int count = buffer.get() & 0xFF;
				this.animationFlowControl = new int[1 + count];

				for (int index = 0; index < count; ++index) {
					this.animationFlowControl[index] = buffer.get() & 0xFF;
				}

				this.animationFlowControl[count] = 9999999;
			} else if (opcode == 4) {
				this.oneSquareAnimation = true;
			} else if (opcode == 5) {
				this.forcedPriority = buffer.get() & 0xFF;
			} else if (opcode == 6) {
				this.leftHandItem = buffer.getShort() & 0xFFFF;
			} else if (opcode == 7) {
				this.rightHandItem = buffer.getShort() & 0xFFFF;
			} else if (opcode == 8) {
				this.anInt2133 = buffer.get() & 0xFF;
			} else if (opcode == 9) {
				this.resetWhenWalk = buffer.get() & 0xFF;
			} else if (opcode == 10) {
				this.priority = buffer.get() & 0xFF;
			} else if (opcode == 11) {
				this.delayType = buffer.get() & 0xFF;
			} else if (opcode == 12) {
				int count = buffer.get() & 0xFF;
				this.anIntArray2118 = new int[count];

				for (int index = 0; index < count; ++index) {
					this.anIntArray2118[index] = buffer.getShort() & 0xFFFF;
				}

				for (int index = 0; index < count; ++index) {
					this.anIntArray2118[index] += (buffer.getShort() & 0xFFFF) << 16;
				}

			} else if (opcode == 13) {
				int count = buffer.get() & 0xFF;
				this.anIntArray2126 = new int[count];

				for (int index = 0; index < count; ++index) {
					this.anIntArray2126[index] = ByteBufferUtils.getMedium(buffer);
				}

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#encode()
	 */
	@Override
	public ByteBuffer encode() {
		ByteBuffer buffer = ByteBuffer.allocate(1132);
		return (ByteBuffer) buffer.flip();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#encode317()
	 */
	@Override
	public ByteBuffer encode317() {
		ByteBuffer buffer = ByteBuffer.allocate(1132);
		return (ByteBuffer) buffer.flip();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.openrs.cache.type.Type#getID()
	 */
	@Override
	public int getID() {
		return id;
	}

	/**
	 * @return the anIntArray2118
	 */
	public int[] getAnIntArray2118() {
		return anIntArray2118;
	}

	/**
	 * @param anIntArray2118
	 *            the anIntArray2118 to set
	 */
	public void setAnIntArray2118(int[] anIntArray2118) {
		this.anIntArray2118 = anIntArray2118;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the frameIDs
	 */
	public int[] getFrameIDs() {
		return frameIDs;
	}

	/**
	 * @param frameIDs
	 *            the frameIDs to set
	 */
	public void setFrameIDs(int[] frameIDs) {
		this.frameIDs = frameIDs;
	}

	/**
	 * @return the frameLengths
	 */
	public int[] getFrameLengths() {
		return frameLengths;
	}

	/**
	 * @param frameLengths
	 *            the frameLengths to set
	 */
	public void setFrameLengths(int[] frameLengths) {
		this.frameLengths = frameLengths;
	}

	/**
	 * @return the anIntArray2126
	 */
	public int[] getAnIntArray2126() {
		return anIntArray2126;
	}

	/**
	 * @param anIntArray2126
	 *            the anIntArray2126 to set
	 */
	public void setAnIntArray2126(int[] anIntArray2126) {
		this.anIntArray2126 = anIntArray2126;
	}

	/**
	 * @return the frameStep
	 */
	public int getFrameStep() {
		return frameStep;
	}

	/**
	 * @param frameStep
	 *            the frameStep to set
	 */
	public void setFrameStep(int frameStep) {
		this.frameStep = frameStep;
	}

	/**
	 * @return the animationFlowControl
	 */
	public int[] getAnimationFlowControl() {
		return animationFlowControl;
	}

	/**
	 * @param animationFlowControl
	 *            the animationFlowControl to set
	 */
	public void setAnimationFlowControl(int[] animationFlowControl) {
		this.animationFlowControl = animationFlowControl;
	}

	/**
	 * @return the oneSquareAnimation
	 */
	public boolean isOneSquareAnimation() {
		return oneSquareAnimation;
	}

	/**
	 * @param oneSquareAnimation
	 *            the oneSquareAnimation to set
	 */
	public void setOneSquareAnimation(boolean oneSquareAnimation) {
		this.oneSquareAnimation = oneSquareAnimation;
	}

	/**
	 * @return the forcedPriority
	 */
	public int getForcedPriority() {
		return forcedPriority;
	}

	/**
	 * @param forcedPriority
	 *            the forcedPriority to set
	 */
	public void setForcedPriority(int forcedPriority) {
		this.forcedPriority = forcedPriority;
	}

	/**
	 * @return the leftHandItem
	 */
	public int getLeftHandItem() {
		return leftHandItem;
	}

	/**
	 * @param leftHandItem
	 *            the leftHandItem to set
	 */
	public void setLeftHandItem(int leftHandItem) {
		this.leftHandItem = leftHandItem;
	}

	/**
	 * @return the anInt2133
	 */
	public int getAnInt2133() {
		return anInt2133;
	}

	/**
	 * @param anInt2133
	 *            the anInt2133 to set
	 */
	public void setAnInt2133(int anInt2133) {
		this.anInt2133 = anInt2133;
	}

	/**
	 * @return the rightHandItem
	 */
	public int getRightHandItem() {
		return rightHandItem;
	}

	/**
	 * @param rightHandItem
	 *            the rightHandItem to set
	 */
	public void setRightHandItem(int rightHandItem) {
		this.rightHandItem = rightHandItem;
	}

	/**
	 * @return the delayType
	 */
	public int getDelayType() {
		return delayType;
	}

	/**
	 * @param delayType
	 *            the delayType to set
	 */
	public void setDelayType(int delayType) {
		this.delayType = delayType;
	}

	/**
	 * @return the resetWhenWalk
	 */
	public int getResetWhenWalk() {
		return resetWhenWalk;
	}

	/**
	 * @param resetWhenWalk
	 *            the resetWhenWalk to set
	 */
	public void setResetWhenWalk(int resetWhenWalk) {
		this.resetWhenWalk = resetWhenWalk;
	}

}
