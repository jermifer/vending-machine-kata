package com.pillartechnology.vendingMachine.controller;

import java.util.HashMap;

import com.pillartechnology.vendingMachine.model.Display;
import com.pillartechnology.vendingMachine.model.InMemoryInventory;
import com.pillartechnology.vendingMachine.model.Price;
import com.pillartechnology.vendingMachine.model.SaleController;

public class VendingMachineFrontController {
	
	private static SaleController saleController;

	@SuppressWarnings("serial")
	public static void init() throws Exception {
		saleController = new SaleController(
				new InMemoryInventory(new HashMap<String, Price> () {
					{
						put("M&Ms", Price.usCents(100));
						put("Snickers", Price.usCents(60));
						put("Ruffles Potato Chips", Price.usCents(40));
					}
				}), 
				new Display() {
					@Override
					public void displayInventoryItemPrice(Price price) {
						System.out.println("$" + price.getUSDollar());
					};

					@Override
					public void displayProductNotFoundMessage(String selectedProductName) {
						System.out.println("Product not available.");
						
					};
				}
		);
	};
	
	public static void main(String[] args) throws Exception {
		init();
		saleController.onSelectItem("Starburst");
	};
	
}
