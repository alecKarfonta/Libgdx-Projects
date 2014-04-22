package com.alec.gravityGuy.models;

import com.alec.gravityGuy.MyMath;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class LightBolt extends Entity {
	
	private Vector2 target;
	private float initVel = 9;
	private float width = .03f;
	private float height = .3f;

	public LightBolt (World world, Vector2 initPos, Vector2 target) {
		this.target = target;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(initPos);
		bodyDef.bullet = true;
		bodyDef.fixedRotation = true;
		bodyDef.gravityScale = 0;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		fixtureDef.shape = shape;
		
		sprite = new Sprite(Assets.instance.weapons.laserBlue);
		sprite.setScale(6, 2);
		sprite.setSize(width,
				height);
		sprite.setOrigin(width/2, height/2);
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.getFixtureList().first().setUserData(this);
		
		float angle = MyMath.getAngleBetween(initPos, target);
		body.setTransform(initPos, (float) Math.toRadians(90 + angle));
		
		Vector2 forceVector = new Vector2();	// polar
		forceVector.x = initVel;
		forceVector.y = angle;
		body.setLinearVelocity(MyMath.getRectCoords(forceVector));
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
	}

	@Override
	public void render(SpriteBatch batch) {
		sprite.setPosition(body.getPosition().x - width/2 , 
				body.getPosition().y - height/2);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		sprite.draw(batch);
	}
	


	public void setTarget(Vector2 newTarget) {
		this.target = newTarget;
		float angle = MyMath.getAngleBetween(getPosition(), target);
		body.setTransform(getPosition(), (float) Math.toRadians(angle));
		Vector2 forceVector = new Vector2();	// polar
		forceVector.x = initVel;
		forceVector.y = angle;
		body.setLinearVelocity(MyMath.getRectCoords(forceVector));
	}
	
}