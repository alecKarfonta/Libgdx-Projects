package com.alec.angrymason.Models;

import com.badlogic.gdx.math.Vector2;
// this abstract class deifines what any moveable entity must be able to do
public abstract class MoveableEntity extends Entity {
	
	protected Vector2 velocity;
	protected float speed;
	protected float rotation;
	
	public MoveableEntity(float speed, float rotation, float width, float height, Vector2 position) {
		super(position, width, height);
		this.speed = speed;
		this.rotation = rotation;
		velocity = new Vector2(0,0);
	}
	public void update(Ship ship){
		bounds.x = position.x;
		bounds.y = position.y;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	
		
}
