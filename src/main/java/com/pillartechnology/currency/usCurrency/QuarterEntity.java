package com.pillartechnology.currency.usCurrency;

import java.math.BigDecimal;

import com.pillartechnology.currency.CoinInterface;

public class QuarterEntity
	implements CoinInterface {
	
	private static final Double VALUE = new Double(0.25);
	private static final Double DIAMETER_IN_MILLIMETERS = new Double(24.26);
	private static final Double THICKNESS_IN_MILLIMETERS = new Double(1.75);
	private static final BigDecimal MASS_IN_GRAMS = new BigDecimal(5.67);
	
	public Double value() {
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
