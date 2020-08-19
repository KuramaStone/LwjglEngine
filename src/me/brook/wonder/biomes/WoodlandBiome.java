package me.brook.wonder.biomes;

import me.brook.wonder.GameEngine;

public class WoodlandBiome extends Biome {

	public WoodlandBiome(GameEngine engine, int temperature, int humidity, int octaves, float scale, float amplitude,
			float lacunarity, float frequency, float heightScale) {
		super(engine, temperature, humidity, octaves, scale, amplitude, lacunarity, frequency, heightScale,
				BiomeColor.WOODLAND);
	}

}
