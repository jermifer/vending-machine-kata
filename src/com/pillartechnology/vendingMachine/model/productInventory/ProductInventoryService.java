package com.pillartechnology.vendingMachine.model.productInventory;

public interface ProductInventoryService {

	ProductInventoryCollection<ProductCode, ProductInventoryItem> getCatalogByProductCode(ProductCode productCode);

}
