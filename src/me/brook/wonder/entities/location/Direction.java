package me.brook.wonder.entities.location;

import org.lwjgl.util.vector.Vector3f;

public enum Direction {
	NORTH, SOUTH, EAST, WEST;

	public static Direction getByRotation(Vector3f rotation) {

		float yaw = rotation.getX();

		if(yaw > 45 && yaw < 45) {
			return Direction.NORTH;
		}
		else if(yaw < -90 && yaw > -135) {
			return Direction.WEST;
		}
		else if(yaw < -135 && yaw > 135) {
			return Direction.SOUTH;
		}
		else {
			return Direction.EAST;
		}

	}
}
