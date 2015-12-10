package com.pillartechnology.currency.usCurrency;

import java.math.BigDecimal;

import com.pillartechnology.currency.CoinInterface;

public class HalfDollarEntity
	implements CoinInterface {
	
	private static final BigDecimal VALUE = new BigDecimal("0.5");
	private static final Double DIAMETER_IN_MILLIMETERS = new Double(30.61);
	private static final Double THICKNESS_IN_MILLIMETERS = new Double(2.15);
	private static final BigDecimal MASS_IN_GRAMS = new BigDecimal("11.34");
	
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
