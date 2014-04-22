package com.alec.gravityGuy.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Lgm1Death {
	public ParticleEffect particles;

	public Lgm1Death(Vector2 initPos) {
		particles = new ParticleEffect();
		particles.load(Gdx.files.internal("particles/lgm1Death.pfx"),
				Gdx.files.internal("particles"));
		particles.setPosition(initPos.x, initPos.y);
		particles.start();
		//AudioManager.instance.play(Assets.instance.sounds.asteroidDeath);
	}


	public boolean shouldDestroy() {
		return particles.isComplete();
	}

	public void render(SpriteBatch batch, float deltaTime) {
		particles.draw(batch, deltaTime);
	}
}