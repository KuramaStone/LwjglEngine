package me.brook.wonder.models;

public class EmptyModel {

	private float[] vertices;
	private float[] normals;
	private float[] textureCoords;
	private int[] indices;

	public EmptyModel(float[] vertices, float[] normals, float[] textureCoords, int[] indices) {
		this.vertices = vertices;
		this.normals = normals;
		this.textureCoords = textureCoords;
		this.indices = indices;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getNormals() {
		return normals;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public int[] getIndices() {
		return indices;
	}

}
