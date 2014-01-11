package com.alec.vpong.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Bumper {
	private Body body;
	private BodyDef bodyDef;
	private Shape shape;
	private FixtureDef fixtureDef;
	
	public Bumper(World world, BodyDef bodyDef, FixtureDef fixtureDef, float x, float y) {
		
		// shape
		shape = new ChainShape();
		((ChainShape)shape).createChain(new Vector2[] { new Vector2(0, .5f), 
														new Vector2(2, 1.5f),
														new Vector2(4, .5f),
														new Vector2(4, 0), 
														new Vector2(2, -1.5f),
														new Vector2(0,0),
														new Vector2(0, .5f)} );

		// fixture definition
		fixtureDef.shape = shape;
		
		// bodydef
		bodyDef.position.set(x,y);
		
		// create the body
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		String type = "bumper";
		body.setUserData(type);
	}
	public void move(Vector2 point) {
		System.out.println("point: (" + point.x + "," + point.y + ")" );
		System.out.println("position: (" + body.getPosition().x + "," + body.getPosition().y + ")" );
		//float direction = MyMath.getSlope(body.getWorldCenter() , new Vector2(point.x, point.y));
		//System.out.println("angle: " + direction);
		//Vector2 force = MyMath.getRectCoords(new Vector2(.001f, direction));
    	
    	//body.applyLinearImpulse(force, body.getWorldCenter(), false);
	}
	public Body getBody() {
		return body;
	}
	public void setBody(Body body) {
		this.body = body;
	}
}
