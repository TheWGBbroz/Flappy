package com.thewgb.flappy.level;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Random;

import com.thewgb.flappy.graphics.Shader;
import com.thewgb.flappy.graphics.Texture;
import com.thewgb.flappy.graphics.VertexArray;
import com.thewgb.flappy.input.Input;
import com.thewgb.flappy.maths.Matrix4f;
import com.thewgb.flappy.maths.Vector3f;

public class Level implements Input.Listener {
	private VertexArray background, fade;
	private Texture bgTexture;
	
	private int xScroll = 0;
	private int map = 0;
	
	private Bird bird;
	
	private Pipe[] pipes = new Pipe[5 * 2];
	private Random random = new Random();
	private int index = 0;
	private float OFFSET = 5.0f;
	private boolean control = true;
	private boolean reset = false;
	private boolean started = false;
	
	private float time = 0.0f;
	
	public Level() {
		float[] vertices = new float[] {
				-10, -10 * 9 / 16, 0,
				-10,  10 * 9 / 16, 0,
				  0,  10 * 9 / 16, 0,
				  0, -10 * 9 / 16, 0
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
		
		fade = new VertexArray(6);
		background = new VertexArray(vertices, indices, tcs);
		bgTexture = new Texture("textures/bg.png");
		
		bird = new Bird();
		
		createPipes();
		Input.addListener(this);
	}
	
	private void createPipes() {
		Pipe.create();
		for(int i = 0; i < 5 * 2; i += 2) {
			pipes[i + 0] = new Pipe(OFFSET + index * 3, random.nextFloat() * 4);
			pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - (random.nextFloat() * 1.0f + 10.5f));
			index += 2;
		}
	}
	
	private void updatePipes() {
		pipes[(index + 0) % 10] = new Pipe(OFFSET + index * 3, random.nextFloat() * 4);
		pipes[(index + 1) % 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - (random.nextFloat() * 1.0f + 10.5f));
		index += 2;
	}
	
	public void update() {
		if(control && started) {
			xScroll--;
			if(-xScroll % 335 == 0)
				map++;
			if(-xScroll > 250 && -xScroll % 120 == 0)
				updatePipes();
		}
		
		if(started)
			bird.update();
		
		if(control && collision()) {
			bird.fall();
			control = false;
		}
		
		//System.out.println(bird.getY());
		
		time += 0.01f;
	}
	
	public void keyPressed(int key) {
		if(!started && key == GLFW_KEY_SPACE) {
			started = true;
		}
		
		if(!reset && !control && key == GLFW_KEY_SPACE) {
			reset = true;
			Input.removeListener(bird);
			Input.removeListener(this);
		}
	}
	
	public void keyReleased(int key) {
	}
	
	private boolean collision() {
		float bx = -xScroll * 0.05f;
		float by = bird.getY();
		
		if(by < -5)
			return true;
		
		float bx0 = bx - bird.getSize() / 2;
		float bx1 = bx + bird.getSize() / 2;
		float by0 = by - bird.getSize() / 2;
		float by1 = by + bird.getSize() / 2;
		
		for(int i = 0; i < pipes.length; i++) {
			float px = pipes[i].getX();
			float py = pipes[i].getY();
			
			float px0 = px;
			float px1 = px + Pipe.getWidth();
			float py0 = py;
			float py1 = py + Pipe.getHeight();
			
			if(bx1 > px0 && bx0 < px1 && by1 > py0 && by0 < py1) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isGameOver() {
		return !control;
	}
	
	public boolean shouldReset() {
		return reset;
	}
	
	public void render() {
		bgTexture.bind();
		Shader.BG.enable();
		Shader.BG.setUniform2f("bird", 0, bird.getY());
		background.bind();
		for(int i = map; i < map + 4; i++) {
			Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0, 0)));
			background.draw();
		}
		Shader.BG.disable();
		bgTexture.unbind();
		
		renderPipes();
		bird.render();
		
		Shader.FADE.enable();
		Shader.FADE.setUniform1f("time", time);
		fade.render();
		Shader.FADE.disable();
	}
	
	private void renderPipes() {
		Shader.PIPE.enable();
		Shader.PIPE.setUniform2f("bird", 0, bird.getY());
		Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0, 0)));
		Pipe.getTexture().bind();
		Pipe.getMesh().bind();
		
		for(int i = 0; i < pipes.length; i++) {
			Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
			Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
			Pipe.getMesh().draw();
		}
		Shader.PIPE.disable();
		
		Pipe.getTexture().unbind();
		Pipe.getMesh().unbind();
	}
}
