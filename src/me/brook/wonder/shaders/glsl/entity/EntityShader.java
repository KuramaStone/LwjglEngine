package me.brook.wonder.shaders.glsl.entity;

import org.lwjgl.util.vector.Matrix4f;

import me.brook.wonder.shaders.ShaderProgram;

public class EntityShader extends ShaderProgram {

	private int transformationMatrix;
	private int projectionMatrix;
	private int viewMatrix;

	public EntityShader() {
		super("entity");
	}

	@Override
	protected void getAllUniformLocations() {
		transformationMatrix = this.getUniformLocation("transformationMatrix");
		projectionMatrix = this.getUniformLocation("projectionMatrix");
		viewMatrix = this.getUniformLocation("viewMatrix");
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(transformationMatrix, matrix);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(projectionMatrix, matrix);
	}

	public void loadViewMatrix(Matrix4f matrix) {
		super.loadMatrix(viewMatrix, matrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

}
