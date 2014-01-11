package com.alec.angrymason.View;

import java.util.ArrayList;
import java.util.Iterator;

import com.alec.angrymason.AngryMason;
import com.alec.angrymason.Models.Bullet;
import com.alec.angrymason.Models.Enemy;
import com.alec.angrymason.Models.Follower;
import com.alec.angrymason.Models.Ship;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

// this class defines the world: everything in our game screen
public class World {
	
	AngryMason game;					
	Ship ship;
	WorldRenderer wRenderer;
	int level = 1;
	
	Iterator<Enemy> eIter; // an iterator that is used to step through the list of enemies on the screen
	Enemy enemy;	// using the generic Enemy type for now
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	// bullet variables
	Iterator<Bullet> bIter;
	Bullet bullet;
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public World(AngryMason game) {
		this.game = game;
		ship = new Ship(new Vector2(5, 5), 1, 1, 0, 5f);  // initialize the position of the ship
		enemies.add(new Follower(4f, 0, 1, 1, new Vector2(10, 10))); // create a new enemy of the type follower
		
		// set the input processor to our custom InputHandler class
		Gdx.input.setInputProcessor(new InputHandler(this));
	}
	
	public void update() {
		// update the ships position and rotation
		ship.update();
		
		//				*** Collision detection 	***
		eIter = enemies.iterator();
		// step through the list of enemies
		while (eIter.hasNext()) {
			enemy = eIter.next();
			// update the enemy's position
			enemy.advance(Gdx.graphics.getDeltaTime(), ship);
			// if the ship's hitbox overlaps the enemy's hitbox
			if (ship.getBounds().overlaps(enemy.getBounds())) {
				// just the log the ship is hit, do something cool later
				Gdx.app.log(AngryMason.LOG, "Ship Hit!!!");
			}
		}
		
		bIter = bullets.iterator();
		// step throught the list of bullets
		while (bIter.hasNext()) {
			bullet = bIter.next();
			// update the position of the bullet
			bullet.update(ship);
			// if the enemies hitbox overlaps the bullets hitbox destroy the enemy
			// ! this is only checking one enemy, the last one created
			if (enemy.getBounds().overlaps(bullet.getBounds())) {
				Gdx.app.log(AngryMason.LOG, "Enemy Hit!!!");
				eIter.remove(); // remove the enemy from the list
				bIter.remove(); // remove the bullet from the list
				AngryAudio.explode();
				// create a new enemy of the follower type at a random position relative to the ship
				enemies.add(new Follower(4f, 0, 1, 1, 
										new Vector2(ship.getPosition().x + (float)(20 * Math.random()), 
												ship.getPosition().y + (float)(20 * Math.random())))); 
												// (speed, rotation, width, height, position(x,y)) 
				
			}
		}
	}
	
	public WorldRenderer getRenderer() {
		return wRenderer;
	}

	public void setwRenderer(WorldRenderer wRenderer) {
		this.wRenderer = wRenderer;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	public void addBullet(Bullet b) {
		bullets.add(b);
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
	
	public void dispose() {
		
	}
	
	
}
