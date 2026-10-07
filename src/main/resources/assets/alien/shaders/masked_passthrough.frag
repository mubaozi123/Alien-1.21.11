#version 150

in vec2 uv;
out vec4 color;

uniform sampler2D uTexture;
uniform sampler2D uMask;
uniform vec2 uMaskPos;
uniform vec2 uMaskSize;

void main() {
    vec2 maskUv = (uv - uMaskPos) / uMaskSize;
    if (maskUv.x < 0.0 || maskUv.x > 1.0 || maskUv.y < 0.0 || maskUv.y > 1.0) {
        discard;
    }

    float maskAlpha = texture(uMask, maskUv).a;
    vec4 texel = texture(uTexture, uv);
    color = vec4(texel.rgb, texel.a * maskAlpha);
}
