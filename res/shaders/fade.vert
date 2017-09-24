#version 330 core

const vec4 vertices[6] = vec4[6](vec4( 1, -1, -0.5, 1),
								 vec4(-1, -1, -0.5, 1),
								 vec4( 1,  1, -0.5, 1),
								 vec4( 1,  1, -0.5, 1),
								 vec4(-1, -1, -0.5, 1),
								 vec4(-1,  1, -0.5, 1));

void main() {
	gl_Position = vertices[gl_VertexID];
}