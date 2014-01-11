package com.alec.angrymason.Screens;


import com.alec.angrymason.AngryMason;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
// this class defines the main menu screen, good example of a simple ui written in libgdx that can be deployed a handheld.
// it is suitable for use on a handheld because of how it uses bitmap fonts and a texture atlas
public class MainMenu implements Screen {

	AngryMason game;
	Stage stage;	// this object keeps track of all of our entites
	BitmapFont fontBlack;	
	BitmapFont fontWhite;
	TextureAtlas atlas; // only really provides a performance boost if your system requires textures sizes to be powers of 2 ( < opengl 2) 
	Skin skin;
	SpriteBatch batch;
	TextButton button;
	Label label;
	
	public MainMenu(AngryMason game) {
		this.game = game; // our game method is passed between screen object, this cant be right
	}
	
	// this method handles the progression of our scene, 
	// it is coninually called to redraw the screen based on the
	// time (delta)
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

	// resize is called when the game starts(right after render), 
	// then each time the screen window is resized on a computer, 
	// or on a handheld when the screen changes orientation.
	// so we do all of our positioning here
	@Override
	public void resize(int width, int height) {
		/**/
		if (stage == null) 
			stage = new Stage(width, height, true);
		stage.clear();
		
		// assign the input processor to the stage
		Gdx.input.setInputProcessor(stage);
		
		// create a style for the button
		TextButtonStyle styleButton = new TextButtonStyle();
		styleButton.up = skin.getDrawable("buttonnormal");
		styleButton.down = skin.getDrawable("buttonpressed");
		styleButton.font = fontBlack;
		/**/
		// create and place the button
		button = new TextButton("Press Me!", styleButton);
		button.setWidth(400);
		button.setHeight(100);
		button.setX(Gdx.graphics.getWidth() / 2 - button.getWidth() / 2);	// Grx.graphics.getWidth() is the width of the screen
		button.setY(Gdx.graphics.getHeight() / 2 - button.getHeight() / 2);
		
		button.addListener(new InputListener() {	// if this looks weird its called an anonymous inner class, and 
													// its just how it sounds; we define a new class but we dont give
													// it a name cause we dont want to use it anywhere else
			// mouse click or touchscreen input
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new GameScreen(game));
			}
		});
				
		// create a new label, with style
		LabelStyle styleLabel = new LabelStyle(fontWhite, Color.WHITE);
		label = new Label("Angry Masons", styleLabel);
		label.setX(0);
		label.setY(Gdx.graphics.getWidth() / 2);
		label.setWidth(width);
		label.setAlignment(Align.center);
		
		// add the button to the stage
		stage.addActor(button);
		// add the label to the stage
		stage.addActor(label);
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
