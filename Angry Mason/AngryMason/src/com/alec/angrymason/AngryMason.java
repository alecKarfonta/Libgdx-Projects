package com.alec.angrymason;

import box2dLight.RayHandler;

import com.alec.angrymason.Screens.MainMenu;
import com.alec.angrymason.Screens.SplashScreen;
import com.alec.angrymason.View.AngryAudio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;

public class AngryMason extends Game {

	public static final String VERSION = "0.0.0.2";
	public static final String LOG = "Angry Masons";
	public static boolean DEBUG = false;
	FPSLogger logFps;
	
	
	private Screen screen;
	
	@Override
	public void create() {	
		// set the first screen
		setScreen(new SplashScreen(this));
		//AngryAudio.playMusic(true);
		logFps = new FPSLogger();
	}

	@Override
	public void dispose() {
		// dispose any object that has a dispose method, all others are handled by the garbage collector
		if (screen != null) super.dispose();
		AngryAudio.dispose();
		
	}


	// just use the methods in the Game class for standard functions
	@Override
	public void render() {	
		super.render();
		if (DEBUG) {
			logFps.log();
		}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	// on a handheld this method gets called when a call comes in
	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
