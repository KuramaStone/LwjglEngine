package me.brook.wonder.renderer.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.chunk.Chunk;
import me.brook.wonder.entities.player.Player;
import me.brook.wonder.models.ModelTexture;
import me.brook.wonder.models.RawModel;
import me.brook.wonder.renderer.RendererAbstract;
import me.brook.wonder.shaders.glsl.chunk.ChunkShader;
import me.brook.wonder.toolbox.Maths;

public class ChunkRenderer extends RendererAbstract {

	private ChunkShader shader;

	public ChunkRenderer(GameEngine engine, Player player) {
		super(engine);
		shader = new ChunkShader();

		shader.start();
		shader.loadProjectionMatrix(player.getCamera().getProjectionMatrix());
		shader.loadTextures();
		shader.stop();
	}

	public void render() {
		shader.start();
		shader.loadViewMatrix(Maths.createViewMatrix(engine.getPlayer()));
		shader.loadLight(engine.getManagers().getLightManager().getLights().get(0));

		for(Chunk chunk : engine.getManagers().getTerrainManager().getLoadedChunks().values()) {
			prepareChunk(chunk);
			loadShaderVariables(chunk);
			GL11.glDrawElements(GL11.GL_TRIANGLES, chunk.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain();
		}
		shader.stop();
	}

	private void prepareChunk(Chunk chunk) {
		RawModel rawModel = chunk.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0); // position
		GL20.glEnableVertexAttribArray(1); // textureCoordinates
		GL20.glEnableVertexAttribArray(2); // normal
		
		ModelTexture texture = chunk.getTexture();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}

	private void unbindTerrain() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void loadShaderVariables(Chunk chunk) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				chunk.getLocation().getPosition(), chunk.getLocation().getRotation(), 1); // do not rotate your terrains!
		shader.loadTransformationMatrix(transformationMatrix);
		shader.setShowHeightMap(chunk.shouldShowHeightMap());
	}

	@Override
	public void cleanUp() {
		shader.cleanUp();
	}

}
