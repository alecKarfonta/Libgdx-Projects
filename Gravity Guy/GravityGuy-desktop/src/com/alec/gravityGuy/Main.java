package com.alec.gravityGuy;

import com.alec.gravityGuy.DesktopInterface;
import com.alec.gravityGuy.GravityGuy;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
		public static void main(String[] args) {
			
			LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
			cfg.title = "Gravity Guy";
			cfg.useGL20 = true;
			cfg.width = 1280;
			cfg.height = 720;
			
			new LwjglApplication(new GravityGuy(new DesktopInterface()), cfg);
		}
}
