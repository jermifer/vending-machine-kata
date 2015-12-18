package com.pillartechnology.vendingMachine.model;

import com.pillartechnology.vendingMachine.model.VendingMachineInventory.VendingMachineInventoryItem;

public interface VendingMachineProductSelectionManager {

	void isRefundCode(Integer input);

	void clearInputs();

	boolean isCompleteProductCode(Integer input);

	//product.quantityInStock() > 0
	boolean isPurchasable(VendingMachineInventoryItem product);

}