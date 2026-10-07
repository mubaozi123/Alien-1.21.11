#version 150

in vec2 uv;
out vec4 color;

uniform sampler2D uTexture;
uniform vec2 texelSize;
uniform vec2 direction;
uniform float radius;

float gaussian(float x, float sigma) {
    return exp(-(x * x) / (2.0 * sigma * sigma));
}

void main() {
    float sigma = max(radius * 0.5, 0.001);
    vec4 sum = texture(uTexture, uv) * gaussian(0.0, sigma);
    float total = gaussian(0.0, sigma);

    for (int i = 1; i < 64; i++) {
        float fi = float(i);
        if (fi > radius) {
            break;
        }
        float w = gaussian(fi, sigma);
        vec2 offset = direction * texelSize * fi;
        sum += texture(uTexture, uv + offset) * w;
        sum += texture(uTexture, uv - offset) * w;
        total += w * 2.0;
    }

    color = sum / total;
    color.a = 1.0;
}
