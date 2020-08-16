package me.brook.wonder.managers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.entities.light.Light;
import me.brook.wonder.entities.location.Location;

public class LightManager extends Manager {

	private List<Light> lights;

	public LightManager(GameEngine engine) {
		super(engine);
		lights = new ArrayList<Light>();
		lights.add(new Light(new Location(-64, 100, -64), new Vector3f(1, 1, 1), false));
		
	}

	public void update() {
		lights.forEach(light -> light.update());
	}

	public List<Light> getLights() {
		return lights;
	}

}
