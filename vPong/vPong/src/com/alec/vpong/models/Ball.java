package com.alec.vpong.models;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ball {
	private Body body;
	private ParticleEmitter flame;
	private float x1, y1;
	private float radius;
	
	
	public Ball(World world, FixtureDef fixtureDef, float x1, float y1, float radius) {
		this.x1 = x1;
		this.y1 = y1;
		this.radius = radius;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x1,y1);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		
		flame = new ParticleEmitter();
		try {
			flame.load(Gdx.files.internal("data/flame").reader(2024));
			Sprite particle = new Sprite(new Texture("data/particle.png"));
			flame.setSprite(particle);
			flame.getScale().setHigh(.3f);
			flame.setMaxParticleCount(100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.setBullet(true);
	}
	
	public void render(SpriteBatch spriteBatch, float delta) {
		// exhaust
		// place the exhaust at the center of the ship
		flame.setPosition(body.getWorldCenter().x, body.getWorldCenter().y); 
		flame.start();
		setExhaustRotation();
		flame.draw(spriteBatch, delta);
	}
	
	private void setExhaustRotation() {
		flame.getAngle().setLow(240);
		flame.getAngle().setHighMin(180);
		flame.getAngle().setHighMax(360);		
	}
	
}
