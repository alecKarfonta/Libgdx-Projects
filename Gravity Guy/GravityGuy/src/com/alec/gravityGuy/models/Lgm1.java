package com.alec.gravityGuy.models;

import com.alec.gravityGuy.Constants;
import com.alec.gravityGuy.MyMath;
import com.alec.gravityGuy.models.Player.VIEW_DIRECTION;
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

public class Lgm1 extends Entity {
	public static final String TAG = Lgm1.class.getName();

	private Player player; // keep a reference to player to track

	// status
	private int health = 100;

	// settings
	private float runSpeed = .5f;
	private float maxSpeed = 2;
	private float jumpForce = 400;
	public Body wheel;
	public boolean isRunningLeft, isRunningRight, isTouchingGround,
			isShieldOn = false, turnOnShield = false, isPlayerInSensor = false;
	private float width = Constants.PLAYER_WIDTH,
			height = Constants.PLAYER_HEIGHT;
	private float timeRunning;
	private Animation running;

	public enum VIEW_DIRECTION {
		LEFT, RIGHT
	}

	public VIEW_DIRECTION viewDirection = VIEW_DIRECTION.RIGHT;
	private int gravity = 1;

	private ParticleEffect deathParticles;

	// rotation
	private float rotationTarget = 0;
	private boolean isRotating, isDying;
	private float currAngle = 0;

	public Lgm1(World world, Player player, float x, float y) {
		this.player = player;
		init(world, x, y);
	}

	public void init(World world, float x, float y) {
		running = Assets.instance.enemies.lgmWalking;
		deathParticles = new ParticleEffect();
		deathParticles.load(Gdx.files.internal("particles/lgm1Death.pfx"),
				Gdx.files.internal("particles"));

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.allowSleep = false;
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 1.2f;
		bodyDef.gravityScale = 0;

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

		// sensor
		CircleShape sensorShape = new CircleShape();
		sensorShape.setRadius(2);
		FixtureDef sensorFixture = new FixtureDef();
		sensorFixture.shape = sensorShape;
		sensorFixture.isSensor = true;

		body.createFixture(sensorFixture);
		body.getFixtureList().peek().setUserData(this);

	}

	public void moveTowardPlayer(Vector2 playerPos) {
		if (getPosition().x < playerPos.x) {
			moveRight();
		} else {
			moveLeft();
		}
	}

	public void damage(int amount) {
		// TODO: animation
		health -= amount;
	}

	public int getHealth() {
		return health;
	}

	public void startDeath() {
		if (!isDying) {
			isDying = true;
			deathParticles.start();
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
		if (isPlayerInSensor) {
			moveTowardPlayer(player.getPosition());
		}

		// apply movement force, while less than max
		if (isRunningLeft && -body.getLinearVelocity().x < maxSpeed) {
			body.applyLinearImpulse(-runSpeed, 0, body.getWorldCenter().x,
					body.getWorldCenter().y, false);
		}
		if (isRunningRight && body.getLinearVelocity().x < maxSpeed) {
			body.applyLinearImpulse(runSpeed, 0, body.getWorldCenter().x,
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
			if (Math.abs(currAngle - rotationTarget) > .01f) {
				currAngle = MyMath.lerp(currAngle, rotationTarget, .07f);
				body.setTransform(body.getPosition(), currAngle);
			} else {
				isRotating = false;
			}
		}

	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		TextureRegion reg = null;

		if (!isTouchingGround) {
			reg = Assets.instance.enemies.lgm1Standing;
		} else if (isRunningLeft || isRunningRight && isTouchingGround) {
			reg = running.getKeyFrame(timeRunning, true);
		} else {
			reg = Assets.instance.enemies.lgm1Standing;
		}

		// render body
		batch.draw(reg.getTexture(), body.getPosition().x - width / 2,
				body.getPosition().y - height / 2, width / 2, height / 2,
				width, height, 1.4f, 1.4f, (float) Math.toDegrees(currAngle),
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg
						.getRegionHeight(),
				viewDirection == ((gravity == 1) ? VIEW_DIRECTION.LEFT
						: VIEW_DIRECTION.RIGHT), false);
		
		if (!deathParticles.isComplete()) {
			deathParticles.setPosition(getPosition().x, getPosition().y);
			deathParticles.draw(batch, delta);
		}
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
