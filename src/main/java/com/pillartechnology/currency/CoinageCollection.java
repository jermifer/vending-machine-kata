package com.pillartechnology.currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class CoinageCollection {
	private List<CoinEntity> _coinageCollection = new ArrayList<CoinEntity>();
	private Hashtable<String, Integer> _coinageCollectionByType = new Hashtable<String, Integer>();
	private Hashtable<String, Integer> _coinageCollectionBySpec = new Hashtable<String, Integer>();
	private Hashtable<BigDecimal, Integer> _coinageCollectionByValue = new Hashtable<BigDecimal, Integer>();
	private CoinEntity _coin;
	
	public void addCoin(CoinEntity coin) {
		if( !_coinageCollectionByType.containsKey(coin.coinType) ) {
			String _coin_spec = coin.diameterInMillimeters.toString();
				_coin_spec += "x" + coin.thicknessInMillimeters.toString();
				_coin_spec += "x" + coin.massInGrams.toString();
			
			_coinageCollection.add(coin);
			_coinageCollectionByType.put(coin.coinType, _coinageCollection.size()-1);
			_coinageCollectionBySpec.put(_coin_spec, _coinageCollection.size()-1);
			_coinageCollectionByValue.put(coin.coinValue, _coinageCollection.size()-1);
		}
	}
	
	public CoinEntity coinByType(String coinType) {
		if( _coinageCollectionByType.containsKey(coinType) ) {
			_coin = _coinageCollection.get(_coinageCollectionByType.get(coinType));
		}
		
		return _coin;
	}
}
