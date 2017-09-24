package com.thewgb.flappy;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.thewgb.flappy.graphics.*;
import com.thewgb.flappy.input.*;
import com.thewgb.flappy.level.*;
import com.thewgb.flappy.maths.*;

public class Main implements Runnable {
	private int width = 1280;
	private int height = 720;
	
	private Thread thread;
	private boolean running = false;
	
	private long window;
	
	private Level level;
	
	private Input input;
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	public void run() {
		init();
		
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(frames + "fps, " + updates + "ups");
				updates = 0;
				frames = 0;
			}
			
			if(glfwWindowShouldClose(window) || Input.keys[GLFW_KEY_ESCAPE])
				running = false;
		}
		
		//glfwDestroyWindow(timer);
		glfwTerminate();
	}
	
	private void init() {
		if(!glfwInit()) {
			System.err.println("Could not initialize GLFW!");
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		window = glfwCreateWindow(width, height, "Flappy", NULL, NULL);
		if(window == NULL) {
			System.err.println("Could not create window!");
			return;
		}
		
		long vidmode = nglfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWVidMode.nwidth(vidmode) - width) / 2, (GLFWVidMode.nheight(vidmode) - height) / 2);
		
		input = new Input();
		glfwSetKeyCallback(window, input);
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(vidmode);
		GL.createCapabilities(true);
		
		
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		Shader.loadAll();
		
		Matrix4f pr_matrix = Matrix4f.orthographic(-10, 10, -10 * 9 / 16, 10 * 9 / 16, -1, 1);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.setUniform1i("tex", 0);
		
		Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BIRD.setUniform1i("tex", 0);
		
		Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.PIPE.setUniform1i("tex", 0);
		
		level = new Level();
	}
	
	private void update() {
		glfwPollEvents();
		level.update();
		if(level.shouldReset())
			level = new Level();
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		level.render();
		int error = glGetError();
		if(error != GL_NO_ERROR) {
			System.out.println("Error code: " + error);
		}
		
		glfwSwapBuffers(window);
	}
	
	
	
	
	public static void main(String[] args) {
		new Main().start();
	}
	
}
