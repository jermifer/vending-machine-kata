package com.pillartechnology.vendingMachine.controller.productSelection;

public interface ProductSelectionHandler {
	public void refundDeposit();
	
	public void clearInputsWithFundsOnDeposit(Integer deposit);

	public void clearInputsWithoutFundsOnDeposit();

	public void declinePurchaseForNotEnoughFunds(Integer productPrice);

	public void declinePurchaseForTooMuchDeposited(Integer productPrice);

	public void completePurchase(Integer productPrice, String productName);

	public void returnChangeDueAfterPurchase();

	public void rejectSelection();

}
