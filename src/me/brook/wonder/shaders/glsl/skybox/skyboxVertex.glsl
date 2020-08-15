#version 400 core

in vec3 position;

out vec4 worldPosition;

uniform mat4 transformationMatrix;	// the entity's position relative to the everything below
uniform mat4 projectionMatrix;		// where the default location of everything is
uniform mat4 viewMatrix;			// camera's perspective

void main(void) {
	// multiply by player position to move everything relative to player
	worldPosition = vec4(position, 1) * transformationMatrix;
	
	gl_Position = projectionMatrix * viewMatrix * vec4(position,1.0);  	// NOTE: viewMatrix must go in between the projectionMatrix and transformationMatrix
}