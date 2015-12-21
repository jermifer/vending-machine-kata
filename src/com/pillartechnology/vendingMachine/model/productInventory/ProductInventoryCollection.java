package com.pillartechnology.vendingMachine.model.productInventory;

public interface ProductInventoryCollection<ProductCode, ProductInventoryItem> {

	void put(ProductCode productCode, ProductInventoryItem product);

	ProductInventoryItem get(ProductCode productCode);

}