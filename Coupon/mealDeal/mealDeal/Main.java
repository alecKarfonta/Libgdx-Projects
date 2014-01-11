package com.alec.mealDeal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Main {
	static ArrayList<Recipe> featuredRecipes = new ArrayList<Recipe>();
	static ArrayList<Recipe> allRecipes;
	static ArrayList<Coupon> coupons;
	
	public static void main(String[] args) {
		// generate the list of featured recipes
		
		Gson gson = new Gson();
		// create tokens to specify the generics of the arraylists in the json files
		Type recipeArrayListType = new TypeToken<ArrayList<Recipe>>(){}.getType();
		Type couponArrayListType = new TypeToken<ArrayList<Coupon>>(){}.getType();
		String[] ingredientTitleWords;
		String[] couponTitleWords;
		
		try {
			// reade all recipes from the json file
			allRecipes = gson.fromJson(new JsonReader(new FileReader("recipes.json")), recipeArrayListType);
			// read all the coupons from json file
			coupons = gson.fromJson(new JsonReader(new FileReader("coupons.json")), couponArrayListType);

			int wordMatchCount = 0;	
			int totalIngredientCouponMatchCount = 0;
			int recipeIngredientCouponMatchCount = 0;
			/**/
			// for each recipe
			for (Recipe recipe : allRecipes) {
				// for each ingredient
				for (Ingredient ingredient : recipe.getIngredients()) {
					// clean the ingredient title
					ingredientTitleWords = cleanString(ingredient.getItem()).split(" ");
					// for each coupon
					for (Coupon coupon : coupons) {
						// clean the titles
						couponTitleWords = cleanString(coupon.getTitle()).split(" ");
						// for each word in the coupon title
						for (String couponWord : couponTitleWords) {
							// for each word in the ingredient title
							for (String ingredientWord : ingredientTitleWords) {
								// if the word from the coupon title matches the word form the ingredient title
								if (couponWord.equals(ingredientWord)) {
									wordMatchCount++;
								}
							}
						}
						// if more than half the words in the ingredient title match words in the coupon title
						if (wordMatchCount > (ingredientTitleWords.length/2)) {
							/*
							 * add more validation here
							 * make sure there are spaces around the matched text to avoid things like lemon 
							 * 		matching lemonade
							 * check if the following words are directly adjacent to the text matched from the 
							 * 		ingredient in the coupon title: free, squash, flavor, platter
							 */
							// set the ingredient's coupon to the one matched
							ingredient.setCoupon(coupon);
							recipeIngredientCouponMatchCount++;
							/** /
							// display each matching coupon
							print("Match", recipe.title);
							print("Ingredient" , ingredient.getItem());
							print("Coupon" , coupon.getTitle());
							System.out.println();
							/**/
						}
						wordMatchCount = 0;
					}
				}
				totalIngredientCouponMatchCount += recipeIngredientCouponMatchCount;
				// if more than two ingredients are on sale for the current recipe
				if (recipeIngredientCouponMatchCount > 2) {
					// add the recipe to the featured recipes list
					featuredRecipes.add(recipe);
				}
				recipeIngredientCouponMatchCount = 0;
			}
			print("Coupons Found", totalIngredientCouponMatchCount + "");
			// after the featured recipes list has been built print it to json
			FileWriter fw = new FileWriter("featuredRecipes.json");
			fw.write(gson.toJson(featuredRecipes));
			fw.close();
			/**/
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2 ) {
			e2.printStackTrace();
		}
	}
	
	public static void print(String title, String desc) {
		// if the description is longer than 100 then trim it
		System.out.println(title + ": " + ((desc.length() > 100) ? desc.subSequence(0, 99) : desc));
	}
	
	public static String cleanString(String unclean) {
		return unclean.toLowerCase().replaceAll(" of ", "").replaceAll(" the ", "").replaceAll(" is ", "").replace("(", "").replace(")", "").replaceAll(" with ", "");
	}
}
