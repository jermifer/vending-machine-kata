package com.pillartechnology.vendingMachine.model.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.pillartechnology.vendingMachine.controller.test.SelectItemInVendingMachineTest.*;

public class InMemoryInventoryTest {

	/**
	 * 
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

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void productFound() throws Exception {
		Catalog inventory = createInventoryWith("Snickers", Price.usCents(75));
		assertEquals(Price.usCents(75), inventory.findPrice("Snickers"));
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void productNotFound() throws Exception {
		Catalog inventory = createInventoryWithout("Snickers");
		assertEquals(null, inventory.findPrice("Snickers"));
	}

	/**
	 * 
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
