package com.pillartechnology.currency.usCurrency;

import java.math.BigDecimal;

import com.pillartechnology.currency.CoinInterface;

public class DimeEntity
	implements CoinInterface {
	
	private static final BigDecimal VALUE = new BigDecimal("0.1");
	private static final Double DIAMETER_IN_MILLIMETERS = new Double(17.91);
	private static final Double THICKNESS_IN_MILLIMETERS = new Double(1.35);
	private static final BigDecimal MASS_IN_GRAMS = new BigDecimal("2.268");
	
	public BigDecimal value() {
		return VALUE;
	};
	
	public Double thicknessInMillimeters() {
		return THICKNESS_IN_MILLIMETERS;
	};
	
	public Double diameterInMillimeters() {
		return DIAMETER_IN_MILLIMETERS;
	};
	
	public BigDecimal massInGrams() {
		return MASS_IN_GRAMS;
	};
}
