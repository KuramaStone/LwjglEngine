package me.brook.wonder.entities.location;

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

		clampRotation();
	}

	public void clampRotation() {
		rotation.x += 180;
		rotation.y += 180;
		rotation.z += 180;
		
		rotation.x %= 360;
		rotation.y %= 360;
		rotation.z %= 360;

		while(rotation.x < 0) {
			rotation.x += 360;
		}
		while(rotation.y < 0) {
			rotation.y += 360;
		}
		while(rotation.z < 0) {
			rotation.z += 360;
		}

		rotation.x -= 180;
		rotation.y -= 180;
		rotation.z -= 180;
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
		clampRotation();
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
		clampRotation();
	}

	public float getPitch() {
		return rotation.getY();
	}

	public void setPitch(float pitch) {
		this.rotation.setY(pitch);
		clampRotation();
	}

	public float getRoll() {
		return rotation.getZ();
	}
	
	public void setRoll(float roll) {
		this.rotation.setZ(roll);
		clampRotation();
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	@Override
	public String toString() {
		return String.format("%sf, %sf, %sf, %sf, %sf, %sf", position.getX(), position.getY(), position.getZ(),
				rotation.getX(),
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
