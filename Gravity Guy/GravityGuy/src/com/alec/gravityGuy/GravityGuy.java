package com.alec.gravityGuy;

import com.alec.gravityGuy.controllers.AudioManager;
import com.alec.gravityGuy.models.Assets;
import com.alec.gravityGuy.views.DirectedGame;
import com.alec.gravityGuy.views.Play;
import com.alec.gravityGuy.views.transitions.ScreenTransition;
import com.alec.gravityGuy.views.transitions.ScreenTransitionFade;
import com.badlogic.gdx.assets.AssetManager;

public class GravityGuy extends DirectedGame {
		
		private GoogleInterface platformInterface;
			
		public GravityGuy () {
			
		}
		
		public GravityGuy (GoogleInterface aInterface) {
			platformInterface = aInterface;
			platformInterface.Login();
			platformInterface.showAds(true);
		}
	
		@Override
		public void create() {
			// Load assets
			Assets.instance.init(new AssetManager());
			GamePreferences.instance.load();
			//AudioManager.instance.play(Assets.instance.music.intro);
			ScreenTransition transition = 
					ScreenTransitionFade.init(1.25f);

			setScreen(new Play(this), transition);
		}
	}