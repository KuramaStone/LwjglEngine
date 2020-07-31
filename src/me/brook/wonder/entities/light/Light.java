package me.brook.wonder.entities.light;

import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.entities.Entity;
import me.brook.wonder.entities.Location;

public class Light {

	private Entity attached;
	private Location location;
	private Vector3f color;

	private boolean relative = true; // does light move around with each vertex? e.g. is it the sun?

	public Light(Location location) {
		this(location, new Vector3f(1, 1, 1), true);
	}

	public Light(Location location, Vector3f color, boolean relative) {
		this.location = location;
		this.color = color;
		this.relative = relative;
	}

	public void update() {
		moveToAttachment();
	}

	public void moveToAttachment() {
		if(attached != null) {
			this.location = attached.getLocation();
		}
	}

	public Location getLocation() {
		return location;
	}

	public Entity getAttached() {
		return attached;
	}

	public void setAttached(Entity attached) {
		this.attached = attached;
		moveToAttachment();
	}

	public boolean isRelative() {
		return relative;
	}

	public void setRelative(boolean relative) {
		this.relative = relative;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

}
