package com.thewgb.flappy.level;

import static org.lwjgl.glfw.GLFW.*;

import com.thewgb.flappy.graphics.Shader;
import com.thewgb.flappy.graphics.Texture;
import com.thewgb.flappy.graphics.VertexArray;
import com.thewgb.flappy.input.Input;
import com.thewgb.flappy.maths.Matrix4f;
import com.thewgb.flappy.maths.Vector3f;

public class Bird implements Input.Listener {
	private float SIZE = 1.0f;
	private VertexArray mesh;
	private Texture texture;
	
	private Vector3f position = new Vector3f();
	private float rot;
	private float delta;
	
	public Bird() {
		mesh = new VertexArray(SIZE, 0.2f);
		texture = new Texture("textures/bird.png");
		
		Input.addListener(this);
	}
	
	public void update() {
		position.y -= delta;
		delta += 0.01f;
		
		rot = -delta * 90.0f;
	}
	
	public void fall() {
		delta = -0.15f;
	}
	
	public void render() {
		Shader.BIRD.enable();
		Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
		texture.bind();
		mesh.render();
		Shader.BIRD.disable();
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getSize() {
		return SIZE;
	}
	
	public void keyPressed(int key) {
		if(Input.isKeyDown(GLFW_KEY_SPACE) && position.y < 5)
			delta = -0.15f;
	}
	
	public void keyReleased(int key) {
	}
}
