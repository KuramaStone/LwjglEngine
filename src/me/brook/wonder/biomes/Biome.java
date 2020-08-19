package me.brook.wonder.biomes;

import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.Info;
import me.brook.wonder.chunk.procedural.NoiseGenerator;
import me.brook.wonder.chunk.procedural.PerlinNoise;

public class Biome {
	
	private static int currentID = 0;

	protected final GameEngine engine;
	protected final int ID = currentID++;
	public final int temperature, humidity;

	protected NoiseGenerator noise;
	protected float heightScale;

	private Vector3f color;

	public Biome(GameEngine engine, int temperature, int humidity, int octaves, float scale, float amplitude,
			float lacunarity, float frequency, float heightScale, Vector3f color) {
		this.engine = engine;
		this.temperature = temperature;
		this.humidity = humidity;
		this.heightScale = heightScale;

		this.color = new Vector3f(color.x / 255, color.y / 255, color.z / 255);

		noise = new PerlinNoise(Info.SEED, octaves, scale, amplitude, lacunarity,
				frequency);
	}

	public float calculateHeightAt(float x, float z) {
		return noise.getHeightAt(x, z) * heightScale;
	}

	public NoiseGenerator getHeightGenerator() {
		return noise;
	}

	public float getHeightScale() {
		return heightScale;
	}

	public Vector3f getColor() {
		return color;
	}

	public int getID() {
		return ID;
	}

}
