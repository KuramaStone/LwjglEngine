package me.brook.wonder.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.entities.Entity;
import me.brook.wonder.entities.player.Player;
import me.brook.wonder.shaders.glsl.entity.EntityShader;
import me.brook.wonder.toolbox.Maths;

public class EntityRenderer extends RendererAbstract {

	private EntityShader entityShader;

	public EntityRenderer(GameEngine engine, Player player) {
		super(engine);
		entityShader = new EntityShader();
		entityShader.start();
		entityShader.loadProjectionMatrix(player.getCamera().getProjectionMatrix());
		entityShader.stop();
	}

	@Override
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0, 0, 1, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	public void render() {
		prepare();

		entityShader.start();
		entityShader.loadViewMatrix(Maths.createViewMatrix(engine.getPlayer()));

		for(Entity entity : engine.getManagers().getEntityManager().getEntities().values()) {
			System.out.println(entity.getModel());
			if(entity.getModel() == null) {
				continue;
			}
			GL30.glBindVertexArray(entity.getModel().getModel().getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getTextureID());

			Matrix4f transformation = Maths.createTransformationMatrix(entity.getLocation().getPosition(),
					entity.getLocation().getRotation(), entity.getScale());
			entityShader.loadTransformationMatrix(transformation);

			GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT,
					0);

			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
		}

		entityShader.stop();
	}

	@Override
	public void cleanUp() {
		entityShader.cleanUp();
	}

}
