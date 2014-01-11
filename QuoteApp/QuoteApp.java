package com.alec.quote;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class QuoteApp extends Game {

	public static final String VERSION = "0.0.1";
	public static final String LOG = "Quote";
	public static boolean debug = false;
			
	@Override
	public void create() {
		setScreen( new Home());
	}
	

}
