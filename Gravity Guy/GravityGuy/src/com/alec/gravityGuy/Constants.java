package com.alec.gravityGuy;


public class Constants {
	public static final String name = "Tesla";
	// Visible game world width is 5 meters
	public static final float VIEWPORT_WIDTH = 20.0f;
	// Visible game world height is 5 meters
	public static final float VIEWPORT_HEIGHT = 20.0f;
	// Visible game world width is 5 meters
	public static final float VIEWPORT_GUI_WIDTH = 1280.0f;
	// Visible game world height is 5 meters
	public static final float VIEWPORT_GUI_HEIGHT = 720.0f;
	public static final String PREFERENCES = "default.prefs";
	public static final String TEXTURE_ATLAS_OBJECTS = "images/GravityGuy.pack";
	public static final String LEVEL_01 = "levels/level-01.png";
	public static final int LIVES_START = 3;
	public static final float PLAYER_WIDTH = .35f;
	public static final float PLAYER_HEIGHT = .7f;
	public static final float PEARL_WIDTH = 2f;
	public static final float PEARL_HEIGHT = 2f;
	public static final float COIN_WIDTH = 1f;
	public static final float COIN_HEIGHT = 1f;
	public static final float GROUND_SIDE = 1f;
	
	
	// filter bits
	public final static short FILTER_NONE = 0x0000;
	public final static short FILTER_GROUND = 0x0001;
	public final static short FILTER_TESLA = 0x0002;
	public final static short FILTER_LIGHTNING = 0x0003;
	public final static short FILTER_SHIELD = 0x0004;
	
	
}
