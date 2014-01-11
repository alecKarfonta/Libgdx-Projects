package com.alec.solarsystem;

import com.alec.screens.NewtonsCannon;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class solarSystem extends Game {
	
	public final static String TITLE = "vPong";
	
	@Override
	public void create() {		
		// no skin yet
		Skin skin = new Skin(Gdx.files.internal("data/UI/menuSkin.json"), new TextureAtlas("data/UI/atlas.pack"));
		setScreen(new NewtonsCannon(skin));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
