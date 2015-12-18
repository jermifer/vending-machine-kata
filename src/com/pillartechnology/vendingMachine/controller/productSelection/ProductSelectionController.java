package com.pillartechnology.vendingMachine.controller.productSelection;

import com.pillartechnology.vendingMachine.model.FundsService;
import com.pillartechnology.vendingMachine.model.VendingMachineProductSelectionManager;
import com.pillartechnology.vendingMachine.model.VendingMachineInventory.VendingMachineInventory;
import com.pillartechnology.vendingMachine.model.VendingMachineInventory.VendingMachineInventoryItem;
import com.pillartechnology.vendingMachine.view.VendingMachineDisplay;

public class ProductSelectionController {

	private final VendingMachineDisplay display;
	private final VendingMachineProductSelectionManager selection;
	private final FundsService fundsService;
	private final VendingMachineInventory inventory;
	private final ProductSelectionHandler handler;

	public ProductSelectionController(
		VendingMachineDisplay display, 
		VendingMachineProductSelectionManager selection, 
		FundsService fundsService,
		VendingMachineInventory inventory,
		ProductSelectionHandler handler
	) {
		this.display = display;
		this.selection = selection;
		this.fundsService = fundsService;
		this.inventory = inventory;
		this.handler = handler;
	}

	public void onRefund() {
		this.handler.refundDeposit();
	}
	
	public void onClear() {
		Integer deposit = this.fundsService.sumOfFundsOnDeposit(); 

		if( deposit > 0 ) {
			this.handler.clearInputsWithFundsOnDeposit(deposit);

		} else {
			this.handler.clearInputsWithoutFundsOnDeposit();
		}
	}

	public void onInput(Integer input) {
		
		//the input comprises a complete product code
		if( this.selection.isCompleteProductCode(input) ) {
			
			//try to find the product in the inventory
			VendingMachineInventoryItem product = this.inventory.findProduct(input);
			
			//product was found and is in stock
			if( this.selection.isPurchasable( product ) ) {
				//get amount already deposited
				Integer deposit = this.fundsService.sumOfFundsOnDeposit();
				
				//get cost of product
				Integer productPrice = product.productPrice();
				
				//not enough funds to buy product
				if( deposit < productPrice ) {
					this.handler.declinePurchaseForNotEnoughFunds(productPrice);
					
				//deposit exceeds cost of product and machine is not able to make change
				} else if( deposit > productPrice && !fundsService.isAbleToMakeChange() ) {
					this.handler.declinePurchaseForTooMuchDeposited(productPrice);
						
				//purchase can be completed
				} else {
					String productName = product.productName();
					
					this.handler.completePurchase( productPrice, productName );
					
					//amount of funds on deposit exceeds cost of product
					if( deposit > productPrice ) {
						this.handler.returnChangeDueAfterPurchase();
					}
				}
				
			//product was not found or is not in stock
			} else {
				this.display.messageInvalidSelection();
			}
		}
	}
}