package me.brook.wonder.renderer.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.entities.Entity;
import me.brook.wonder.entities.player.Player;
import me.brook.wonder.models.TexturedModel;
import me.brook.wonder.renderer.RendererAbstract;
import me.brook.wonder.shaders.glsl.entity.EntityShader;
import me.brook.wonder.toolbox.Maths;

public class EntityRenderer extends RendererAbstract {

	private EntityShader shader;

	public EntityRenderer(GameEngine engine, Player player) {
		super(engine);
		shader = new EntityShader();
		
		shader.start();
		shader.loadProjectionMatrix(player.getCamera().getProjectionMatrix());
		shader.stop();
	}

	public void render() {
		shader.start();
		shader.loadViewMatrix(Maths.createViewMatrix(engine.getPlayer()));

		for(Entity entity : engine.getManagers().getEntityManager().getEntities().values()) {
			TexturedModel model = entity.getModel();
			if(model == null) {
				continue;
			}

			// prepare model
			GL30.glBindVertexArray(model.getModel().getVaoID());
			GL20.glEnableVertexAttribArray(0); // position
			GL20.glEnableVertexAttribArray(1); // textureCoordinates
			GL20.glEnableVertexAttribArray(2); // normal
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());

			// prepare shaders
			Matrix4f transformation = Maths.createTransformationMatrix(entity.getLocation().getPosition(),
					entity.getLocation().getRotation(), entity.getScale());
			shader.loadTransformationMatrix(transformation);

			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			// unbind model
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			GL30.glBindVertexArray(0);
		}

		shader.stop();
	}

	@Override
	public void cleanUp() {
		shader.cleanUp();
	}

}
