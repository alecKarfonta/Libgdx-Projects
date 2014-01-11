package com.alec.angrymason.Models;

import com.badlogic.gdx.math.Vector2;

// an abstract class that defines what any enemy should be able to do
public abstract class Enemy extends MoveableEntity {

	public Enemy(float speed, float rotation, float width, float height, Vector2 position) {
		super(speed, rotation, width, height, position);
	}
	
	public abstract void advance(float delta, Ship ship);
	
}
