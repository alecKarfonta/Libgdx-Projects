package com.alec.screens;

import java.util.ArrayList;

import com.alec.bodies.Box;
import com.alec.listeners.MyContactListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;

public class Play implements Screen {

	//**	world variables
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private float zoom = .005f;
	
	//**	 world properties
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8;
	private final int POSITIONITERATONS = 3;
	private float width, height, left, right, top, bottom; // in world units (meters)
	
	//**	 world objects
	private Body worldOutline, groundBody;
	private Box[][] boxes;
	private int rows, cols;
	// temporarily store all bodies to draw each one's sprite
	private Array<Body> tmpBodies;
	// temporarily store all bodies that need to be destroyed on the next world step
	private ArrayList<Body> destroyQueue;
	// mouse click stuff
	private MouseJoint mouseJoint;   
	private Body hitBody[] = new Body[2]; // up to three bodies could be clicked at once
	private Body tempBody;
	private Vector3 testPoint = new Vector3();
	private Vector2 dragPosition = new Vector2();
	
	private boolean isDebugMode = true;
	private float timeSinceUpdate;
	
	@Override
	public void render(float delta) {
		/**/ 	// general code for rending with libgdx and box2d
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// increment the world 
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATONS);
		
		// update the camera
		camera.update();
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		/**/	// draw each sprite on it's body
		world.getBodies(tmpBodies);
		for (Body body : tmpBodies) {
			if (body.getUserData() != null ) {
				if (body.getUserData() instanceof Sprite) {
					Sprite sprite = (Sprite)body.getUserData();
					sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
					sprite.setRotation((float)Math.toDegrees(body.getAngle()));
					sprite.draw(spriteBatch);
				}
			}
		}
		/**/
		
		spriteBatch.end();
		
		// render the debug outlines
		if (isDebugMode)
			debugRenderer.render(world, camera.combined);
		
		timeSinceUpdate += delta;
		if (timeSinceUpdate > .5f) {
			timeSinceUpdate = 0;
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					boxes[col][row].setAlive((Math.round(Math.random()) == 1 ? true : false));
				}
			}
		}
		
		
		// dont destoy any bodies, just change their sprite
		/** / 	// destroy any bodies in the queue
		if (!destroyQueue.isEmpty()) {
			// use the world to destroy each body
			for (Body body : destroyQueue) {
				world.destroyBody(body);
			}
			// empty the queue list
			destroyQueue.clear();
		}
		
		//outlineWorld();
		/**/
		
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	public void createWorld() {
		/**/	// general code for creating a box2d world
		// create the world with surface gravity
		world = new World(new Vector2(0f, 0f), true);
		world.setContactListener(new MyContactListener());
		debugRenderer = new Box2DDebugRenderer();
		spriteBatch = new SpriteBatch();
		// setup a camera with a 1:1 ratio to the screen contents

		width = Gdx.graphics.getWidth() * zoom;
		height = Gdx.graphics.getHeight() * zoom;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = zoom;
		// position the camera at the center of the screen
		camera.position.set(0,0,0);
		
		// establish screen edges for conveniance
		float border = .9f; // 10% border
		width *= border;
		height *= border;
		left = -(width/2) + .1f;
		right = width/2;
		top = height/2 - .1f;
		bottom = -(height/2);
		
		tmpBodies = new Array<Body>();
		destroyQueue = new ArrayList<Body>();
		/**/
		if (isDebugMode) {
			print("Screen Dimensions" , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			print("Camera Viewport Dimension", camera.viewportWidth, camera.viewportHeight);
			print("World Dimensions", width, height);
			print("Horizontal Edges", left, right);
			print("Vertical Edges", top, bottom);
			outlineWorld();
		}
	}
	
	public void outlineWorld() {
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		fixtureDef.density = 1;
		fixtureDef.friction = .32f;
		fixtureDef.restitution = 0;
		Shape shape = new PolygonShape();
		((PolygonShape)shape).set(new float[] {
											left , top,
											right, top,
											right, bottom,
											left, bottom
											
		});
		fixtureDef.shape = shape;
		worldOutline = world.createBody(bodyDef);
		worldOutline.createFixture(fixtureDef);
	}
	
	public void createGrid() {
		float boxSize = .25f;
		float boxBorder = .3f;
		rows = (int) (Math.floor(height) / (boxSize + boxBorder));
		cols = (int) (Math.floor(width) / (boxSize + boxBorder));
		
				
		boxes = new Box[cols][rows];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				boxes[col][row] = new Box(world,
						left + boxSize + ((col * (boxSize + boxBorder))), 
						top - boxSize - ((row * (boxSize + boxBorder))),
						boxSize);
			}
		}
		
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col += 2) {
				boxes[col][row].setAlive(false);
			}
		}
		
	}
	
	@Override
	public void show() {
		/**/	// general code to initialize the screen and box2d objects
		createWorld();
		createGrid();
		
		// handle the input
		Gdx.input.setInputProcessor(new InputMultiplexer(
				// anonymous inner class for screen specific input
				new InputAdapter() {
			
			// Handle keyboard input
			@Override
			public boolean keyDown(int keycode) {
				switch(keycode) {
				case Keys.ESCAPE:
					((Game) Gdx.app.getApplicationListener()).setScreen(new LevelMenu());
					break;
				}
				return false;
			}
			
			// zoom
			@Override
			public boolean scrolled(int amount) {
				if (amount == 1) {
					camera.zoom += (camera.zoom * .25f);
				} else if (amount == -1) {
					camera.zoom -= (camera.zoom * .25f);
				}

				System.out.println(camera.zoom);
				return true;
			}
			
			// call back to test the click point for fixtures
			QueryCallback callback = new QueryCallback() {
			       @Override public boolean reportFixture (Fixture fixture) {
			          // if the hit fixture's body is the ground body ignore it
			          if (fixture.getBody() == groundBody) return true;

			          if (fixture.testPoint(testPoint.x, testPoint.y)) {
			              tempBody = fixture.getBody();
			                return false;
			          } else
			                return true;
			       }
			    };         
			
			// click or touch
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				// convert from vector2 to vector3
				testPoint.set(screenX, screenY, 0);
				// convert meters to pixel cords
	            camera.unproject(testPoint);
	            
	            // reset the hit body
	            hitBody[pointer] = null;
	            
	            // query the world for fixtures within a 2px box around the mouse click
	            world.QueryAABB(callback, testPoint.x - 1.0f, testPoint.y - 1.0f, testPoint.x + 1.0f, testPoint.y + 1.0f);
	            hitBody[pointer] = tempBody;

	            // if something was hit 
	            if (hitBody[pointer] != null) {
	                MouseJointDef mouseJointDef = new MouseJointDef();
	                mouseJointDef.bodyA = groundBody; // ignored?
	                mouseJointDef.bodyB = hitBody[pointer];
	                mouseJointDef.collideConnected = true;                                                 
	                mouseJointDef.target.set(hitBody[pointer].getPosition().x, hitBody[pointer].getPosition().y);
	                mouseJointDef.maxForce = 3000.0f * hitBody[pointer].getMass();

	                mouseJoint = (MouseJoint)world.createJoint(mouseJointDef);
	                hitBody[pointer].setAwake(true);
	            } 
	            tempBody = null;
	            return false;
			}
			
			@Override 
			public boolean touchUp (int x, int y, int pointer, int button) {
                // if a mouse joint exists we simply destroy it
                if (mouseJoint != null) {
                        world.destroyJoint(mouseJoint);
                        mouseJoint = null;
                }
                return false;
			}
			
			@Override 
			public boolean touchDragged (int x, int y, int pointer) {
                // if a mouse joint exists we  update the target of the joint based on the new mouse coordinates
                if (mouseJoint != null) {
                		// convert from meters to pixels
                        camera.unproject(testPoint.set(x, y, 0));
                        // move the mouse joint to the new mouse location
                        mouseJoint.setTarget(dragPosition.set(testPoint.x, testPoint.y));
                }
                return false;
        }
		})); // car));	// second input adapter for the input multiplexer
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
		world.dispose();
		debugRenderer.dispose();
		//boxSprite.getTexture().dispose();
	}

	
	public void print(String desc, String item1, String item2) {
		System.out.println(desc + ": (" + item1 + "," + item2 + ")");
	}
	public void print(String desc, float item1, float item2) {
		System.out.println(desc + ": (" + item1 + "," + item2 + ")");
	}
	public void print(String desc, int item1, int item2) {
		System.out.println(desc + ": (" + item1 + "," + item2 + ")");
	}
	public void print(String desc, double item1, double item2) {
		System.out.println(desc + ": (" + item1 + "," + item2 + ")");
	}
	public void print(String desc, String text) {
		System.out.println(desc + ": " + text);
	}
	public void print(String text) {
		System.out.println(text);
	}
}
