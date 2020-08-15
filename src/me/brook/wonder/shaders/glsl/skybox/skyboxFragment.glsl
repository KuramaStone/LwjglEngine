#version 400 core
#define NEWTON_ITER 2
#define HALLEY_ITER 0

in vec4 worldPosition;

out vec4 out_Color;

uniform vec3 skyColor;

float cbrt(float x) {
	float y = sign(x) * uintBitsToFloat(floatBitsToUint(abs(x)) / 3u + 0x2a514067u);

	for(int i = 0; i < NEWTON_ITER; ++i)
    	y = (2. * y + x / (y * y)) * .333333333;

    for(int i = 0; i < HALLEY_ITER; ++i) {
    	float y3 = y * y * y;
        y *= (y3 + 2. * x) / (2. * y3 + x);
    }
    
    return y;
}

void main(void) {
	float horizonLevel = 0.001;

	vec3 onSphere = normalize(worldPosition.xyz);


	// make a sharp transition between black and white with a slight blur in-between
    float a = sqrt(cbrt(onSphere.y));
    
    out_Color = mix(vec4(0, 0, 0, 1), vec4(skyColor, 1), a);
}