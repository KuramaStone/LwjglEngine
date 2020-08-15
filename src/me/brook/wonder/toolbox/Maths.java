package me.brook.wonder.toolbox;

import java.text.DecimalFormat;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.entities.Location;
import me.brook.wonder.entities.player.Player;

public class Maths {

	private static DecimalFormat df = new DecimalFormat("00");

	public static final int SECONDS_IN_DAY = 86400;

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Location location, float scale,
			boolean translateFirst, Vector3f origin) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		if(translateFirst)
			Matrix4f.translate(location.getPosition(), matrix, matrix);

		// add origin
		Matrix4f.translate(origin, matrix, matrix);

		Matrix4f.rotate((float) Math.toRadians(location.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(location.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(location.getRoll()), new Vector3f(0, 0, 1), matrix, matrix);
		// subtract origin
		Matrix4f.translate(new Vector3f(-origin.x, -origin.y, -origin.z), matrix, matrix);

		if(!translateFirst)
			Matrix4f.translate(location.getPosition(), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}

	public static Matrix4f createViewMatrix(Player player) {
		return createViewMatrix(player, true);
	}

	public static Matrix4f createViewMatrix(Player player, boolean withRotation) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(-player.getLocation().getPitch()), new Vector3f(1, 0, 0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(-player.getLocation().getYaw()), new Vector3f(0, 1, 0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(-player.getLocation().getRoll()), new Vector3f(0, 0, 1), viewMatrix,
				viewMatrix);
		if(withRotation) {
			Vector3f cameraPos = player.getLocation().getPosition();
			Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
			Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		}
		return viewMatrix;
	}

	public static String getTime(int seconds) {
		int hours = seconds / 60 / 60;
		seconds = seconds - hours * 60 * 60;
		int minutes = seconds / 60;

		String ante = "AM";

		if(hours >= 12) {
			ante = "PM";
			hours -= 12;
		}

		return hours + ":" + df.format(minutes) + ante;
	}

	private static final float SIZE = 1000;

	public static final float[] CUBE_VERTICES = {
			-SIZE, SIZE, -SIZE,
			-SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,

			SIZE, -SIZE, -SIZE,
			SIZE, SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE,
			-SIZE, -SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,

			-SIZE, SIZE, -SIZE,
			-SIZE, SIZE, SIZE,
			-SIZE, -SIZE, SIZE,

			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, SIZE,
			SIZE, SIZE, SIZE,

			SIZE, SIZE, SIZE,
			SIZE, SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,

			-SIZE, -SIZE, SIZE,
			-SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,

			SIZE, SIZE, SIZE,
			SIZE, -SIZE, SIZE,
			-SIZE, -SIZE, SIZE,

			-SIZE, SIZE, -SIZE,
			SIZE, SIZE, -SIZE,
			SIZE, SIZE, SIZE,

			SIZE, SIZE, SIZE,
			-SIZE, SIZE, SIZE,
			-SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE, SIZE,
			SIZE, -SIZE, -SIZE,

			SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE, SIZE,
			SIZE, -SIZE, SIZE };

}
