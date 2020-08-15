package me.brook.wonder.managers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

import me.brook.wonder.GameEngine;

public class DisplayManager extends Manager {

	private int MAX_FPS = 120;
	private int DELTA_FPS = 60;

	private int width = 1280;
	private int height = 720;

	private List<Double> lastFPS;
	private int averageFPS;
	private long lastUpdate;

	private float delta = 1;

	public DisplayManager(GameEngine engine) {
		super(engine);
		lastFPS = new ArrayList<Double>();
	}

	public void createDisplay() throws LWJGLException {
		ContextAttribs attribs = new ContextAttribs(3, 2)
				.withForwardCompatible(true)
				.withProfileCore(true);

		Display.setDisplayMode(new DisplayMode(width, height));
		Display.create(new PixelFormat().withSamples(8), attribs);
		Display.setTitle("Wonder");
		GL11.glEnable(GL13.GL_MULTISAMPLE);

		GL11.glViewport(0, 0, width, height);

	}

	public void update() {
		Display.sync(MAX_FPS);
		Display.update();

		double fps = (1e9 / (System.nanoTime() - lastUpdate));
		lastFPS.add(fps);
		if(lastFPS.size() == 1000) {
			lastFPS.remove(0);
		}
		double sum = 0;
		for(double d : lastFPS) {
			sum += d;
		}
		averageFPS = (int) (sum / 1000);
		// System.out.println(averageFPS);
		delta = (float) (DELTA_FPS / fps);
		lastUpdate = System.nanoTime();
	}

	public void screenshot() {

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy+HH-mm-ss");
		String name = sdf.format(new Date(System.currentTimeMillis()));

		File file = new File("screenshots\\" + name + ".png");

		int i = 0;
		while(file.exists()) {
			file = new File("screenshots\\" + name + "_" + i++ + ".png");
		}
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		}
		catch(IOException e1) {
			e1.printStackTrace();
		}

		try {// Try to screate image, else show exception.
			ImageIO.write(getScreen(), "png", file);
			System.out.printf("Created screenshot '%s'\n", name);
		}
		catch(Exception e) {
			System.out.println("ScreenShot() exception: " + e);
		}
	}

	public BufferedImage getScreen() {
		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}

		return image;
	}

	public void close() {
		Display.destroy();
	}

	public float getTimeDelta() {
		return delta;
	}

	public int getFps() {
		return averageFPS;
	}

}
