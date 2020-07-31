package me.brook.wonder.chunk;

import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.chunk.heightmap.PerlinNoise;
import me.brook.wonder.entities.Location;
import me.brook.wonder.models.ModelTexture;
import me.brook.wonder.models.RawModel;

public class Chunk {

	public static float SIZE = 8;
	private static final int VERTEX_COUNT = (int) (SIZE * 1);

	private final GameEngine engine;

	private Location location;
	private RawModel model;
	private ModelTexture texture;
	private boolean showHeightMap = false;

	private PerlinNoise heightGen;
	private long seed;
	private Coords coords;

	public Chunk(GameEngine engine, int x, int z, long seed) {
		this.engine = engine;
		location = new Location(x * SIZE, 0, z * SIZE);
		
		coords = new Coords((int) (location.getX() / SIZE), (int) (location.getZ() / SIZE));
		this.seed = seed;
	}

	public RawModel generateModel() {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		heightGen = new PerlinNoise(8, 0.5f, seed);

		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];

//		System.out.println();
//		System.out.println("loc: " + location.getX());
		int vertexPointer = 0;
		for(int i = 0; i < VERTEX_COUNT; i++) {
			for(int j = 0; j < VERTEX_COUNT; j++) {
				int x = (int) (location.getX() + i);
				int z = (int) (location.getZ() + j);

				float height = heightGen.generateHeight(x, z);
				if(coords.getZ() == 0) {
					if(j == 0) {
//						System.out.println(x + ": " + x);
					}
				}


				vertices[vertexPointer * 3] = -(float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = location.getY() + height;
				vertices[vertexPointer * 3 + 2] = -(float) i / ((float) VERTEX_COUNT - 1) * SIZE;

				Vector3f normal = calculateNormal(x, z);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;

				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}

		int pointer = 0;
		for(int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for(int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}

		model = engine.getLoader().loadToVAO(vertices, textureCoords, normals, indices);
		return model;
	}

	private Vector3f calculateNormal(float x, float z) {
		float heightL = heightGen.generateHeight(x - 1, z);
		float heightR = heightGen.generateHeight(x + 1, z);
		float heightD = heightGen.generateHeight(x, z - 1);
		float heightU = heightGen.generateHeight(x, z + 1);

		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
		// Vector3f a = new Vector3f(0, heightGen.generateHeight(x, z), 0);
		// Vector3f b = new Vector3f(0, heightGen.generateHeight(x, z + 1), 1);
		// Vector3f c = new Vector3f(1, heightGen.generateHeight(x + 1, z), 0);
		//
		// Vector3f e1 = new Vector3f();
		// Vector3f e2 = new Vector3f();
		// Vector3f.sub(b, a, e1);
		// Vector3f.sub(c, a, e2);
		//
		// Vector3f no = new Vector3f();
		// Vector3f.cross(e1, e2, no);
		//
		// no.normalise();
		// return no;

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

	public RawModel getModel() {
		return model;
	}

	public void loadTexture() {
		texture = new ModelTexture(engine.getLoader().loadTexture("res/grass.png"));
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
