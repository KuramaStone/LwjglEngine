package me.brook.wonder.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.brook.wonder.GameEngine;
import me.brook.wonder.entities.Entity;
import me.brook.wonder.entities.Location;
import me.brook.wonder.models.ModelTexture;
import me.brook.wonder.models.TexturedModel;

public class EntityManager extends Manager {

	private Map<UUID, Entity> entities;

	public EntityManager(GameEngine engine) {
		super(engine);
		entities = new HashMap<UUID, Entity>();

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

	boolean first = true;

	public void update() {

		if(first) {
			first = false;

			addEntity(new Entity(engine,
					new TexturedModel(engine.getLoader().loadObjModel("bunny"),
							new ModelTexture(engine.getLoader().loadTexture("res\\uwu.png"))),
					new Location(0, engine.getManagers().getTerrainManager().getHeightAt(0, 0), 0)));
		}

		entities.values().forEach(ent -> {
			ent.rotate(0, 0, 0);
		});

	}

}
