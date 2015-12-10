package com.pillartechnology.currency;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import com.pillartechnology.currency.*;

public class CoinFactory {	
	/**
	 * createDime
	 */
	public CoinEntity createDime() {
		CoinEntity _coin = new CoinEntity(
			"Dime",
			"USD",
			new BigDecimal("0.1"),
			17.91,
			1.35,
			new BigDecimal("2.268")
		);
		
		return _coin;
	}
	
	/**
	 * createPenny
	 */
	public CoinEntity createPenny() {
		CoinEntity _coin = new CoinEntity(
			"Penny",
			"USD",
			new BigDecimal("0.01"),
			19.05,
			1.55,
			new BigDecimal("2.5")
		);
		
		return _coin;
	}
}


