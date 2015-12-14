package com.pillartechnology.vendingMachine.model;

/**********************************************************************************************
 * CATALOG INTERFACE
 * @author jennifer.mankin
 *
 */
public interface Catalog {
	
	Price findPrice(String selectedProductName);

}
