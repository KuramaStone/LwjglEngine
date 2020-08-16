package me.brook.wonder.renderer.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.entities.location.Location;
import me.brook.wonder.models.RawModel;
import me.brook.wonder.renderer.RendererAbstract;
import me.brook.wonder.shaders.glsl.skybox.SkyboxShader;
import me.brook.wonder.toolbox.Maths;

public class SkyRenderer extends RendererAbstract {

	private SkyboxShader shader;

	private Vector3f[] points;

	public SkyRenderer(GameEngine engine) {
		super(engine);

		shader = new SkyboxShader();
		shader.start();
		shader.loadProjectionMatrix(engine.getPlayer().getCamera().getProjectionMatrix());
		shader.stop();

		Random random = new Random(1234);
		points = new Vector3f[1000];
		for(int i = 0; i < points.length; i++) {
			float x = (random.nextFloat() * 2 - 1) * 1000;
			float y = (random.nextFloat() * 2 - 1) * 1000;
			float z = (random.nextFloat() * 2 - 1) * 1000;
			points[i] = new Vector3f(x, y, z);
		}
	}

	public void render() {
		RawModel skybox = engine.getManagers().getSkyManager().getSkybox();

		// disable depth test for this.
		engine.getManagers().getRendererManager().clearDepthTest();

		shader.start();
		// we make a view matrix with no movement relative to player, but it does
		// rotate.
		shader.loadViewMatrix(Maths.createViewMatrix(engine.getPlayer(), false));

		bindSkybox();
		loadShaderUniforms();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, skybox.getVertexCount());

		unbindSkybox();

		shader.stop();
		engine.getManagers().getRendererManager().enableDepthTest();
	}

	private void loadShaderUniforms() {
		// prepare shaders
		Matrix4f transformation = Maths.createTransformationMatrix(new Location(0, 10, 0, 0, 0, 0), 1, true,
				new Vector3f(0, 0, 0));
		shader.loadTransformationMatrix(transformation);
		shader.loadSkyColor(engine.getManagers().getSkyManager().getSkyColor());

	}

	private void unbindSkybox() {
		// unbind model
		GL30.glBindVertexArray(0);
		GL20.glDisableVertexAttribArray(0);
	}

	private void bindSkybox() {
		RawModel skybox = engine.getManagers().getSkyManager().getSkybox();
		// prepare model
		GL30.glBindVertexArray(skybox.getVaoID());
		GL20.glEnableVertexAttribArray(0); // position
	}

	@Override
	public void cleanUp() {
		shader.cleanUp();
	}

}
