package com.pillartechnology.vendingMachine.model.productInventory;

public interface ProductInventoryItem {
	
	public ProductCode productCode();

	public String productName();

	public Integer quantityInStock();

	public Integer productPrice();

}
