package com.pillartechnology.vendingMachine.model;

/**********************************************************************************************
 * DISPLAY INTERFACE
 * @author jennifer.mankin
 *
 */
public interface Display {

	void displayInventoryItemPrice(Price price);

	void displayProductNotFoundMessage(String selectedProductName);
}