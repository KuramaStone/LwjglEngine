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
import me.brook.wonder.chunk.procedural.SimplexNoise;

public class BiomeManager {

	private List<Biome> biomes;

//	private SimplexNoise altitude = new SimplexNoise(Info.SEED, Info.ALT_OCTAVES, Info.ALT_SCALE, 1);
//	private SimplexNoise humidity = new SimplexNoise(Info.SEED, Info.ALT_OCTAVES, Info.HUMID_SCALE, 1);
//	private SimplexNoise rivers = new SimplexNoise(Info.SEED, Info.RIVER_OCTAVES, Info.RIVER_SCALE, 1);

	public BiomeManager(GameEngine engine) {
		biomes = new ArrayList<>();

		// Add all the biomes

		int temperature = 6500;

		biomes.add(new OceanBiome(engine, temperature, 0));

		temperature = 5000;
		biomes.add(new JungleBiome(engine, temperature, 3000));

		temperature = 3000;
		biomes.add(new TropicalForestBiome(engine, temperature, 4750));
		biomes.add(new SavannaBiome(engine, temperature, 500));
		biomes.add(new DesertBiome(engine, temperature, 0));

		temperature = 500;
		biomes.add(new MountainBiome(engine, temperature, 0));
		temperature = 250;
		biomes.add(new MountainPeakBiome(engine, temperature, 0));

		// Sort the biomes by temperature and then humidity
	}
//
//	public float getAverageHeightOfBiomes(Biome[] biomes, float x, float z) {
//
//		float f = 0;
//		for(Biome b : biomes) {
//			f += b.getHeightAt(x, z);
//		}
//
//		return f;
//	}
//
//	public Biome[] getBiomeAt(float x, float z) {
//		return getBiomesAt(altitude.getSimplex(x, z), humidity.getSimplex(x, z), rivers.getSimplex(x, z), 3);
//	}
//
//	public Biome[] getBiomesAt(double altitude, double humidity, double rivers, int size) {
//
//		// If altitude is correct AND {lakes} is high enough.
//
//		// 200t is 1 degree C. 0t is 30C, 4000t is 0C, and 10000t is -20C
//		double temp = humidity;
//		humidity = humidity * ((10000 - altitude) / 10000);
//
//		double temperature = 10000 - altitude;
//
//		int index = 0;
//		Biome match = null;
//		for(int i = 0; i < biomes.size(); i++) {
//			Biome b = biomes.get(i);
//
//			if(temperature >= b.temperature &&
//					humidity >= b.humidity) {
//				if(match == null) {
//					match = b;
//					index = i;
//					break;
//				}
//			}
//
//		}
//
//		Biome[] array = new Biome[size];
//
//		// don't overflow
//		if(index + size >= biomes.size()) {
//			index = biomes.size() - size - 1;
//		}
//		// dont underflow
//		if(index - size < 0) {
//			index = size;
//		}
//
//		int c = 0;
//		for(int i = -size; i <= size; i++) {
//			array[c++] = biomes.get(index + i);
//		}
//
//		return array;
//	}

}
