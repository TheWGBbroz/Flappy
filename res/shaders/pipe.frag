#version 410 core

layout (location = 0) out vec4 color;

in DATA {
	vec2 tc;
	vec3 position;
} fs_in;

uniform vec2 bird;
uniform sampler2D tex;
uniform int top;

void main() {
	vec2 texCoords = fs_in.tc;
	if(top == 1) {
		texCoords.y = 1 - texCoords.y;
	}
	color = texture(tex, texCoords);
	if(color.w < 1)
		discard;
	color *= 3 / (length(bird - fs_in.position.xy) + 2.5) + 0.2;
	color.w = 1;
}