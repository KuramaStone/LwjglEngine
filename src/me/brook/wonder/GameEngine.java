package me.brook.wonder;

import org.lwjgl.opengl.Display;

import me.brook.wonder.entities.location.Location;
import me.brook.wonder.entities.player.Player;
import me.brook.wonder.renderer.Loader;

public class GameEngine implements Runnable {

	// Essentials
	private Managers managers;
	private Loader loader;

	// Player
	private Player player;

	// Misc
	private boolean running = true;

	public GameEngine() {
		managers = new Managers(this);
		loader = new Loader();
	}

	private void update() {
		if(Display.isCloseRequested()) {
			running = false;
		}
		player.update();
		managers.update();
	}

	public void enable() {
		managers.createDisplayManager();
		player = new Player(this, null, new Location(0, 50, 0, 0, 269, 0.0f));

		running = true;
		managers.createRendererManager();
		managers.createBiomeManager();
		managers.createTerrainManager();
		managers.createEntityManager();
		managers.createLightManager();
		managers.createSkyManager();

	}

	private void disable() {
		loader.cleanUp();
		managers.disable();
		System.out.println(this.getPlayer().getLocation());
	}

	public void run() {

		enable();

		while(running) {
			update();
		}

		disable();
	}

	public static void main(String[] args) {
		new GameEngine().run();
	}

	public Loader getLoader() {
		return loader;
	}

	public Managers getManagers() {
		return managers;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
