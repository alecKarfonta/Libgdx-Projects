package com.alec.quoteapp;

import com.badlogic.gdx.Game;

public class QuoteApp extends Game {

	public static final String VERSION = "0.0.1";
	public static final String LOG = "Quote";
	public static boolean debug = false;
			
	@Override
	public void create() {
		setScreen( new Home());
	}
	

}
