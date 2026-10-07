#version 150

in vec2 uv;
out vec4 color;

uniform sampler2D uBlurTex;
uniform vec4 uRect;
uniform vec2 uScreenSize;
uniform float uPowerFactor;
uniform float uNoise;
uniform float uRefractionPower;
uniform float uGlowWeight;
uniform float uGlowBias;
uniform float uGlowEdge0;
uniform float uGlowEdge1;
uniform float uAlpha;
uniform float uRadius;
uniform float uHardMask;

const float M_E = 2.718281828459045;
const vec2 CENTER = vec2(0.5);

float rand(vec2 co) {
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

float f(float x) {
    return 1.0 - 2.3 * pow(5.2 * M_E, -6.9 * x - 0.7);
}

float sdRoundedRect(vec2 frag, vec2 rectSize, float radius) {
    vec2 halfSize = rectSize * 0.5;
    vec2 p = frag - halfSize;
    vec2 q = abs(p) - halfSize + vec2(radius);
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - radius;
}

float Glow(vec2 uv) {
    vec2 glowUV = uv * 2.0 - 1.0;
    return sin(atan(glowUV.y, glowUV.x) - 0.5);
}

bool OutOfBounds(vec2 value) {
    return max(value.x, value.y) > 1.0 || min(value.x, value.y) < 0.0;
}

vec2 ToScreenUV(vec2 ndc) {
    return ndc * 0.5 + vec2(0.5);
}

void main() {
    vec2 localUV = (gl_FragCoord.xy - uRect.xy) / uRect.zw;
    if (OutOfBounds(localUV)) {
        discard;
    }

    float edge;
    float dist;
    if (uHardMask > 0.5) {
        edge = 1.0;
        dist = min(min(localUV.x, 1.0 - localUV.x), min(localUV.y, 1.0 - localUV.y)) * 2.0;
    } else {
        float radius = clamp(uRadius, 0.0, min(uRect.z, uRect.w) * 0.5);
        float d = sdRoundedRect(gl_FragCoord.xy - uRect.xy, uRect.zw, radius);
        float aa = max(fwidth(d), 0.75);
        edge = 1.0 - smoothstep(0.0, aa, d);
        if (edge <= 0.001) {
            discard;
        }
        dist = max(-d, 0.0) / max(1.0, min(uRect.z, uRect.w) * 0.5);
    }

    float refraction = pow(f(dist), uRefractionPower);

    vec2 rectCenter = uRect.xy + uRect.zw * 0.5;
    vec2 vMidPointNDC = rectCenter / uScreenSize * 2.0 - 1.0;
    vec2 vLocalOffsetNDC = (gl_FragCoord.xy - rectCenter) / uScreenSize * 2.0;
    vec2 targetNDC = vMidPointNDC + vLocalOffsetNDC * refraction;
    vec2 sampleUV = ToScreenUV(targetNDC);

    if (OutOfBounds(sampleUV)) {
        discard;
    }

    vec4 glass = texture(uBlurTex, sampleUV);

    float noise = (rand(gl_FragCoord.xy * 1e-3) - 0.5) * uNoise;
    glass.rgb += vec3(noise);
    glass.rgb *= edge;

    float glow = Glow(localUV);
    float glowMask = smoothstep(uGlowEdge0, uGlowEdge1, dist);
    float glowStrength = glow * uGlowWeight * glowMask + 1.0 + uGlowBias;
    glass.rgb *= glowStrength;

    float edgeBand = (1.0 - smoothstep(0.0, 0.12, dist)) * edge;
    float directionalRim = max(glow, 0.0) * edgeBand;
    float grazing = pow(1.0 - min(dist, 1.0), 5.5) * edgeBand;
    vec3 rimColor = vec3(1.0, 0.965, 0.9);
    glass.rgb += rimColor * (directionalRim * 0.42 + grazing * 0.18) * uGlowWeight;

    color = vec4(glass.rgb, edge * uAlpha);
}
