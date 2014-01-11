package com.game.vPong;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.vpong.vPong;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "vPong";
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 720;
		
		new LwjglApplication(new vPong(), cfg);
	}
}
