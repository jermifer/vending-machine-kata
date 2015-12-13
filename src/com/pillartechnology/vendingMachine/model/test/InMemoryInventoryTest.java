package com.pillartechnology.vendingMachine.model.test;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import com.pillartechnology.vendingMachine.model.Catalog;
import com.pillartechnology.vendingMachine.model.InMemoryInventory;
import com.pillartechnology.vendingMachine.model.Price;

public class InMemoryInventoryTest {

	/**********************************************************************************************
	 * TEST METHODS
	 ********************************************************************************************/
	
	/**
	 * PRODUCTFOUND
	 * @throws Exception
	 */
	@Test
	public void productFound() throws Exception {
		Catalog inventory = createInventoryWith("Snickers", Price.usCents(75));
		assertEquals(Price.usCents(75), inventory.findPrice("Snickers"));
	}
	
	/**
	 * PRODUCTNOTFOUND
	 * @throws Exception
	 */
	@Test
	public void productNotFound() throws Exception {
		Catalog inventory = createInventoryWithout("Snickers");
		assertEquals(null, inventory.findPrice("Snickers"));
	}

	/**********************************************************************************************
	 * FACTORY METHODS
	 ********************************************************************************************/
	
	/**
	 * CREATEINVENTORYWITHOUT
	 * @param productNameToAvoid
	 * @return
	 */
	private Catalog createInventoryWithout(String productNameToAvoid) {
		return new InMemoryInventory(Collections.<String, Price> singletonMap("not" + productNameToAvoid, Price.usCents(50)));
	}

	/**
	 * CREATEINVENTORYWITH
	 * @param productName
	 * @param price
	 * @return
	 */
	private Catalog createInventoryWith(String productName, Price price) {
		return new InMemoryInventory(Collections.<String, Price> singletonMap(productName, price));
		
	}

}
