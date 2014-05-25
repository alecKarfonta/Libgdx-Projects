package com.alec.libs;

import com.badlogic.gdx.math.Vector2;

public class MyMath {
	public static Vector2 getRectCoords(double r, double theta) {
		return new Vector2((float) (r * Math.cos(theta)), 
				(float) (r * Math.sin(theta)));
	}
}
