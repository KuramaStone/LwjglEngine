package me.brook.wonder.managers;

import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.models.RawModel;
import me.brook.wonder.toolbox.Maths;

public class SkyManager extends Manager {
	
	private Vector3f skyColor = new Vector3f(0f, 0.75f, 1.0f);

	private RawModel skybox;

	public SkyManager(GameEngine engine) {
		super(engine);

		skybox = engine.getLoader().loadToVAO(Maths.CUBE_VERTICES, 3);
	}
	
	public RawModel getSkybox() {
		return skybox;
	}
	
	public Vector3f getSkyColor() {
		return skyColor;
	}

}
