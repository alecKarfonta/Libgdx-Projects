package com.alec.angrymason.Screens;

import com.alec.angrymason.AngryMason;
import com.alec.angrymason.View.World;
import com.alec.angrymason.View.WorldRenderer;
import com.badlogic.gdx.Screen;
// this class defines the main screen in the game
public class GameScreen implements Screen {

	AngryMason game;
	World world;	// this object is where all of our entities are defined
	WorldRenderer render;
	
	public GameScreen(AngryMason game) {
		this.game = game;
		world = new World(game);
		render = new WorldRenderer(world);
	}
	
	// look at World for a description of these methods
	@Override
	public void render(float delta) {
		world.update();
		render.render();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		dispose();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		world.dispose();
	}

}
