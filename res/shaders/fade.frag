#version 330 core

layout (location = 0) out vec4 color;

uniform float time;

void main() {
	if(time > 1)
		discard;
	color = vec4(0, 0, 0, -time + 1);
}