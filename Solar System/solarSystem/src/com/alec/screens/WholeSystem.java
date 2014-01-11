package com.alec.screens;

import java.util.ArrayList;

import com.alec.models.CelestialBody;
import com.alec.solarsystem.MyMath;
import com.alec.solarsystem.listeners.MyContactListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;

public class WholeSystem implements Screen {

	// world variables
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private Array<Body> tmpBodies = new Array<Body>();
	private int zoom = 1;
	
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
	
	// solar system stuff
	private float planetScale = 10;	// 100x
	private float radiiScale = .0001f; // 1/1000
	private float densityScale = radiiScale; // since density is a function of radii?
	private float au = 149597871 * radiiScale;
	private final float G = (float) (6.67384 * Math.pow(10, -2.5f)); // fiddle wtih gravity so the orbit speed can be greater
	
	// world objects
	private CelestialBody sun, earth, mercury, venus, mars, jupiter, saturn, uranus;
	private Body groundBody;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Shape shape;
	private MouseJoint mouseJoint;   
	private Body hitBody[] = new Body[2]; // up to three bodies could be clicked at once
	private Body tempBody;
	private Array<Body> worldBodies;
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
		
		// update the camera
		camera.update();
		
		// add each sprite
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		
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
		// calculate the force of gravity
		forceVectorPolar.x = (float) ((G * sun.getBody().getMass() * earth.getBody().getMass()) 
								/ (Math.pow(MyMath.getDistanceBetween(sun.getBody().getPosition(), earth.getBody().getPosition()), 2)));	// magnitude
		forceVectorPolar.y = MyMath.getAngleBetween(earth.getBody().getPosition(), sun.getBody().getPosition());		// direction
		earth.getBody().applyForceToCenter(MyMath.getRectCoords(forceVectorPolar), false);
		
		forceVectorPolar.x = (float) ((G * sun.getBody().getMass() * mercury.getBody().getMass()) 
				/ (Math.pow(MyMath.getDistanceBetween(sun.getBody().getPosition(), mercury.getBody().getPosition()), 2)));	// magnitude
		forceVectorPolar.y = MyMath.getAngleBetween(mercury.getBody().getPosition(), sun.getBody().getPosition());		// direction
		mercury.getBody().applyForceToCenter(MyMath.getRectCoords(forceVectorPolar), false);
		//System.out.println("Force Vector: (" + MyMath.getRectCoords(forceVectorPolar).x + "," + MyMath.getRectCoords(forceVectorPolar).y + ")");
		/**/
		spriteBatch.end();
		
		// render the screen
		//debugRenderer.render(world, camera.combined);
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
		camera.zoom = zoom;
		
		// position the camera at the center of the screen
		camera.position.set(0,0,0);
	}
	
	public void createSolarSystem() {
		Vector2 orbitVector = new Vector2();
		orbitVector.y = 90;		// theta = 90 degrees
		 // force to set earth in orbit
		
		// make the sun the standard unit and scale everything from there
		sun = new CelestialBody("sun", world, 0, 0, 100, 1400f, "data/Images/Planets/sun.png");

		// place the earth at the radius of the sun
		earth = new CelestialBody("earth", world, sun.getRadius() * 1.5f, 0,  
				1 * planetScale, 5.5f, "data/Images/Planets/earth.png");
		
		// set the earth in orbit
		orbitVector.x = 80;
		earth.getBody().setLinearVelocity(MyMath.getRectCoords(orbitVector));
		
		orbitVector.x = 90;
		mercury = new CelestialBody("mercury", world, sun.getRadius() * 1.15f, 0, .5f * planetScale, 5.5f,
								"data/Images/Planets/mercury.png");
		mercury.getBody().setLinearVelocity(MyMath.getRectCoords(orbitVector));
	}
	
	public void createGround() {
		// body definition
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(left, top);
		
		// clear the shape for the next chain
		shape = new ChainShape();
		
		((ChainShape)shape).createChain(new Vector2[] {new Vector2(left, top),
												new Vector2(left, top+1)} );
		
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
				case Keys.UP:
					camera.position.add(0, 10, 0);
					break;
				case Keys.DOWN:
					camera.position.add(0, -10, 0);
					break;
				case Keys.LEFT:
					camera.position.add(-10, 0, 0);
					break;
				case Keys.RIGHT:
					camera.position.add(10, 0, 0);
					break;
				}
				return false;
			}
			
			// zoom
			@Override
			public boolean scrolled(int amount) {
				testPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				// convert meters to pixel cords
	            camera.unproject(testPoint);
	            camera.position.set(testPoint);
				if (amount == 1) {
					camera.zoom = camera.zoom + (camera.zoom * .05f);
				} else if (amount == -1) {
					camera.zoom = camera.zoom - (camera.zoom * .05f);
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
