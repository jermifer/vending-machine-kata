package com.pillartechnology.vendingMachine.controller.productSelection;

import com.pillartechnology.vendingMachine.controller.test.ProductSelectionControllerTest.ProductSelectionLibrary;
import com.pillartechnology.vendingMachine.model.FundsService;
import com.pillartechnology.vendingMachine.model.VendingMachineProductSelectionManager;
import com.pillartechnology.vendingMachine.view.VendingMachineDisplay;

public class ProductSelectionHandler {
	
	private final VendingMachineDisplay display;
	private final VendingMachineProductSelectionManager selection;
	private final FundsService fundsService;
	
	public ProductSelectionHandler( 
		ProductSelectionLibrary library
	) {
		this.display = library.display;
		this.selection = library.selection;
		this.fundsService = library.fundsService;
	}

	public void refundDeposit() {
		this.fundsService.refundFundsOnDeposit();
		this.selection.clearInputs();
		this.display.messageMakeSelection();
	}
	
	private void _clearInputs() {
		this.selection.clearInputs();
	}

	public void clearInputsWithFundsOnDeposit(Integer deposit) {
		this._clearInputs();
		this.display.messageAmountOnDeposit(deposit);
	}

	public void clearInputsWithoutFundsOnDeposit() {
		this._clearInputs();
		this.display.messageMakeSelection();
	}

	public void declinePurchaseForNotEnoughFunds(Integer productPrice) {
		this.display.messageProductCost(productPrice);
	}

	public void declinePurchaseForTooMuchDeposited(Integer productPrice) {
		this.fundsService.refundFundsOnDeposit();
		this.selection.clearInputs();
		this.display.messageExactChangeOnly();
	}

	public void completePurchase(Integer productPrice, String productName) {
		//move funds equaling the price of the product into the repository  
		this.fundsService.addFundsToRespository( productPrice );

		//clear out selection, so next one can proceed
		this._clearInputs();

		//output product dispensed to console
		this.display.promptProductDispensed( productName );
	}

	public void returnChangeDueAfterPurchase() {
		this.fundsService.makeChange();
	}

	public void rejectSelection() {
		this.display.messageInvalidSelection();
	}

}
