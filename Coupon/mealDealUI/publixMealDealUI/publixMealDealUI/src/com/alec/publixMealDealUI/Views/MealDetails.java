package com.alec.publixMealDealUI.Views;

import java.util.ArrayList;

import com.alec.publicMealDealUI.Models.Ingredient;
import com.alec.publicMealDealUI.Models.Recipe;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MealDetails implements Screen {

	private Stage stage;
	private Table table;
	private SpriteBatch batch;
	private Group background, foreground;
	private Texture backgroundTexture;
	private ArrayList<String> ingredientTitles = new ArrayList<String>();
	private List ingredientTitleList;
	private List prepStepList;
	private Skin skin;
	private Recipe recipe;
	private ArrayList<Recipe> recipes;

	
	public MealDetails(Recipe recipe, ArrayList<Recipe> recipes, Skin skin) {
		this.recipe = recipe;
		this.recipes = recipes;
		this.skin = skin;
	}
	
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		background = new Group();
		background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		foreground = new Group();
		foreground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		batch = new SpriteBatch();
		
		backgroundTexture = new Texture("data/img/background.png");
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	
		table = new Table(skin);
		table.setFillParent(true);

		final TextButton back = new TextButton("BACK", skin);
		back.pad(10);

		// create lists for the ingredients, prep steps, and ....
		for (Ingredient ingredient : recipe.getIngredients()) {
			ingredientTitles.add(ingredient.getItem());
		}
		
		// libgdx lists for display in scrollpane
		ingredientTitleList = new List(ingredientTitles.toArray(new String[ingredientTitles.size()]), skin);
		prepStepList = new List(recipe.getPrepSteps().toArray(new String[recipe.getPrepSteps().size()]), skin );
		
		ScrollPane ingredientScrollPane = new ScrollPane(ingredientTitleList, skin);
		ScrollPane prepStepScrollPane = new ScrollPane(prepStepList, skin);
		
		ClickListener buttonHandler = new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// event.getListenerActor() returns the source of the event, e.g. a button that was clicked
				if(event.getListenerActor() == back) {
					// fade and slide the stage out
					stage.addAction(Actions.parallel(Actions.moveTo(-stage.getWidth(), 0, .5f)
									, Actions.fadeOut(.5f)));
					// after the fade is finished open the next screen
					stage.addAction(Actions.sequence(Actions.delay(.5f),
										Actions.run(new Runnable() {
											@Override
											public void run() {
												((Game) Gdx.app.getApplicationListener()).setScreen(new Home(recipes, skin));
											}
										})));
					/**/
				}
			}
		};

		back.addListener(buttonHandler);

		// create a label for a title
		Label title = new Label(recipe.getTitle(), skin);
		
		table.add(title).colspan(3).top().row();
		table.add(ingredientScrollPane).pad(20).prefHeight(300).prefWidth(600);
		table.add().pad(20);
		table.add(prepStepScrollPane).pad(20).prefHeight(300).prefWidth(600).row();
		
		table.add(back).colspan(3).expandY().bottom().right().row();
		
		table.center();
		stage.addActor(table);
		
		// fade and slide the stage in
		stage.addAction(Actions.parallel(Actions.moveTo(stage.getWidth(), 0), Actions.alpha(0.0f)));
		
		stage.addAction(Actions.parallel(Actions.moveTo(0, 0, .5f), Actions.fadeIn(.5f))); 
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		batch.begin();
		batch.draw(backgroundTexture, 0, 0);	
		batch.end();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
		table.invalidateHierarchy();
	}

	@Override
	public void hide() {
		//dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		if (ingredientTitles != null) {
			ingredientTitles.clear();
			ingredientTitles = null;
		}
		if (ingredientTitleList != null) {
			ingredientTitleList.clear();
			ingredientTitleList = null;
		}
		stage.dispose();
		skin.dispose();
		this.dispose();
	}


	

}
