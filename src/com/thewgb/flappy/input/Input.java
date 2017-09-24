package com.thewgb.flappy.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback {
	
	public static boolean[] keys = new boolean[65536];
	
	private static List<Input.Listener> listeners = new ArrayList<>();
	
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW_RELEASE;
		
		if(action == GLFW_PRESS) {
			for(int i = 0; i < listeners.size(); i++) {
				listeners.get(i).keyPressed(key);
			}
		}else if(action == GLFW_RELEASE) {
			for(int i = 0; i < listeners.size(); i++) {
				listeners.get(i).keyReleased(key);
			}
		}
	}
	
	public static boolean isKeyDown(int key) {
		return keys[key];
	}
	
	public static void addListener(Input.Listener listener) {
		listeners.add(listener);
	}
	
	public static void removeListener(Input.Listener listener) {
		listeners.remove(listener);
	}
	
	public static interface Listener {
		public void keyPressed(int key);
		public void keyReleased(int key);
	}
}
