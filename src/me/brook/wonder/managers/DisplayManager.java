package me.brook.wonder.managers;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import me.brook.wonder.GameEngine;

public class DisplayManager extends Manager {
	
	private int MAX_FPS = 120;

	private int width = 1280;
	private int height = 720;

	public DisplayManager(GameEngine engine) {
		super(engine);
	}

	public void createDisplay() throws LWJGLException {
		ContextAttribs attribs = new ContextAttribs(3, 2)
				.withForwardCompatible(true)
				.withProfileCore(true);

		Display.setDisplayMode(new DisplayMode(width, height));
		Display.create(new PixelFormat(), attribs);
		Display.setTitle("Wonder");

		GL11.glViewport(0, 0, width, height);
	}

	public void update() {
		Display.sync(MAX_FPS);
		Display.update();
	}

	public void close() {
		Display.destroy();
	}

}
