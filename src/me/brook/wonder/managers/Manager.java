package me.brook.wonder.managers;

import me.brook.wonder.GameEngine;

public abstract class Manager {
	
	protected final GameEngine engine;

	public Manager(GameEngine engine) {
		this.engine = engine;
	}

}
