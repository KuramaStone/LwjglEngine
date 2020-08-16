package me.brook.wonder.chunk.procedural;

public class PerlinNoise extends NoiseGenerator {

	// Generation
	private long seed;
	private int octaves;
	private float scale;
	private float amplitude;

	private float lacunarity;
	private float frequency;

	// misc
	private FastNoise noise;

	public PerlinNoise(long seed, int octaves, float scale, float amplitude, float lacunarity, float frequency) {
		this.seed = seed;
		this.octaves = octaves;
		this.scale = scale;
		this.amplitude = amplitude;
		this.lacunarity = lacunarity;
		this.frequency = frequency;

		noise = new FastNoise((int) seed);
		noise.SetFractalOctaves(octaves);
		noise.SetFractalLacunarity(lacunarity);
		noise.SetFractalGain(amplitude);
		noise.SetFrequency(frequency);
	}

	public float getHeightAt(float x, float y) {
		x *= scale * frequency;
		y *= scale * frequency;
		float perlin = noiseAt(seed, x, y);

		float amp = this.amplitude;
		float totalAmp = 1;

		long seed = this.seed;
		for(int layer = 1; layer < octaves; layer++) {
			x *= lacunarity;
			y *= lacunarity;

			totalAmp += amp;
			amp *= amplitude;
			perlin += noiseAt(seed++, x, y) * amp;
		}
		perlin /= totalAmp;
		seed -= octaves;

		return (perlin + 1) / 2;
	}

	private float noiseAt(long seed, float x, float y) {
		return noise.SinglePerlin((int) seed, x, y);
	}

}
