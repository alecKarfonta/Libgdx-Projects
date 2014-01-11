package com.game.vpong;

import com.badlogic.gdx.math.Vector2;

public class MyMath {
	// given a vector in polar coordinates return the rectangular equivalent
	public static Vector2 getRectCoords(Vector2 polar) {
		return new Vector2((float) (polar.x * Math.cos(Math.toRadians(polar.y)))
							, (float) (polar.x * Math.sin(Math.toRadians(polar.y))));
	
	}
	// given a vector in rectangular coordinates return the polar equivalent
	public static Vector2 getPolarCoords(Vector2 rect) {
		return new Vector2((float) (Math.sqrt(Math.pow(rect.x,2) + Math.pow(rect.y,2)))
							, (float) (Math.atan((rect.y/rect.x))));
	}
	// given two Vector2 representing points in rectangular coords, return the slope between these point
	public static float getSlope(Vector2 p1, Vector2 p2) {
		return ((p2.y - p1.y) / p2.x - p2.y);
	}
	
}
