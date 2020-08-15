package me.brook.wonder.chunk;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.chunk.procedural.NoiseGenerator;
import me.brook.wonder.entities.Location;
import me.brook.wonder.models.EmptyModel;
import me.brook.wonder.models.ModelTexture;
import me.brook.wonder.models.RawModel;

public class Chunk {

	public static float SIZE = 100;
	private static final float DETAIL = 1f;
	private static final int VERTEX_COUNT = (int) (SIZE * DETAIL);

	private final GameEngine engine;

	private RawModel model;
	private ModelTexture texture;
	private EmptyModel emptyModel;

	private boolean showHeightMap = true;
	private NoiseGenerator heightGen;

	private Location location;
	private Coords coords;

	public Chunk(GameEngine engine, NoiseGenerator heightGen, int x, int z) {
		this.engine = engine;
		this.heightGen = heightGen;

		location = new Location(x * SIZE, 0, z * SIZE, 0, 0, 0);

		coords = new Coords((int) (location.getX() / SIZE), (int) (location.getZ() / SIZE));
	}

	private static BufferedImage image = new BufferedImage(100 * 5 + 1, 100 * 5 + 1, BufferedImage.TYPE_INT_RGB);

	public EmptyModel generateModel() {
		int vertexes = VERTEX_COUNT + 1;

		int squared = vertexes * vertexes;

		float[] vertices = new float[squared * 3];
		float[] normals = new float[squared * 3];
		float[] textureCoords = new float[squared * 2];
		int[] indices = new int[6 * (vertexes - 1) * (vertexes * 1)];

		int vertexPointer = 0;
		for(int i = 0; i < vertexes; i++) {
			for(int j = 0; j < vertexes; j++) {
				float vx = (float) i / (vertexes - 1) * SIZE;
				float vz = (float) j / (vertexes - 1) * SIZE;

				float x = location.getZ() + vx;
				float z = location.getX() + vz;
				float height = calculateHeight(x, z);

				float f = getHeightAt(x, z);
				image.setRGB((int) x + 200, (int) z + 200, new Color(f, f, f).getRGB());

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

		try {
			ImageIO.write(image, "png", new File("res\\chunks\\rendered.png"));
		}
		catch(Exception e) {
			e.printStackTrace();
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

		return emptyModel = new EmptyModel(vertices, normals, textureCoords, indices);
	}

	public RawModel loadToVao() {

		return model = engine.getLoader().loadToVAO(emptyModel.getVertices(), emptyModel.getTextureCoords(),
				emptyModel.getNormals(), emptyModel.getIndices());
	}

	public float calculateHeight(float x, float z) {
		return getHeightAt(x, z) * 32;
	}

	public float getHeightAt(float x, float z) {
		// Biome[] b = engine.getManagers().getBiomeManager().getBiomeAt(x, z);
		//
		// return engine.getManagers().getBiomeManager().getAverageHeightOfBiomes(b);
		return heightGen.getHeightAt(x, z);
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
