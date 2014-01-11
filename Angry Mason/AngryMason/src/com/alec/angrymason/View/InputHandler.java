package com.alec.angrymason.View;

import com.alec.angrymason.Models.Bullet;
import com.alec.angrymason.Models.Ship;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InputHandler implements InputProcessor {
	
	World world;
	Ship ship;
	Vector3 touch = new Vector3();
	Vector2 v2Touch = new Vector2();
	
	public InputHandler(World world) {
		this.world = world;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		ship = world.getShip();
		switch(keycode) {
			case Keys.W:
					ship.getVelocity().y = 1;
				break;
			case Keys.A:
					ship.getVelocity().x = -1;
				break;
			case Keys.S:
					ship.getVelocity().y  = -1;
				break;
			case Keys.D:
					ship.getVelocity().x = 1;
				break;
			default:
				break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.W:
			if (ship.getVelocity().y == 1) 
				ship.getVelocity().y = 0;
			break;
		case Keys.A:
			if (ship.getVelocity().x == -1) 
				ship.getVelocity().x = 0;
			break;
		case Keys.S:
			if (ship.getVelocity().y == -1) 
				ship.getVelocity().y  = 0;
			break;
		case Keys.D:
			if (ship.getVelocity().x == 1) 
				ship.getVelocity().x = 0;
			break;
		default:
			break;
	}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	//			*** Touch Input ***
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// get camera from renderer
		touch.set(screenX, screenY, 0);
		// unproject the input
		world.getRenderer().getCamera().unproject(touch);
		v2Touch.set(touch.x, touch.y);
		ship = world.getShip();
		world.addBullet(new Bullet(Bullet.speed, 0, .1f, 8/20f		// (speed, rotation, width, height) 
				, new Vector2(ship.getPosition().x + ship.getWidth() / 2 , ship.getPosition().y + ship.getHeight() / 2) // (position)
				, new Vector2(v2Touch.sub(ship.getPosition())))); // velocity
		AngryAudio.shoot();
		return true;
	}

	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {	
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
