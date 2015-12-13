package com.pillartechnology.vendingMachine.controller;

import com.pillartechnology.vendingMachine.model.Catalog;
import com.pillartechnology.vendingMachine.model.VendingMachineDisplay;
import com.pillartechnology.vendingMachine.model.Price;

/**********************************************************************************************
 * PRODUCT CONTROLLER CLASS
 * @author jennifer.mankin
 *
 */
public class ProductSelectionController {

	private final VendingMachineDisplay display;
	private Catalog inventory;

	public ProductSelectionController(Catalog inventory, VendingMachineDisplay display) {
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
