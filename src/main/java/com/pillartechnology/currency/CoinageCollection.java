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
	
	/**
	 * addCoin defines a unique coin that is a valid member of this coinage
	 * @param coin
	 */
	public void addCoin(CoinEntity coin) {
		String _coin_spec = _coinSpec(coin);
		
		/* coins in the collection must be unique types */
		if( _coinageCollectionByType.containsKey(coin.coinType)	) {
			throw new IllegalStateException(coin.coinType + " already exists in the collection.");
		}
		
		/* coins in the collection must have unique specs */
		if( _coinageCollectionBySpec.containsKey(_coin_spec) ) {
			throw new IllegalStateException(coin.coinType + " already exists in the collection.");
		}

		/* add the coin to the collection */
		_coinageCollection.add(coin);
		
		/* index the position of the coin in the collection by type, spec, and value */
		int _coinage_collection_index = _coinageCollection.size()-1;
		_coinageCollectionByType.put(coin.coinType, _coinage_collection_index);
		_coinageCollectionBySpec.put(_coin_spec, _coinage_collection_index);
		_coinageCollectionByValue.put(coin.coinValue, _coinage_collection_index);
	}
	
	/**
	 * coinByType valid coinage of the type in question
	 * @param coinType
	 * @return CoinEntity or null
	 */
	public CoinEntity coinByType(String coinType) {
		if( _coinageCollectionByType.containsKey(coinType) ) {
			_coin = _coinageCollection.get(_coinageCollectionByType.get(coinType));
		}
		
		return _coin;
	}
	
	/**
	 * coinByValue finds all valid coinage with the specified value
	 * @param coinType
	 * @return an array of CoinEntities or null
	 */
	/*public CoinEntity[] coinByValue(String coinValue) {
		BigDecimal _coin_value = new BigDecimal(coinValue);
		
		if( _coinageCollectionByValue.containsKey(_coin_value) ) {
			_coin = _coinageCollection.get(_coinageCollectionByValue.get(_coin_value));
		}
		
		return _coin;
	}*/
	
	/* *********************************************************************************************
	 * PRIVATE METHODS
	 */
	private String _coinSpec(CoinEntity coin) {
		String _coin_spec = coin.diameterInMillimeters.toString();
			_coin_spec += "x" + coin.thicknessInMillimeters.toString();
			_coin_spec += "x" + coin.massInGrams.toString();
			
		return _coin_spec;
	}
}
