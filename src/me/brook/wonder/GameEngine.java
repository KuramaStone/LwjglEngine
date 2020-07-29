package me.brook.wonder;

import org.lwjgl.opengl.Display;

import me.brook.wonder.entities.Location;
import me.brook.wonder.entities.player.Player;
import me.brook.wonder.renderer.Loader;

public class GameEngine {

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
		if (Display.isCloseRequested()) {
			running = false;
		}
		player.update();
		managers.update();
	}

	public void enable() {
		managers.createDisplayManager();
		player = new Player(null, new Location(0, 0, 0));

		running = true;
		managers.createRendererManager();
		managers.createEntityManager();

	}

	private void disable() {
		loader.cleanUp();
		managers.disable();
	}

	public void run() {

		enable();

		while (running) {
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

}
