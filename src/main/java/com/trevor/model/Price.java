package com.trevor.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Price implements JsonObject{
	private BigDecimal value;
	private String currencyCode;
	
	public Price(){
		
	}
	
	public Price(BigDecimal value, String currencyCode){
		this.value = value;
		this.currencyCode = currencyCode;
	}
	
	public Price(double value, String currencyCode){
		this(new BigDecimal(value), currencyCode);
	}

	public BigDecimal getValue() {
		return value.setScale(2, RoundingMode.HALF_EVEN);
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	@JsonProperty("currency_code")
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
}
