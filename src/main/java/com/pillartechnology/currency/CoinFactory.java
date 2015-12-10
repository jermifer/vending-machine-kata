package com.pillartechnology.currency;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import com.pillartechnology.currency.*;

public class CoinFactory {	
	public CoinEntity createDime() {
		CoinEntity _coin = new CoinEntity(
			"Dime",
			"US",
			new BigDecimal("0.1"),
			17.91,
			1.35,
			new BigDecimal("2.268")
		);
		
		return _coin;
	}
	
	public CoinEntity createPenny() {
		CoinEntity _coin = new CoinEntity(
				"Dime",
				"US",
				new BigDecimal("0.1"),
				17.91,
				1.35,
				new BigDecimal("2.268")
				);
		
		return _coin;
	}
}


