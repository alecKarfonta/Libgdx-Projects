package com.alec.mealDeal;

public class Coupon {
	private String title;
	private String desc;
	private String price;
	private String discount;
	private String expDate;
	
	public Coupon() {
		this("","","","","");
	}
	
	public Coupon(String title) {
		this(title,"","","","");
	}
	
	public Coupon(String title, String desc, String discount) {
		this(title,desc,discount,"","");
	}

	public Coupon(String title, String desc, String price, String discount, String expDate) {
		this.title = title;
		this.desc = desc;
		this.price = price; 
		this.discount = discount;
		this.expDate = expDate;
	}

	public String getTitle() {
		return title;
	}

	public String getDesc() {
		return desc;
	}

	public String getPrice() {
		return price;
	}

	public String getDiscount() {
		return discount;
	}

	public String getExpDate() {
		return expDate;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String toString() {
		return "Title: " + title 
				+ "\nDescription: " + ((desc.length() > 100) ? desc.substring(0, 99) : desc) 
				+ "\nPrice: " + price 
				+ "\nDiscount: " + discount 
				+ "\nExpires: " + expDate;
	}
}
