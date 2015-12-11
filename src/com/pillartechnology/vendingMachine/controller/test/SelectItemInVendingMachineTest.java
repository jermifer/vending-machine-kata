package com.pillartechnology.vendingMachine.controller.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.Expectations;

public class SelectItemInVendingMachineTest {
	/**********************************************************************************************
	 * DISPLAY INTERFACE
	 * @author jennifer.mankin
	 *
	 */
	public interface Display {
	
		void displayInventoryItemPrice(Price price);

		void displayProductNotFoundMessage(String selectedProductName);
	}

	/**********************************************************************************************
	 * CATALOG INTERFACE
	 * @author jennifer.mankin
	 *
	 */
	public interface Catalog {
		
		Price findPrice(String selectedProductName);

	}
	
	/**********************************************************************************************
	 * PRICE CLASS
	 * @author jennifer.mankin
	 *
	 */
	public static class Price {
		
		private int usCents;

		public Price(int usCents) {
			this.usCents = usCents;
		}
		
		public static Price usCents(int usCents) {
			return new Price(usCents);
		}
		
		@Override
		public boolean equals(Object other) {
			if( other instanceof Price ) {
				Price that = (Price) other;
				return ( this.usCents == that.usCents );
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return usCents;
		}
		
		@Override
		public String toString() {
			return "$" + usCents / 100.0d;
		}
		
	}
	
	/**********************************************************************************************
	 * SALE CONTROLLER CLASS
	 * @author jennifer.mankin
	 *
	 */
	public static class SaleController {

		private final Display display;
		private Catalog inventory;

		public SaleController(Catalog inventory, Display display) {
			this.inventory = inventory;
			this.display = display;
		}

		public void onSelectItem(String selectedProductName) {
			Price price = inventory.findPrice(selectedProductName);
			if( price == null ) {
				this.display.displayProductNotFoundMessage(selectedProductName);
			} else {
				this.display.displayInventoryItemPrice(price);
			}
		}
		
	}
	
	/**********************************************************************************************
	 * TESTS
	 **********************************************************************************************/

	private Mockery mockery = new Mockery();

	/**********************************************************************************************
	 * TESTPRODUCTEXISTSININVENTORY
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testproductExistsInInventory() {
		final Catalog inventory = mockery.mock(Catalog.class);
		final Display display = mockery.mock(Display.class);
		
		mockery.checking(new Expectations() {
			{
				allowing(inventory).findPrice(with("M&Ms")); //"stub" implementation returns hard-coded data
				will(returnValue(Price.usCents(100)));
				
				oneOf(display).displayInventoryItemPrice(Price.usCents(100));
			}
		});
		
		SaleController saleController = new SaleController(inventory, display);
		saleController.onSelectItem("M&Ms");
	}
	
	/**********************************************************************************************
	 * TESTPRODUCTDOESNOTEXISTININVENTORY
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testproductDoesNotExistInInventory() throws Exception {
		final Catalog inventory = mockery.mock(Catalog.class);
		final Display display = mockery.mock(Display.class);
		
		mockery.checking(new Expectations() {
			{
				allowing(inventory).findPrice(with("M&Ms"));
				will(returnValue(null));
				
				oneOf(display).displayProductNotFoundMessage("M&Ms");
			}
		});
		
		SaleController saleController = new SaleController(inventory, display);
		saleController.onSelectItem("M&Ms");
	}
}
