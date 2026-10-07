#version 150

in vec2 uv;
out vec4 color;

uniform sampler2D uCurrent;
uniform sampler2D uPrevious;
uniform float uBlendFactor;

void main() {
    vec4 current = texture(uCurrent, uv);
    vec4 previous = texture(uPrevious, uv);
    color = vec4(mix(current.rgb, previous.rgb, uBlendFactor), 1.0);
}
