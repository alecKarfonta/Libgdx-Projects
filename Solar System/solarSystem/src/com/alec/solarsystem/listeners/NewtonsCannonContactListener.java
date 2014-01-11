package com.alec.solarsystem.listeners;

import com.alec.models.ModelData;
import com.alec.screens.NewtonsCannon;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class NewtonsCannonContactListener implements ContactListener {

	NewtonsCannon game;
	
	public NewtonsCannonContactListener(NewtonsCannon newtonsCannon) {
		this.game = newtonsCannon;
	}

	@Override
	public void beginContact(Contact contact) {
		System.out.println("run!");
		if (contact.getFixtureA().getBody().getUserData() instanceof ModelData
				&& contact.getFixtureB().getBody().getUserData() instanceof ModelData  ) {
			if (((ModelData)contact.getFixtureA().getBody().getUserData()).getName().equals("earth")
				&& ((ModelData)contact.getFixtureB().getBody().getUserData()).getName().equals("cannonBall")		) {
				game.destroyBody(contact.getFixtureB().getBody());
			} else if (((ModelData)contact.getFixtureB().getBody().getUserData()).getName().equals("earth")
					&& ((ModelData)contact.getFixtureA().getBody().getUserData()).getName().equals("cannonBall")		) {
					game.destroyBody(contact.getFixtureA().getBody());
					
				}
		}
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
		
	}

}
