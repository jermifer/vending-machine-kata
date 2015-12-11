package com.pillartechnology.vendingMachine.model;

/**********************************************************************************************
 * SALE CONTROLLER CLASS
 * @author jennifer.mankin
 *
 */
public class SaleController {

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