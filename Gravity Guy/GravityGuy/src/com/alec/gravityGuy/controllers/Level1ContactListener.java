package com.alec.gravityGuy.controllers;

import com.alec.gravityGuy.models.Ground;
import com.alec.gravityGuy.models.Lgm1;
import com.alec.gravityGuy.models.LightBolt;
import com.alec.gravityGuy.models.Player;
import com.alec.gravityGuy.views.Play;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Level1ContactListener implements ContactListener {
	public static final String TAG = Level1ContactListener.class.getName();
	private Play play;

	public Level1ContactListener(Play play) {
		this.play = play;
	}

	@Override
	public void beginContact(Contact contact) {
		// player - enemy sensor
		if (contact.getFixtureA().getUserData() instanceof Player
				&& contact.getFixtureB().getUserData() instanceof Lgm1) {
			((Lgm1) contact.getFixtureB().getUserData()).isPlayerInSensor = true;
		} else if (contact.getFixtureB().getUserData() instanceof Player
				&& contact.getFixtureA().getUserData() instanceof Lgm1) {
			((Lgm1) contact.getFixtureA().getUserData()).isPlayerInSensor = true;
		} else if (contact.getFixtureA().getUserData() instanceof LightBolt
				&& contact.getFixtureB().getUserData() instanceof Lgm1
				&& !contact.getFixtureB().isSensor()) {
			((Lgm1) contact.getFixtureB().getUserData()).damage(25);
		} else if (contact.getFixtureB().getUserData() instanceof LightBolt
				&& contact.getFixtureA().getUserData() instanceof Lgm1
				&& !contact.getFixtureA().isSensor()) {
			((Lgm1) contact.getFixtureA().getUserData()).damage(25);
		} else if (contact.getFixtureA().getUserData() instanceof Player
				&& contact.getFixtureB().getUserData() instanceof Ground) {
			((Player) contact.getFixtureA().getUserData()).isTouchingGround = true;
		} else if (contact.getFixtureB().getUserData() instanceof Player
				&& contact.getFixtureA().getUserData() instanceof Ground) {
			((Player) contact.getFixtureB().getUserData()).isTouchingGround = true;
		}
	}

	@Override
	public void endContact(Contact contact) {
		if (contact.getFixtureA() != null && contact.getFixtureB() != null) {
				//&& contact.getFixtureA().getUserData() != null
				//&& contact.getFixtureB().getUserData() != null) {
			// player - ground
			if (contact.getFixtureA().getUserData() instanceof Player
					&& contact.getFixtureB().getUserData() instanceof Ground) {
				((Player) contact.getFixtureA().getUserData()).isTouchingGround = false;
			} else if (contact.getFixtureB().getUserData() instanceof Player
					&& contact.getFixtureA().getUserData() instanceof Ground) {
				((Player) contact.getFixtureB().getUserData()).isTouchingGround = false;
			}
			// player - enemy sensor
			if (contact.getFixtureA().getUserData() instanceof Player
					&& contact.getFixtureB().getUserData() instanceof Lgm1) {
				((Lgm1) contact.getFixtureB().getUserData()).isPlayerInSensor = false;
			} else if (contact.getFixtureB().getUserData() instanceof Player
					&& contact.getFixtureA().getUserData() instanceof Lgm1) {
				((Lgm1) contact.getFixtureA().getUserData()).isPlayerInSensor = false;
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		if (contact.getFixtureA().getUserData() != null
				&& contact.getFixtureB().getUserData() != null) {
			if (contact.getFixtureA().getUserData() instanceof Player
					&& contact.getFixtureB().getUserData() instanceof Ground) {
				((Player) contact.getFixtureA().getUserData()).isTouchingGround = true;
			} else if (contact.getFixtureB().getUserData() instanceof Player
					&& contact.getFixtureA().getUserData() instanceof Ground) {
				((Player) contact.getFixtureB().getUserData()).isTouchingGround = true;
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
