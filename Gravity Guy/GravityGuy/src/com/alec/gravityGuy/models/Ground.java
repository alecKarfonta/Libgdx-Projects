package com.alec.gravityGuy.models;

import com.alec.gravityGuy.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Ground extends Entity {

	private int sections = 1;
	private float x,y;
	
	public Ground(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void init(World world) {
		this.width = Constants.GROUND_SIDE;
		this.height = Constants.GROUND_SIDE;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(x,y);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = .5f;
		fixtureDef.friction = .32f;
		fixtureDef.restitution = .2f;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2 * sections, height / 2);
		fixtureDef.shape = shape;
		
		
		sprite = new Sprite(Assets.instance.level.backgroundFloor);
		sprite.setSize(width,
				height);
		sprite.setOrigin(width / 2, height / 2);
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.getFixtureList().get(0).setUserData(this);
	}
	
	public void addSection() {
		sections++;
	}

	@Override
	public void render(SpriteBatch batch) {
		for (int index = 0; index < sections; index++) {
			sprite.setPosition(body.getPosition().x - ((width / 2) * sections) + index * width, 
					body.getPosition().y - height / 2);
			sprite.setRotation((float) Math.toDegrees(body.getAngle()));
			sprite.draw(batch);
		}
	}
	
	

}
