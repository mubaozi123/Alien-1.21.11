#version 150

in vec2 uv;
out vec4 color;

uniform sampler2D uTexture;
uniform vec2 texelSize;
uniform float offset;

void main() {
    vec2 o = texelSize * offset;
    vec4 sum = texture(uTexture, uv) * 0.2;
    sum += texture(uTexture, uv + vec2( o.x,  o.y)) * 0.2;
    sum += texture(uTexture, uv + vec2(-o.x,  o.y)) * 0.2;
    sum += texture(uTexture, uv + vec2( o.x, -o.y)) * 0.2;
    sum += texture(uTexture, uv + vec2(-o.x, -o.y)) * 0.2;
    color = vec4(sum.rgb, 1.0);
}
