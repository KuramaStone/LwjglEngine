package me.brook.wonder.renderer.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.chunk.Chunk;
import me.brook.wonder.models.ModelTexture;
import me.brook.wonder.models.RawModel;
import me.brook.wonder.renderer.RendererAbstract;
import me.brook.wonder.shaders.glsl.chunk.ChunkShader;
import me.brook.wonder.toolbox.Maths;

public class ChunkRenderer extends RendererAbstract {

	private ChunkShader shader;

	public ChunkRenderer(GameEngine engine) {
		super(engine);
		shader = new ChunkShader();

		shader.start();
		shader.loadProjectionMatrix(engine.getPlayer().getCamera().getProjectionMatrix());
		shader.loadTextures();
		shader.stop();
	}

	public void render() {
		shader.start();
		shader.loadViewMatrix(Maths.createViewMatrix(engine.getPlayer()));
		shader.loadLight(engine.getManagers().getLightManager().getLights().get(0));

		for(Chunk chunk : engine.getManagers().getTerrainManager().getLoadedChunks().values()) {
			RawModel model = chunk.getRawModel();

			bindChunk(chunk);
			loadShaderUniforms(chunk);
			// GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			unbindChunk();
		}
		shader.stop();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}

	private void bindChunk(Chunk chunk) {
		RawModel rawModel = chunk.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0); // position
		GL20.glEnableVertexAttribArray(1); // textureCoordinates
		GL20.glEnableVertexAttribArray(2); // normal

		ModelTexture texture = chunk.getTexture();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}

	private void unbindChunk() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL13.glActiveTexture(0);
	}

	private void loadShaderUniforms(Chunk chunk) {
		Matrix4f transformation = Maths.createTransformationMatrix(chunk.getLocation(), 1, true,
				new Vector3f(Chunk.SIZE / 2, 0, Chunk.SIZE / 2));
		shader.loadTransformationMatrix(transformation);
		shader.setShowHeightMap(chunk.shouldShowHeightMap());
		shader.loadCoords(chunk.getCoords());
	}

	@Override
	public void cleanUp() {
		shader.cleanUp();
	}

}
