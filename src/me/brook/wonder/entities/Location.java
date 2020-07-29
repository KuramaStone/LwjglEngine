package me.brook.wonder.entities;

import org.lwjgl.util.vector.Vector3f;

public class Location {

	private Vector3f position;
	private Vector3f rotation; // yaw, pitch, and roll

	public Location(float x, float y, float z) {
		this(new Vector3f(x, y, z));
	}

	public Location(Vector3f location) {
		this(location, new Vector3f());
	}

	public Location(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	public void move(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}

	public void rotate(float x, float y, float z) {
		rotation.x += x;
		rotation.y += y;
		rotation.z += z;
	}

	public float getX() {
		return position.getX();
	}

	public float getY() {
		return position.getY();
	}

	public float getZ() {
		return position.getZ();
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getYaw() {
		return rotation.getX();
	}

	public float getPitch() {
		return rotation.getY();
	}

	public float getRoll() {
		return rotation.getZ();
	}

	public Vector3f getRotation() {
		return rotation;
	}

	@Override
	public String toString() {
		return String.format("%s, %s, %s : %s,%s,%s", position.getX(), position.getY(), position.getZ(), rotation.getX(), rotation.getY(), rotation.getZ());
	}

}
