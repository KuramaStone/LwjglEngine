package me.brook.wonder.managers;

import java.util.HashMap;
import java.util.Map;

import me.brook.wonder.GameEngine;
import me.brook.wonder.Info;
import me.brook.wonder.chunk.Chunk;
import me.brook.wonder.chunk.ChunkLoader;
import me.brook.wonder.chunk.procedural.NoiseGenerator;
import me.brook.wonder.chunk.procedural.PerlinNoise;
import me.brook.wonder.entities.location.Coords;

public class TerrainManager extends Manager {

	// contains chunks to render
	private Map<Coords, Chunk> loaded;
	// contains chunks stored in memory, but not to be rendered
	private Map<Coords, Chunk> unloaded;
	// Chunks to add to loaded next update
	private Map<Coords, Chunk> scheduledAdditions;

	// Seed to create perlin maps
	private NoiseGenerator heightGen;
	private float worldScale = 1000.0f;

	private ChunkLoader loader;

	public TerrainManager(GameEngine engine) {
		super(engine);
		heightGen = new PerlinNoise(Info.SEED, 3, 1f, 0.5f, 2.0f, 0.01f);
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
		
		engine.getLoader().removeVAO(chunk.getRawModel().getVaoID());
	}

	public long getSeed() {
		return Info.SEED;
	}

	public boolean isChunkPrepared(Coords coords) {
		Chunk c = loaded.get(coords);

		if(c == null) {

			c = unloaded.get(coords);

			if(c == null) {
				c = scheduledAdditions.get(coords);
			}
		}

		return c != null && c.getRawModel() != null;
	}

	// Loads chunk to be rendered. Only to be used for prepared chunks
	public void loadPreparedChunk(Chunk chunk) {
		if(chunk.getRawModel() == null) {
			chunk.loadToVao();
		}

		loaded.put(chunk.getCoords(), chunk);
		unloaded.remove(chunk.getCoords());
	}

	// Move loaded chunk to unloaded cache
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
		loader.stop();
	}

	public float getHeightAt(int x, int z) {
		Chunk c = loaded.get(new Coords(x, z));

		if(c != null) {
			return c.calculateHeight(x, z);
		}

		return 0;
	}
	
	public float getWorldScale() {
		return worldScale;
	}

}
