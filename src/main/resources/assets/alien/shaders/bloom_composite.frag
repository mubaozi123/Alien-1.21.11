#version 150

in vec2 uv;
out vec4 color;

uniform sampler2D uTexture;
uniform sampler2D uOcclusion;

void main() {
    vec4 glow = texture(uTexture, uv);
    float occlusion = clamp(texture(uOcclusion, uv).a, 0.0, 1.0);
    float alpha = glow.a * (1.0 - occlusion);
    color = vec4(glow.rgb * alpha, alpha);
}
