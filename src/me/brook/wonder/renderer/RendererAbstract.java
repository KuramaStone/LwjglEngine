package me.brook.wonder.renderer;

import me.brook.wonder.GameEngine;
import me.brook.wonder.managers.Manager;

public abstract class RendererAbstract extends Manager {

	public RendererAbstract(GameEngine engine) {
		super(engine);
	}
	public abstract void cleanUp();

}
