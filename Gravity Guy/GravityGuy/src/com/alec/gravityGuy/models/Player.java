package com.alec.gravityGuy.models;

import com.alec.gravityGuy.Constants;
import com.alec.gravityGuy.MyMath;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Player extends Entity {
	public static final String TAG = Player.class.getName();
	
	private World world; 	// hold a reference to the world for TODO: fix

	private int health = 100;
	
	// settings
	private float runSpeed = 1;
	private float maxSpeed = 6;
	private float jumpForce = 2000;
	private Body wheel;
	public boolean isRunningLeft, isRunningRight, isTouchingGround, isShieldOn = false,
			turnOnShield = false;
	private float width = Constants.PLAYER_WIDTH;

	public float height = Constants.PLAYER_HEIGHT;
	private float timeRunning;
	private Animation running;
	public enum VIEW_DIRECTION {
		LEFT, RIGHT
	}
	public VIEW_DIRECTION viewDirection = VIEW_DIRECTION.RIGHT;
	private int gravity = 1;
	private ParticleEffect jetpackParticles;
	private ParticleEffect laserExhaustParticles;
	
	//  rotation
	private float rotationTarget = 0;
	private boolean isRotating;
	private float currAngle = 0;
	
	public Player(World world) {
		this.world = world;
		init(world);
	}

	public void init(World world) {
		running = Assets.instance.player.running;
		jetpackParticles = new ParticleEffect();
		jetpackParticles.load(Gdx.files.internal("particles/jetpack.pfx"), Gdx.files.internal("particles"));
		
		laserExhaustParticles = new ParticleEffect();
		laserExhaustParticles.load(Gdx.files.internal("particles/laserExhaust.pfx"), Gdx.files.internal("particles"));
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0,0);
		bodyDef.allowSleep = false;
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 2.2f;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 10;
		fixtureDef.friction = .32f;
		fixtureDef.restitution = .2f;
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.FILTER_TESLA;
		fixtureDef.filter.maskBits = Constants.FILTER_GROUND;

		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.getFixtureList().first().setUserData(this);

		// create wheels
		CircleShape wheelShape = new CircleShape();
		wheelShape.setRadius(width / 2);
		FixtureDef wheelFixtureDef = new FixtureDef();
		wheelFixtureDef.shape = wheelShape;
		wheelFixtureDef.density = 10;
		wheelFixtureDef.friction = .92f;
		wheelFixtureDef.restitution = .2f;

		bodyDef.fixedRotation = false;
		bodyDef.angularDamping = 56;
		wheel = world.createBody(bodyDef);
		wheel.createFixture(wheelFixtureDef);
		wheel.getFixtureList().first().setUserData(this);

		// create axis'
		RevoluteJointDef axisDef = new RevoluteJointDef();
		axisDef.bodyA = body;
		axisDef.bodyB = wheel;
		axisDef.collideConnected = false;
		axisDef.enableMotor = true;
		axisDef.localAnchorA.set(width / 2 - wheelShape.getRadius(),
				-height / 2 * .8f);
		world.createJoint(axisDef);
		
		PolygonShape sensorShape = new PolygonShape();
		sensorShape.setAsBox(width, height * .2f, getPosition().add(new Vector2(0, -height/2 * 1.1f)), 0);
		
		FixtureDef sensorFixture = new FixtureDef();
		sensorFixture.isSensor = true;
		sensorFixture.shape = sensorShape;
		
		body.createFixture(sensorFixture);
		body.getFixtureList().peek().setUserData(this);
		
	}
	
	public void setPosition(float x , float y) {
		body.setTransform(new Vector2(x, y + height), 0);
		wheel.setTransform(new Vector2(x, y + height), 0);
	}

	public LightBolt fireLaser(Vector2 target) {
		LightBolt lightBolt = new LightBolt(world, 
				getPosition().add(.08f * (viewDirection == VIEW_DIRECTION.LEFT ? -1 : 1), .03f), 
				target);
		laserExhaustParticles.start();
		return lightBolt;
	}
	
	public void jump() {
		Gdx.app.debug(TAG, "jumping()");
		
		if (isTouchingGround) {
			body.applyForceToCenter(0, jumpForce * gravity,  false);
			jetpackParticles.start();
		}
	}

	public void moveRight() {
		timeRunning = 0.0f;
		isRunningRight = true;
	}

	public void stopRunningRight() {
		isRunningRight = false;
	}

	public void moveLeft() {
		timeRunning = 0.0f;
		isRunningLeft = true;
		
	}

	public void stopRunningLeft() {
		isRunningLeft = false;
	}

	@Override
	public void update(float delta) {
		
		// apply movement force, while less than max
		if (isRunningLeft && -body.getLinearVelocity().x < maxSpeed) {
			body.applyLinearImpulse(-runSpeed * (isTouchingGround ? 1 : .5f), 0, body.getWorldCenter().x,
					body.getWorldCenter().y, false);
		}
		if (isRunningRight && body.getLinearVelocity().x < maxSpeed) {
			body.applyLinearImpulse(runSpeed * (isTouchingGround ? 1 : .5f), 0, body.getWorldCenter().x,
					body.getWorldCenter().y, false);
		}
		
		// set view dirction
		if (isRunningLeft) {
			viewDirection = VIEW_DIRECTION.LEFT;
		} else if (isRunningRight) {
			viewDirection = VIEW_DIRECTION.RIGHT;
		}

		// increment running timer
		if (isRunningLeft || isRunningRight && isTouchingGround) {
			timeRunning += delta;
		}
		
		// rotate
		if (isRotating) {
			currAngle = body.getAngle();
			// if the curr angle is different from the rotation angle
			if (Math.abs(currAngle - rotationTarget) > .01f ) {
				currAngle = MyMath.lerp(currAngle, rotationTarget, .07f);
				// TODO: check
				body.setTransform(body.getPosition(), currAngle );
			} else {
				isRotating = false;
			}
		}

	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		TextureRegion reg = null;

		if (!isTouchingGround) {
			reg = Assets.instance.player.jumping;
		} else if (isRunningLeft || isRunningRight && isTouchingGround) {
			reg = running.getKeyFrame(timeRunning, true);
		} else {
			reg = Assets.instance.player.still;
		}
		
		// render body
		batch.draw(reg.getTexture(), 
				body.getPosition().x - width / 2,
				body.getPosition().y - height / 2, 
				width / 2, height / 2, 
				width, height, 
				1.4f, 1.4f, 
				(float)Math.toDegrees(currAngle), 
				reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(),
				viewDirection == ((gravity == 1) ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT), false);
		
		// render jetpack particles
		if (!jetpackParticles.isComplete()) {
			jetpackParticles.setPosition(getPosition().x - .15f * (viewDirection == VIEW_DIRECTION.LEFT ? -1 : 1), getPosition().y + .05f * (gravity == 1 ? 1 : -1));
			jetpackParticles.draw(batch, delta);
		} 
		
		// render laser exhaust particles
		if (!laserExhaustParticles.isComplete()) {
			laserExhaustParticles.setPosition(getPosition().x + .25f * (viewDirection == VIEW_DIRECTION.LEFT ? -1 : 1), getPosition().y + .05f * (gravity == 1 ? 1 : -1));
			if ((viewDirection == VIEW_DIRECTION.LEFT ? true : false)) {
				laserExhaustParticles.setFlip(true, true);
			}
			laserExhaustParticles.draw(batch, delta);
		} 
	}
	
	public int getHealth () {
		return health;
	}

	public void rotateDown() {
		isRotating = true;
		rotationTarget = 0;
		gravity = 1;
	}
	
	public void rotateUp() {
		isRotating = true;
		rotationTarget = (float) Math.toRadians(180);
		gravity = -1;
	}

}
