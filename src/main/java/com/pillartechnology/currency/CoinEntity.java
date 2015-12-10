package com.pillartechnology.currency;

import java.math.BigDecimal;

public class CoinEntity {
	
	public final String coinType;
	public final String country;
	public final BigDecimal coinValue;
	public final Double diameterInMillimeters;
	public final Double thicknessInMillimeters;
	public final BigDecimal massInGrams;
	
	public CoinEntity(
		String coinType,
		String country, 
		BigDecimal coinValue, 
		Double diameterInMillimeters, 
		Double thicknessInMillimeters, 
		BigDecimal massInGrams
	) {
		this.coinValue = coinValue;
		this.country = country;
		this.coinType = coinType;
		this.diameterInMillimeters = diameterInMillimeters;
		this.thicknessInMillimeters = thicknessInMillimeters;
		this.massInGrams = massInGrams;
	}
}
