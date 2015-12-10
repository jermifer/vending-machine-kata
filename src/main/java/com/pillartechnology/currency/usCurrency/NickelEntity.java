package com.pillartechnology.currency.usCurrency;

import java.math.BigDecimal;

import com.pillartechnology.currency.CoinInterface;

public class NickelEntity
	implements CoinInterface {
	
	private static final Double VALUE = new Double(0.05);
	private static final Double DIAMETER_IN_MILLIMETERS = new Double(21.21);
	private static final Double THICKNESS_IN_MILLIMETERS = new Double(1.95);
	private static final BigDecimal MASS_IN_GRAMS = new BigDecimal("5");
	
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
