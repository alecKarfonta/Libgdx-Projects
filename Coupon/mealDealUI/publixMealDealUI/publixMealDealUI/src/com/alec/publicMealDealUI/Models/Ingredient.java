package com.alec.publicMealDealUI.Models;

public class Ingredient {
	private String item;
	private String category;
	private String department;
	private String quantity;
	private Coupon coupon;
	
	public Ingredient() {
		
	}
	
	public Ingredient(String item) {
		this.item = item;
		this.coupon = null;
	}
	public Ingredient(String item, String category, String department,
			String quantity) {
		this.item = item;
		this.category = category;
		this.department = department;
		this.quantity = quantity;
		this.coupon = null;
	}
	
	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	
}
