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

public class CompareCoupons {
	static ArrayList<Coupon> publixCoupons;
	static ArrayList<Coupon> walmartCoupons;
	static ArrayList<Coupon> couponsComCoupons;
	static ArrayList<ArrayList<Coupon>> matchedCoupons = new ArrayList<ArrayList<Coupon>>();
	
	public static void main(String[] args) {

		System.out.println("start");
		// match coupons 
		matchedCoupons.add(new ArrayList<Coupon>());	// index 0 - publix coupon list
		matchedCoupons.add(new ArrayList<Coupon>());	// index 1 - walmart coupon list
		matchedCoupons.add(new ArrayList<Coupon>());	// index 2 - couponsCom coupon list
		
		Gson gson = new Gson();
		// create tokens to specify the generics of the arraylists in the json files
		Type couponArrayListType = new TypeToken<ArrayList<Coupon>>(){}.getType();
		String[] publixCouponTitleWords;
		try {
			// read all the coupons from json file
			publixCoupons = gson.fromJson(new JsonReader(new FileReader("publixCoupons.json")), couponArrayListType);
			walmartCoupons = gson.fromJson(new JsonReader(new FileReader("walmartCoupons.json")), couponArrayListType);
			couponsComCoupons = gson.fromJson(new JsonReader(new FileReader("couponsComCoupons.json")), couponArrayListType);
			
			String cleanPublixTitle;
			String cleanWalmartTitle;
			String cleanWalmartDesc;
			/** using regions matches
			for (Coupon publixCoupon : publixCoupons) {
				cleanPublixTitle = cleanString(publixCoupon.getTitle());
				for (Coupon walmartCoupon : walmartCoupons) {
					cleanWalmartTitle = cleanString(walmartCoupon.getTitle());
					for (String walmartWord : cleanString(walmartCoupon.getTitle()).split(" ")) {
						
						for (int index = 0; index < (cleanPublixTitle.length() - walmartWord.length()); index++) {
							if (cleanPublixTitle.regionMatches(index, walmartWord, 0, walmartWord.length())) {
								print("Match", cleanPublixTitle + " - " + walmartWord);
							}
						}
					}
				}
			}
			/**/
			
			
			/**/ //old for each method/
			int wordMatchCount = 0;	
			// for each publix coupon
			for (Coupon publixCoupon : publixCoupons) {
				// clean the publix title and split it into a string[]
				publixCouponTitleWords = cleanString(publixCoupon.getTitle()).split(" ");
				//print("Publix", cleanString(publixCoupon.getTitle()));
				// for each word in the publix coupon title
				for (String publixWord : publixCouponTitleWords) {
					// if the publix word is longer than 3 chars
					if (publixWord.length() > 3) {
						// for each walmart coupon
						for (Coupon walmartCoupon : walmartCoupons) {
							// for each word in the walmart coupon desc
							for (String walmartWord : cleanString(walmartCoupon.getDesc()).split(" ")) {
								// if the walmart word is longer than 3 chars
								if (walmartWord.length() > 3 ) {
									// if a word matches 
									if (publixWord.equals(walmartWord)) {
										wordMatchCount++;
									}
								}
							}
							// for each word in the walmart coupon title
							for (String walmartWord : cleanString(walmartCoupon.getTitle()).split(" ")) {
								// if the walmart word is longer than 3 chars
								if (walmartWord.length() > 3 ) {
									// if a word matches 
									if (publixWord.equals(walmartWord)) {
										wordMatchCount++;
									}
								}
							}
							// if each word in the publix title exists in the walmart title or description
							if (wordMatchCount >= (publixCouponTitleWords.length/2)) {
								matchedCoupons.get(0).add(publixCoupon);
								matchedCoupons.get(1).add(walmartCoupon);
							}
							wordMatchCount = 0;
						}
						for (Coupon couponsComCoupon : couponsComCoupons) {
							// for each word in the couponsCom coupon desc
							for (String couponsComWord : cleanString(couponsComCoupon.getDesc()).split(" ")) {
								// if the couponsComWords word is longer than 3 chars
								if (couponsComWord.length() > 3 ) {
									// if a word matches 
									if (publixWord.equals(couponsComWord)) {
										wordMatchCount++;
									}
								}
							}
							if (wordMatchCount >= (publixCouponTitleWords.length/2)) {
								matchedCoupons.get(0).add(publixCoupon);
								matchedCoupons.get(2).add(couponsComCoupon);
							}
							wordMatchCount = 0;
						}
				}
			}
		}
		/**/
			
			for (int index = 0; index < matchedCoupons.get(0).size(); index++) {
				print("Publix Title", matchedCoupons.get(0).get(index).getTitle());
				print("Walmart Title", matchedCoupons.get(1).get(index).getTitle());
				print("CoupnsCom Title", matchedCoupons.get(2).get(index).getTitle());
				System.out.println("\n-----------------------------------");
			}
			
			// after the featured recipes list has been built print it to json
			FileWriter fw = new FileWriter("matchedCoupons.json");
			fw.write(gson.toJson(matchedCoupons));
			fw.close();
			/**/
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2 ) {
			e2.printStackTrace();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public static void print(String title, String desc) {
		// if the description is longer than 100 then trim it
		System.out.println(title + ": " + desc);
	}
	
	public static String cleanString(String unclean) {
		return unclean.toLowerCase()
				.replaceAll(" of ", "")			// remove the word of
				.replaceAll(" the ", "")		// remove the word the
				.replaceAll(" is ", "")			// remove the word is
				.replaceAll(" with ", "")		// remove the word with
				//.replaceAll("[^a-zA-Z0-9]+", "")
				/**/
				.replaceAll("\\(", "")			// remove all (
				.replaceAll("\\)", "")			// remove all )
				.replaceAll("1",  "")			// remove the number one
				.replaceAll("¨", "")			// remove the R trademark symbol
				.replaceAll("©", "");			// remove the C trademark symbol
				/**/
	}
}
