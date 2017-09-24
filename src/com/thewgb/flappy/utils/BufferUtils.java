package com.thewgb.flappy.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {
	private BufferUtils() {}
	
	public static ByteBuffer createByteBuffer(byte[] array) {
		ByteBuffer res = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		res.put(array).flip();
		return res;
	}
	
	public static FloatBuffer createFloatBuffer(float[] array) {
		FloatBuffer res = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		res.put(array).flip();
		return res;
	}
	
	public static IntBuffer createIntBuffer(int[] array) {
		IntBuffer res = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
		res.put(array).flip();
		return res;
	}
}
