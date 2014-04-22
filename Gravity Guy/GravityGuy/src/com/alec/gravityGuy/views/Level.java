package com.alec.gravityGuy.views;

import com.alec.gravityGuy.models.Ground;
import com.alec.gravityGuy.models.Lgm1;
import com.alec.gravityGuy.models.Lgm1Death;
import com.alec.gravityGuy.models.LightBolt;
import com.alec.gravityGuy.models.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Level {
	public static final String TAG = Level.class.getName();

	public static enum BLOCK_TYPE {
		EMPTY(0, 0, 0), // black
		GROUND(140, 98, 57), // brown
		PLAYER_SPAWN(0, 255, 0), // green
		LGM1(255, 255, 0), // yellow
		PEARL(255, 0, 0); // red

		private int color;

		private BLOCK_TYPE(int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 0xff; // format
		}

		private boolean sameColor(int color) {
			return this.color == color;
		}

	}

	private Play play;
	public Player player;
	public Array<Ground> grounds;
	public Array<LightBolt> lightBolts;
	public Array<Lgm1> lgm1s;
	public Array<Lgm1Death> lgm1Deaths;

	public Level(Play play, String filename) {
		init(play, filename);
	}

	public void init(Play play, String filename) {
		this.play = play;
		player = new Player(play.world);
		grounds = new Array<Ground>();
		lightBolts = new Array<LightBolt>();
		lgm1s = new Array<Lgm1>();
		lgm1Deaths = new Array<Lgm1Death>();
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));

		int lastPixel = -1;
		int invY;
		int height = Gdx.graphics.getHeight();
		for (int y = 0; y < pixmap.getHeight(); y++) {
			for (int x = 0; x < pixmap.getWidth(); x++) {
				int currentPixel = pixmap.getPixel(x, y);
				invY = height - y;
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					// empty block - do nothing
				} else if (BLOCK_TYPE.GROUND.sameColor(currentPixel)) {
					Gdx.app.log(TAG, "Ground created: ("  + x + "," + y + ")");
					if (lastPixel != currentPixel) {
						// find the width of the building
						grounds.add(new Ground(x , invY));
					} else {
						grounds.peek().addSection();
					}
				} else if (BLOCK_TYPE.PLAYER_SPAWN.sameColor(currentPixel)) {
					Gdx.app.log(TAG, "Player created: ("  + x + "," + y + ")");
					player.setPosition(x, invY);
				} else if (BLOCK_TYPE.LGM1.sameColor(currentPixel)) {
					Gdx.app.log(TAG, "Lgm1 created: ("  + x + "," + y + ")");
					lgm1s.add(new Lgm1(play.world, player, x, invY));
				} else if (BLOCK_TYPE.PEARL.sameColor(currentPixel)) {
					//pearls.add(new Pearl(play.world, x, invY));
				} else {
					int r = 0xff & (currentPixel >>> 24); // red channel
					int g = 0xff & (currentPixel >>> 16); // green channel
					int b = 0xff & (currentPixel >>> 8); // blue channel
					int a = 0xff & currentPixel; // alpha channel
					Gdx.app.error(TAG, "Unknown Object at (" + x + "," + y
							+ ") : " + r + " " + " " + g + " " + b + " " + a);
				}
				lastPixel = currentPixel;
			}
		}
		for (Ground ground : grounds) {
			ground.init(play.world);
		}
		pixmap.dispose();
		Gdx.app.debug(TAG, "Level '" + filename + "' loaded");
	}

	public void updateAndRender(SpriteBatch batch, float delta) {		// models with particles need batch and delta
		player.update(delta);
		player.render(batch, delta);
		for (LightBolt lightBolt : lightBolts) {
			lightBolt.update(delta);
			if (lightBolt.stateTime > 1) {
				play.destroyBody(lightBolt.body);
				lightBolts.removeValue(lightBolt, true);
			}
		}
		for (Lgm1 lgm1 : lgm1s) {
			lgm1.update(delta);
			lgm1.render(batch, delta);
			if (lgm1.getHealth() <= 0) {
				destroyLgm1(lgm1);
			}
		}
		for (Lgm1Death lgm1Death : lgm1Deaths) {
			lgm1Death.render(batch, delta);
			
			if (lgm1Death.shouldDestroy()) {
				lgm1Deaths.removeValue(lgm1Death, true);
			}
		}
	}

	public void render(SpriteBatch batch) {
		for (Ground ground : grounds) {
			ground.render(batch);
		}
		for (LightBolt lightBolt : lightBolts) {
			lightBolt.render(batch);
		}
	}

	public void destroyLgm1 (Lgm1 lgm1) {
		if (lgm1s.contains(lgm1, true)) {
			lgm1Deaths.add(new Lgm1Death(lgm1.getPosition()));
			lgm1s.removeValue(lgm1, true);
			play.destroyBody(lgm1.body);
			play.destroyBody(lgm1.wheel);
		}
	}

	public void destroyLightBolt (LightBolt lightBolt) {
		if (lightBolts.contains(lightBolt, true)) {
			// TODO: particle effect
			lightBolts.removeValue(lightBolt, true);
			play.destroyBody(lightBolt.body);
			lightBolt = null;
		}
	}
	
}
