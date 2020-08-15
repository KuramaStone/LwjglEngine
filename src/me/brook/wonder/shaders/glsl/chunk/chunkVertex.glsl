#version 400 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec4 worldPosition;
out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
out vec3 toCameraVector;
out vec3 toLightVector;

uniform mat4 transformationMatrix;	// the entity's position relative to the everything below
uniform mat4 projectionMatrix;		// where the default location of everything is
uniform mat4 viewMatrix;			// camera's perspective

// light details
uniform vec3 lightPosition;			// location of the light source (only one source of light so far)
uniform float lightRelative;

void main(void) {
	worldPosition = transformationMatrix * vec4(position,1.0); // position is the position of the current vertex
	
	gl_Position = projectionMatrix * viewMatrix * worldPosition;  	// NOTE: viewMatrix must go in between the projectionMatrix and transformationMatrix
	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz; //(swizzle it) convert from vec4 back to vec3
	
	if(lightRelative == 1) {
		toLightVector = lightPosition - worldPosition.xyz;
	}
	else {
		toLightVector = lightPosition;
	}
	
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
	
	pass_textureCoordinates = textureCoordinates;
}