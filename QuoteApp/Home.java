package com.alec.quote;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Home implements Screen {

	private Skin skin;
	private Stage stage;
	private Group background, foreground;
	Camera camera;
	Table table;
	Texture backgroundTexture;
	SpriteBatch spriteBatch;
	Label title;
			
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// load the skin (styles, textures, etc)
		skin = new Skin(Gdx.files.internal("data/skin.json"), new TextureAtlas("data/atlas.pack"));
		
		stage = new Stage();
		background = new Group();
		background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		foreground = new Group();
		foreground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		spriteBatch = new SpriteBatch();
		
		Gdx.input.setInputProcessor(stage);
		backgroundTexture = new Texture("data/img/background.png");
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
