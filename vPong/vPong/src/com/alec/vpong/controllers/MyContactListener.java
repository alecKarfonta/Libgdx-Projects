package com.alec.vpong.controllers;

import com.alec.vpong.views.Play;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA().getBody().getUserData() != null
				&& contact.getFixtureB().getBody().getUserData() != null) {
				if ((contact.getFixtureA().getBody().getUserData().equals("ball")
						|| contact.getFixtureB().getBody().getUserData().equals("ball"))
						&& (contact.getFixtureA().getBody().getUserData().equals("leftInRegion")
								|| contact.getFixtureB().getBody().getUserData().equals("leftInRegion"))) {
					
					System.out.println("Score!");
					Play.incrementScore();
				}
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
		
	}

}
