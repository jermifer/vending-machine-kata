package com.pillartechnology.vendingMachine.model.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.pillartechnology.vendingMachine.controller.test.SelectItemInVendingMachineTest.*;

public class InMemoryInventoryTest {

	public class InMemoryInventory implements Catalog {
		public InMemoryInventory(Map<String, Price> pricesByProduct) {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Price findPrice(String productName) {
			return Price.usCents(75);
		}
	}

	@Test
	public void productFound() throws Exception {
		Catalog inventory = createInventoryWith("Snickers", Price.usCents(75));
		assertEquals(Price.usCents(75), inventory.findPrice("Snickers"));
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
