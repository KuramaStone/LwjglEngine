package me.brook.wonder.managers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import me.brook.wonder.GameEngine;
import me.brook.wonder.renderer.RendererAbstract;
import me.brook.wonder.renderer.render.EntityRenderer;
import me.brook.wonder.renderer.render.ChunkRenderer;

public class RenderManager extends Manager {

	private List<RendererAbstract> renderers;

	private EntityRenderer entityRenderer;
	private ChunkRenderer terrainRenderer;

	public RenderManager(GameEngine engine) {
		super(engine);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		renderers = new ArrayList<RendererAbstract>();
		renderers.add(entityRenderer = new EntityRenderer(engine, engine.getPlayer()));
		renderers.add(terrainRenderer = new ChunkRenderer(engine, engine.getPlayer()));
	}

	public void render() {
		prepare();

		terrainRenderer.render();
		entityRenderer.render();
	}

	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0, 0, 1, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void cleanUp() {
		renderers.forEach(renderer -> renderer.cleanUp());
	}

}
