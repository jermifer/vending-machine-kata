package com.pillartechnology.vendingMachine.controller.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.pillartechnology.vendingMachine.model.Catalog;
import com.pillartechnology.vendingMachine.model.Display;
import com.pillartechnology.vendingMachine.model.Price;
import com.pillartechnology.vendingMachine.model.SaleController;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.Expectations;

public class SelectItemInVendingMachineTest {
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
