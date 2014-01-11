package com.alec.angrymason.Models;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

// an abstract class that descibes any entity in the game: the ship, the bullet the ship fires, an enemy...
public abstract class Entity {
	protected Vector2 position;	// a Vector2 object is simply a way to store a 2 dimensional vector: (x,y)
	protected float width;
	protected float height;
	protected Rectangle bounds; // this defines the hitbox for the entity
	
	public Entity(Vector2 position, float width, float height) {
		this.position = position;
		this.width = width;
		this.height = height;
		bounds = new Rectangle(position.x, position.y, width, height);
	}

	public Rectangle getBounds(){
		return bounds;
	}
	
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
}
