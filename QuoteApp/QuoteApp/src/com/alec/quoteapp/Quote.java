package com.alec.quoteapp;

import java.util.ArrayList;

public class Quote {
	private String quote;
	private String author;
	private ArrayList<String> categories;
	
	public Quote() {
		this("","");
	}
	
	public Quote(String quote) {
		this.quote = quote;
		this.author = "unknown";
	}
	
	public Quote(String quote, String author) {
		super();
		this.quote = quote;
		this.author = author;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	
}
