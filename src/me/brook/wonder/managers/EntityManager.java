package me.brook.wonder.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.brook.wonder.GameEngine;
import me.brook.wonder.entities.Entity;

public class EntityManager extends Manager {

	private Map<UUID, Entity> entities;

	public EntityManager(GameEngine engine) {
		super(engine);
		entities = new HashMap<UUID, Entity>();

		// addEntity(
		// new Entity(
		// new TexturedModel(
		// engine.getLoader().loadToVAO(
		// new float[] { -0.5f, 0.5f, 0, -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, 0.5f, 0.5f, 0f
		// },
		// new float[] { 0, 0, 0, 1, 1, 1, 1, 0 }, new int[] { 0, 1, 3, 3, 1, 2 }),
		// new ModelTexture(engine.getLoader().loadTexture("res/uwu.png"))),
		// new Location(0, 0, -2)));

	}

	public void addEntity(Entity entity) {
		UUID uuid = UUID.randomUUID();
		while(entities.containsKey(uuid)) {
			uuid = UUID.randomUUID();
		}

		entities.put(uuid, entity);
	}

	public Map<UUID, Entity> getEntities() {
		return entities;
	}

	public void update() {
		entities.values().forEach(ent -> {
		});

	}

}
