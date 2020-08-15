package me.brook.wonder.shaders.glsl.chunk;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import me.brook.wonder.chunk.Coords;
import me.brook.wonder.entities.light.Light;
import me.brook.wonder.shaders.ShaderProgram;

public class ChunkShader extends ShaderProgram {

	private int transformationMatrix;
	private int projectionMatrix;
	private int viewMatrix;
	
	private int coords;

	// light data
	private int lightPosition;
	private int lightRelative;
	
	// texture
	private int backgroundTexture;
	private int showHeightMap;

	public ChunkShader() {
		super("chunk");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		transformationMatrix = this.getUniformLocation("transformationMatrix");
		projectionMatrix = this.getUniformLocation("projectionMatrix");
		viewMatrix = this.getUniformLocation("viewMatrix");

		lightPosition = this.getUniformLocation("lightPosition");
		lightRelative = this.getUniformLocation("lightRelative");
		
		backgroundTexture = this.getUniformLocation("backgroundTexture");
		showHeightMap = this.getUniformLocation("showHeightMap");
		coords = this.getUniformLocation("coords");
	}
	
	public void loadCoords(Coords coords) {
		super.loadVector(this.coords, new Vector2f(coords.getX(), coords.getZ()));
	}
	
	public void setShowHeightMap(boolean boo) {
		super.loadBoolean(showHeightMap, boo);
	}
	
	public void loadTextures() {
		super.loadInt(backgroundTexture, 0);
	}

	public void loadLight(Light light) {
		super.loadVector(lightPosition, light.getLocation().getPosition());
		super.loadBoolean(lightRelative, light.isRelative());
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
