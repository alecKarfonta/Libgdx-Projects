package com.alec.mealDeal;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;

public class PublixReadCouponList {
	static ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	static ArrayList<Integer> categoryIds = new ArrayList<Integer>();
	static ArrayList<Coupon> coupons = new ArrayList<Coupon>();
	
	public static void main(String[] args) {
		
		/**/	//	get publix coupons	/
		Validate.isTrue(true);
		// start at a category of coupons
		MyUrl url = new MyUrl("http://weeklyad.publix.com/publix/Default.aspx?action=browsecategoryl1");
		Map<String, String> params = new HashMap<String, String>();
		params.put("viewmode", "0");
		params.put("cattreeid", "5117975");
		params.put("storeid", "2601858");
		url.setParameters(params);
		
		try {
			Document doc = Jsoup.connect(url.toString()).get();
			print("Connected to", url.toString());
			categoryIds = getCategories(doc);
			for (int categoryId : categoryIds) {
				// change the category id for each page
				if (params.containsKey("cattreeid")) {
					params.remove("cattreeid");
					params.put("cattreeid", categoryId + "");
				}
				/**/
				doc = Jsoup.connect(url.toString()).get();
				coupons.addAll(getCoupons(doc));
				/**/
			}
			System.out.println("coupon count: " + coupons.size());
			
			Gson gson = new Gson();
			
			FileWriter writer = new FileWriter("publixCoupons.json");
			
			writer.write(gson.toJson(coupons));
			
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<Integer> getCategories(Document doc) {
		ArrayList<Integer> categoryIds = new ArrayList<Integer>();
		
		for (Element li : doc.select("li")) {
			if (li.attr("id").contains("catID")) {
				categoryIds.add(Integer.parseInt(li.attr("id").substring(6)));
			}
		}
		return categoryIds;
	}

	public static ArrayList<Coupon> getCoupons(Document doc) {
		
		ArrayList<Coupon> coupons = new ArrayList<Coupon>();
		int itemIndex = -1;
		
		for (Element div : doc.select("div")) {
			// title
			if (div.attr("class").contains("mccatltTITLE")) {
				coupons.add(new Coupon(div.text()));
				++itemIndex;
			} else 
			// price
			if (div.attr("class").contains("mccatltDEAL")) {
				coupons.get(itemIndex).setPrice(div.text());
			} else
			// description
			if (div.attr("class").contains("mccatdesc")) {
				coupons.get(itemIndex).setDesc(div.text());
			} else 
			// discount
			if (div.attr("class").contains("mccatltPRICEQ")) {
				coupons.get(itemIndex).setDiscount(div.text());
			} else
			// expiration date
			if (div.attr("class").contains("mcpgltDATE")) {
				coupons.get(itemIndex).setExpDate(div.text());
			}
		}
		
		return coupons;
	}
	
	public ArrayList<Ingredient> getIngredients(Document doc) {
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		int index = -1;
		// exract each ingredient
		for (Element input : doc.select("input")) {
			if (input.attr("name").contains("groceryItemParameter")) {
				//print("Item", input.attr("value"));
				ingredients.add(new Ingredient(input.attr("value")));
				++index;
			} else if (input.attr("name").contains("groceryCategoryParameter")) {
				//print("Category", input.attr("value"));
				ingredients.get(index).setCategory(input.attr("value"));
			} else if (input.attr("name").contains("groceryDepartmentParameter")) {
				//print("Department", input.attr("value"));
				ingredients.get(index).setDepartment(input.attr("value"));
			} else if (input.attr("name").contains("groceryRecipeQtyParameter")) {
				//print("Quantity", input.attr("value"));
				ingredients.get(index).setQuantity(input.attr("value"));
				System.out.println();
			}
		}
		
		return ingredients;
	}
	
	
	public static void print(String title, String desc) {
		// if the description is longer than 100 then trim it
		System.out.println(title + ": " + ((desc.length() > 100) ? desc.subSequence(0, 99) : desc));
	}
}
