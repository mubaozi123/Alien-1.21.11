#version 150

#define TAU 6.28318530718

in vec2 uv;
out vec4 color;

uniform sampler2D uTexture;
uniform vec2 uTextureSize;
uniform vec2 uResolution;
uniform float uShaderTime;
uniform float uWidth;
uniform int uFillMode;
uniform float uFillAlpha;
uniform vec4 uGradientColor;
uniform float uGradientFactor;
uniform float uFlowSpeed;
uniform float uFlowFactor;
uniform float uLiquidIntensity;
uniform float uLiquidFactor;
uniform float uOutlineAlpha;

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

vec4 getFill(vec3 centerColor, vec2 fragCoordXY)
{
    if (uFillMode == 1)
    {
        float time = uShaderTime / 5.0;
        float distance = sqrt(fragCoordXY.x * fragCoordXY.x + fragCoordXY.y * fragCoordXY.y) + time;
        distance = distance / uGradientFactor;
        distance = ((sin(distance) + 1.0) / 2.0);
        float j = 1.0 - distance;
        float r = centerColor.r * distance + uGradientColor.r * j;
        float g = centerColor.g * distance + uGradientColor.g * j;
        float b = centerColor.b * distance + uGradientColor.b * j;
        float a = uFillAlpha * distance + uGradientColor.a * j;
        return vec4(r, g, b, a);
    }

    if (uFillMode == 2)
    {
        float time = uShaderTime / 500.0;
        vec2 flowUv = (2.0 * fragCoordXY - uResolution) / min(uResolution.x, uResolution.y);
        for (float i = 1.0; i < uFlowSpeed; i++)
        {
            flowUv.x += uFlowFactor / i * cos(i * 2.5 * flowUv.y + time);
            flowUv.y += uFlowFactor / i * cos(i * 1.5 * flowUv.x + time);
        }
        float wave = max(abs(sin(time - flowUv.y - flowUv.x)), 0.001);
        return vec4(centerColor.r / wave, centerColor.g / wave, centerColor.b / wave, uFillAlpha);
    }

    if (uFillMode == 3)
    {
        float time = uShaderTime / 1000.0;
        vec2 liquidUv = fragCoordXY / uResolution;
        vec2 p = mod(liquidUv * TAU, TAU) - 250.0;
        vec2 i = vec2(p);
        float c = 1.0;
        float inten = uLiquidIntensity / 1000.0;

        for (int n = 0; n < int(uLiquidFactor); n++)
        {
            float t = time * (1.0 - (3.5 / float(n + 1)));
            i = p + vec2(cos(t - i.x) + sin(t + i.y), sin(t - i.y) + cos(t + i.x));
            c += 1.0 / length(vec2(p.x / (sin(i.x + t) / inten), p.y / (cos(i.y + t) / inten)));
        }

        c /= uLiquidFactor;
        c = 1.17 - pow(c, 1.4);
        vec3 liquidColor = vec3(pow(abs(c), 8.0));
        liquidColor = clamp(liquidColor + centerColor, 0.0, 1.0);
        return vec4(liquidColor, uFillAlpha);
    }

    if (uFillMode == 4)
    {
        vec2 rainbowUv = (fragCoordXY / uResolution - 0.5) + 0.5;
        float theta = rainbowUv.x * 3.14159;
        float phi = rainbowUv.y * 3.14159 * 0.5;
        vec3 dir = vec3(cos(phi) * cos(theta), sin(phi), cos(phi) * sin(theta));
        float time = uShaderTime / 750.0;
        float rot = time * 0.2;
        mat2 rotMat = mat2(cos(rot), -sin(rot), sin(rot), cos(rot));
        dir.xz = rotMat * dir.xz;

        float dist = length(dir.xy);
        float angle = atan(dir.y, dir.x);
        float spiral = sin(dist * 10.0 - angle * 3.0 - time * 2.0);
        float hue = fract(dist * 2.0 - time * 0.3 + angle / 6.28318);
        vec3 rainbowColor = hsv2rgb(vec3(hue, 0.8, 1.0));
        float rings = sin(dist * 20.0 - time * 3.0);
        rings = pow(max(0.0, rings), 3.0);

        vec3 finalColor = rainbowColor * (spiral * 0.3 + 0.7);
        finalColor += vec3(1.0) * rings * 0.5;
        float glow = exp(-dist * 3.0);
        finalColor += vec3(1.0, 0.9, 1.0) * glow * 0.5;
        return vec4(finalColor, uFillAlpha);
    }

    return vec4(centerColor, uFillAlpha);
}

vec3 getSobelColor(vec2 sampleUv, vec2 oneTexel)
{
    for (int r = 1; r <= int(ceil(uWidth)); ++r)
    {
        vec2 dx = vec2(oneTexel.x * float(r), 0.0);
        vec2 dy = vec2(0.0, oneTexel.y * float(r));

        vec4 s = texture(uTexture, sampleUv - dx);
        if (s.a > 0.0) return s.rgb;
        s = texture(uTexture, sampleUv + dx);
        if (s.a > 0.0) return s.rgb;
        s = texture(uTexture, sampleUv - dy);
        if (s.a > 0.0) return s.rgb;
        s = texture(uTexture, sampleUv + dy);
        if (s.a > 0.0) return s.rgb;

        vec2 od = vec2(dx.x, dy.y);
        s = texture(uTexture, sampleUv + od);
        if (s.a > 0.0) return s.rgb;
        s = texture(uTexture, sampleUv + vec2(od.x, -od.y));
        if (s.a > 0.0) return s.rgb;
        s = texture(uTexture, sampleUv + vec2(-od.x, od.y));
        if (s.a > 0.0) return s.rgb;
        s = texture(uTexture, sampleUv - od);
        if (s.a > 0.0) return s.rgb;
    }
    return vec3(0.0);
}

void main()
{
    vec2 oneTexel = 1.0 / uTextureSize;
    vec2 fragCoordXY = uv * uTextureSize;
    vec2 dx = vec2(oneTexel.x * uWidth, 0.0);
    vec2 dy = vec2(0.0, oneTexel.y * uWidth);

    vec4 center = texture(uTexture, uv);
    vec4 left = texture(uTexture, uv - dx);
    vec4 right = texture(uTexture, uv + dx);
    vec4 up = texture(uTexture, uv - dy);
    vec4 down = texture(uTexture, uv + dy);
    float e = abs(center.a - left.a) + abs(center.a - right.a) + abs(center.a - up.a) + abs(center.a - down.a);
    float edge = clamp(e, 0.0, 1.0);

    if (center.a > 0.0)
    {
        color = getFill(center.rgb, fragCoordXY);
        return;
    }

    if (edge > 0.0)
    {
        vec3 outlineRGB = uFillMode == 4 ? vec3(getFill(center.rgb, fragCoordXY)) : getSobelColor(uv, oneTexel);
        color = vec4(outlineRGB, edge * uOutlineAlpha);
    }
    else
    {
        color = vec4(0.0);
    }
}
