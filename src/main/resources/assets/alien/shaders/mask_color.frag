#version 150

in vec2 uv;
out vec4 color;

uniform sampler2D uMask;
uniform vec2 uMaskPos;
uniform vec2 uMaskSize;
uniform vec4 uColor;

void main() {
    vec2 maskUv = (uv - uMaskPos) / uMaskSize;
    if (maskUv.x < 0.0 || maskUv.x > 1.0 || maskUv.y < 0.0 || maskUv.y > 1.0) {
        discard;
    }

    float maskAlpha = texture(uMask, maskUv).a;
    color = vec4(uColor.rgb, uColor.a * maskAlpha);
}
