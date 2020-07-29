package me.brook.wonder.managers;

import java.util.ArrayList;
import java.util.List;

import me.brook.wonder.GameEngine;
import me.brook.wonder.renderer.EntityRenderer;
import me.brook.wonder.renderer.RendererAbstract;

public class RenderManager extends Manager {

	private List<RendererAbstract> renderers;

	private EntityRenderer entityRenderer;
	
	public RenderManager(GameEngine engine) {
		super(engine);
		renderers = new ArrayList<RendererAbstract>();
		renderers.add(entityRenderer = new EntityRenderer(engine, engine.getPlayer()));
	}

	public void renderEntities() {
		entityRenderer.render();
	}

	public void cleanUp() {
		renderers.forEach(shader -> shader.cleanUp());

		entityRenderer.cleanUp();
	}
	
}
