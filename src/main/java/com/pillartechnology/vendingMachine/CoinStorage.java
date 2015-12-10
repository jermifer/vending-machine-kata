package com.pillartechnology.vendingMachine;

import java.util.*;

import com.pillartechnology.currency.CoinInterface;

public class CoinStorage {
	private List<CoinInterface> _coinsOnDeposit = new ArrayList<CoinInterface>();
	
	public void addCoin(CoinInterface depositedCoin) {
		_coinsOnDeposit.add(depositedCoin);
	}
	
	public List<CoinInterface> coinsOnDeposit() {
		return _coinsOnDeposit;
	}
	
	public void removeCoinsByAmount(Double amount) {
		Iterator<CoinInterface> _coinIterator = _coinsOnDeposit.iterator();
		
		while( amount > 0 && _coinIterator.hasNext() ) {
			
		}
	}
}
