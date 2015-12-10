package com.pillartechnology.currency;


import java.math.BigDecimal;

public interface CoinInterface {
	BigDecimal value();
	Double thicknessInMillimeters();
	Double diameterInMillimeters();
	BigDecimal massInGrams();
}
