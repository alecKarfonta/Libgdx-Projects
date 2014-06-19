package com.alec.polymorphMan;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class MyMath {
	//**	Matrix Transformations
	// reflection about the y-axis
	public static ArrayList<Vector2> reflectY(ArrayList<Vector2> origVectors) {
		ArrayList<Vector2> newVectors = new ArrayList<Vector2>();
		for (Vector2 vector : origVectors) {
			newVectors.add(new Vector2(-vector.x,vector.y));
		}
		return newVectors;
	}
	public static Vector2[] reflectY(Vector2[] origVectors) {
		Vector2[] newVectors = new Vector2[origVectors.length];
		for (int index = 0; index < origVectors.length; index++) {
			newVectors[index] = new Vector2(-origVectors[index].x,origVectors[index].y);
		}
		return newVectors;
	}
	// this method using float arrays is probably more efficient, but is very ugly and could cause hard to find
	// index out of range error because of the index + 1, so just stick with the Vector2 versions of this method
	public static float[] reflectY(float[] origVectors) {
		float[] newVectors = new float[origVectors.length];
		for (int index = 0; index < origVectors.length; index += 2) {
			newVectors[index] = -origVectors[index];
			newVectors[index + 1] = origVectors[index + 1];
		}
		return newVectors;
	}
	// reflection about the x-axis
	public static ArrayList<Vector2> reflectX(ArrayList<Vector2> origVectors) {
		ArrayList<Vector2> newVectors = new ArrayList<Vector2>();
		for (Vector2 vector : origVectors) {
			newVectors.add(new Vector2(vector.x,-vector.y));
		}
		return newVectors;
	}
	public static Vector2[] reflectX(Vector2[] origVectors) {
		Vector2[] newVectors = new Vector2[origVectors.length];
		for (int index = 0; index < origVectors.length; index++) {
			newVectors[index] = new Vector2(origVectors[index].x,-origVectors[index].y);
		}
		return newVectors;
	}
	// reflection about the line
	public static ArrayList<Vector2> reflectLine(ArrayList<Vector2> origVectors) {
		ArrayList<Vector2> newVectors = new ArrayList<Vector2>();
		for (Vector2 vector : origVectors) {
			newVectors.add(new Vector2(vector.y,vector.x));
		}
		return newVectors;
	}
	// orthogonal projection on x-axis
	public static ArrayList<Vector2> orthoX(ArrayList<Vector2> origVectors) {
		ArrayList<Vector2> newVectors = new ArrayList<Vector2>();
		for (Vector2 vector : origVectors) {
			newVectors.add(new Vector2(vector.x,0));
		}
		return newVectors;
	}
	// orthogonal projection on y-axis
	public static ArrayList<Vector2> orthoY(ArrayList<Vector2> origVectors) {
		ArrayList<Vector2> newVectors = new ArrayList<Vector2>();
		for (Vector2 vector : origVectors) {
			newVectors.add(new Vector2(0,vector.y));
		}
		return newVectors;
	}
	// shear X
	public static ArrayList<Vector2> shearX(ArrayList<Vector2> origVectors, float factor) {
		ArrayList<Vector2> newVectors = new ArrayList<Vector2>();
		for (Vector2 vector : origVectors) {
			newVectors.add(new Vector2(vector.x + (factor * vector.y), vector.y));
		}
		return newVectors;
	}
	// shear Y
	public static ArrayList<Vector2> shearY(ArrayList<Vector2> origVectors, float factor) {
		ArrayList<Vector2> newVectors = new ArrayList<Vector2>();
		for (Vector2 vector : origVectors) {
			newVectors.add(new Vector2(vector.x, vector.y + (factor * vector.x)));
		}
		return newVectors;
	}
	// rotate ccw
	public static ArrayList<Vector2> rotateCCW(ArrayList<Vector2> origVectors, float degree) {
		ArrayList<Vector2> newVectors = new ArrayList<Vector2>();
		float radians = (float)Math.toRadians(degree);
		for (Vector2 vector : origVectors) {
			newVectors.add(new Vector2((float)(vector.x * Math.cos(radians) - vector.y * Math.sin(radians)), 
										(float)(vector.x * Math.sin(radians) + vector.y * Math.cos(radians)) ));
		}
		return newVectors;
	}
	
	//** 	Coordinate Conversions
	// from polar to rect, polar is assumed to have x = magnitude and y = angle in degrees
	public static Vector2 getRectCoords(Vector2 polar) {
		return new Vector2((float) (polar.x * Math.cos(Math.toRadians(polar.y)))
							, (float) (polar.x * Math.sin(Math.toRadians(polar.y))));
	
	}
	public static Vector2 getRectCoords(float rho, float theta) {
		return new Vector2((float) (rho * Math.cos(Math.toRadians(theta)))
							, (float) (rho * Math.sin(Math.toRadians(theta))));
	
	}
	
	// from rect to polar
	public static Vector2 getPolarCoords(Vector2 rect) {
		return new Vector2((float) (Math.sqrt(Math.pow(rect.x,2) + Math.pow(rect.y,2)))
							, (float) (Math.atan((rect.y/rect.x))));
	}
	
	//** Line stuff
	// given two vectors representing points in rectangular coords, return the slope between them
	public static double getSlope(Vector2 p1, Vector2 p2) {
		return ((p2.y - p1.y) / p2.x - p2.y);
	}
	public static float magnitude(Vector2 vector) {
		return (float)Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
	}
	public static Vector2 normalize(Vector2 origVector) {
		float magnitude = magnitude(origVector);
		return new Vector2((origVector.x / magnitude), (origVector.y / magnitude) );
	}
	public static float crossProduct(Vector2 v1, Vector2 v2) {		// returns angle orthogonal to v1,v2
		return v1.x * v2.y - v1.y * v2.x;
	}
	// angle between two points in degrees
	public static float getAngleBetween(Vector2 p1, Vector2 p2) {
		float deltaX = p2.x - p1.x;
		float deltaY = p2.y - p1.y;
		
		return (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
	}
	public static float getDistanceBetween(Vector2 p1, Vector2 p2) {
		
		return (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
	}
	public static Vector2 getMidwayPoint(Vector2 p1, Vector2 p2) {
		return new Vector2((p1.x+p2.x)/2, (p1.y+p2.y)/2);
	}
}

