package com.alec.solenium;

public class Item {
	private String title;
	private String regPrice;
	private String newPrice;
	
	public Item() {
		this("", "", "");
	}
	
	public Item(String title) {
		this(title, "","");
	}
	
	public Item(String title, String regPrice, String newPrice) {
		super();
		this.title = title;
		this.regPrice = regPrice;
		this.newPrice = newPrice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRegPrice() {
		return regPrice;
	}

	public void setRegPrice(String regPrice) {
		this.regPrice = regPrice;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}
	
	
	
	
}
