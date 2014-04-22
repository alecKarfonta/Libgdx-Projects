package com.alec.gravityGuy.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Entity {
	public Body body;
	public Sprite sprite;
	public float width, height;
	public float stateTime;
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public Vector2 getOrigin() {
		return body.getWorldCenter();	// ?
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

	public void update(float deltaTime) {
		stateTime += deltaTime;
	}

	public void render(SpriteBatch batch) {
		sprite.setPosition(body.getWorldCenter().x - width, 
				body.getWorldCenter().y - height);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		sprite.draw(batch);
	}
	
	public void render(SpriteBatch batch, float delta)
	{
		
	}
}
