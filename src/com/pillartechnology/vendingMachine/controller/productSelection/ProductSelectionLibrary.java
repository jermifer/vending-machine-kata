package com.pillartechnology.vendingMachine.controller.productSelection;

import com.pillartechnology.vendingMachine.model.funds.FundsService;
import com.pillartechnology.vendingMachine.model.vendingMachineInventory.VendingMachineInventoryManager;
import com.pillartechnology.vendingMachine.view.VendingMachineDisplay;

public interface ProductSelectionLibrary {

	VendingMachineDisplay vendingMachineDisplay();
	ProductSelectionManager productSelectionManager();
	FundsService fundsService();
	VendingMachineInventoryManager inventory();
	
}
