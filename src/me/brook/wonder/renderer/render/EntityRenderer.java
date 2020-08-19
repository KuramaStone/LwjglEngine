package me.brook.wonder.renderer.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.entities.Entity;
import me.brook.wonder.models.TexturedModel;
import me.brook.wonder.renderer.RendererAbstract;
import me.brook.wonder.shaders.glsl.entity.EntityShader;
import me.brook.wonder.toolbox.Maths;

public class EntityRenderer extends RendererAbstract {

	private EntityShader shader;

	public EntityRenderer(GameEngine engine) {
		super(engine);
		shader = new EntityShader();

		shader.start();
		shader.loadProjectionMatrix(engine.getPlayer().getCamera().getProjectionMatrix());

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

			bindEntity(entity);
			loadShaderUniforms(entity);
			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			unbindEntity(entity);
		}

		shader.stop();
	}

	private void unbindEntity(Entity entity) {
		engine.getManagers().getRendererManager().enableCulling();
		// unbind model
		GL30.glBindVertexArray(0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
	}

	private void bindEntity(Entity entity) {

		if(entity.hasTransparency()) {
			engine.getManagers().getRendererManager().disableCulling();
		}
		// prepare model
		GL30.glBindVertexArray(entity.getModel().getModel().getVaoID());
		GL20.glEnableVertexAttribArray(0); // position
		GL20.glEnableVertexAttribArray(1); // textureCoordinates
		GL20.glEnableVertexAttribArray(2); // normals

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getTextureID());
	}

	private void loadShaderUniforms(Entity entity) {
		// prepare shaders
		Matrix4f transformation = Maths.createTransformationMatrix(entity.getLocation(), entity.getScale(), true,
				new Vector3f(0, 0, 0));
		shader.loadTransformationMatrix(transformation);

	}

	@Override
	public void cleanUp() {
		shader.cleanUp();
	}

}
