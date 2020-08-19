package me.brook.wonder.biomes;

import me.brook.wonder.GameEngine;

public class GrasslandBiome extends Biome {

	public GrasslandBiome(GameEngine engine, int temperature, int humidity, int octaves, float scale, float amplitude,
			float lacunarity, float frequency, float heightScale) {
		super(engine, temperature, humidity, octaves, scale, amplitude, lacunarity, frequency, heightScale, BiomeColor.GRASSLAND);
	}

}
