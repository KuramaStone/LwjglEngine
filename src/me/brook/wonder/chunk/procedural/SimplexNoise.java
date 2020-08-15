package me.brook.wonder.chunk.procedural;

import java.util.Random;

public class SimplexNoise extends NoiseGenerator {

	// Generation
	private long seed;
	private int octaves;
	private float scale;
	private float amplitude;

	private float persistance;
	private float frequency;
	public float periodExponentRange;

	// misc
	private FastNoise noise;

	public SimplexNoise(long seed, int octaves, float scale, float amplitude, float persistance, float frequency,
			float periodExponent) {
		this.seed = seed;
		this.octaves = octaves;
		this.scale = scale;
		this.amplitude = amplitude;
		this.persistance = persistance;
		this.frequency = frequency;
		this.periodExponentRange = periodExponent;

		noise = new FastNoise((int) seed);
	}

	public float getHeightAt(float x, float z) {
		x *= scale;
		z *= scale;
		float[] smooth = new float[octaves];

		smooth = generateSmoothOctaves(x, z);

		float perlin = 0;

		float persistance = this.persistance;
		float amp = amplitude;
		float totalAmp = 0;

		for(int l = octaves - 1; l >= 0; l--) {
			amp *= persistance;
			totalAmp += amp;

			perlin += smooth[l] * amp;
		}

		perlin /= totalAmp;

//		 return (noise.GetPerlin(x, z) + 1) / 2;
		return perlin;
	}

	private float[] generateSmoothOctaves(float x, float z) {
		float[] smoothened = new float[octaves];

		for(int layer = 0; layer < octaves; layer++) {
			float smooth = 0;

			float period = (float) Math.pow(periodExponentRange, layer);
			float freq = this.frequency / period;

			float scaledX = x / scale;
			float x0 = (float) ((Math.floor(scaledX / period)) * period);
			float x1 = (x0 + period);
			float horizontal = (scaledX - x0) * freq;

			float scaledZ = z / scale;
			float y0 = (float) ((Math.floor(scaledZ / period)) * period);
			float y1 = (y0 + period);

			float vertical = (scaledZ - y0) * freq;

			float top = lerp(getNoiseAt(x0, y0), getNoiseAt(x1, y0),
					horizontal);

			float bottom = lerp(getNoiseAt(x0, y1), getNoiseAt(x1, y1),
					horizontal);

			smooth = lerp(top, bottom, vertical);

			smoothened[layer] = smooth;
		}

		return smoothened;
	}

	private float lerp(float a, float b, float c) {
		return super.interpolateCosine(a, b, c);
	}

	private Random random = new Random();

	public float getNoiseAt(float i, float j) {
		long s = (long) (i * -2142 + j * 8574 + seed);
		random.setSeed(s);
		return (noise.GetPerlin(i, j) + 1) / 2;
	}

	public long getSeed() {
		return seed;
	}

}
