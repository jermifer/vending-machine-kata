package com.pillartechnology.vendingMachine.controller.productSelection;

import com.pillartechnology.vendingMachine.model.productInventory.ProductCode;
import com.pillartechnology.vendingMachine.model.productInventory.ProductInventoryItem;

public interface ProductSelectionManager {

	void isRefundCode(Integer input);

	void clearInputs();

	boolean isCompleteProductCode(Integer productCode);

	//product.quantityInStock() > 0 && product != null
	boolean isPurchasable(ProductInventoryItem product);

	ProductCode getProductCodeFrom(Integer input);

}

