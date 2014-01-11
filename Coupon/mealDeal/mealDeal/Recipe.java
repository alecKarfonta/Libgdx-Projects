package com.alec.mealDeal;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class Recipe {
	ArrayList<Ingredient> ingredients;
	ArrayList<String> prepSteps;
	ArrayList<String> cookSteps;
	BufferedImage image;
	String title;
	
	public Recipe() {
		ingredients = new ArrayList<Ingredient>();
		prepSteps = new ArrayList<String>();
		cookSteps = new ArrayList<String>();
	}
	
	public Recipe(String title) {
		this.title = title;
		ingredients = new ArrayList<Ingredient>();
	}
	
	public void addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public ArrayList<String> getPrepSteps() {
		return prepSteps;
	}

	public void setPrepSteps(ArrayList<String> prepSteps) {
		this.prepSteps = prepSteps;
	}

	public ArrayList<String> getCookSteps() {
		return cookSteps;
	}

	public void setCookSteps(ArrayList<String> cookSteps) {
		this.cookSteps = cookSteps;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
