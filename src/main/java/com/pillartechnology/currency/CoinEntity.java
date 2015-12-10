package com.pillartechnology.currency;

import java.math.BigDecimal;

public class CoinEntity {
	
	public final String coinType;
	public final String currencyCode;
	public final BigDecimal coinValue;
	public final Double diameterInMillimeters;
	public final Double thicknessInMillimeters;
	public final BigDecimal massInGrams;	
	
	public CoinEntity(
		String coinType,
		String iso4217CurrencyCode, 
		BigDecimal coinValue, 
		Double diameterInMillimeters, 
		Double thicknessInMillimeters, 
		BigDecimal massInGrams
	) {
		this.coinValue = coinValue;
		this.currencyCode = iso4217CurrencyCode;
		this.coinType = coinType;
		this.diameterInMillimeters = diameterInMillimeters;
		this.thicknessInMillimeters = thicknessInMillimeters;
		this.massInGrams = massInGrams;
	}
}
