package com.trevor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
	private int id;
	private String name;
	private Price currentPrice;
	
	public Product(int id, String name, Price currentPrice) {
		super();
		this.id = id;
		this.name = name;
		this.currentPrice = currentPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("current_price")
	public Price getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Price currentPrice) {
		this.currentPrice = currentPrice;
	}
}
