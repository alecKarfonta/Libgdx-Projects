package com.alec.polymorphMan.models;

import com.alec.polymorphMan.MyMath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class StickMan {
	private float headRadius, trunkLength, armLength, legLength;
	private float armAngle, legAngle;
	//private int numberOfArms, numberOfLegs;
	
	public StickMan(float headRadius, float trunkLength, float armLength,
			float legLength, float armAngle, float legAngle) {
		this.headRadius = headRadius;
		this.trunkLength = trunkLength;
		this.armLength = armLength;
		this.legLength = legLength;
		this.armAngle = armAngle;
		this.legAngle = legAngle;
	}
	
	public StickMan spawn() {
		
		// Newman..
		return new StickMan(mutate(headRadius), mutate(trunkLength), 
				mutate(armLength), mutate(legLength), 
				mutate(armAngle), mutate(legAngle));
	}
	
	public void render(SpriteBatch batch, Vector2 origin, ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.ORANGE);
		
		Vector2 trunkEnd = MyMath.getRectCoords(new Vector2(trunkLength, 270));
		shapeRenderer.line(origin, trunkEnd);
		
		Vector2 headCenter = new Vector2(origin).add(new Vector2(0,headRadius));
		shapeRenderer.circle(headCenter.x, headCenter.y, headRadius);

		Vector2 leftArmEnd = MyMath.getRectCoords(new Vector2(armLength, -armAngle));
		Vector2 rightArmEnd = MyMath.getRectCoords(new Vector2(armLength, 180 + armAngle));
		shapeRenderer.line(new Vector2(origin).add(new Vector2(0,-trunkLength*.20f)), 
				new Vector2(origin).add(leftArmEnd));
		shapeRenderer.line(new Vector2(origin).add(new Vector2(0,-trunkLength*.20f)), 
				new Vector2(origin).add(rightArmEnd));
		
		
		Vector2 leftLegEnd = MyMath.getRectCoords(new Vector2(legLength, 270 + legAngle));
		Vector2 rightLegEnd = MyMath.getRectCoords(new Vector2(legLength, 270 - legAngle));
		shapeRenderer.line(trunkEnd, 
				new Vector2(trunkEnd).add(leftLegEnd));
		shapeRenderer.line(trunkEnd, 
				new Vector2(trunkEnd).add(rightLegEnd));
		
		shapeRenderer.end();
	}
	
	private float mutate(float original) {
		double dice = Math.random();
		// a value mutates by 10% 
		// 30% chance the value will grow, stay the same, or shrink
		if (dice < .20) {
			return original += .1;// * original;
		} else if (dice < .40) {
			// stay the same
			return original;
		} else {
			return original -= .1;// * original;
		}
		
	}
	
}
