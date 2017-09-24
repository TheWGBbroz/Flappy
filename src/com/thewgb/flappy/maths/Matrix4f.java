package com.thewgb.flappy.maths;

import java.nio.FloatBuffer;

import com.thewgb.flappy.utils.BufferUtils;

public class Matrix4f {
	public static final int SIZE = 4 * 4;
	
	public float[] elements = new float[SIZE];
	
	public Matrix4f() {
	}
	
	public Matrix4f multiply(Matrix4f mat) {
		Matrix4f res = new Matrix4f();
		for(int y = 0; y < 4; y++) {
			for(int x = 0; x < 4; x++) {
				float sum = 0;
				for(int e = 0; e < 4; e++) {
					sum += this.elements[x + e * 4] * mat.elements[e + y * 4];
				}
				res.elements[x + y * 4] = sum;
			}
		}
		
		return res;
	}
	
	public FloatBuffer toFloatBuffer() {
		return BufferUtils.createFloatBuffer(elements);
	}
	
	
	public static Matrix4f identity() {
		Matrix4f res = new Matrix4f();
		res.elements[0 + 0 * 4] = 1;
		res.elements[1 + 1 * 4] = 1;
		res.elements[2 + 2 * 4] = 1;
		res.elements[3 + 3 * 4] = 1;
		
		return res;
	}
	
	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f res = identity();
		
		res.elements[0 + 0 * 4] = 2 / (right - left);
		res.elements[1 + 1 * 4] = 2 / (top   - bottom);
		res.elements[2 + 2 * 4] = 2 / (near  - far);
		
		res.elements[0 + 3 * 4] = (left + right) / (left - right);
		res.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		res.elements[2 + 3 * 4] = (far + near  ) / (far - near)  ;
		
		return res;
	}
	
	public static Matrix4f translate(Vector3f vec) {
		Matrix4f res = identity();
		
		res.elements[0 + 3 * 4] = vec.x;
		res.elements[1 + 3 * 4] = vec.y;
		res.elements[2 + 3 * 4] = vec.z;
		
		return res;
	}
	
	public static Matrix4f rotate(float angle) {
		Matrix4f res = identity();
		
		float r = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(r);
		float sin = (float) Math.sin(r);
		
		res.elements[0 + 0 * 4] = cos;
		res.elements[1 + 0 * 4] = sin;
		
		res.elements[0 + 1 * 4] = -sin;
		res.elements[1 + 1 * 4] = cos;
		
		return res;
	}
}
