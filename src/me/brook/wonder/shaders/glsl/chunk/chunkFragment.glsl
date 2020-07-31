#version 400 core

in vec3 vertexPos;
in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toCameraVector;
in vec3 toLightVector;

out vec4 out_Color;

uniform sampler2D background;
uniform float showHeightMap;

void main(void){


	vec3 unitNormal  = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 unitLightVector = normalize(toLightVector);  // pointing from the surface to the light source

	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, 0.2);
	
 	vec2 tiledCoords = pass_textureCoordinates * 1;
 	vec4 textureColor = texture(background, tiledCoords);
 	
	if(showHeightMap == 1) {
	 	out_Color = vec4(vec3(vertexPos.y / 16), 1.0);
	 }
	 else {
		out_Color = textureColor * vec4(vec3(brightness), 1);
	}
}