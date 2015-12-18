package com.pillartechnology.vendingMachine.view;

public interface VendingMachineDisplay {

	void messageMakeSelection();

	void promptProductDispensed(String productName);

	void messageAmountOnDeposit(Integer fundsOnDepositAmount);

	void messageInvalidSelection();

	void messageProductCost(Integer productCost);

	void messageExactChangeOnly();
}