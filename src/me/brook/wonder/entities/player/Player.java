package me.brook.wonder.entities.player;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.GameEngine;
import me.brook.wonder.entities.Entity;
import me.brook.wonder.entities.Location;
import me.brook.wonder.models.TexturedModel;

public class Player extends Entity {

	private final Camera camera;

	private float walkSpeed = 0.1f, rotateSpeed = 2.5f;
	private float currentSpeed;
	private float sprintMultiplier = 10.0f;
	private boolean isSprinting = false;

	private Vector3f rotationVelocity = new Vector3f();

	public Player(GameEngine engine, TexturedModel model, Location location) {
		super(engine, model, location, 10.0f);
		camera = new Camera();
	}

	public void update() {
		currentSpeed = checkMovementSpeed();

		movePosition();
		moveRotation();
		keyBindings();
	}

	private void keyBindings() {

		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			engine.setRunning(false);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			engine.getManagers().getDisplayManager().screenshot();
		}
	}

	private void moveRotation() {
		Vector3f requestedRot = checkMouse();
		addRequestedToVelocity(requestedRot); // add it to the rotation velocity for smooth turning
		rotate(rotationVelocity.x, rotationVelocity.y, rotationVelocity.z);
		applyFrictionToRotation();
		rotationVelocity.scale(0.4f);
		clampRotation();
	}

	private void movePosition() {
		Vector3f requestedMov = detectKeyInput(); // get 0 degree movement
		rotateRelativeToLocation(requestedMov); // rotate movement to player's yaw
		requestedMov.scale(currentSpeed); // scale it to the current speed
		move(requestedMov.x, requestedMov.y, requestedMov.z);
	}

	private void applyFrictionToRotation() {
		rotationVelocity.scale(0.75f);
	}

	private void addRequestedToVelocity(Vector3f vector) {
		Vector3f velocity = new Vector3f();
		float maxSpeed = rotateSpeed * engine.getManagers().getDisplayManager().getTimeDelta();
		vector.scale(maxSpeed);
		Vector3f.add(rotationVelocity, vector, velocity);

		velocity.x = Math.min(Math.max(velocity.x, -maxSpeed), maxSpeed);
		velocity.y = Math.min(Math.max(velocity.y, -maxSpeed), maxSpeed);
		velocity.z = Math.min(Math.max(velocity.z, -maxSpeed), maxSpeed);

		rotationVelocity = velocity;
	}

	private void clampRotation() {
		Vector3f rot = this.getLocation().getRotation();
		rot.x %= 360;
		rot.y %= 360;
		rot.z %= 360;

		while(rot.x < 0) {
			rot.x += 360;
		}
		while(rot.y < 0) {
			rot.y += 360;
		}
		while(rot.z < 0) {
			rot.z += 360;
		}
	}

	public void rotateRelativeToLocation(Vector3f vector) {

		float x = vector.x;
		float z = vector.z;

		float angle = (float) Math.toRadians(this.getLocation().getYaw());
		float sin = (float) Math.sin(-angle);
		float cos = (float) Math.cos(angle);

		float xx = (float) (x * cos - z * sin);
		float zz = (float) (x * sin + z * cos);

		vector.x = xx;
		vector.z = zz;
	}

	private Vector3f checkMouse() {
		if(!Mouse.isGrabbed()) {
			Mouse.setGrabbed(true);
		}
		Vector3f rot = new Vector3f();

		int dx = Mouse.getDX();
		int dy = Mouse.getDY();

		if(dx < 0) {
			rot.x = 1;
		}
		else if(dx > 0) {
			rot.x = -1;
		}

		if(dy < 0) {
			rot.y = -1;
		}
		else if(dy > 0) {
			rot.y = 1;
		}

		return rot;
	}

	private float checkMovementSpeed() {
		isSprinting = Keyboard.isKeyDown(56);

		float speed = walkSpeed;

		if(isSprinting) {
			speed *= sprintMultiplier;
		}

		return speed * engine.getManagers().getDisplayManager().getTimeDelta();
	}

	/*
	 * Gets current movement request without adjustments for angle
	 */
	private Vector3f detectKeyInput() {
		Vector3f movement = new Vector3f();

		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			movement.z -= 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			movement.z += 1;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			movement.x -= 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			movement.x += 1;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			movement.y += 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			movement.y -= 1;
		}

		return movement;
	}

	public Camera getCamera() {
		return camera;
	}

}
