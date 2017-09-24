package com.thewgb.flappy.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.thewgb.flappy.utils.BufferUtils;

public class VertexArray {
	private int vao, vbo, ibo, tbo;
	private int count;
	
	public VertexArray(int count) {
		this.count = count;
		vao = glGenVertexArrays();
	}
	
	public VertexArray(float width, float height, float z) {
		float[] vertices = new float[] {
				0,     0,      z,
				0,     height, z,
				width, height, z,
				width, 0,      z
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
		
		load(vertices, indices, tcs);
	}
	
	public VertexArray(float size, float z) {
		float[] vertices = new float[] {
				-size / 2, -size / 2, z,
				-size / 2,  size / 2, z,
				 size / 2,  size / 2, z,
				 size / 2, -size / 2, z
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
		
		load(vertices, indices, tcs);
	}
	
	public VertexArray(float vertices[], byte[] indices, float[] textureCoords) {
		load(vertices, indices, textureCoords);
	}
	
	private void load(float[] vertices, byte[] indices, float[] textureCoords) {
		count = indices.length;
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);
		
		tbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCoords), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.TCOORD_ATTRIB, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.TCOORD_ATTRIB);
		
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void bind() {
		glBindVertexArray(vao);
		if(ibo > 0)
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	
	public void unbind() {
		glBindVertexArray(0);
		if(ibo > 0)
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void draw() {
		if(ibo > 0)
			glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
		else
			glDrawArrays(GL_TRIANGLES, 0, count);
	}
	
	public void render() {
		bind();
		draw();
	}
}
