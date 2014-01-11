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

public class CelestialBody {
	private Body body;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Shape shape;
	private float radius;
	
	public CelestialBody(String name, World world, float x, float y, float radius, float density, String imagePath) {
		this(name,world,x,y,radius,density,imagePath, (short) 0x0000, (short) 0x0012);
	}
	
	public CelestialBody(String name, World world, float x, float y, float radius, float density, String imagePath, short categoryBits, short maskBits ) {
		this.radius = radius;
		
		bodyDef = new BodyDef();
		bodyDef.gravityScale = 0;
		bodyDef.position.set(x,y);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.allowSleep = false;

		shape = new CircleShape();
		shape.setRadius(radius);
		
		fixtureDef  = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.restitution = 0; // inellastic collisions
		fixtureDef.density = density;
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = categoryBits;
		fixtureDef.filter.maskBits = maskBits;

		Texture texture = new Texture(imagePath);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Sprite sprite = new Sprite(texture);
		sprite.setSize(radius * 2, radius * 2);
		sprite.setOrigin(radius, radius);
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.setUserData(new ModelData(name, sprite));
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
