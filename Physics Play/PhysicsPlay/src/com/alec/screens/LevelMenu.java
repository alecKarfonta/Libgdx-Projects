package com.alec.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

public class LevelMenu implements Screen {

	private Stage stage;
	private Table table;
	private Skin skin;


	@Override
	public void render(float delta) {
		// clear the screen to black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// update and draw the stage to the screen
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void show() {
		// create a new stage
		stage = new Stage();

		// get the user input
		Gdx.input.setInputProcessor(stage);

		// create a new skin to hold all of the styles and textures
		skin = new Skin(Gdx.files.internal("data/ui/menuSkin.json"), new TextureAtlas("data/ui/atlas.pack"));

		// create a new table the size of the current window
		table = new Table(skin);
		table.setFillParent(true);

		// create a list for the levels
		List list = new List(new String[] {"one", "two", "thrasdfasfsadfadsfasdfasdfadfasdfasdfasdfasdfasdfee", "and", "so", "on", "two", "threadfasdfae", "and", "so", "on", "tafdasdfasdfwo", "three", "and", "so", "on", "two", "three", "and", "so", "on", "two", "three", "and", "so", "on"}, skin);

		// create a scroll pane to scroll the list of levels
		ScrollPane scrollPane = new ScrollPane(list, skin);


		/*
		 * create some buttons
		 */
		TextButton play = new TextButton("PLAY", skin, "big");
		// use an anonymous inner class for the listener
		play.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(sequence(
						Actions.moveTo(-stage.getWidth(), 0, .5f),
						run(new Runnable() {
							@Override
							public void run() {
								((Game) Gdx.app.getApplicationListener()).setScreen(new Play());
							}
						})));
			}
		});
		play.pad(15);

		TextButton back = new TextButton("BACK", skin);
		back.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(sequence(Actions.fadeOut(1), run(new Runnable() {

					@Override
					public void run() {
						((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
					}
				})));
			}
		});
		back.pad(10);

		// add each element to the table
		table.add(new Label("SELECT LEVEL", skin, "big")).colspan(3).expandX().spaceBottom(50);
		table.row();
		table.add(scrollPane).uniformX().expandY().top().left(); 
		table.add(play).uniformX();
		table.add(back).uniformX().bottom().right();

		// add the table to the stage
		stage.addActor(table);

		// animate the stage's entrance
		stage.addAction(Actions.sequence(
				Actions.moveBy(stage.getWidth(), 0),
				Actions.alpha(0)
				));
		stage.addAction(Actions.parallel(
				Actions.moveTo(0, 0, .5f),
				Actions.alpha(1f, 5f)
				)); 
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
		dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

}
