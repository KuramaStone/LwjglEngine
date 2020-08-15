package me.brook.wonder.biomes;

import me.brook.wonder.GameEngine;
import me.brook.wonder.chunk.procedural.SimplexNoise;

public class Biome {

	protected final GameEngine engine;
	public final int temperature, humidity;

	protected SimplexNoise noise;

	public Biome(GameEngine engine, int temperature, int humidity) {
		this.engine = engine;
		this.temperature = temperature;
		this.humidity = humidity;
	}

}
