package me.brook.wonder.chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import me.brook.wonder.GameEngine;
import me.brook.wonder.entities.location.Coords;
import me.brook.wonder.entities.location.Direction;
import me.brook.wonder.managers.TerrainManager;

public class ChunkLoader {

	private GameEngine engine;
	private TerrainManager manager;

	private Thread thread;
	public List<Coords> loadingList;
	private AtomicBoolean addingPointsToThread = new AtomicBoolean(false);
	private int chunksToPreparePerSecond = 100;

	private Map<Direction, Coords[]> spiral;

	public ChunkLoader(GameEngine engine, TerrainManager manager) {
		this.engine = engine;
		this.manager = manager;

		loadSpiral();
		loadingList = new ArrayList<>();

		startThread();
	}

	public void update() {
		trimOutOfBoundsChunks();

		loadChunksAround(engine.getPlayer().getCoords(), Direction.getByRotation(engine.getPlayer().getLocation().getRotation()));
		loadScheduledChunks();
	}

	private void startThread() {
		thread = new Thread(new Runnable() {

			@Override
			public void run() {

				long lastLoad = 0;
				while(true) {
					long millis = 1000 / chunksToPreparePerSecond;

					List<Coords> list = new ArrayList<Coords>(loadingList);
					while(!list.isEmpty()) {
						if(millis + lastLoad <= System.currentTimeMillis()) {
							Coords coords = list.get(0);

							if(shouldChunkBeGenerated(coords) && !isChunkPrepared(coords)) {
								// generate chunk mesh
								Chunk chunk = generateChunk(coords);
								// add chunk to list to be added to LWJGL inside of the main thread
								addChunkToSchedule(chunk);
							}
							list.remove(coords);

							lastLoad = System.currentTimeMillis();
						}
					}

				}

			}
		});
		thread.start();
	}

	protected void addChunkToSchedule(Chunk chunk) {
		manager.scheduleChunkToAdd(chunk);
	}

	/*
	 * Generates chunk meshes
	 */
	protected Chunk generateChunk(Coords coords) {
		Chunk chunk = new Chunk(engine, manager.getHeightGen(), coords);
		chunk.generateModel();

		return chunk;
	}

	// Removes chunks outside of range plus a buffer
	private void trimOutOfBoundsChunks() {

		for(Coords coords : new ArrayList<>(manager.getLoadedChunks().keySet())) {
			if(isChunkOutOfBounds(coords, getChunkRange() + 3)) {
				manager.unload(manager.getLoadedChunks().get(coords));
			}
		}

	}

	private void loadScheduledChunks() {
		new ArrayList<>(manager.getScheduledAdditions().values()).forEach(chunk -> {
			manager.loadPreparedChunk(chunk);
		});
		manager.getScheduledAdditions().clear();
	}

	public void loadChunksAround(Coords coords, Direction direction) {
		addingPointsToThread.set(true);

		// load chunks in pattern.
		for(Coords original : spiral.get(direction)) {
			Coords p = original.add(coords);

			// don't prepare chunks that are already prepared
			if(!isChunkPrepared(p) && shouldChunkBeGenerated(p)) {
				prepareChunk(p);
			}

		}

		addingPointsToThread.set(false);
	}

	/*
	 * Check if the chunk is out of bounds
	 */
	private boolean shouldChunkBeGenerated(Coords coords) {
		if(coords == null) {
			return false;
		}
		return !isChunkOutOfBounds(coords, getChunkRange());
	}

	public boolean isChunkOutOfBounds(Coords coords, int range) {
		Coords player = engine.getPlayer().getCoords();

		int minX = player.getX() - range, maxX = player.getX() + range;
		int minZ = player.getZ() - range, maxZ = player.getZ() + range;
		return coords.getX() < minX || coords.getX() > maxX ||
				coords.getZ() < minZ || coords.getZ() > maxZ;
	}

	/*
	 * Adds coords to loading thread to generate the model information outside of
	 * the LWJGL thread.
	 */
	private void prepareChunk(Coords coords) {
		loadingList.add(coords);
	}

	/*
	 * Returns true if chunk exists in location, and if it has a generated its model
	 */
	private boolean isChunkPrepared(Coords coords) {
		return manager.isChunkPrepared(coords);
	}

	private void loadSpiral() {
		spiral = new HashMap<>();
		int x = 0, z = 0;

		int dx = 0, dz = -1;
		int temp;

		int total = (int) Math.pow(getChunkRange() * 2 + 1, 2);
		Coords[] list = new Coords[total];
		for(int i = 0; i < total; i++) {
			list[i] = new Coords(x, z);

			if(x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
				temp = dx;
				dx = -dz;
				dz = temp;
			}
			x += dx;
			z += dz;
		}
		spiral.put(Direction.EAST, list);

		// rotate each one and then keep

		spiral.put(Direction.SOUTH, list = rotateBy90(list));
		spiral.put(Direction.WEST, list = rotateBy90(list));
		spiral.put(Direction.NORTH, list = rotateBy90(list));
	}

	private Coords[] rotateBy90(Coords[] origin) {
		Coords[] list = new Coords[origin.length];

		for(int i = 0; i < list.length; i++) {
			Coords c = origin[i];

			int x = -c.getZ();
			int z = c.getX();

			list[i] = new Coords(x, z);
		}

		return list;
	}

	private int getChunkRange() {
		return engine.getPlayer().getCamera().getChunkRange();
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		thread.stop();
	}

}
