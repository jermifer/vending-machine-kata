package com.pillartechnology.vendingMachine.controller.productSelection;

import com.pillartechnology.vendingMachine.model.vendingMachineInventory.VendingMachineInventoryItem;

public interface ProductSelectionManager {

	void isRefundCode(Integer input);

	void clearInputs();

	boolean isCompleteProductCode(Integer input);

	//product.quantityInStock() > 0 && product != null
	boolean isPurchasable(VendingMachineInventoryItem product);

}

