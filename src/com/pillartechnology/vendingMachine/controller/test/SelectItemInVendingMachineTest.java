package com.pillartechnology.vendingMachine.controller.test;

import org.junit.Test;

import com.pillartechnology.vendingMachine.controller.ProductSelectionController;
import com.pillartechnology.vendingMachine.model.Catalog;
import com.pillartechnology.vendingMachine.model.VendingMachineDisplay;
import com.pillartechnology.vendingMachine.model.Price;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Expectations;

public class SelectItemInVendingMachineTest {
	private Mockery context = new JUnit4Mockery();

	/**********************************************************************************************
	 * TESTPRODUCTEXISTSININVENTORY
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testproductExistsInInventory() {
		final Catalog inventory = context.mock(Catalog.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		
		context.checking(new Expectations() {
			{
				allowing(inventory).findPrice(with("M&Ms"));
				will(returnValue(Price.usCents(100)));
				oneOf(display).displayInventoryItemPrice(Price.usCents(100));
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(inventory, display);
		productSelectionController.onSelectItem("M&Ms");
	}
	
	/**********************************************************************************************
	 * TESTPRODUCTDOESNOTEXISTININVENTORY
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testproductDoesNotExistInInventory() throws Exception {
		final Catalog inventory = context.mock(Catalog.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		
		context.checking(new Expectations() {
			{
				allowing(inventory).findPrice(with("M&Ms"));
				will(returnValue(null));
				
				oneOf(display).displayProductNotFoundMessage("M&Ms");
			}
		});
		
		ProductSelectionController saleController = new ProductSelectionController(inventory, display);
		saleController.onSelectItem("M&Ms");
	}
}
