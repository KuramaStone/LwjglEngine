package me.brook.wonder;

import org.lwjgl.LWJGLException;

import me.brook.wonder.managers.BiomeManager;
import me.brook.wonder.managers.DisplayManager;
import me.brook.wonder.managers.EntityManager;
import me.brook.wonder.managers.LightManager;
import me.brook.wonder.managers.Manager;
import me.brook.wonder.managers.RenderManager;
import me.brook.wonder.managers.SkyManager;
import me.brook.wonder.managers.TerrainManager;

public class Managers extends Manager {

	private DisplayManager display;
	private RenderManager renderer;
	private EntityManager entity;
	private TerrainManager terrain;
	private LightManager lightManager;
	private SkyManager skyManager;
	private BiomeManager biomeManager;

	public Managers(GameEngine engine) {
		super(engine);
	}
	
	public void createDisplayManager() {
		display = new DisplayManager(engine);
		try {
			display.createDisplay();
		}
		catch(LWJGLException e) {
			e.printStackTrace();
		}
	}

	public void createTerrainManager() {
		terrain = new TerrainManager(engine);
	}

	public void createEntityManager() {
		entity = new EntityManager(engine);
	}

	public void createRendererManager() {
		renderer = new RenderManager(engine);
	}

	public void createLightManager() {
		lightManager = new LightManager(engine);
	}
	
	public void createSkyManager() {
		skyManager = new SkyManager(engine);
	}
	
	public void createBiomeManager() {
		biomeManager = new BiomeManager(engine);
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
	
	public TerrainManager getTerrainManager() {
		return terrain;
	}
	
	public LightManager getLightManager() {
		return lightManager;
	}
	
	public SkyManager getSkyManager() {
		return skyManager;
	}
	
	public BiomeManager getBiomeManager() {
		return biomeManager;
	}

	public void update() {
		terrain.update();
		entity.update();
		lightManager.update();
		renderer.render();

		display.update();
	}

	public void disable() {
		renderer.cleanUp();
		terrain.cleanUp();

		display.close();
	}

}
