package me.brook.wonder.chunk.procedural;

public abstract class NoiseGenerator {

	public abstract float getHeightAt(float x, float z);

	protected float interpolateLinear(float i, float j, float a) {
		return i + a * (j - i);
	}

	protected float interpolateCosine(float i, float j, float a) {
		float mu2 = (float) ((1 - Math.cos(a * Math.PI)) / 2);
		return (i * (1 - mu2) + j * mu2);
	}

}
