package me.brook.wonder.chunk;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.chunk.procedural.NoiseGenerator;
import me.brook.wonder.entities.location.Coords;
import me.brook.wonder.entities.location.Location;
import me.brook.wonder.models.EmptyModel;
import me.brook.wonder.models.ModelTexture;
import me.brook.wonder.models.RawModel;

public class Chunk {

	public static float SIZE = 512;
	private static final float DETAIL = 1f;
	private static final int VERTEX_COUNT = (int) (SIZE * DETAIL);

	private final GameEngine engine;

	private RawModel model;
	private ModelTexture texture;
	private EmptyModel emptyModel;

	private boolean showHeightMap = false;
	// private NoiseGenerator heightGen;
	private BufferedImage biomeMap;

	private Location location;
	private Coords coords;

	private Map<Vector2f, Float> cache;

	public Chunk(GameEngine engine, NoiseGenerator heightGen, Coords coords) {
		this.engine = engine;
		// this.heightGen = heightGen;
		this.coords = coords;
		cache = new HashMap<Vector2f, Float>();

		location = new Location(coords.getX() * SIZE, 0, coords.getZ() * SIZE, 0, 0, 0);

	}

	public EmptyModel generateModel() {

		int vertexes = VERTEX_COUNT + 1;

		int squared = vertexes * vertexes;

		float[] vertices = new float[squared * 3];
		float[] normals = new float[squared * 3];
		float[] textureCoords = new float[squared * 2];
		int[] indices = new int[6 * (vertexes - 1) * (vertexes * 1)];

		biomeMap = new BufferedImage(vertexes, vertexes, BufferedImage.TYPE_INT_RGB);

		int vertexPointer = 0;
		for(int i = 0; i < vertexes; i++) {
			for(int j = 0; j < vertexes; j++) {
				float vx = (float) i / (vertexes - 1) * SIZE;
				float vz = (float) j / (vertexes - 1) * SIZE;

				float x = location.getZ() + vx;
				float z = location.getX() + vz;

				float height = calculateHeight(x, z);

				Vector3f c = engine.getManagers().getBiomeManager().getBiomesAt(x, z)[0].getColor();
				biomeMap.setRGB(i, j, new Color(c.getX(), c.getY(), c.getZ()).getRGB());

				// create vertex position; 3d coords for 1 corner of triangle
				vertices[vertexPointer * 3 + 0] = vz;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = vx;

				Vector3f normal = calculateNormal(x, z);
				normals[vertexPointer * 3 + 0] = normal.z;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.x;

				textureCoords[vertexPointer * 2] = vx;
				textureCoords[vertexPointer * 2 + 1] = vz;
				vertexPointer++;
			}
		}

		int pointer = 0;
		for(int gz = 0; gz < vertexes - 1; gz++) {
			for(int gx = 0; gx < vertexes - 1; gx++) {
				int topLeft = (gz * vertexes) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexes) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}

		clearHeightCache();

		return emptyModel = new EmptyModel(vertices, normals, textureCoords, indices);
	}

	private void clearHeightCache() {
		cache.clear();
	}

	public RawModel loadToVao() {

		return model = engine.getLoader().loadToVAO(emptyModel.getVertices(), emptyModel.getTextureCoords(),
				emptyModel.getNormals(), emptyModel.getIndices());
	}

	public float calculateHeight(float x, float z) {

		Vector2f vec = new Vector2f(x, z);
		if(cache.containsKey(vec)) {
			return cache.get(vec);
		}

		float h = getHeightAt(x, z) * 16;
		cache.put(vec, h);
		return h;
	}

	public float getHeightAt(float x, float z) {
		return engine.getManagers().getBiomeManager().getAverageHeightAt(x, z);
		// return heightGen.getHeightAt(x, z);
	}

	private Vector3f calculateNormal(float x, float z) {
		Vector3f a = new Vector3f(0, calculateHeight(x, z), 0);
		Vector3f b = new Vector3f(0, calculateHeight(x, z + 1), 1);
		Vector3f c = new Vector3f(1, calculateHeight(x + 1, z), 0);

		Vector3f e1 = new Vector3f();
		Vector3f e2 = new Vector3f();
		Vector3f.sub(b, a, e1);
		Vector3f.sub(c, a, e2);

		Vector3f no = new Vector3f();
		Vector3f.cross(e1, e2, no);

		no.normalise();
		return no;

	}

	public int getChunkX() {
		return coords.getX();
	}

	public int getChunkZ() {
		return coords.getZ();
	}

	public Location getLocation() {
		return location.clone();
	}

	public RawModel getRawModel() {
		return model;
	}

	public EmptyModel getEmptyModel() {
		return emptyModel;
	}

	public void loadTexture() {
		texture = new ModelTexture(engine.getLoader().loadTexture(biomeMap));
	}

	public ModelTexture getTexture() {
		if(texture == null) {
			loadTexture();
		}
		return texture;
	}

	public Coords getCoords() {
		return coords;
	}

	public boolean shouldShowHeightMap() {
		return showHeightMap;
	}

}
