package com.game.vpong;

import com.alec.vpong.views.Play;
import com.badlogic.gdx.Game;

public class vPong extends Game {
	
	public final static String TITLE = "vPong";
	
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
