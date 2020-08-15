package me.brook.wonder;

public class Info {
	
	public static final long SEED = 4123412;

	public static int ALT_OCTAVES = 8;
	public static int HUMID_OCTAVES = 8;
	public static int LAKE_OCTAVES = 8;
	public static int RIVER_OCTAVES = 5;

	public static float SCALE = 100.0f;
	public static float ALT_SCALE = 0.05f * SCALE;
	public static float HUMID_SCALE = 0.5f * SCALE;
	public static float LAKE_SCALE = .1f * SCALE;
	public static float RIVER_SCALE = 0.05f * SCALE;

	// Will apparently be reduced by {Chunk.preloaded_distance} to allow for loading
	// slightly outside the window borders.
	public static final int range = 5;

	public static boolean SAVE = false;
}
