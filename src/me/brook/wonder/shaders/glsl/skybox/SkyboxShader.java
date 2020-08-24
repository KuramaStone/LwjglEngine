package me.brook.wonder.shaders.glsl.skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.shaders.ShaderProgram;

public class SkyboxShader extends ShaderProgram {

	private int transformationMatrix;
	private int projectionMatrix;
	private int viewMatrix;

	private int skyColor;
	private int nightTextures;

	public SkyboxShader() {
		super("skybox");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		transformationMatrix = this.getUniformLocation("transformationMatrix");
		projectionMatrix = this.getUniformLocation("projectionMatrix");
		viewMatrix = this.getUniformLocation("viewMatrix");
		skyColor = this.getUniformLocation("skyColor");

		nightTextures = this.getUniformLocation("nightTextures");
	}
	
	public void loadSkyTextures() {
		super.loadInt(nightTextures, 0);
	}

	public void loadSkyColor(Vector3f color) {
		super.loadVector(skyColor, color);
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

}
