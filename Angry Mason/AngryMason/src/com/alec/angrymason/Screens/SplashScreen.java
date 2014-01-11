package com.alec.angrymason.Screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.alec.angrymason.AngryMason;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {

	Texture splashBackground;
	Sprite splashSprite;
	SpriteBatch spriteBatch;
	AngryMason game;
	TweenManager manager; // the tween manager is used to perform different types of transisitoin: fade, slide.... if you are familiar with flash they are analogous to tweens in flash
	
	public SplashScreen(AngryMason game) {
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		
		// clear the screen with black.
		// most methods that draw to the screen in libgdx are called in two parts. first you define what you want to do or make and then you actually do it.
		Gdx.gl.glClearColor(0, 0, 0, 1);	// so here we first define that color that we want to use for clearing the screen
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // and now we actually call the method to perform the clear
		// Gdx.app.log(AngryMason.LOG, "Rendering");
		
		manager.update(delta);
		spriteBatch.begin();
		splashSprite.draw(spriteBatch);
		spriteBatch.end();
		
	}
	
	@Override
	public void show() {
		splashBackground = new Texture("data/splashscreen.png");
		splashBackground.setFilter(TextureFilter.Linear, TextureFilter.Linear); // i think this adds anti aliasing?
		
		splashSprite = new Sprite(splashBackground);
		splashSprite.setColor(1,1,1,0);
		splashSprite.setX((Gdx.graphics.getWidth() / 2) - (splashSprite.getWidth() / 2));
		splashSprite.setY((Gdx.graphics.getHeight() / 2) - (splashSprite.getHeight() / 2));
		
		
		spriteBatch = new SpriteBatch();
		
		Tween.registerAccessor(Sprite.class, new SpriteTween());
		
		manager = new TweenManager();
		
		// create a new callback (listener)
		TweenCallback cb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				tweenCompleted();
			}
		};
	
		
		// fade in the background, wait 2.5 seconds, then fade out(yoyo), and have it trigger the complete callback when finished
		Tween.to(splashSprite, SpriteTween.ALPHA, 2f).target(1).ease(TweenEquations.easeInQuad).repeatYoyo(1, 1f).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
	}
	
	private void tweenCompleted() {
		Gdx.app.log(AngryMason.LOG, "Tween Complete");
		game.setScreen(new MainMenu(game));
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	
	
}
