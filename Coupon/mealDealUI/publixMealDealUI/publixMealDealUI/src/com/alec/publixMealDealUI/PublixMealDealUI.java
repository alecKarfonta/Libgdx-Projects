package com.alec.publixMealDealUI;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.alec.publicMealDealUI.Models.Recipe;
import com.alec.publixMealDealUI.Views.Home;
import com.alec.publixMealDealUI.Views.MealDetails;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PublixMealDealUI extends Game {

	public static final String VERSION = "0.0.0.1";
	public static final String LOG = "Publix Meal Deals";
	public static boolean DEBUG = false;
	private FPSLogger logFps;
	
	private Skin skin;
	ArrayList<String> recipeTitles;
	ArrayList<Recipe> featuredRecipeList;
	
	private Screen screen;
	
	@Override
	public void create() {
		// create a new skin to hold all of the styles and textures
		skin = new Skin(Gdx.files.internal("data/ui/menuSkin.json"), new TextureAtlas("data/ui/atlas.pack"));

		// read the lists of featured recipes
		Gson gson = new Gson();
		Type recipeArrayListType = new TypeToken<ArrayList<Recipe>>(){}.getType();
		
		// read the featured recipes list from json file
		featuredRecipeList = gson.fromJson(Gdx.files.internal("data/featuredRecipes.json").readString(), recipeArrayListType);
		
		
		setScreen(new Home(featuredRecipeList, skin));
		logFps = new FPSLogger();
	}
	
	@Override
	public void dispose() {
		if (screen != null) {
			screen.dispose();
			super.dispose();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
}
