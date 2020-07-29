package me.brook.wonder;

import org.lwjgl.LWJGLException;

import me.brook.wonder.managers.DisplayManager;
import me.brook.wonder.managers.EntityManager;
import me.brook.wonder.managers.Manager;
import me.brook.wonder.managers.RenderManager;

public class Managers extends Manager {

	private DisplayManager display;
	private RenderManager renderer;
	private EntityManager entity;

	public Managers(GameEngine engine) {
		super(engine);
	}

	public void createDisplayManager() {
		display = new DisplayManager(engine);
		try {
			display.createDisplay();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public void createEntityManager() {
		entity = new EntityManager(engine);
	}

	public void createRendererManager() {
		renderer = new RenderManager(engine);
	}

	public DisplayManager getDisplayManager() {
		return display;
	}

	public RenderManager getRendererManager() {
		return renderer;
	}

	public EntityManager getEntityManager() {
		return entity;
	}

	public void update() {
		entity.update();
		renderer.renderEntities();
		
		display.update();
	}

	public void disable() {
		renderer.cleanUp();

		display.close();
	}

}
