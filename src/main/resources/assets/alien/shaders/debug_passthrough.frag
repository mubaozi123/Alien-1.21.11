#version 150

in vec2 uv;
out vec4 color;

uniform sampler2D uTexture;

void main() {
    vec4 sampled = texture(uTexture, uv);
    color = vec4(1.0 - sampled.rgb, 1.0);
}
