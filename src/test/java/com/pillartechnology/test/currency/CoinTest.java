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
		CoinageCollection _coinage = new CoinageCollection();
		
		CoinEntity _coin = _coinFactory.createDime();
		_coinage.addCoin(_coin);
		
		System.out.println("\n");
		System.out.println(_coin.coinType);
		System.out.println(_coin.currencyCode);
		System.out.println(_coin.diameterInMillimeters);
		System.out.println(_coin.thicknessInMillimeters);
		System.out.println(_coin.massInGrams);
		
		_coin = _coinFactory.createPenny();
		_coinage.addCoin(_coin);
		
		System.out.println("\n");
		System.out.println(_coin.coinType);
		System.out.println(_coin.currencyCode);
		System.out.println(_coin.diameterInMillimeters);
		System.out.println(_coin.thicknessInMillimeters);
		System.out.println(_coin.massInGrams);
		
		CoinEntity _penny = _coinage.coinByType("Nickel");
		
		System.out.println("\n");
		if( _penny == null ) {
			System.out.println("Nickel is not valid coinage.");
		} else {
			System.out.println("Nickel successfully retrieved.");
		}
		
		/*CoinEntity _10cents = _coinage.coinByValue("0.1");
		
		System.out.println("\n");
		if( _10cents == null ) {
			System.out.println("No valid coinage worth 10 cents was found.");
		} else {
			System.out.println(_10cents.coinType +  " successfully retrieved.");
		}*/
	}
}
