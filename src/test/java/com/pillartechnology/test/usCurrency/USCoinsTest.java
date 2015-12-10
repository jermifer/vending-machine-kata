package com.pillartechnology.test.usCurrency;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import com.pillartechnology.currency.CoinEntity;
import com.pillartechnology.currency.usCurrency.*;

import org.junit.Before;
import org.junit.Test;

public class USCoinsTest {
	
	private DimeEntity _dime;
	private GoldDollarEntity _gold_dollar;
	private HalfDollarEntity _half_dollar;

	@Before
	public void setUp() throws Exception {
		_dime = new DimeEntity();
		_gold_dollar = new GoldDollarEntity();
		_half_dollar = new HalfDollarEntity();
	}

	@Test
	public final void coin() {
		CoinEntity _coin = new CoinEntity(
			"Plastic", 
			(new BigDecimal("0.1")), 
			17.91, 
			1.35, 
			(new BigDecimal("3.55"))
		);
		System.out.println("\nCOIN");
		System.out.println(_coin.coinType);
		System.out.println(_coin.diameterInMillimeters);
		System.out.println(_coin.thicknessInMillimeters);
		System.out.println(_coin.massInGrams);
	}

}
