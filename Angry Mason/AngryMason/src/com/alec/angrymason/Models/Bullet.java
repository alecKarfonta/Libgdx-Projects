package com.alec.angrymason.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

// this is the bullet that the ship fires
public class Bullet extends MoveableEntity {

	public static float speed = 15;
	
	public Bullet(float speed, float rotation, float width, float height, Vector2 position, Vector2 velocity) {
		super(speed, rotation, width, height, position);
		this.velocity = velocity;
	}
	
	public void update(Ship ship) {
		// new position += velocitiy * time
		position.add(velocity.tmp().mul(Gdx.graphics.getDeltaTime() * speed));
		rotation = velocity.angle() - 90;
		super.update(ship);
	}
}

