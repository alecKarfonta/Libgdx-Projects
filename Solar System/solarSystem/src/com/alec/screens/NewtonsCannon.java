package com.alec.screens;

import java.util.ArrayList;

import com.alec.models.CannonBall;
import com.alec.models.CelestialBody;
import com.alec.models.ModelData;
import com.alec.solarsystem.MyMath;
import com.alec.solarsystem.listeners.NewtonsCannonContactListener;
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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;

public class NewtonsCannon implements Screen {

	// world variables
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private Stage stage;
	private Table table;
	private Skin skin;
	private Group backgroundGroup, foregroundGroup;
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private ArrayList<Body> destroyQueue;
	private Array<Body> tmpBodies = new Array<Body>();
	private ArrayList<CannonBall> cannonBalls = new ArrayList<CannonBall>(); 
	private int zoom = 10;
	
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
	private float cannonWidth;
	private Vector2 cannonBallVector;
	private DistanceJoint earthCannonJoint;
	private final float G = (float) (6.67384); // fiddle wtih gravity so the orbit speed can be greater
	
	// UI stuff
	private Label forceSliderLabel;
	private Slider forceSlider;
	
	// world objects
	private CelestialBody earth, moon;
	private Body groundBody, cannon;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Shape shape;
	private Texture backgroundTexture;
	private MouseJoint mouseJoint;   
	private Body hitBody[] = new Body[2]; // up to three bodies could be clicked at once
	private Body tempBody;
	private Vector3 testPoint = new Vector3();
	private Vector2 dragPosition = new Vector2();
	private Vector2 forceVectorPolar = new Vector2();
	private Sprite sprite;
	//**	Filter Bits (for collision filtering)
	private final short EARTH = 0x0001;
	private final short CANNONBALL = 0x0002;
	private final short MOON = 0x0003;
	private final short CANNON = 0x0004;
	
	private float timeSinceUpdate;
	
	public NewtonsCannon(Skin skin) {
		this.skin = skin; // share the same skin throughout the program
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		
		//earth.getBody().setLinearVelocity(0,0); // ad hoc

		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATONS);

		stage.act(delta);
		stage.draw();
		
		// center the camera on the earth
		//camera.position.set(earth.getBody().getPosition(), 0);
		camera.update();
		
		// add each sprite
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
				
		world.getBodies(tmpBodies);
		for (Body body : tmpBodies) {
			if (body.getUserData() != null ) {
				if (body.getUserData() instanceof ModelData) {
					timeSinceUpdate += delta;
					if (timeSinceUpdate > 2) {
						timeSinceUpdate = 0;
						//earth.getBody().setLinearVelocity(0, 0);
						//earth.getBody().setTransform(0, 0, 0);
					}
					// apply gravity to each object that is not the earth or the cannon
					if (!((ModelData)body.getUserData()).getName().equals("earth") && !((ModelData)body.getUserData()).getName().equals("cannon")) {
						forceVectorPolar.x = (float) ((G * body.getMass() * earth.getBody().getMass()) 
								/ (Math.pow(MyMath.getDistanceBetween(body.getPosition(), earth.getBody().getPosition()), 2)));	// magnitude
							forceVectorPolar.y = MyMath.getAngleBetween( body.getPosition(), earth.getBody().getPosition());		// direction
							body.applyForceToCenter(MyMath.getRectCoords(forceVectorPolar), false);
							body.setTransform(body.getPosition(), (float) Math.toRadians(forceVectorPolar.y));
					}
					sprite = ((ModelData)body.getUserData()).getSprite();
					sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
					sprite.setRotation((float)Math.toDegrees(body.getAngle()));
					sprite.draw(spriteBatch);
				}
			}
		}

		
		spriteBatch.end();
		
		//debugRenderer.render(world, camera.combined);
		
		if (!destroyQueue.isEmpty()) {
			for (Body body : destroyQueue) {
				world.destroyBody(body);
			}
			earth.getBody().setTransform(0, 0, 0);
			earth.getBody().setLinearVelocity(0, 0);
			destroyQueue.clear();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		print("Game", "resize");
		/**/
		// reset the camera size to the width of the window scaled to the zoom level
		camera.viewportWidth = width / zoom;
		camera.viewportHeight = height / zoom;
		stage.setViewport(width, height);
		//this.width = width / zoom;
		//this.height = height / zoom;
		/**/
		table.invalidateHierarchy();
	}

	public void createWorld() {
		// create the world with surface gravity
		world = new World(new Vector2(0f, -9.8f), true);
		world.setContactListener(new NewtonsCannonContactListener(this));
		debugRenderer = new Box2DDebugRenderer();
		
		// setup a camera with a 1:1 ratio to the screen contents
		camera = new OrthographicCamera(width, height);
		
		// position the camera at the center of the screen
		camera.position.set(0,0,0);
		
		//backgroundGroup = new Group();
		foregroundGroup = new Group();
		
		backgroundTexture = new Texture("data/Images/deepfield.jpg");
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public void createUI() {
		stage = new Stage(width, height, true);
		table = new Table(skin);
		
		//backgroundGroup.setZIndex(0);
		//backgroundGroup.addActor(new Image(new TextureRegion(backgroundTexture, 0 ,0, 1024, 768)));
		//foregroundGroup.setZIndex(1);
		forceSliderLabel = new Label("Force", skin, "medWhite");
		
		/**/	// xSlider
		// TODO: move this style code to the json skin file
		SliderStyle horizSliderStyle = new SliderStyle();
		horizSliderStyle.background = new SpriteDrawable(new Sprite(new Texture("data/Images/SliderBar.bmp")));
		horizSliderStyle.knob = new SpriteDrawable(new Sprite(new Texture("data/Images/Slider.bmp")));
	
		forceSlider = new Slider(1, 50, .5f, false, horizSliderStyle);
		forceSlider.setValue(25);
		cannonBallVector.x = forceSlider.getValue();
		forceSlider.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				cannonBallVector.x = forceSlider.getValue();
			}
			
		});
		/**/
		table.setFillParent(true);
		table.row().expand();
		table.add(forceSliderLabel).align(Align.top);
		table.add(forceSlider).align(Align.top);
		//foregroundGroup.addActor(table);
		
		
		//stage.addActor(backgroundGroup);
		stage.addActor(table);
	}
	
	public void createEarth() {
		
		 // force to set earth in orbit
		// place the earth at the radius of the sun
		earth = new CelestialBody("earth", world, 0, 0,  
				15, 5.5f, "data/Images/Planets/earth_real.png", EARTH, (short) (CANNONBALL | MOON) );
		//earth.getBody().setAngularVelocity(.2f); // tidal locking
		// create cannon
		PolygonShape cannonShape = new PolygonShape();
		cannonWidth = earth.getRadius() * .25f;
		Vector2 topEdgeOfEarth = MyMath.getRectCoords(earth.getRadius(), 90);
		cannonShape.setAsBox(cannonWidth, cannonWidth / 2);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0;
		fixtureDef.restitution = 0;
		fixtureDef.friction = 0;
		fixtureDef.shape = cannonShape;
		fixtureDef.filter.categoryBits = CANNON;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(topEdgeOfEarth.add(0, cannonWidth/2));
		bodyDef.linearDamping = 10;
		
		cannon = world.createBody(bodyDef);
		cannon.createFixture(fixtureDef);
	
		Texture texture = new Texture("data/Images/cannon.gif");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Sprite sprite = new Sprite(texture);
		sprite.setOrigin(0, 0);
		sprite.setSize(cannonWidth * 2	, cannonWidth);
		cannon.setUserData(new ModelData("cannon", sprite));
		
		// set up the cannon ball stuff
		cannonBallVector = new Vector2();
		cannonBallVector.y = 0;	// theta = 0 (horizontal)

		/** /
		DistanceJointDef jointDef = new DistanceJointDef();
		jointDef.bodyA = earth.getBody();
		jointDef.bodyB = cannon;
		jointDef.length = earth.getRadius() + (cannonWidth/2);
		//jointDef.localAnchorA = MyMath.getRectCoords(earth.getRadius(), 90);
		earthCannonJoint = (DistanceJoint) world.createJoint(jointDef);
		/**/
	}
	
	public void createMoon() {
		
		// create the moon, 1/3 the size of the earth
		moon = new CelestialBody("moon", world, earth.getRadius() * 5f, 0, earth.getRadius() * .33f, 5.5f,
				"data/Images/Planets/moon_real.png", MOON, (short) (EARTH | CANNONBALL));
		// set it into orbit around the earth
		Vector2 orbitVector = new Vector2();
		orbitVector.y = 270;		// theta = 90 degrees
		orbitVector.x = 20; 
		moon.getBody().setLinearVelocity(MyMath.getRectCoords(orbitVector));
	}
	
	// for mouse drag
	public void createGround() {
		// body definition
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(left, top);
		
		// clear the shape for the next chain
		shape = new ChainShape();
		
		((ChainShape)shape).createChain(new Vector2[] {new Vector2(left, top),
												new Vector2(left, top + .01f)} );
		
		// fixture definition
		fixtureDef.shape = shape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 0;
		
		// add the floor to the world
		groundBody = world.createBody(bodyDef);
		groundBody.createFixture(fixtureDef);
	}

	public void destroyBody(Body body) {
		destroyQueue.add(body);
	}
	
	@Override
	public void show() {
		timeSinceUpdate = 0.0f;
		destroyQueue = new ArrayList<Body>();
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
		createEarth();	
		createMoon();
		createUI();
		
		// handle the input
		Gdx.input.setInputProcessor(new InputMultiplexer(
				// anonymous inner class for screen specific input
				new InputAdapter() {
			
			// Handle keyboard input
			@Override
			public boolean keyDown(int keycode) {
				switch(keycode) {
				case Keys.SPACE:
					cannonBalls.add(new CannonBall(world, 
							cannon.getPosition().x + cannonWidth * 1.1f, cannon.getPosition().y + (cannonWidth / 2) * .75f, 
							System.currentTimeMillis(), 
							CANNONBALL, (short)(EARTH | MOON | CANNONBALL)));
					cannonBalls.get(cannonBalls.size() - 1).getBody().setLinearVelocity(MyMath.getRectCoords(cannonBallVector));
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
				case Keys.ESCAPE:
					((Game) Gdx.app.getApplicationListener()).setScreen(new LevelMenu());
					break;
				}
				return false;
			}
			
			// zoom
			@Override
			public boolean scrolled(int amount) {
				// center the camera on the zoom location
				/** /
				testPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				// convert meters to pixel cords
	            camera.unproject(testPoint);
	            camera.position.set(testPoint);
	            /**/
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
                // else no object is being dragged so drag the screen
                } else {
                	/** /	// drag the screen
                	testPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    				// convert meters to pixel cords
    	            camera.unproject(testPoint);
    	            camera.position.set(testPoint);
    	            /**/
                }
                return false;
        }
		}, stage));	// second input adapter for the input multiplexer
		
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
		//stage.dispose();
		
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
