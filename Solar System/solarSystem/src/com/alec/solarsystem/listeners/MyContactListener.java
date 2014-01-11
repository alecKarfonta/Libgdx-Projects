package com.alec.solarsystem.listeners;

import com.alec.models.ModelData;
import com.alec.screens.NewtonsCannon;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	
	@Override
	public void beginContact(Contact contact) {
		
	}

	@Override
	public void endContact(Contact contact) {
		if (contact.getFixtureA().getUserData() instanceof ModelData) {
			//if (((ModelData)contact.getFixtureA().getUserData()).getName() {
				
			//}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
		
	}

}
