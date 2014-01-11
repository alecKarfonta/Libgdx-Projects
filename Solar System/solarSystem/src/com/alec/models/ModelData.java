package com.alec.models;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class ModelData {
	private String name = null;
	private Sprite sprite = null;
	
	public ModelData() {};
	
	public ModelData(String name, Sprite sprite) {
		this.name = name;
		this.sprite = sprite;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public String getName() {
		return name;
	}
	
}
