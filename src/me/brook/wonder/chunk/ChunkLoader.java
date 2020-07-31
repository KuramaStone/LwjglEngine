package me.brook.wonder.chunk;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import me.brook.wonder.GameEngine;
import me.brook.wonder.managers.TerrainManager;

public class ChunkLoader {

	// constructor stuff
	private GameEngine engine;
	private TerrainManager manager;

	// Information
	public List<Point> loadingThreadList;
	private boolean addingPointsToThread = false;

	private long lastChunkLoad;

	public ChunkLoader(GameEngine engine, TerrainManager manager) {
		this.engine = engine;
		this.manager = manager;

		loadingThreadList = new ArrayList<>();
	}

	// Slowly load the chunks. 1 per second or so.
	private void loadFromThreadList() {

		if(!loadingThreadList.isEmpty() && lastChunkLoad < System.currentTimeMillis() + (1000 / 2)) {
			Point p = loadingThreadList.get(0);

			// If the point has turned null
			if(p == null) {
				loadingThreadList.remove(0);
			}
			// If the point is outside the bounds
			else if(isChunkOOBs(p.x, p.y)) {
				loadingThreadList.remove(0);
			}
			else {
				if(loadChunk(p.x, p.y)) {
					loadingThreadList.remove(0);
				}
			}

			lastChunkLoad = System.currentTimeMillis();
		}
	}

	boolean boo = false;

	public void update() {
		// if(boo) {
		// return;
		// }
		// loadChunk(0, 0);
		// loadChunk(0, 1);
		// boo = true;
		// if(boo) {
		// return;
		// }
		loadFromThreadList();
		// load chunks in the direction the player is facing first.
		int direction = getDirection();

		// unload chunks not in range
		trimChunks();
		// If the current number of chunks isn't at a max AND others aren't loading,
		// then load more.
		if(shouldRefresh()) {
			loadChunksAt(engine.getPlayer().getLocation().getChunkX(),
					engine.getPlayer().getLocation().getChunkZ(),
					direction);
		}

		if(!manager.getScheduledAdditions().isEmpty()) {
			manager.getScheduledAdditions().forEach(c -> manager.load(c));
			manager.getScheduledAdditions().clear();
		}
	}

	private int getDirection() {
		float yaw = engine.getPlayer().getLocation().getYaw();

		if(yaw <= 45 && yaw >= 315) {
			return 0;
		}
		else if(yaw <= 225 && yaw > 135) {
			return 1;
		}
		else if(yaw < 135) {
			return 2;
		}
		else {
			return 3;
		}
	}

	public void loadChunksAt(int centerX, int centerY, int mode) {
		// System.out.println(centerX + " " + centerY);

		addingPointsToThread = true;

		// Remove loadingPoints that are now outside the bounds
		loadingThreadList.clear();

		// Add chunks to load to the list in a spiral pattern from the center.
		int total = (int) Math.pow(engine.getPlayer().getCamera().getChunkRange() * 2 + 1, 2);

		// Shift slightly to the appearance of the apparent center to look like the
		// center.

		int x = 0, y = 0;

		int dx = 0, dy = -1;
		int temp;

		if(mode == -1) {
			for(int i = 0; i < total; i++) {
				addChunkToThread(x + centerX, y + centerY);

				if(x == y || (x < 0 && x == -y) || (x > 0 && x == 1 - y)) {
					temp = dx;
					dx = -dy;
					dy = temp;
				}
				x += dx;
				y += dy;
			}
		}
		else if(mode == 0) { // Top to bottom loading
			for(int y1 = -engine.getPlayer().getCamera().getChunkRange(); y1 < engine.getPlayer().getCamera().getChunkRange()
					+ 1; y1++) {
				for(int x1 = -engine.getPlayer().getCamera().getChunkRange(); x1 < engine.getPlayer().getCamera()
						.getChunkRange() + 1; x1++) {
					addChunkToThread(x1 + centerX, y1 + centerY);
				}
			}
		}
		else if(mode == 1) { // Right to left loading
			for(int x1 = engine.getPlayer().getCamera().getChunkRange(); x1 > -engine.getPlayer().getCamera().getChunkRange()
					- 1; x1--) {
				for(int y1 = -engine.getPlayer().getCamera().getChunkRange(); y1 < engine.getPlayer().getCamera()
						.getChunkRange() + 1; y1++) {
					addChunkToThread(x1 + centerX, y1 + centerY);
				}
			}
		}
		else if(mode == 2) { // Bottom to top loading
			for(int y1 = engine.getPlayer().getCamera().getChunkRange(); y1 > -engine.getPlayer().getCamera().getChunkRange()
					- 1; y1--) {
				for(int x1 = -engine.getPlayer().getCamera().getChunkRange(); x1 < engine.getPlayer().getCamera()
						.getChunkRange() + 1; x1++) {
					addChunkToThread(x1 + centerX, y1 + centerY);
				}
			}
		}
		else if(mode == 3) { // Left to right loading
			for(int x1 = -engine.getPlayer().getCamera().getChunkRange(); x1 < engine.getPlayer().getCamera().getChunkRange()
					+ 1; x1++) {
				for(int y1 = -engine.getPlayer().getCamera().getChunkRange(); y1 < engine.getPlayer().getCamera()
						.getChunkRange() + 1; y1++) {
					addChunkToThread(x1 + centerX, y1 + centerY);
				}
			}
		}

		addingPointsToThread = false;
	}

	private void addChunkToThread(int x, int y) {

		if(isChunkLoaded(x, y) == null) {
			Point point = new Point(x, y);
			if(!loadingThreadList.contains(point)) {
				loadingThreadList.add(point);
			}
		}
	}

	private boolean isChunkOOBs(int x, int z) {
		int minX = engine.getPlayer().getLocation().getChunkX() - engine.getPlayer().getCamera().getChunkRange();
		int maxX = engine.getPlayer().getLocation().getChunkX() + engine.getPlayer().getCamera().getChunkRange();

		int minZ = engine.getPlayer().getLocation().getChunkZ() - engine.getPlayer().getCamera().getChunkRange();
		int maxZ = engine.getPlayer().getLocation().getChunkZ() + engine.getPlayer().getCamera().getChunkRange();
		return x < minX || x > maxX ||
				z < minZ || z > maxZ;
	}

	private boolean isChunkOOBs(Chunk chunk) {
		return isChunkOOBs(chunk.getChunkX(), chunk.getChunkZ());
	}

	// If too many chunks are in memory, then save them.
	private void unloadChunk(Chunk chunk) {
		manager.unload(chunk);
	}

	private boolean loadChunk(int chunkX, int chunkY) {

		// Try to get chunk from unloaded list
		Chunk chunk = isChunkUnloaded(chunkX, chunkY);

		// If not found, then try to load from file.
		// if(chunk == null) {
		// if(hasChunkBeenSaved(chunkX, chunkY)) {
		// String id = getChunkName(chunkX, chunkY);
		// Image image = Map.loadMap(id, fileChunks);
		//
		// chunk = new Chunk(biomes, chunkX, chunkY, manager.getSeed(), image);
		// }
		// }

		if(chunk == null) {
			chunk = new Chunk(engine, chunkX, chunkY, manager.getSeed());
		}

		if(!addingPointsToThread) {
			chunk.generateModel();
			manager.scheduleChunkToAdd(chunk);
			// if(Info.SAVE && !hasChunkBeenSaved(chunk.getChunkX(), chunk.getChunkZ())) {
			// saveChunk(chunk);
			// }
			return true;
		}

		return false;
	}
	//
	// private boolean hasChunkBeenSaved(int chunkX, int chunkY) {
	// String id = getChunkName(chunkX, chunkY);
	// return new File(fileChunks, id).exists();
	// }

	// private void saveChunk(Chunk chunk) throws IOException {
	// String id = getChunkName(chunk);
	// Map.saveMap(chunk.getImage(), id, fileChunks);
	// }
	//
	// private String getChunkName(Chunk chunk) {
	// return getChunkName(chunk.getChunkX(), chunk.getChunkZ());
	// }
	//
	// private String getChunkName(int x, int y) {
	// String id = String.format("%s_%s.png", x, y);
	// return id;
	// }

	public Chunk isChunkLoaded(int i, int j) {
		for(Chunk chunk : manager.getLoadedChunks().values()) {
			if(chunk.getChunkX() == i && chunk.getChunkZ() == j) {
				return chunk;
			}
		}

		return null;
	}

	public Chunk isChunkUnloaded(int i, int j) {
		for(Chunk chunk : manager.getUnloadedChunks().values()) {
			if(chunk.getChunkX() == i && chunk.getChunkZ() == j) {
				return chunk;
			}
		}

		return null;
	}

	public boolean shouldRefresh() {
		int needed = (int) (manager.getLoadedChunks().size()
				- Math.pow(engine.getPlayer().getCamera().getChunkRange() * 2 + 1, 2));
		return needed != 0
				&& this.loadingThreadList.size() == 0;
	}

	public void trimChunks() {
		// Unload chunks not within range of this point
		for(Chunk chunk : new ArrayList<>(manager.getLoadedChunks().values())) {
			if(isChunkOOBs(chunk)) {
				unloadChunk(chunk);
			}
		}
	}

}
