package me.brook.wonder.chunk.procedural;

public class TriangleNoise extends NoiseGenerator {

	private PerlinNoise perlin;
	private float triangleSize;

	public TriangleNoise(long seed, int octaves, float scale, float amplitude, float lacunarity, float frequency,
			float triangleSize) {
		this.perlin = new PerlinNoise(seed, octaves, scale, amplitude, lacunarity, frequency);
		this.triangleSize = triangleSize;
	}

	/*
	 * We create a triangle for each point instead of a square like in simplex. We
	 * then average those values for every point in that triangle according to the
	 * three corners. We have to figure out which triangle the point is in.
	 */
	public float getHeightAt(float x, float z) {

		// decide if this location should be a left or right triangle
		boolean firstTriangle = isPointLeftTriangle(x, z);

		float x0 = (float) (Math.floor(x / triangleSize) * triangleSize);
		float x1 = x0 + triangleSize;
		float z0 = (float) (Math.floor(z / triangleSize) * triangleSize);
		float z1 = z0 + triangleSize;

		// get the three corner values
		float a, b, c;
		if(firstTriangle) {
			a = noiseAt(x0, z0);
			b = noiseAt(x0, z1);
			c = noiseAt(x1, z1);
		}
		else {
			a = noiseAt(x0, z0);
			b = noiseAt(x1, z0);
			c = noiseAt(x1, z1);
		}

		return (a + b + c) / 3;
	}

	// Since angle is a 45 degree from top left to bottom right, we can just see which value is bigger AFTER converting to relative position in square
	private boolean isPointLeftTriangle(float x, float z) {
		x %= triangleSize;
		z %= triangleSize;
		
		if(x < z) {
			return true;
		}
		
		return false;
	}

	private float noiseAt(float x, float z) {
		return perlin.getHeightAt(x, z);
	}

}
