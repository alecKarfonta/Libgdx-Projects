package com.alec.angrymason.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

// this is a type of enemy that moves toward the ship, using a linear interpolation algorithm(the lerp method of position)
public class Follower extends Enemy {
	// make the follower always spin
	float rotationSpeed = 100;
	
	public Follower(float speed, float rotation, float width, float height, Vector2 position) {
		super(speed, rotation, width, height, position);
	}

	@Override
	public void advance(float delta, Ship ship) {
		// this method moves the follower toward the ship at a speed porportional to the distance from the ship
		position.lerp(ship.getPosition(), Gdx.graphics.getDeltaTime());
		// increment the rotation
		rotation += delta * rotationSpeed;
		
		if(rotation > 360)
			rotation -= 360;
		
		super.update(ship);
	}
}
