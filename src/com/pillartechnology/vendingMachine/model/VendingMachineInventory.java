package com.pillartechnology.vendingMachine.model;

public interface VendingMachineInventory {

	VendingMachineInventoryItem findProduct(Integer productCode);

}