package com.pillartechnology.test.currency;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import com.pillartechnology.currency.*;

import org.junit.Before;
import org.junit.Test;

public class CoinTest {
	
	@Test
	public final void coin() {
		CoinFactory _coinFactory = new CoinFactory();
		
		CoinEntity _coin = _coinFactory.createDime();
		System.out.println("\nCOIN");
		System.out.println(_coin.coinType);
		System.out.println(_coin.diameterInMillimeters);
		System.out.println(_coin.thicknessInMillimeters);
		System.out.println(_coin.massInGrams);
	}

}
