package com.pillartechnology.vendingMachine.model;

import java.util.Map;

/**
 * INMEMORYINVENTORY
 * @author jennifer.mankin
 *
 */
public class InMemoryInventory implements Catalog {
	private Map<String, Price> pricesByProduct;

	public InMemoryInventory(Map<String, Price> pricesByProduct) {
		this.pricesByProduct = pricesByProduct;
	}
	
	@Override
	public Price findPrice(String productName) {
		return this.pricesByProduct.get(productName);
	}
}