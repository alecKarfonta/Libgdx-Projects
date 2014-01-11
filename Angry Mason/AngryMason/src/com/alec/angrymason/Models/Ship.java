package com.alec.angrymason.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
// this class defines the ship
public class Ship extends MoveableEntity {
	
	public Ship(Vector2 position, float width, float height, float rotation, float speed) {
		super(speed, rotation, width, height, position);
		
	}

	public void update() {
		// new position = velocity * time
		position.add(velocity.tmp().mul(Gdx.graphics.getDeltaTime() * speed));
		
		// keep the rotation if the ship has been moved
		if (velocity.x != 0 || velocity.y != 0)
			rotation = velocity.angle() - 90;
		
		// set the bounds of the ship
		bounds.x = position.x;
		bounds.y = position.y;
	}
	
}
