package com.pillartechnology.tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.Expectations;

public class SelectItemInVendingMachineTest {
	public interface Display {
	
		void displayInventoryItemPrice(Price price);

		void displayProductNotFoundMessage(String selectedItemName);
	}

	public interface Inventory {
		
		Price findPrice(String selectedItemName);

	} 
	
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
			return "a price";
		}
		
	}
	
	public static class SaleController {

		private final Display display;
		private Inventory inventory;

		public SaleController(Inventory inventory, Display display) {
			this.inventory = inventory;
			this.display = display;
		}

		public void onSelectItem(String selectedItemName) {
			Price price = inventory.findPrice(selectedItemName);
			if( price == null ) {
				this.display.displayProductNotFoundMessage(selectedItemName);
			} else {
				this.display.displayInventoryItemPrice(price);
			}
		}
		
	}

	private Mockery mockery = new Mockery();

	@Test
	public final void testproductExistsInInventory() {
		final Inventory inventory = mockery.mock(Inventory.class);
		final Display display = mockery.mock(Display.class);
		
		mockery.checking(new Expectations() {
			{
				allowing(inventory).findPrice(with("M&Ms"));
				will(returnValue(Price.usCents(100)));
				
				oneOf(display).displayInventoryItemPrice(Price.usCents(100));
			}
		});
		
		SaleController saleController = new SaleController(inventory, display);
		saleController.onSelectItem("M&Ms");
	}
	
	@Test
	public final void testproductDoesNotExistInInventory() throws Exception {
		final Inventory inventory = mockery.mock(Inventory.class);
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
