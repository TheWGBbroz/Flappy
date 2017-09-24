package com.thewgb.flappy.level;

import com.thewgb.flappy.graphics.Texture;
import com.thewgb.flappy.graphics.VertexArray;
import com.thewgb.flappy.maths.Matrix4f;
import com.thewgb.flappy.maths.Vector3f;

public class Pipe {
	private static float width = 1.5f;
	private static float height = 8.0f;
	private static Texture texture;
	private static VertexArray mesh;
	
	public static void create() {
		float[] vertices = new float[] {
				0,     0,      0.1f,
				0,     height, 0.1f,
				width, height, 0.1f,
				width, 0,      0.1f
		};
		
		byte[] indices = new byte[] {
				0, 1, 2,
				2, 3, 0
		};
		
		float[] tcs = new float[] {
				0, 1,
				0, 0,
				1, 0,
				1, 1
		};
		
		mesh = new VertexArray(vertices, indices, tcs);
		texture = new Texture("textures/pipe.png");
	}
	
	public static VertexArray getMesh() {
		return mesh;
	}
	
	public static Texture getTexture() {
		return texture;
	}
	
	public static float getWidth() {
		return width;
	}
	
	public static float getHeight() {
		return height;
	}
	
	private Vector3f position = new Vector3f();
	private Matrix4f ml_matrix;
	
	public Pipe(float x, float y) {
		position.x = x;
		position.y = y;
		ml_matrix = Matrix4f.translate(position);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public Matrix4f getModelMatrix() {
		return ml_matrix;
	}
}
