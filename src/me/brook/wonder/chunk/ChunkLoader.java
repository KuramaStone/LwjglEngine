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
	private Thread thread;
	private boolean isThreadRunning = true;

	private int extendedTrimmingRange = 2;
	private int chunksPerSecond = 12;

	public ChunkLoader(GameEngine engine, TerrainManager manager) {
		this.engine = engine;
		this.manager = manager;

		loadingThreadList = new ArrayList<>();
		startThread();
	}

	private long lastChunkLoad;

	private void startThread() {
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(isThreadRunning) {
					try {

						List<Point> list = new ArrayList<Point>(loadingThreadList);
						if(!list.isEmpty() && lastChunkLoad + (1000 / chunksPerSecond) < System.currentTimeMillis()) {
							Point p = list.get(0);

							// If the point has turned null
							if(p == null) {
								loadingThreadList.remove(p);
							}
							// If the point is outside the bounds
							else if(isChunkOOBs(p.x, p.y)) {
								loadingThreadList.remove(p);
							}
							else {
								if(loadChunk(p.x, p.y)) {
									loadingThreadList.remove(p);
								}

							}

							lastChunkLoad = System.currentTimeMillis();

						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	boolean boo = false;

	public void update() {
		// load chunks in the direction the player is facing first.
		int direction = getDirection();
		if(manager.getLoadedChunks().size() == 0) {
			direction = -1;
		}

		// unload chunks not in range
		trimChunks();

		// If the current number of chunks isn't at a max AND others aren't loading,
		// then load more.
		if(!areMaximumChunksLoaded()) {
			loadChunksAt(engine.getPlayer().getLocation().getChunkX(),
					engine.getPlayer().getLocation().getChunkZ(),
					direction);
		}

		if(!manager.getScheduledAdditions().isEmpty()) {
			new ArrayList<>(manager.getScheduledAdditions().values()).forEach(c -> manager.load(c));
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
		removeOOBChunksFromThread();

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
					if(isChunkLoaded(x1 + centerX, y1 + centerY) == null) {
						addChunkToThread(x1 + centerX, y1 + centerY);
					}
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

	private void removeOOBChunksFromThread() {

		for(int i = 0; i < loadingThreadList.size(); i++) {
			Point p = loadingThreadList.get(i);

			if(p != null) {
				if(isChunkOOBs(p.x, p.y)) {
					loadingThreadList.remove(p);
					i--;
				}
			}

		}

	}

	private void addChunkToThread(int x, int y) {

		if(!manager.isChunkGenerated(new Coords(x, y))) {
			Point point = new Point(x, y);
			if(!loadingThreadList.contains(point)) {
				loadingThreadList.add(point);
			}
		}
	}

	public boolean isChunkOOBs(int x, int z) {
		int minX = engine.getPlayer().getLocation().getChunkX() - engine.getPlayer().getCamera().getChunkRange();
		int maxX = engine.getPlayer().getLocation().getChunkX() + engine.getPlayer().getCamera().getChunkRange();

		int minZ = engine.getPlayer().getLocation().getChunkZ() - engine.getPlayer().getCamera().getChunkRange();
		int maxZ = engine.getPlayer().getLocation().getChunkZ() + engine.getPlayer().getCamera().getChunkRange();
		// return false;
		return x < minX || x > maxX ||
				z < minZ || z > maxZ;
	}

	public boolean isChunkOOBs(Chunk chunk) {
		return isChunkOOBs(chunk.getChunkX(), chunk.getChunkZ());
	}

	// If too many chunks are in memory, then save them.
	private void unloadChunk(Chunk chunk) {
		manager.unload(chunk);
	}

	private boolean loadChunk(int chunkX, int chunkY) {

		if(manager.isChunkGenerated(new Coords(chunkX, chunkY))) {
			return false;
		}

		if(!addingPointsToThread) {
			// Try to get chunk from unloaded list
			Chunk chunk = isChunkUnloaded(chunkX, chunkY);

			if(chunk == null) {
				chunk = new Chunk(engine, manager.getHeightGen(), chunkX, chunkY);
			}
			if(chunk.getEmptyModel() == null) {
				chunk.generateModel();
			}
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

	public boolean areMaximumChunksLoaded() {
		int needed = (int) (manager.getLoadedChunks().size()
				- Math.pow((engine.getPlayer().getCamera().getChunkRange() + extendedTrimmingRange) * 2 + 1, 2));
		return needed == 0;
	}

	public void trimChunks() {
		// Unload chunks not within range of this point
		for(Chunk chunk : new ArrayList<>(manager.getLoadedChunks().values())) {
			if(isChunkOOBsForTrimming(chunk)) {
				unloadChunk(chunk);
			}
		}
	}

	private boolean isChunkOOBsForTrimming(int x, int z) {
		int minX = engine.getPlayer().getLocation().getChunkX() - engine.getPlayer().getCamera().getChunkRange()
				- extendedTrimmingRange;
		int maxX = engine.getPlayer().getLocation().getChunkX() + engine.getPlayer().getCamera().getChunkRange()
				+ extendedTrimmingRange;

		int minZ = engine.getPlayer().getLocation().getChunkZ() - engine.getPlayer().getCamera().getChunkRange()
				- extendedTrimmingRange;
		int maxZ = engine.getPlayer().getLocation().getChunkZ() + engine.getPlayer().getCamera().getChunkRange()
				+ extendedTrimmingRange;
		// return false;
		return x < minX || x > maxX ||
				z < minZ || z > maxZ;
	}

	private boolean isChunkOOBsForTrimming(Chunk chunk) {
		return isChunkOOBsForTrimming(chunk.getChunkX(), chunk.getChunkZ());
	}

	public boolean isThreadRunning() {
		return isThreadRunning;
	}

	@SuppressWarnings("deprecation")
	public void setThreadRunning(boolean isThreadRunning) {
		this.isThreadRunning = isThreadRunning;

		if(!isThreadRunning) {
			thread.stop();
		}
		else {
			if(thread != null && thread.isAlive()) {
				thread.stop();
			}
			startThread();
		}
	}

}
