package com.alec.screens;


import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.alec.tween.ActorAccessor;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen {

	Stage stage;
	Skin skin;
	Table table;
	TweenManager tweenManager;
	
	@Override
	public void show() {
		// create a new stage object to hold all of the other objects
		stage = new Stage();

		// set the input processor to the stage
		Gdx.input.setInputProcessor(stage);
		
		// create a new skin to hold the styles and textures
		skin = new Skin(Gdx.files.internal("data/ui/menuSkin.json"), new TextureAtlas("data/ui/atlas.pack"));
		
		// create a new table the size of the windows
		table = new Table(skin);
		table.setFillParent(true);
		
		// create a heading
		Label heading = new Label("title", skin, "big");
		heading.scale(3);
		
		/*
		 * create some buttons
		 */
		TextButton tbPlay = new TextButton("Play", skin, "big");
		tbPlay.pad(20);
		// use an anonymous inner class for then click event listener
		tbPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(Actions.sequence(
											Actions.moveBy(-stage.getWidth(), 0f, .5f),
											Actions.run(new Runnable() {
					 @Override
					public void run() {
						 ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelMenu());	 
					}
				})));
			}
		});
		TextButton tbSettings = new TextButton("Settings", skin);
		tbSettings.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game)Gdx.app.getApplicationListener()).setScreen(new Settings());
			}
		});
		TextButton tbExit = new TextButton("Exit", skin, "big");
		tbExit.pad(20);
		tbExit.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Gdx.app.exit();
				}
		});
		
		// add everything to the table
		table.add(heading).spaceBottom(100);
		table.row();		// increments the row so all new actors are placed in the new row
		table.add(tbPlay).padBottom(15);
		table.row();
		table.add(tbSettings).padBottom(15);
		table.row();
		table.add(tbExit);
		
		// add the table to the stage
		stage.addActor(table);
	
		// create the animation for the entrance		
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());
		
		// time line for heading color animation ( cycles through all colors )
		Timeline.createSequence().beginSequence()
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0,0,1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0,1,0))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,1,0))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,0,1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0,0,1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1,1,1))
		.end().repeat(Tween.INFINITY, 0).start(tweenManager);
		
		// time line for buttons and heading fade in
		Timeline.createSequence().beginSequence()
		.push(Tween.set(tbPlay, ActorAccessor.ALPHA).target(0))
		.push(Tween.set(tbSettings, ActorAccessor.ALPHA).target(0))
		.push(Tween.set(tbExit, ActorAccessor.ALPHA).target(0))
		.push(Tween.from(heading, ActorAccessor.ALPHA, .5f).target(0))	// change the heading alpha from .5 to 0
		.push(Tween.to(tbPlay, ActorAccessor.ALPHA, .25f).target(1))		// change the play button aplha to 0
		.push(Tween.to(tbSettings, ActorAccessor.ALPHA, .25f).target(1))
		.push(Tween.to(tbExit, ActorAccessor.ALPHA, .25f).target(1))
		.end().start(tweenManager);
		
		// table fade  and slide in
		Tween.from(table, ActorAccessor.ALPHA, .5f).target(0).start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .5f).target(Gdx.graphics.getHeight() / 8).start(tweenManager);
		
		// update the animation
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void render(float delta) {
		// clear the screen with black
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
		
		tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		// set the view to the new window width and height
		stage.setViewport(width, height, true);		
		// invalidate the table hierarchy for it to reposition elements
		table.invalidateHierarchy();		
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

}
