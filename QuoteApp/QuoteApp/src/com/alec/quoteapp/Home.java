package com.alec.quoteapp;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Home implements Screen {

	private Skin skin;
	private Stage stage;
	private Group background, foreground;
	Camera camera;
	Table table;
	Texture backgroundTexture;
	SpriteBatch spriteBatch;
	ArrayList<Quote> quotes;
	Label lblQuote;
	Label lblAuthor;
	TextButton btnLike, btnDislike;
	ScrollPane scrollPane;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// draw the stage
		stage.act(delta);

		// play the sprites
		spriteBatch.begin();
		spriteBatch.draw(backgroundTexture, 0, 0);	
		spriteBatch.end();
		
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// set the new view port to the new window width and height
		stage.setViewport(width, height, false);
		// invalidate the table hierarchy in order to reposition all elements
		table.invalidateHierarchy();
	}

	@Override
	public void show() {
		// load the skin (styles, textures, etc)
		skin = new Skin(Gdx.files.internal("data/skin.json"), new TextureAtlas("data/atlas.pack"));
		
		// load the quotes
		Gson gson = new Gson();
		Type quoteArrayListType = new TypeToken<ArrayList<Quote>>(){}.getType();
		try {
			quotes = gson.fromJson(new JsonReader(Gdx.files.internal("data/quotes.json").reader()), quoteArrayListType);
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		stage = new Stage();
		background = new Group();
		background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		foreground = new Group();
		foreground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		spriteBatch = new SpriteBatch();
		
		Gdx.input.setInputProcessor(stage);
		backgroundTexture = new Texture("data/background.png");
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		table = new Table(skin);
		table.setFillParent(true);

		try {
			int quoteIndex = (int)(Math.random() * quotes.size());
			lblQuote = new Label(quotes.get(quoteIndex).getQuote(), skin, "big");
			lblQuote.setWrap(true);
			lblQuote.setAlignment(0); // center
			
			lblAuthor = new Label(quotes.get(quoteIndex).getAuthor(), skin);
			lblAuthor.setAlignment(0); // center
			
			btnLike = new TextButton("Like", skin);
			btnDislike = new TextButton("Dislike", skin);
			ClickListener inputHandler = new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					nextQuote();
				}
			};
			
			btnLike.addListener(inputHandler);
			btnDislike.addListener(inputHandler);
			
			scrollPane = new ScrollPane(lblQuote, skin);
			
			table.align(Align.center);
			table.add(scrollPane).width((float)(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * .10)));
			table.row();
			table.add(lblAuthor).width((float)(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * .10))).padBottom(15f);
			table.row();
			table.add(btnLike);
			table.row();
			table.add(btnDislike);
									
			// add the table to the stage
			foreground.addActor(table);

			// Notice the order
			stage.addActor(background);
			stage.addActor(foreground);
			
			// fade the stage in
			stage.addAction(Actions.alpha(0.0f));
			
			stage.addAction(Actions.fadeIn(.5f));
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		
	}
	
	public void nextQuote() {
		// fade stage out
		stage.addAction(Actions.sequence(Actions.alpha(0, .5f), Actions.alpha(1, .5f)));
		
		int quoteIndex = (int)(Math.random() * quotes.size());
		lblQuote.setText(quotes.get(quoteIndex).getQuote());
		lblAuthor.setText(quotes.get(quoteIndex).getAuthor());
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
		spriteBatch.dispose();
		skin.dispose();
		stage.dispose();
		backgroundTexture.dispose();		
	}

}
