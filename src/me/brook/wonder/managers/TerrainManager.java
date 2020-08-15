package me.brook.wonder.managers;

import java.util.HashMap;
import java.util.Map;

import me.brook.wonder.GameEngine;
import me.brook.wonder.Info;
import me.brook.wonder.chunk.Chunk;
import me.brook.wonder.chunk.ChunkLoader;
import me.brook.wonder.chunk.Coords;
import me.brook.wonder.chunk.procedural.NoiseGenerator;
import me.brook.wonder.chunk.procedural.SimplexNoise;

public class TerrainManager extends Manager {

	// contains chunks to render
	private Map<Coords, Chunk> loaded;
	// contains chunks stored in memory, but not to be rendered
	private Map<Coords, Chunk> unloaded;
	// Chunks to add to loaded next update
	private Map<Coords, Chunk> scheduledAdditions;

	// Seed to create perlin maps
	private NoiseGenerator heightGen;

	private ChunkLoader loader;

	public TerrainManager(GameEngine engine) {
		super(engine);
		heightGen = new SimplexNoise(Info.SEED, 5, 0.1f, 1.0f, 0.5f, 1.0f, 2.5f);
		loaded = new HashMap<Coords, Chunk>();
		unloaded = new HashMap<Coords, Chunk>();
		scheduledAdditions = new HashMap<Coords, Chunk>();

		loader = new ChunkLoader(engine, this);
		loader.update();
	}

	int i = 0;

	public void update() {
		i++;
		loader.update();
	}

	public Map<Coords, Chunk> getLoadedChunks() {
		return loaded;
	}

	public Map<Coords, Chunk> getUnloadedChunks() {
		return unloaded;
	}

	public void remove(Chunk chunk) {
		loaded.remove(new Coords(chunk.getChunkX(), chunk.getChunkZ()));
		unloaded.remove(new Coords(chunk.getChunkX(), chunk.getChunkZ()));
	}

	public long getSeed() {
		return Info.SEED;
	}

	public boolean isChunkGenerated(Coords coords) {
		Chunk c = loaded.get(coords);

		if(c == null) {

			c = unloaded.get(coords);

			if(c == null) {
				c = scheduledAdditions.get(coords);
			}
		}

		return c != null && c.getRawModel() != null;
	}

	public void load(Chunk chunk) {
		if(chunk.getRawModel() == null) {
			chunk.loadToVao();
		}

		loaded.put(chunk.getCoords(), chunk);
		unloaded.remove(chunk.getCoords());
	}

	public void unload(Chunk chunk) {
		loaded.remove(chunk.getCoords());
		unloaded.put(chunk.getCoords(), chunk);
	}

	public void scheduleChunkToAdd(Chunk chunk) {
		scheduledAdditions.put(chunk.getCoords(), chunk);
	}

	public Map<Coords, Chunk> getScheduledAdditions() {
		return scheduledAdditions;
	}

	public NoiseGenerator getHeightGen() {
		return heightGen;
	}

	@Override
	public void cleanUp() {
		loader.setThreadRunning(false);
	}

	public float getHeightAt(int x, int z) {
		Chunk c = loaded.get(new Coords(x, z));

		if(c != null) {
			return c.calculateHeight(x, z);
		}

		return 0;
	}

}
