package me.brook.wonder.entities.player;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

public class Camera {
	
	private float FOV = 70;
	private float NEAR_PLANE = 0.01f;
	private float FAR_PLANE = 1000f;
	
	private int chunkRange = 1;
	
	private Matrix4f projectionMatrix;
	
	public Camera() {
		createProjectionMatrix();
	}
	
	public Matrix4f createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = (FAR_PLANE - NEAR_PLANE);

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		
		return projectionMatrix;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public int getChunkRange() {
		return chunkRange;
	}

}
