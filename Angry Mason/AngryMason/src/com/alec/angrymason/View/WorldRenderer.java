package com.alec.angrymason.View;

import java.util.ArrayList;
import java.util.Iterator;

import com.alec.angrymason.AngryMason;
import com.alec.angrymason.Models.Bullet;
import com.alec.angrymason.Models.Enemy;
import com.alec.angrymason.Models.Ship;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
// this class defines our world renderer which handles our camera, textures, and anything drawing related
public class WorldRenderer {
	
	World world;
	SpriteBatch batch;
	Ship ship;
	OrthographicCamera cam; // orthographic just means its perpendicular or your looking down at the x y axes 
	Texture shipTexture, followerTexture, bulletTexture, backgroundTexture;
	float width, height;
	ShapeRenderer shapeRenderer; // draws simple shapes 
	
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	Iterator<Enemy> eIter;
	Enemy enemy;
	
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	Iterator<Bullet> bIter;
	Bullet bullet;
	
	// this looks like a flame
	ParticleEmitter exhaust;
	
	public WorldRenderer(World world) {
		this.world = world;
		
		// hack to unproject touch input?
		world.setwRenderer(this);
		
		// scale the screen?
		width = Gdx.graphics.getWidth() / 30;
		height = Gdx.graphics.getHeight() / 30;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);
		cam.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined); // dont worry about this
		
		// background 
		backgroundTexture = new Texture("data/itsFullOfStars.png");
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// ship
		shipTexture = new Texture("data/ship.png");
		shipTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// bullet
		bulletTexture = new Texture("data/bullet.png");
		bulletTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				
		// enemies
		followerTexture = new Texture("data/follower.png");
		followerTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				
		shapeRenderer = new ShapeRenderer(); // is this even doing anything?
		
		// exhaust
		exhaust = new ParticleEmitter();
		try {
			// the file for this exhaust can be created with this https://code.google.com/p/libgdx/wiki/ParticleEditor
			exhaust.load(Gdx.files.internal("data/exhaust").reader(2024));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Texture particleTexture = new Texture(Gdx.files.internal("data/particle.png"));
		Sprite particle = new Sprite(particleTexture);
		exhaust.setSprite(particle);
		// set the size of the exhaust, this should be based on the speed of the ship
		exhaust.getScale().setHigh(0.3f);
		exhaust.start();
	}
	
	public void render() {
		// clear the screen with black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// add the entities 
		ship = world.getShip();
		enemies = world.getEnemies();
		bullets = world.getBullets();
		
		// exhaust
		// place the exhaust at the center of the ship
		exhaust.setPosition(ship.getPosition().x + ship.getWidth() / 2, ship.getPosition().y + ship.getHeight() /2); 
		exhaust.start();
		setExhaustRotation();
		
		// update the camera
		cam.position.set(ship.getPosition().x, ship.getPosition().y, 0);
		cam.update();
		
		// run the sprite batch
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		batch.draw(backgroundTexture, 0, 0	// texture, draw location
				, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()	// origin(x,y), texture dimension(x,y)
				, .1f, .1f, 0 												// scale(x,y), rotation 
				, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() 	// crop origin(x,y), crop dimensions(x,y) ( just get the whole image)
				, false, false);											// translate over axes(x,y)
		
		// draw the exhaust, based on the time
		exhaust.draw(batch, Gdx.graphics.getDeltaTime());

		// 				*** Ship ***
		batch.draw(shipTexture, ship.getPosition().x, ship.getPosition().y					// texture, draw location(x,y)
				, ship.getWidth()/2, ship.getHeight()/2, ship.getWidth(), ship.getHeight()	// origin(x,y), texture dimension(x,y)
				, 1, 1, ship.getRotation() 													// scale(x,y), rotation 
				, 0, 0, shipTexture.getWidth(), shipTexture.getHeight() 					// crop origin(x,y), crop dimensions(x,y)
				, false, false);	// translate over axes(x,y)
		
		
		// 				*** Enemies	***
		eIter = enemies.iterator();	// ?
		// draw each enemy in the list
		for (Enemy e : enemies) {
			batch.draw(followerTexture, e.getPosition().x, e.getPosition().y		// texture, draw location(x,y)
					, e.getWidth()/2, e.getHeight()/2, e.getWidth(), e.getHeight()	// origin(x,y), texture dimension(x,y)
					, 1, 1, e.getRotation() 										// scale(x,y), rotation 
					, 0, 0, followerTexture.getWidth(), followerTexture.getHeight() // crop origin(x,y), crop dimensions(x,y)
					, false, false);												// translate over axes(x,y)
		}
		

		// 				*** Bullets	***
		bIter = bullets.iterator();
		while(bIter.hasNext()) {
			bullet = bIter.next();
			batch.draw(bulletTexture, bullet.getPosition().x, bullet.getPosition().y					// texture, draw location(x,y)
					, bullet.getWidth()/2, bullet.getHeight()/2, bullet.getWidth(), bullet.getHeight()	// origin(x,y), texture dimension(x,y)
					, 1, 1, bullet.getRotation() 														// scale(x,y), rotation 
					, 0, 0, bulletTexture.getWidth(), bulletTexture.getHeight() 						// crop origin(x,y), crop dimensions(x,y)
					, false, false);	// translate over axes (x,y)
		}
		batch.end();
		
		// in debug mode draw the hitboxes of each entity so that we can see exactly when collisions happen
		if (AngryMason.DEBUG == true) {
			// Shape Renderer (DEBUG ONLY)
			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.begin(ShapeType.Rectangle);
			
			// add a shape renderer for the ship		
			shapeRenderer.setColor(Color.CYAN);
			shapeRenderer.rect(ship.getBounds().x, ship.getBounds().y, ship.getBounds().width, ship.getBounds().height);
			
			// add a rectangle for the follower					
			shapeRenderer.setColor(Color.RED);
			while(eIter.hasNext()) {
				enemy = eIter.next();
				shapeRenderer.rect(enemy.getBounds().x, enemy.getBounds().y, enemy.getBounds().width, enemy.getBounds().height);
			}
			for (Bullet b : bullets) {
				shapeRenderer.rect(b.getBounds().x, b.getBounds().y, b.getBounds().width, b.getBounds().height);
			}
			
			shapeRenderer.end();
			/**/
		}
	}
	
	private void setExhaustRotation() {
		float angle = ship.getRotation();
		// have no idea how this works, but it does 
		exhaust.getAngle().setLow(angle + 270);
		exhaust.getAngle().setHighMin(angle + 270 - 45);
		exhaust.getAngle().setHighMax(angle + 270 + 45);		
	}

	public OrthographicCamera getCamera() {
		return cam;
	}
	
	public void dispose() {
		batch.dispose();
		shipTexture.dispose();
	}
}
