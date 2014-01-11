package com.alec.mealDeal;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;

public class WalmartReadCouponList extends TestCase {

	static ArrayList<Integer> categoryIds = new ArrayList<Integer>();
	static ArrayList<Coupon> coupons = new ArrayList<Coupon>();
	
	public static void main(String[] args) {
		int couponIndex = -1;
			//	get publix coupons	/
		Validate.isTrue(true);
		// start at a category of coupons
		MyUrl url = new MyUrl("http://coupons.walmart.com");
		
		try {
			
			// read coupons from page
			Document doc = Jsoup.connect(url.toString())
										.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
										.referrer("http://www.google.com")
										.get();
			print("Connnected to", url.toString());
			//System.out.println(doc.html());
			for (Element h4 : doc.select("h4")) {
				if (h4.attr("class").contains("offer-product")) {
					coupons.add(new Coupon(h4.text()));
					couponIndex++;
					print("title", h4.text());
				}
			}
			couponIndex = 0;
			for (Element h3 : doc.select("h3")) {
				//print("h3", h3.text());
				if (h3.attr("class").contains("offer-value")) {
					coupons.get(couponIndex).setDiscount(h3.text());
					couponIndex++;
					print("value", h3.text());
				}
			}
			couponIndex = 0;
			for (Element p : doc.select("p")) {
				if (p.attr("class").contains("offer-details")) {
					coupons.get(couponIndex).setDesc(p.text());
					couponIndex++;
					print("details", p.text());
				}
			}
			
			Gson gson = new Gson();
			//GsonBuilder gsonBuilder = new GsonBuilder();
			
			FileWriter writer = new FileWriter("walmartCoupons.json");
			
			writer.write(gson.toJson(coupons));
			
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**/
	
	public static void print(String title, String desc) {
		// if the description is longer than 100 then trim it
		System.out.println(title + ": " + ((desc.length() > 100) ? desc.subSequence(0, 99) : desc));
	}
}
