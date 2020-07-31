package me.brook.wonder.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.brook.wonder.GameEngine;
import me.brook.wonder.chunk.Chunk;
import me.brook.wonder.chunk.ChunkLoader;
import me.brook.wonder.chunk.Coords;

public class TerrainManager extends Manager {

	// contains chunks to render
	private Map<Coords, Chunk> loaded;
	// contains chunks stored in memory, but not to be rendered
	private Map<Coords, Chunk> unloaded;
	// Chunks to add to loaded next update
	private List<Chunk> scheduledAdditions;

	// Seed to create perlin maps
	private long seed = 12345;

	private ChunkLoader loader;

	public TerrainManager(GameEngine engine) {
		super(engine);
		loaded = new HashMap<Coords, Chunk>();
		unloaded = new HashMap<Coords, Chunk>();
		scheduledAdditions = new ArrayList<Chunk>();

		loader = new ChunkLoader(engine, this);
	}

	public void update() {

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
		return seed;
	}

	public void load(Chunk chunk) {
		loaded.put(chunk.getCoords(), chunk);
		unloaded.remove(chunk.getCoords());
	}

	public void unload(Chunk chunk) {
		loaded.remove(chunk.getCoords());
		unloaded.put(chunk.getCoords(), chunk);
	}

	public void scheduleChunkToAdd(Chunk chunk) {
		scheduledAdditions.add(chunk);
	}
	
	public List<Chunk> getScheduledAdditions() {
		return scheduledAdditions;
	}

}
