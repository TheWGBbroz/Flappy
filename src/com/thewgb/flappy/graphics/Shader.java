package com.thewgb.flappy.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import com.thewgb.flappy.maths.Matrix4f;
import com.thewgb.flappy.maths.Vector3f;
import com.thewgb.flappy.utils.ShaderUtils;

public class Shader {
	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;
	
	public static Shader BG, BIRD, PIPE, FADE;
	
	private boolean enabled = false;
	
	public static void loadAll() {
		BG = new Shader  ("shaders/bg.vert"  , "shaders/bg.frag"  );
		BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
		PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
		FADE = new Shader("shaders/fade.vert", "shaders/fade.frag");
	}
	
	private final int id;
	private Map<String, Integer> locationCache = new HashMap<>();
	
	public Shader(String vertexPath, String fragmentPath) {
		id = ShaderUtils.load(vertexPath, fragmentPath);
	}
	
	public int getUniform(String name) {
		if(locationCache.containsKey(name))
			return locationCache.get(name);
		
		int res = glGetUniformLocation(id, name);
		if(res == -1)
			System.err.println("Could not find uniform variable '" + name + "'!");
		else
			locationCache.put(name, res);
		
		return res;
	}
	
	public void setUniform1i(String name, int value) {
		if(!enabled)
			enable();
		glUniform1i(getUniform(name), value);
	}
	
	public void setUniform1f(String name, float value) {
		if(!enabled)
			enable();
		glUniform1f(getUniform(name), value);
	}
	
	public void setUniform2f(String name, float x, float y) {
		if(!enabled)
			enable();
		glUniform2f(getUniform(name), x, y);
	}
	
	public void setUniform3f(String name, Vector3f vec) {
		if(!enabled)
			enable();
		glUniform3f(getUniform(name), vec.x, vec.y, vec.z);
	}
	
	public void setUniformMat4f(String name, Matrix4f mat) {
		if(!enabled)
			enable();
		glUniformMatrix4fv(getUniform(name), false, mat.toFloatBuffer());
	}
	
	public void enable() {
		glUseProgram(id);
		enabled = true;
	}
	
	public void disable() {
		glUseProgram(0);
		enabled = false;
	}
}
