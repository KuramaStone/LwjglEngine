package me.brook.wonder.managers;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

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
		delta = (float) (DELTA_FPS / fps);
		lastUpdate = System.nanoTime();
	}

	public void screenshot() {

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy+hh-mm-ss");
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
		int width = Display.getWidth();
		int height = Display.getHeight();

		// Creating an rbg array of total pixels
		int[] pixels = new int[width * height];
		int bindex;
		// allocate space for RBG pixels
		ByteBuffer fb = ByteBuffer.allocateDirect(width * height * 3);

		// grab a copy of the current frame contents as RGB
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, fb);

		BufferedImage imageIn = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// convert RGB data in ByteBuffer to integer array
		for(int i = 0; i < pixels.length; i++) {
			bindex = i * 3;
			pixels[i] = ((fb.get(bindex) << 16)) +
					((fb.get(bindex + 1) << 8)) +
					((fb.get(bindex + 2) << 0));
		}
		// Allocate colored pixel to buffered Image
		imageIn.setRGB(0, 0, width, height, pixels, 0, width);

		// Creating the transformation direction (horizontal)
		AffineTransform at = AffineTransform.getScaleInstance(1, -1);
		at.translate(0, -imageIn.getHeight(null));

		// Applying transformation
		AffineTransformOp opRotated = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage imageOut = opRotated.filter(imageIn, null);

		return imageOut;
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
