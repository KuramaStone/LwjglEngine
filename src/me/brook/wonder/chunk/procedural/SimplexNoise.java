package me.brook.wonder.chunk.procedural;

import java.util.Random;

public class SimplexNoise extends NoiseGenerator {

	// Generation
	private long seed;
	private int octaves;
	private float scale;
	private float amplitude;

	private float frequency;

	// misc
	private FastNoise noise;

	public SimplexNoise(long seed, int octaves, float scale, float amplitude, float frequency) {
		this.seed = seed;
		this.octaves = octaves;
		this.scale = scale;
		this.amplitude = amplitude;
		this.frequency = frequency;

		noise = new FastNoise((int) seed);
		noise.SetFractalOctaves(octaves);
		noise.SetFractalLacunarity(frequency);
		noise.SetFractalGain(amplitude);
	}

	public float getHeightAt(float x, float z) {
		float perlin = 0;
		// x *= scale;
		// z *= scale;
		// float[] smooth = new float[octaves];
		//
		// smooth = generateSmoothOctaves(x, z);
		//
		//
		// float persistance = this.persistance;
		// float amp = amplitude;
		// float totalAmp = 0;
		//
		// for(int l = octaves - 1; l >= 0; l--) {
		// amp *= persistance;
		// totalAmp += amp;
		//
		// perlin += smooth[l] * amp;
		// }
		//
		// perlin /= totalAmp;

		perlin = (noise.GetPerlinFractal(x, z) + 1) / 2;
		return perlin;
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
