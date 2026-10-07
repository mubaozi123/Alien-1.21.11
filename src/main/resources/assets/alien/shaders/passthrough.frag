#version 150

in vec2 uv;
out vec4 color;

uniform sampler2D uTexture;

void main() {
    color = texture(uTexture, uv);
}
