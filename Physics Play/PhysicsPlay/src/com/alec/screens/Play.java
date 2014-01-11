package com.alec.screens;

import java.util.ArrayList;

import com.alec.bodies.Car;
import com.alec.bodies.Crate;
import com.alec.listeners.MyContactListener;
import com.alec.physicsplay.MyMath;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;

public class Play implements Screen {

	// world variables
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private Array<Body> tmpBodies = new Array<Body>();
	private int zoom = 25;
	
	// world properties
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8;
	private final int POSITIONITERATONS = 3;
	private int width = Gdx.graphics.getWidth();
	private int height = Gdx.graphics.getHeight();
	private int right = width/2/zoom;
	private int left = -(width/2/zoom);
	private int top = height/2/zoom;
	private int bottom = -(height/2/zoom);
	private float groundHeight = bottom + 0.1f;
	
	// solar system stuff
	private int orbitSpeed = 1;
	private int orbitPosition = 1;
	private int au = 1;
	private final float G = (float) (6.67384 * Math.pow(10, -11));
	
	// world objects
	private Car car;
	private Body sun, earth;
	private Body groundBody;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Shape shape;
	private MouseJoint mouseJoint;   
	private ArrayList<Crate> crates = new ArrayList<Crate>();
	private Body hitBody[] = new Body[2]; // up to three bodies could be clicked at once
	private Body tempBody;
	private Vector3 testPoint = new Vector3();
	private Vector2 dragPosition = new Vector2();
	private Vector2 forceVectorPolar = new Vector2();
	
	// filter bits
	private final short BOUNDARY = 0x0001;
	private final short CAR = 0x0002;
	private final short CRATE = 0x0003;
	private final short RADAR = 0x0020;
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// increment the world 
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATONS);
		
		// position the camera at the center of the car
		//camera.position.set(car.getChassis().getPosition().x, car.getChassis().getPosition().y, 0);
		
		// update the camera
		camera.update();
		
		// add each sprite
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		
		// render the car's effects
		//car.render(spriteBatch, Gdx.graphics.getDeltaTime());
		
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
		Vector2 forceVectorPolar = new Vector2();
		
		forceVectorPolar.x = 100;	// magnitude
		forceVectorPolar.y = MyMath.getAngleBetween(earth.getWorldCenter(), sun.getWorldCenter());		// direction
		// apply a force of a variable magnitude in the direction of the orthogonal between the earth and sun center
		earth.applyForceToCenter(MyMath.getRectCoords(forceVectorPolar), false);
		System.out.println("Force Vector: (" + MyMath.getRectCoords(forceVectorPolar).x + "," + MyMath.getRectCoords(forceVectorPolar).y + ")");
		spriteBatch.end();
		
		// render the screen
		debugRenderer.render(world, camera.combined);
	}
	
	@Override
	public void resize(int width, int height) {
		// reset the camera size to the width of the window scaled to the zoom level
		camera.viewportWidth = width / zoom;
		camera.viewportHeight = height / zoom;
		this.width = width;
		this.height = height;
	}

	public void createWorld() {
		// create the world with surface gravity
		world = new World(new Vector2(0f, -9.8f), true);
		world.setContactListener(new MyContactListener());
		debugRenderer = new Box2DDebugRenderer();
		
		// setup a camera with a 1:1 ratio to the screen contents
		camera = new OrthographicCamera(width, height);

		// position the camera at the center of the screen
		camera.position.set(0,0,0);
		
	}
	
	public void createSolarSystem() {
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		Shape shape;
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.gravityScale = 0;
		
		fixtureDef.friction = .3f;
		fixtureDef.restitution = 0;
		
		// sun
		bodyDef.position.set(0,0);
		shape = new CircleShape();
		shape.setRadius(4);
		fixtureDef.density = 100;
		fixtureDef.shape = shape;
		sun = world.createBody(bodyDef);
		sun.createFixture(fixtureDef);
		
		// earth
		shape = new CircleShape();
		shape.setRadius(1);
		fixtureDef.density = 1;
		fixtureDef.shape = shape;
		earth = world.createBody(bodyDef);
		earth.createFixture(fixtureDef);
		
		Vector2 orbitVector = new Vector2(); // polar
		orbitVector.x = au * 7;	// rho (orbit distance)
		orbitVector.y = orbitPosition;
		
		earth.setTransform( MyMath.getRectCoords(orbitVector), 
				(float)(Math.toRadians(MyMath.getAngleBetween(sun.getWorldCenter(), 
										earth.getWorldCenter()))) );

	}
	
	public void createGround() {
		// body definition
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		
		// clear the shape for the next chain
		shape = new ChainShape();
		
		((ChainShape)shape).createChain(new Vector2[] {new Vector2(left - 100, groundHeight),
												new Vector2(right + 100, groundHeight)} );
		
		// fixture definition
		fixtureDef.shape = shape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 0;
		
		// add the floor to the world
		groundBody = world.createBody(bodyDef);
		groundBody.createFixture(fixtureDef);
	}
	
	public void createCharacter() {
		
		FixtureDef wheelFixtureDef = new FixtureDef();
		FixtureDef sledFixtureDef = new FixtureDef();
		FixtureDef radarFixtureDef = new FixtureDef();
		
		fixtureDef.density = 5;
		fixtureDef.friction = .4f;
		fixtureDef.restitution = .2f;
		
		wheelFixtureDef.density = fixtureDef.density * 1.5f;
		wheelFixtureDef.friction = 100;
		wheelFixtureDef.restitution = .2f;
		
		sledFixtureDef.density = .1f;
		sledFixtureDef.friction = 0;
		sledFixtureDef.restitution = 0;
		
		radarFixtureDef.density = 0;
		radarFixtureDef.friction = 0;
		radarFixtureDef.restitution = 0;
		radarFixtureDef.isSensor = true;
		radarFixtureDef.filter.categoryBits = RADAR;
		radarFixtureDef.filter.maskBits = CRATE;
		
		car = new Car(world, fixtureDef,
				wheelFixtureDef, sledFixtureDef, 
				radarFixtureDef, 
				0, 3, // (x,y)
				3, 1.5f);	// width, height
		
	}
	
	public void createSomeBlocks() {
		// create some blocks
		for (int index = 0; index < 100; index++) {
			crates.add(new Crate(world, 
					(float)(Math.random() * 3),
					(float)(Math.random() * 3), 
					0, 20 + index,
					CRATE, CRATE | CAR | BOUNDARY));
		}
		
	}
	
	@Override
	public void show() {
		
		// create the variable many bodies will share
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		spriteBatch = new SpriteBatch();
		
		// initialize some default values
		fixtureDef.shape = shape;
		fixtureDef.density = 2.5f;
		fixtureDef.friction = .3f;
		fixtureDef.restitution = .25f;
		
		// create each part of the screen
		createWorld();
		createGround();		
		//createCharacter();
		//createSomeBlocks();
		createSolarSystem();
		
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
					camera.zoom = camera.zoom + (camera.zoom * .25f);
				} else if (amount == -1) {
					camera.zoom = camera.zoom - (camera.zoom * .25f);
				}
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
		
		if (shape != null) {
			shape.dispose();
		}
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
	
}
