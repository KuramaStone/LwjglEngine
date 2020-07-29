package me.brook.wonder.toolbox;

import static java.lang.Math.toRadians;

import java.text.DecimalFormat;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

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

	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) toRadians(rotation.getX()), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) toRadians(rotation.getY()), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) toRadians(rotation.getZ()), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Player player) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		
		Matrix4f.rotate((float) toRadians(-player.getLocation().getPitch()), new Vector3f(1, 0, 0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate((float) toRadians(-player.getLocation().getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) toRadians(-player.getLocation().getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		
		Vector3f cameraPos = player.getLocation().getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
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

}
