package com.alec.angrymason;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Angry Mason " + AngryMason.VERSION;
		cfg.vSyncEnabled = true;
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 720;
		
		new LwjglApplication(new AngryMason(), cfg);
	}
}
