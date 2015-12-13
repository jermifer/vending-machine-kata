package com.pillartechnology.vendingMachine.controller;

import java.util.HashMap;
import java.util.Scanner;

import com.pillartechnology.vendingMachine.model.VendingMachineDisplay;
import com.pillartechnology.vendingMachine.model.InMemoryInventory;
import com.pillartechnology.vendingMachine.model.Price;

public class VendingMachineFrontController {
	
	private static ProductSelectionController productSelectionController;

	@SuppressWarnings("serial")
	public static void init() throws Exception {
		productSelectionController = new ProductSelectionController(
				new InMemoryInventory(new HashMap<String, Price> () {
					{
						put("M&Ms", Price.usCents(100));
						put("Snickers", Price.usCents(60));
						put("Ruffles Potato Chips", Price.usCents(40));
					}
				}), 
				new VendingMachineDisplay() {
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
		Scanner scan = new Scanner(System.in);
		String selectedProductName;
		
		System.out.println("Enter a product name and press enter");
		
		selectedProductName = scan.nextLine();
		System.out.println("You entered " + selectedProductName);
		
		
		init();
		productSelectionController.onSelectItem(selectedProductName);
		
		scan.close();
	};
	
}
