package me.brook.wonder.chunk.heightmap;

import java.util.Random;

public class PerlinNoise {

	private float freq = 1;

	private Random rnd;
	private long alphaSeed;
	private long betaSeed;

	private int xOffset;
	private int yOffset;
	private int layers;
	private float scale;

	public PerlinNoise(int layers, float scale, long seed) {
		this.xOffset = (int) (xOffset * scale);
		this.yOffset = (int) (yOffset * scale);
		this.layers = layers;
		this.scale = scale;
		this.betaSeed = seed >> 48;
		this.betaSeed = seed << 16;

		if(seed == -1) {
			rnd = new Random();
			seed = rnd.nextLong();
			rnd = new Random(seed);
		}
		else {
			rnd = new Random(seed);
		}

		this.alphaSeed = seed;
	}

	public float generateHeight(float x, float y) {
		float[] smooth = new float[layers];

		for(int l = 0; l < layers; l++) {
			alphaSeed += 2341;
			smooth[l] = smoothen(x, y, l);
		}
		alphaSeed -= 2341 * layers;

		float perlin = 0;

		float persistance = 0.5f;
		float amp = 1.0f;
		float totalAmp = 0;

		for(int l = layers - 1; l >= 0; l--) {
			amp *= persistance;
			totalAmp += amp;

			perlin += smooth[l] * amp;
		}

		perlin /= totalAmp;

		return perlin * 16;
	}

	private float smoothen(float x, float y, int layer) {
		x = x / scale;
		y = y / scale;
		float smooth = 0;

		int period = (int) Math.pow(2.5, layer);
		float frequency = freq / period;

		int x0 = (int) (Math.floor(x / period)) * period;
		int x1 = (x0 + period);
		float horizontal = (x - x0) * frequency;

		int y0 = (int) (Math.floor(y / period)) * period;
		int y1 = (y0 + period);

		float vertical = (y - y0) * frequency;

		float top = lerp(getSmoothNoiseAt(x0, y0), getSmoothNoiseAt(x1, y0),
				horizontal);

		float bottom = lerp(getSmoothNoiseAt(x0, y1), getSmoothNoiseAt(x1, y1),
				horizontal);

		smooth = lerp(top, bottom, vertical);

		return smooth;
	}

	private float getSmoothNoiseAt(float x, float z) {
		int factor = 1;

		float sum = 0;
		for(int i = -factor; i <= factor; i++) {
			for(int j = -factor; j <= factor; j++) {
				sum += getNoiseAt(x+i, z+j);
			}
		}

		return (float) (sum / Math.pow(factor * factor + 1, 2));
	}

	private float lerp(float i, float j, float a) {
		return i + a * (j - i);
	}

	private float getNoiseAt(float x, float y) {
		rnd.setSeed(alphaSeed + (long) (y * 694269) + (long) (x * 420420) + betaSeed);
		float value = rnd.nextFloat();

		return value;
	}

}
