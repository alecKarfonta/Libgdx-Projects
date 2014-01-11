package com.alec.publixMealDealUI.Views;

import com.alec.publixMealDealUI.PublixMealDealUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Splash implements Screen {

	// 						declarations
	PublixMealDealUI ui;
	Stage stage;
	BitmapFont fontBlack;
	BitmapFont fontWhite;
	TextureAtlas atlas;
	Skin skin;
	SpriteBatch batch;

	public Splash(PublixMealDealUI ui) {
		this.ui = ui;
	}
	
	@Override
	public void render(float delta) {
		/**/
		// Clear the screen to white
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		/**/
		// draw the stage
		stage.act(delta);
		
		// play the sprites
		batch.begin();
		stage.draw();
		
		batch.end();
		/**/
	}

	@Override
	public void resize(int width, int height) {
		/**/
		// create user interface in resize so that it is rendered on start up
		if (stage == null) 
			stage = new Stage(width, height, true);
		stage.clear();
		
		// assign the input processor
		Gdx.input.setInputProcessor(stage);
		/** /
		// create a style for the button
		TextButtonStyle styleButton = new TextButtonStyle();
		styleButton.up = skin.getDrawable("buttonnormal");
		styleButton.down = skin.getDrawable("buttonpressed");
		styleButton.font = fontBlack;
		
		/** /
		// create and place the button
		button = new TextButton("Press Me!", styleButton);
		button.setWidth(400);
		button.setHeight(100);
		button.setX(Gdx.graphics.getWidth() / 2 - button.getWidth() / 2);
		button.setY(Gdx.graphics.getHeight() / 2 - button.getHeight() / 2);
		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				ui.setScreen(new Home(ui));
			}
		});
		/** /
				
		
		
		// add the button to the stage
		stage.addActor(button);
		// add the label to the stage
		stage.addActor(title);
		/**/
	}

	@Override
	public void show() {
		/**/
		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/button.pack");
		skin = new Skin();
		skin.addRegions(atlas);
		fontWhite = new BitmapFont(Gdx.files.internal("data/Fonts/whiteFont.fnt"), false);
		fontBlack = new BitmapFont(Gdx.files.internal("data/Fonts/blackFont.fnt"), false);
		/**/
	}

	@Override
	public void hide() {
		dispose();
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
		batch.dispose();
		skin.dispose();
		atlas.dispose();
		fontBlack.dispose();
		fontWhite.dispose();
		stage.dispose();
		
		
	}


	

}
