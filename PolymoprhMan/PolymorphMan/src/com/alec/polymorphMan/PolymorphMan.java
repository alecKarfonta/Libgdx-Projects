package com.alec.polymorphMan;

import com.alec.polymorphMan.views.Play;
import com.badlogic.gdx.Game;

public class PolymorphMan extends Game {
public final static String TITLE = "Physics Play";
	
	@Override
	public void create() {		
		setScreen(new Play());
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
