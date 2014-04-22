package com.alec.gravityGuy.views;

import java.util.ArrayList;

import com.alec.gravityGuy.Constants;
import com.alec.gravityGuy.controllers.CameraController;
import com.alec.gravityGuy.controllers.Level1ContactListener;
import com.alec.gravityGuy.models.Assets;
import com.alec.gravityGuy.models.Gauge;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Play extends AbstractGameScreen {

	public Play(DirectedGame game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	public static final String TAG = Assets.class.getName();
	public World world;

	// world variables
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera gameCamera;
	private CameraController cameraHelper;
	private SpriteBatch spriteBatch;
	private Sprite background;
	private float backgroundAngle;

	private Gauge healthGauge;
	private ArrayList<Body> destroyQueue;

	// world properties
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8;
	private final int POSITIONITERATONS = 3;
	private float width, height;
	
	private int gravity = 1;

	private boolean isDebug = true, isPaused = false;

	private Level level;

	@Override
	public void show() {
		createWorld();
		createUI();
		createLevel();
	}

	public void createWorld() {
		// create the world with surface gravity
		world = new World(new Vector2(0f, -9.8f), true);
		world.setContactListener(new Level1ContactListener(this));
		debugRenderer = new Box2DDebugRenderer();

		// setup a camera with a 1:1 ratio to the screen contents
		gameCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
				Constants.VIEWPORT_HEIGHT);

		cameraHelper = new CameraController();

		spriteBatch = new SpriteBatch();

		if (isDebug) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		} else {
			Gdx.app.setLogLevel(Application.LOG_ERROR);
		}
	}
	
	public void createUI() {
		healthGauge = new Gauge(100, -width / 2, height / 2 - 15,
				Assets.instance.ui.healthGaugeInfill);
	}

	public void createLevel() {
		background = new Sprite(Assets.instance.level.background);
		float backgroundWidth = 5 * -Constants.VIEWPORT_WIDTH;
		float backgroundHeight = 5 * -Constants.VIEWPORT_HEIGHT;
		background.setBounds(-backgroundWidth / 2, -backgroundHeight / 2 - 50,
				backgroundWidth, backgroundHeight);
		background.setOrigin(backgroundWidth / 2 - 50,
				backgroundHeight / 2 - 50);
		
		
		destroyQueue = new ArrayList<Body>();
		level = new Level(this, "levels/level-01.png");
		
		// start the camera above the player
		cameraHelper.setPosition(level.player.getPosition().add(new Vector2(0,15)));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(10 / 255.0f, 10 / 255.0f, 40 / 255.0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// increment the world
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATONS);

		spriteBatch.setProjectionMatrix(gameCamera.combined);
		spriteBatch.begin();

		backgroundAngle += (delta / 4);
		background.setRotation(backgroundAngle);
		background.draw(spriteBatch);

		level.updateAndRender(spriteBatch, delta);
		level.render(spriteBatch);
		
		healthGauge.render(spriteBatch, level.player.getHealth());

		spriteBatch.end();

		cameraHelper.setTarget(level.player.getPosition());
		cameraHelper.update(delta);
		cameraHelper.applyTo(gameCamera);

		if (isDebug) {
			debugRenderer.render(world, gameCamera.combined);
		}

		destroyQueue();
	}

	@Override
	public void resize(int width, int height) {
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		gameCamera.viewportWidth = width;
		gameCamera.viewportHeight = height;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		isPaused = true;
	}

	@Override
	public void resume() {
		Assets.instance.init(new AssetManager());
		isPaused = false;
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		debugRenderer.dispose();
		world.dispose();
	}

	public void flipGravity() {
		if (gravity == 1) {
			gravity = -1;
			level.player.rotateUp();
		} else {
			gravity = 1;
			level.player.rotateDown();
		}
		world.setGravity(new Vector2(0, (float) (-9.8 * gravity)));
	}

	@Override
	public InputProcessor getInputProcessor() {
		return new InputAdapter() {
			// Handle keyboard input
			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
				case Keys.ESCAPE:
					game.setScreen(new Play(game));
					break;
				case Keys.A:
					level.player.moveLeft();
					break;
				case Keys.D:
					level.player.moveRight();
					break;
				case Keys.SPACE:
					level.player.jump();
					break;
				case Keys.G:
					flipGravity();
				}
				
				return false;
			}
			@Override
			public boolean keyUp(int keycode) {
				switch (keycode) {
				case Keys.ESCAPE:
					// ((Game)
					// Gdx.app.getApplicationListener()).setScreen(new
					// LevelMenu());
					break;
				case Keys.A:
					level.player.stopRunningLeft();
					break;
				case Keys.D:
					level.player.stopRunningRight();
					break;
				}
				
				return false;
			}
			
			// zoom
			@Override
			public boolean scrolled(int amount) {
				if (amount == 1) {
					cameraHelper.addZoom(gameCamera.zoom * .25f);
				} else if (amount == -1) {
					cameraHelper.addZoom(-gameCamera.zoom * .25f);
				}
				return false;
			}
			// click or touch
			@Override
			public boolean touchDown(int screenX, int screenY,
					int pointer, int button) {
				Vector3 testPoint = new Vector3();
				// convert from vector2 to vector3
				testPoint.set(screenX, screenY, 0);
				// convert meters to pixel cords
				gameCamera.unproject(testPoint);
				
				level.lightBolts.add(level.player.fireLaser(new Vector2(testPoint.x, testPoint.y)));
				
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
		};
	}
	
	public void destroyBody(Body body) {
		// be sure the body you are trying to destroy is not already in the
		// queue
		if (!destroyQueue.contains(body))
			destroyQueue.add(body);
	}
	
	private void destroyQueue() {
		if (!destroyQueue.isEmpty()) {
			Gdx.app.debug(TAG, "destroy(): destroying queue");
			for (Body body : destroyQueue) {
				world.destroyBody(body);
			}
			destroyQueue.clear();
		}
	}

}
