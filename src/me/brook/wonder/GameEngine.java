package me.brook.wonder;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;

import me.brook.wonder.chunk.procedural.NoiseGenerator;
import me.brook.wonder.chunk.procedural.SimplexNoise;
import me.brook.wonder.entities.Location;
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
		player = new Player(this, null, new Location(-87.819046f, 184.89166f, -44.69425f, 232.30153f, 319.73196f, 0.0f));

		running = true;
		managers.createRendererManager();
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
		BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		NoiseGenerator gen = new SimplexNoise(Info.SEED, 5, 0.1f, 1.0f, 0.5f, 1.0f, 2.5f);
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				float h = gen.getHeightAt(x - 200, y - 200);
				image.setRGB(x, y, new Color(h, h, h).getRGB());
			}
		}

		try {
			ImageIO.write(image, "png", new File("res\\chunks\\noise.png"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}

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
