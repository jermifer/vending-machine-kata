package com.pillartechnology.currency;

import java.math.BigDecimal;

public class CoinFactory {
	public CoinEntity createDime() {
		CoinEntity _coin = new CoinEntity(
			"Dime",
			new BigDecimal("0.1"),
			17.91,
			1.35,
			new BigDecimal("2.268")
		);
		
		return _coin;
	}
}


