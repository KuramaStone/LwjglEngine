package me.brook.wonder.managers;

import java.util.ArrayList;
import java.util.List;

import me.brook.wonder.GameEngine;
import me.brook.wonder.Info;
import me.brook.wonder.biomes.Biome;
import me.brook.wonder.biomes.DesertBiome;
import me.brook.wonder.biomes.JungleBiome;
import me.brook.wonder.biomes.MountainBiome;
import me.brook.wonder.biomes.MountainPeakBiome;
import me.brook.wonder.biomes.OceanBiome;
import me.brook.wonder.biomes.SavannaBiome;
import me.brook.wonder.biomes.TropicalForestBiome;
import me.brook.wonder.chunk.procedural.PerlinNoise;

public class BiomeManager extends Manager {

	private List<Biome> biomes;

	public BiomeManager(GameEngine engine) {
		super(engine);
		biomes = new ArrayList<>();

		// Add all the biomes

		int temperature = 6500;

		 biomes.add(new OceanBiome(engine, temperature, 0, 3, 1f, 0.5f, 2.0f, 0.01f,
		 32));

		temperature = 5000;
		 biomes.add(new JungleBiome(engine, temperature, 3000, 1, 0.9f, 0.5f, 2.0f,
		 0.01f, 96));

		temperature = 3000;
		 biomes.add(new TropicalForestBiome(engine, temperature, 4750, 3, 1f, 0.5f,
		 2.0f, 0.01f, 96));
		biomes.add(new SavannaBiome(engine, temperature, 500, 1, 0.1f, 0.5f, 2.0f, 0.01f, 16));
		biomes.add(new DesertBiome(engine, temperature, 0, 2, 0.33f, 0.5f, 2.0f, 0.01f, 4));

		temperature = 500;
		biomes.add(new MountainBiome(engine, temperature, 0, 4, 0.05f, 0.7f, 2.0f, 0.05f, 32));
		temperature = 250;
		 biomes.add(new MountainPeakBiome(engine, temperature, 0, 3, 1f, 0.5f, 2.0f,
		 0.01f, 148));

		// Sort the biomes by temperature and then humidity

			new SavannaBiome(engine, temperature, 500, 1, 0.5f, 0.5f, 2.0f, 0.01f,
					64).getHeightGenerator().drawImage(500, 500, "res\\biomes\\savanna.png");
	}

	public float getAverageHeightAt(float x, float z) {
		return getAverageHeightOfBiomes(getBiomesAt(x, z), x, z);
	}

	PerlinNoise altitude = new PerlinNoise(Info.SEED + 235421, 2, 0.05f, 0.5f, 2.0f, 0.01f);

	PerlinNoise humidity = new PerlinNoise(Info.SEED + 52135, 2, 0.05f, 0.5f, 2.0f, 0.01f);

	PerlinNoise rivers = new PerlinNoise(Info.SEED, 5, 0.01f, 0.5f, 2.0f, 0.01f);

	public float getAverageHeightOfBiomes(Biome[] biomes, float x, float z) {

		float f = 0;
		for(Biome b : biomes) {
			f += b.calculateHeightAt(x, z);
		}
		f /= biomes.length;
		f = 0;

		return f;
	}

	public Biome[] getBiomesAt(float x, float z) {
		return getBiomesAt(altitude.getHeightAt(x, z) * 10000, humidity.getHeightAt(x, z) * 10000,
				rivers.getHeightAt(x, z) * 10000, 1);
	}

	public Biome[] getBiomesAt(double altitude, double humidity, double rivers,
			int size) {

		// If altitude is correct AND {lakes} is high enough.

		// 200t is 1 degree C. 0t is 30C, 4000t is 0C, and 10000t is -20C
		humidity = humidity * ((10000 - altitude) / 10000);

		double temperature = 10000 - altitude;

		int index = 0;
		Biome match = null;
		for(int i = 0; i < biomes.size(); i++) {
			Biome b = biomes.get(i);

			if(temperature >= b.temperature &&
					humidity >= b.humidity) {
				if(match == null) {
					match = b;
					index = i;
					break;
				}
			}

		}

		return new Biome[] { biomes.get(index) };
	}

}
