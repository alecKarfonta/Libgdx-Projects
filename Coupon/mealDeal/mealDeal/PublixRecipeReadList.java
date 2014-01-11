package com.alec.mealDeal;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PublixRecipeReadList {
	static ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	
	public static void main(String[] args) {
		/**/	//	get publix recipes	//
		Validate.isTrue(true);
		String rootUrl = "http://www.publix.com/aprons/meals/AllRecipes/RecipeList.do";
		
		try {
			// open the list of recipes
			Document doc = Jsoup.connect(rootUrl).get();
			int recipeIndex = -1; // use iterator?
			// for each link in the document
			for (Element link : doc.select("a[href]")) {
				//if (recipeIndex > 2 ) break; // temporary break statement
				
				// if the link goes to a recipe
				if(link.attr("href").contains("mealId")) {
					//print("linK", link.attr("href"));
					// open the recipe
					doc = Jsoup.connect("http://www.publix.com" + link.attr("href")).get();
					
					// get the title 
					for (Element meta : doc.select("meta")) {
						if (meta.attr("name").contains("Description")) {
							print("title", meta.attr("content"));
							recipes.add(new Recipe(meta.attr("content")));
							recipeIndex++;
						}
					}
					int ingredientIndex = -1;
					// get each ingredient
					for (Element input : doc.select("input")) {
						if (input.attr("name").contains("groceryItemParameter")) {
							//print("Item", input.attr("value"));
							recipes.get(recipeIndex).addIngredient(new Ingredient(input.attr("value")));
							++ingredientIndex;
						} else if (input.attr("name").contains("groceryCategoryParameter")) {
							//print("Category", input.attr("value"));
							recipes.get(recipeIndex).getIngredients().get(ingredientIndex).setCategory(input.attr("value"));
						} else if (input.attr("name").contains("groceryDepartmentParameter")) {
							//print("Department", input.attr("value"));
							recipes.get(recipeIndex).getIngredients().get(ingredientIndex).setDepartment(input.attr("value"));
						} else if (input.attr("name").contains("groceryRecipeQtyParameter")) {
							//print("Quantity", input.attr("value"));
							recipes.get(recipeIndex).getIngredients().get(ingredientIndex).setQuantity(input.attr("value"));
						} 
					}
					
				} 
			}
					
			Gson gson = new Gson();
			//GsonBuilder gsonBuilder = new GsonBuilder();
			
			FileWriter writer = new FileWriter("recipes.json");
			
			writer.write(gson.toJson(recipes));
			
			writer.close();
		
			
			/**		ignore the meal id for now
			    else if (input.attr("name").contains("groceryMealIdParameter")) {
			
				print("MealId", input.attr("value"));
				recipe.getIngredients().get(ingredientIndex).setCategory(input.attr("value"));
				System.out.println();
			}
			/**/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void print(String title, String desc) {
		// if the description is longer than 100 then trim it
		System.out.println(title + ": " + ((desc.length() > 100) ? desc.subSequence(0, 99) : desc));
	}
}
