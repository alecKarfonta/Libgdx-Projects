package com.alec.publixMealDealUI.Views;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.alec.publicMealDealUI.Models.Recipe;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class Home implements Screen {

	// 			
	Stage stage;
	Group background, foreground;
	Camera camera;
	Table table;
	Skin skin;
	
	Texture backgroundTexture;
	SpriteBatch batch;
	TextButton button;
	Label title;

	ArrayList<String> recipeTitles;
	ArrayList<Recipe> featuredRecipeList;
	
	public Home(ArrayList<Recipe> featuredRecipeList, Skin skin) {
		this.featuredRecipeList = featuredRecipeList;
		this.skin = skin;
	}
	@Override
	public void show() {
		/**/
		// create a new stage
		stage = new Stage();
		background = new Group();
		background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		foreground = new Group();
		foreground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		batch = new SpriteBatch();
	
		/**  /
		camera = new OrthographicCamera();
	    camera.viewportHeight = Gdx.graphics.getHeight();
	    camera.viewportWidth = Gdx.graphics.getWidth();

	    camera.position.set(camera.viewportWidth * .5f,
	            camera.viewportHeight * .5f, 0f);
		/**/
		// get the user input
		Gdx.input.setInputProcessor(stage);
		backgroundTexture = new Texture("data/img/background.png");
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		// create a new table the size of the current window
		table = new Table(skin);
		table.setFillParent(true);

		try {
			// create an array list of strings to hold each title
			recipeTitles = new ArrayList<String>();
			// read each title into an arraylist
			for (Recipe recipe : featuredRecipeList) {
				recipeTitles.add(recipe.getTitle());
			}
			// create an array list of strings to hold each title
			recipeTitles = new ArrayList<String>();
			// read each title into an arraylist
			for (Recipe recipe : featuredRecipeList) {
				recipeTitles.add(recipe.getTitle());
			}
			// create a libgdx list 
			final List recipeTitleList = new List(recipeTitles.toArray(new String[recipeTitles.size()]), skin);

			/**/ 	
			// create a scroll pane to scroll the list of levels
			ScrollPane scrollPane = new ScrollPane(recipeTitleList, skin);
			
			TextButton details = new TextButton("View Meal", skin);
			ClickListener inputHandler = new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (recipeTitleList.getSelection() != null) {
						// fade and slide the stage out
						stage.addAction(Actions.parallel(Actions.moveTo(-stage.getWidth(), 0, .5f)
										, Actions.fadeOut(.5f)));
						// after the fade is finished open the next screen
						stage.addAction(Actions.sequence(Actions.delay(.5f),
											Actions.run(new Runnable() {
												@Override
												public void run() {
													Gdx.app.getApplicationListener().dispose();
													((Game) Gdx.app.getApplicationListener()).setScreen(new MealDetails(featuredRecipeList.get(recipeTitleList.getSelectedIndex()), featuredRecipeList, skin));
												}
											})));
					}
				}
			};
			
			// use an anonymous inner class for the listener
			details.addListener(inputHandler);
			details.pad(15);
	
			// exit button
			TextButton Exit = new TextButton("Exit", skin);
			Exit.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					stage.addAction(Actions.sequence(Actions.fadeOut(.35f), Actions.run(new Runnable() {
						@Override
						public void run() {
							Gdx.app.exit();
						}
					})));
				}
			});
			Exit.pad(10);
	
			// add each element to the table
			table.add(new Label("Featured Meals", skin, "big")).colspan(3);
			table.row();
			table.add(scrollPane).pad(20).colspan(3).row(); 
			table.add(details);
			table.add(Exit).bottom().right();
			// add the table to the stage
			foreground.addActor(table);

			// Notice the order
			stage.addActor(background);
			stage.addActor(foreground);
			
			// fade and slide the stage in
			stage.addAction(Actions.parallel(Actions.moveTo(stage.getWidth(), 0), Actions.alpha(0.0f)));
			
			stage.addAction(Actions.parallel(Actions.moveTo(0, 0, .5f), Actions.fadeIn(.5f)));
		
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**/
	}
	
	@Override
	public void render(float delta) {
		/**/	
		// Clear the screen to white
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// draw the stage
		stage.act(delta);

		// play the sprites
		batch.begin();
		batch.draw(backgroundTexture, 0, 0);	
		batch.end();
		
		stage.draw();
		/**/

	}

	private void print(String string, String string2) {
		System.out.println(string + ": " + string2);
	}

	@Override
	public void resize(int width, int height) {
		// set the new view port to the new window width and height
		stage.setViewport(width, height, false);
		// invalidate the table hierarchy in order to reposition all elements
		table.invalidateHierarchy();
	}

	@Override
	public void hide() {
		//dispose();
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
		if (recipeTitles != null) {
			recipeTitles.clear();
			recipeTitles = null;
		}
		if (featuredRecipeList != null) {
			featuredRecipeList.clear();
			featuredRecipeList = null;
		}
		batch.dispose();
		skin.dispose();
		stage.dispose();
		backgroundTexture.dispose();
		this.dispose();
	}


	

}
