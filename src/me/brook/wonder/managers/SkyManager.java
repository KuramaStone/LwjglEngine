package me.brook.wonder.managers;

import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.models.RawModel;
import me.brook.wonder.toolbox.Maths;

public class SkyManager extends Manager {
	
	private Vector3f skyColor = new Vector3f(0f, 0.75f, 1.0f);
	
	private int skyTexture;

	private RawModel skybox;

	public SkyManager(GameEngine engine) {
		super(engine);

		skyTexture = engine.getLoader().loadCubeMap(new String[] {
				"res\\skybox\\night_front.png",
				"res\\skybox\\night_back.png",
				"res\\skybox\\night_top.png", // top
				"res\\skybox\\night_bottom.png", // bottom
				"res\\skybox\\night_left.png",
				"res\\skybox\\night_right.png",
		});
		skybox = engine.getLoader().loadToVAO(Maths.CUBE_VERTICES, 3);
	}
	
	public RawModel getSkybox() {
		return skybox;
	}
	
	public Vector3f getSkyColor() {
		return skyColor;
	}
	
	public int getSkyTexture() {
		return skyTexture;
	}

}
