package com.alec.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class CannonBall {
	private Body body;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Shape shape;
	private float radius;
	private long birthTime;
	
	public CannonBall(World world, float x, float y, long birthTime, short categoryBits, short maskBits) {
		this.birthTime = birthTime;
		bodyDef = new BodyDef();
		bodyDef.gravityScale = 0;
		bodyDef.position.set(x,y);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.allowSleep = false;
		
		radius = .5f;
		shape = new CircleShape();
		shape.setRadius(radius);
		
		fixtureDef  = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.restitution = 0; // inellastic collisions
		fixtureDef.density = 3.5f;
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = categoryBits;
		fixtureDef.filter.maskBits = maskBits;
		
		Texture texture = new Texture("data/Images/cannonBall.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Sprite sprite = new Sprite(texture);
		sprite.setSize(radius * 2, radius * 2);
		sprite.setOrigin(radius, radius);
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.setUserData(new ModelData("cannonBall" ,sprite));
	}
	
	public long getAge() {
		return System.currentTimeMillis() - birthTime;
	}
	
	public Body getBody() {
		return this.body;
	}
	
	public float getRadius() {
		return this.radius;
	}
	
	public Shape getShape() {
		return this.shape;
	}
}
