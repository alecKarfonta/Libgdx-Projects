package com.alec.vpong.views;

import java.io.IOException;

import MPClient.MPClient;

import com.alec.vpong.controllers.MyContactListener;
import com.alec.vpong.models.Ball;
import com.alec.vpong.models.Bumper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class Play implements Screen {

	// world
	private World world;						// box2d world
	private Box2DDebugRenderer debugRenderer;	// box2d renderer
	private Stage stage;						// libgdx stage
	private OrthographicCamera camera;			// libgdx camera
	private MPClient mpClient;					// multiplayer
	private SpriteBatch spriteBatch;				
	// world properties
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8;
	private final int POSITIONITERATIONS = 5;
	private int zoom = 25;
	private int width, height, right, left, top, bottom, groundHeight;
	private BitmapFont fontBlack;
	private BitmapFont fontWhite;
	
	// objects
	private Label scoreLabel, clientInfoLabel;
	private Body groundBody, netBody, mouseBody, player2MouseBody, leftInBody, leftOutBody, rightInBody, rightOutBody;
	private Bumper bumper1, bumper2;
	private Ball ball;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Shape shape;
	private MouseJoint mouseJoint, player2MouseJoint;
	private MouseJointDef mouseJointDef, player2MouseJointDef;
	
	private boolean moveBumper = true;
	private static int score = 0;
	// filter bits
	private final short BOUNDARY = 0x0001;
	private final short NET = 0x0002;
	private final short BALL = 0x0003;
	private final short BUMPER = 0x0004;
	
	float lastRequestTime = 0.0f;
	
	@Override
	public void render(float delta) {
		// clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		lastRequestTime += delta;
		if (lastRequestTime > .1f) {
			lastRequestTime = 0.0f;
			mpClient.nl.requestPositionUpdate();
			// check for player move
			if (mpClient.nl.player2Position != null) {
				clientInfoLabel.setText(mpClient.nl.player2Position.toString());
				mpClient.nl.received(null, null);
				player2MouseJoint.setTarget(mpClient.nl.player2Position); 
			}
		}
		
		// Increment the world
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		
		// add each sprite
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		
		ball.render(spriteBatch, delta);
		
		// draw the stage
		stage.act(delta);

		scoreLabel.setText("Score: " + score);
		stage.draw();
		
		spriteBatch.end();

        // update the camera
     	camera.update();
		     	
        // render the screen
        debugRenderer.render(world, camera.combined);
	}
	
	public void createWorld() {
		// create the world with surface gravity
		world = new World(new Vector2(0f, -9.8f), true);
		world.setContactListener(new MyContactListener());
		debugRenderer = new Box2DDebugRenderer();
		
		// setup a camera with a 1:1 ratio to the screen contents
		camera = new OrthographicCamera(width, height);
		
		fontWhite = new BitmapFont(Gdx.files.internal("data/Fonts/whiteFont.fnt"), false);
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		right = width/2/zoom;
		left = -(width/2/zoom);
		top = height/2/zoom;
		bottom = -(height/2/zoom);
		groundHeight = bottom + 10;

		stage = new Stage(width, height, true);

		// center the camera 
		camera.position.set(0,0,0);
	}
	
	public void createGround() {		
		// body definition
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		
		// clear the shape for the next chain
		shape = new ChainShape();
		
		// make the floor
		((ChainShape)shape).createChain(new Vector2[] {new Vector2(left - 100, groundHeight),
												new Vector2(right + 100, groundHeight)} );
		
		// fixture definition
		fixtureDef.shape = shape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 0f;
		fixtureDef.density = 1.0f;
		
		// add the floor to the world
		groundBody = world.createBody(bodyDef);
		groundBody.createFixture(fixtureDef);
		String type = "ground";
		groundBody.setUserData(type);
	}
	
	public void createNet() {
		// body definition
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, groundHeight + 4);
		
		// clear the shape
		shape = new PolygonShape();
		
		// make the shape of the net
		((PolygonShape)shape).setAsBox(.05f, 4f);
		
		// fixture definition
		fixtureDef.shape = shape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 1;
		fixtureDef.density = 1;
		
		// add the net to the world
		netBody = world.createBody(bodyDef);
		netBody.createFixture(fixtureDef);
	}
	
	public void createCharacters() {
		//					ball
		// body definition
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(2f,5f);
		// shape
		shape = new CircleShape();
		shape.setRadius(.5f);
		
		// fixture definition
		fixtureDef.shape = shape;
		fixtureDef.friction = .32f;
		fixtureDef.density = .5f;
		fixtureDef.restitution = .75f;
		
		// create the bumpers
		ball = new Ball(world, fixtureDef, 2f, 5f, .5f);
		
		bumper1 = new Bumper(world, bodyDef, fixtureDef, 6f, groundHeight + 5f);
		
		bumper2 = new Bumper(world, bodyDef, fixtureDef, -6f, groundHeight + 5f);
	}
	
	public void createInRegions() {
		
		// body definition
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(-20f, groundHeight);
		
		// clear the shape for the next chain
		shape = new PolygonShape();
		
		// make the floor
		((PolygonShape)shape).setAsBox(20f, .25f);
		// fixture definition
		fixtureDef.shape = shape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 0f;
		fixtureDef.density = 1.0f;
		
		// add the left in region to the world
		leftInBody = world.createBody(bodyDef);
		leftInBody.createFixture(fixtureDef);
		String type = "leftInRegion";
		leftInBody.setUserData(type);
		
		// add the right in region to the world
		bodyDef.position.set(20f, groundHeight);
		rightInBody = world.createBody(bodyDef);
		rightInBody.createFixture(fixtureDef);
		type = "rightInRegion";
		rightInBody.setUserData(type);
	}
	
	@Override
	public void show() {
		
		// multiplayer
		try {
			mpClient = new MPClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		createWorld();
		
		// create the variables many bodies will share
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		spriteBatch = new SpriteBatch();

		// initialize some default values
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, 0);
		
		fixtureDef.density = 1f;
		fixtureDef.friction = .3f;
		fixtureDef.restitution = 1f;
		
		// create each part of the world
		createGround();	
		createNet();
		createCharacters();
		createInRegions();
		
		LabelStyle styleLabel = new LabelStyle(fontWhite, Color.WHITE);
		scoreLabel = new Label("Score: " + score, styleLabel);
		scoreLabel.setX(0);
		scoreLabel.setY(width / 2);
		scoreLabel.setWidth(width);
		scoreLabel.setAlignment(Align.center);
		stage.addActor(scoreLabel);
		
		clientInfoLabel = new Label("Unknown mode", styleLabel);

		clientInfoLabel.setX(0);
		clientInfoLabel.setY(width/2 + 50);
		clientInfoLabel.setWidth(width);
		clientInfoLabel.setAlignment(Align.center);
		stage.addActor(clientInfoLabel);
		
		// mouse joints
		mouseBody = world.createBody(bodyDef);
		mouseJointDef = new MouseJointDef();
		mouseJointDef.maxForce = 500 * bumper1.getBody().getMass();
		mouseJointDef.collideConnected = true;
		mouseJointDef.bodyA = mouseBody;
		mouseJointDef.bodyB = bumper1.getBody();
		
		bodyDef.position.set(bumper2.getBody().getWorldCenter());
		player2MouseBody = world.createBody(bodyDef);
		player2MouseJointDef = new MouseJointDef();
		player2MouseJointDef.maxForce = 500 * bumper2.getBody().getMass();
		player2MouseJointDef.collideConnected = true;
		player2MouseJointDef.bodyA = player2MouseBody;
		player2MouseJointDef.bodyB = bumper2.getBody();
		player2MouseJoint = (MouseJoint)world.createJoint(player2MouseJointDef);
		
		// handle the input
		Gdx.input.setInputProcessor(new InputMultiplexer(
				// anonymous inner class for screen specific input
				new InputAdapter() {
			
			// Handle keyboard input
			@Override
			public boolean keyDown(int keycode) {
				switch(keycode) {
				case Keys.ESCAPE:
					//((Game) Gdx.app.getApplicationListener()).setScreen(new LevelMenu());
					break;
				case Keys.SPACE: 
					moveBumper = false;
					break;
				}
				return false;
			}
			@Override
			public boolean keyUp(int keycode) {
				switch(keycode) {
				case Keys.SPACE: 
					moveBumper = true;
					break;
				}
				return false;
			}
			
			// zoom
			@Override
			public boolean scrolled(int amount) {
				if (amount == 1) {
					camera.zoom = camera.zoom + (camera.zoom * .25f);
				} else if (amount == -1) {
					camera.zoom = camera.zoom - (camera.zoom * .25f);
				}
				return true;
			}
			
			// click or touch
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				
	            return false;
			}
			
			@Override 
			public boolean touchUp (int x, int y, int pointer, int button) {
                
                return false;
			}
			
			@Override 
			public boolean touchDragged (int x, int y, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				Vector3 temp = new Vector3();
				// convert from click position to world coordinates
				camera.unproject(temp.set(screenX, screenY, 0));
				if (((temp.x > 1) && (temp.x < (width / 2))) && ((temp.y > -(height / 2)) && (temp.y < (height / 2)))) {
					//System.out.println("Mouse moved");
					if (mouseJoint == null) {
						bumper1.getBody().setAwake(true);
						bumper1.getBody().setTransform(temp.x, temp.y, 0);
						mouseBody.setTransform(new Vector2(temp.x, temp.y), 0);
						mouseJointDef.target.set(temp.x, temp.y);
						
						mouseJoint = (MouseJoint)world.createJoint(mouseJointDef);
					}
					
					// move player one bumper
					mouseJoint.setTarget(new Vector2(temp.x - 2, temp.y - 1));
					// update position on network
					mpClient.updatePosition(new Vector2(temp.x, temp.y));
				}
				
				return false;
			}
        
			
		}));	// second input adapter for the input multiplexer
		
		
		shape.dispose();
	}
	
	public static void incrementScore() {
		score++;
	}

	@Override
	public void resize(int width, int height) {
		// reset the camera size to the width of the window scaled to the zoom level
		camera.viewportWidth = width / zoom;
		camera.viewportHeight = height / zoom;
		this.width = width;
		this.height = height;
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
		
	}
	@Override
	public void dispose() {
		stage.dispose();
	}
	
}
