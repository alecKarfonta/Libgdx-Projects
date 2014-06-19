package com.alec.polymorphMan.views;

import com.alec.polymorphMan.Constants;
import com.alec.polymorphMan.MyMath;
import com.alec.polymorphMan.controllers.CameraHelper;
import com.alec.polymorphMan.models.StickMan;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Play implements Screen {
	public final String TAG = Play.class.getName();
	
	private OrthographicCamera camera;
	private CameraHelper cameraHelper;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	

	private float spawnTimer = 0.0f;
	private StickMan adam;

	@Override
	public void show() {
		createWorld();
		createLevel();
		
		// handle the input
		Gdx.input.setInputProcessor(new InputMultiplexer(
		// anonymous inner class for screen specific input
				new InputAdapter() {
					// Handle keyboard input
					@Override
					public boolean keyDown(int keycode) {
						switch (keycode) {
						case Keys.ESCAPE:
							break;
						}
						return false;
					}
					// zoom
					@Override
					public boolean scrolled(int amount) {
						if (amount == 1) {
							cameraHelper.addZoom(camera.zoom * .25f);
						} else if (amount == -1) {
							cameraHelper.addZoom(-camera.zoom * .25f);
						}
						Vector3 mouseLocation = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
						camera.unproject(mouseLocation);
						Vector2 midPoint = MyMath.getMidwayPoint(new Vector2(camera.position.x, camera.position.y), 
								new Vector2(mouseLocation.x, mouseLocation.y));
						Gdx.app.debug(TAG, "scrolled() : midpoint = (" + midPoint.x + "," + midPoint.y + ")");
						cameraHelper.setTarget(midPoint);		
								
						return true;
					}
					// click or touch
					@Override
					public boolean touchDown(int screenX, int screenY,
							int pointer, int button) {
						return false;
					}
					@Override
					public boolean touchUp(int x, int y, int pointer, int button) {

						return false;
					}
					@Override
					public boolean touchDragged(int x, int y, int pointer) {

						return false;
					}
				})); // second input adapter for the input multiplexer

	}

	public void createWorld() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// setup a camera with a 1:1 ratio to the screen contents
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
				Constants.VIEWPORT_HEIGHT);
		cameraHelper = new CameraHelper();

		spriteBatch = new SpriteBatch();
		
		Gdx.gl.glLineWidth(2);
		shapeRenderer = new ShapeRenderer();
	}
	
	public void createLevel() {
		adam = new StickMan(1, 3, 2, 2.5f, 45, 45 );
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		cameraHelper.update(delta);
		
		cameraHelper.applyTo(camera);

		// add each sprite
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();

		shapeRenderer.setProjectionMatrix(camera.combined);
		adam.render(spriteBatch, new Vector2(0,0), shapeRenderer);

		spawnTimer += delta;
		if (spawnTimer > .5f) {
			spawnTimer = 0.0f;
			adam = adam.spawn();
		}
		
		// draw borders
		//shapeRenderer.begin(type);
		

		spriteBatch.end();
	}
	

	@Override
	public void resize(int width, int height) {
		// reset the camera size to the width of the window scaled to the zoom
		// level
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
	
	

}
