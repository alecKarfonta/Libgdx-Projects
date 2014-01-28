package com.alec.bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Box {
	
	private Body body;
	private boolean isAlive = true;
	private float size;
	private Sprite aliveSprite, deadSprite;
	private Animation animation;
	
	
	public Box(World world, float x, float y, float size) {
		this.size = size;
		body = null;
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		Shape shape = new PolygonShape();

		Texture texture = new Texture("data/images/");
		TextureRegion[] textureRegion;
		
		
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(x,y);
		((PolygonShape)shape).setAsBox(size, size);
		fixtureDef.shape = shape;
		fixtureDef.density = 1;
		fixtureDef.friction = .32f;
		fixtureDef.restitution = .5f;
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		
		aliveSprite = new Sprite(texture);
		texture = new Texture("data/images/box_dead.png");
		deadSprite = new Sprite(texture);
		aliveSprite.setSize(size * 2, size * 2);
		aliveSprite.setOrigin(aliveSprite.getWidth() / 2, aliveSprite.getHeight() / 2);
		deadSprite.setSize(size * 2, size * 2);
		deadSprite.setOrigin(deadSprite.getWidth() / 2, deadSprite.getHeight() / 2);
		body.setUserData(aliveSprite);
		texture.dispose();
	}
	
	public Body getBody() {
		return this.body;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		if (this.isAlive != isAlive) {
			if (isAlive) {
				body.setUserData(aliveSprite);
			} else {
				body.setUserData(deadSprite);
			}
			this.isAlive = isAlive;
		}
		
	}
	
		
}
