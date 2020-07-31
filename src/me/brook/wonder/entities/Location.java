package me.brook.wonder.entities;

import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.chunk.Chunk;

public class Location {

	private Vector3f position;
	private Vector3f rotation; // yaw, pitch, and roll

	public Location(float x, float y, float z) {
		this(new Vector3f(x, y, z));
	}

	public Location(float x, float y, float z, float yaw, float pitch, float roll) {
		this(new Vector3f(x, y, z), new Vector3f(yaw, pitch, roll));
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

	public void setYaw(float yaw) {
		this.rotation.setX(yaw);
	}

	public float getPitch() {
		return rotation.getY();
	}

	public void setPitch(float pitch) {
		this.rotation.setY(pitch);
	}

	public float getRoll() {
		return rotation.getZ();
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	@Override
	public String toString() {
		return String.format("%s, %s, %s : %s,%s,%s", position.getX(), position.getY(), position.getZ(), rotation.getX(),
				rotation.getY(), rotation.getZ());
	}

	public int getChunkX() {
		return (int) (getX() / Chunk.SIZE);
	}

	public int getChunkZ() {
		return (int) (getZ() / Chunk.SIZE);
	}

	public Location clone() {
		return new Location(new Vector3f(position), new Vector3f(rotation));
	}

}
