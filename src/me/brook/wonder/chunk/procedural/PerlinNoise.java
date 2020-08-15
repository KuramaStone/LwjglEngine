package me.brook.wonder.chunk.procedural;

import java.util.Random;

public class PerlinNoise extends NoiseGenerator {

	// Generation
	private long seed;
	private int octaves;
	private float scale;
	private float amplitude;

	private float persistance;
	private float frequency;
	public float periodExponent;

	// misc
	private FastNoise noise;
	private Random random;

	public PerlinNoise(long seed, int octaves, float scale, float amplitude, float persistance, float frequency,
			float periodExponent) {
		this.seed = seed;
		this.octaves = octaves;
		this.scale = scale;
		this.amplitude = amplitude;
		this.persistance = persistance;
		this.frequency = frequency;
		this.periodExponent = periodExponent;

		noise = new FastNoise((int) seed);
		random = new Random();
	}

	public float getHeightAt(float x, float y) {
		x *= scale;
		y *= scale;
		float[] smooth = new float[octaves];

		for(int l = 0; l < octaves; l++) {
			seed += 2341;
			smooth[l] = smoothen(l, x, y);
		}
		seed -= 2341 * octaves;

		float perlin = 0;

		float persistance = this.persistance;
		float amp = this.amplitude;
		float totalAmp = 0;

		for(int l = octaves - 1; l >= 0; l--) {
			amp *= persistance;
			totalAmp += amp;

			perlin += smooth[l] * amp;
		}

		perlin /= totalAmp;

		return perlin;
	}

	private float smoothen(int layer, float x, float y) {
		float smooth = 0;

		int period = (int) Math.pow(periodExponent, layer);
		float frequency = this.frequency / period;

		int x0 = (int) (Math.floor(x / period)) * period;
		int x1 = (x0 + period);
		float horizontal = (x - x0) * frequency;

		int y0 = (int) (Math.floor(y / period)) * period;
		int y1 = (y0 + period);

		float vertical = (y - y0) * frequency;

		float top = lerp(noiseAt(x0, y0), noiseAt(x1, y0),
				horizontal);

		float bottom = lerp(noiseAt(x0, y1), noiseAt(x1, y1),
				horizontal);

		smooth = lerp(top, bottom, vertical);

		return smooth;
	}

	private float lerp(float i, float j, float a) {
		return i + a * (j - i);
	}

	private float noiseAt(float x, float y) {
		random.setSeed(seed + (long) (y * 694269) + (long) (x * 420420));
		float value = random.nextFloat();

		return value;
	}

}
