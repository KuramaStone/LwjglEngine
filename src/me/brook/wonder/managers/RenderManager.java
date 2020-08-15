package me.brook.wonder.managers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import me.brook.wonder.GameEngine;
import me.brook.wonder.renderer.RendererAbstract;
import me.brook.wonder.renderer.render.ChunkRenderer;
import me.brook.wonder.renderer.render.EntityRenderer;
import me.brook.wonder.renderer.render.SkyRenderer;

public class RenderManager extends Manager {

	private List<RendererAbstract> renderers;

	private EntityRenderer entityRenderer;
	private ChunkRenderer terrainRenderer;
	private SkyRenderer skyRenderer;

	public RenderManager(GameEngine engine) {
		super(engine);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glFrontFace(GL11.GL_CCW);

		renderers = new ArrayList<RendererAbstract>();
		renderers.add(entityRenderer = new EntityRenderer(engine));
		renderers.add(terrainRenderer = new ChunkRenderer(engine));
		renderers.add(skyRenderer = new SkyRenderer(engine));
	}

	public void render() {
		prepare();

		// Sky renderer 
		skyRenderer.render();
		
		terrainRenderer.render();
		entityRenderer.render();
	}

	private void prepare() {
		enableDepthTest();

		GL11.glClearColor(1, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	public void enableDepthTest() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void clearDepthTest() {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void cleanUp() {
		renderers.forEach(renderer -> renderer.cleanUp());
	}

}
