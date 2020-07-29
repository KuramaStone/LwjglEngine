package me.brook.wonder.entities.player;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import me.brook.wonder.entities.Entity;
import me.brook.wonder.entities.Location;
import me.brook.wonder.models.TexturedModel;

public class Player extends Entity {

	private final Camera camera;

	private float walkSpeed = 0.002f;
	private float currentSpeed;
	private float sprintMultiplier = 5.0f;
	private boolean isSprinting = false;

	public Player(TexturedModel model, Location location) {
		super(model, location, 1.0f);
		camera = new Camera();
	}

	public void update() {
//		System.out.println(location.toString());
		currentSpeed = checkMovementSpeed();
		Vector3f requested = detectKeyInput();

		move(requested.x, requested.y, requested.z);
	}

	private float checkMovementSpeed() {
		isSprinting = Keyboard.isKeyDown(56);

		float speed = walkSpeed;

		if(isSprinting) {
			speed *= sprintMultiplier;
		}

		return speed;
	}

	/*
	 * Gets current movement request without adjustments for angle
	 */
	private Vector3f detectKeyInput() {
		Vector3f movement = new Vector3f();

		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			movement.z -= currentSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			movement.z += currentSpeed;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			movement.x -= currentSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			movement.x += currentSpeed;
		}

		return movement;
	}

	public Camera getCamera() {
		return camera;
	}

}
