package com.pillartechnology.vendingMachine.controller.test;

import org.junit.Rule;
import org.junit.Test;

import org.jmock.integration.junit4.JUnitRuleMockery;

import org.jmock.Expectations;

public class ProductSelectionControllerTest {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	
	public interface VendingMachineInventory {

		VendingMachineInventoryItem findProduct(Integer productCode);

		String productName();

	}

	public interface FundsService {

		void refundFundsOnDeposit();

		void makeChange();

		void addFundsToRespository();

		Integer sumOfFundsOnDeposit();
		
		String toString(int amount);
	}

	public interface VendingMachineDisplay {

		void messageMakeSelection();

		void promptProductDispensed(String productName);

		void messageAmountOnDeposit(Integer fundsOnDepositAmount);

		void messageInvalidSelection();

	}

	public interface VendingMachineProductSelectionManager {

		void isRefundCode(Integer input);

		void clearInputs();

		boolean isCompleteProductCode(Integer input);

		//product.quantityInStock() > 0
		boolean isPurchasable(VendingMachineInventoryItem product);

	}
	
	public interface VendingMachineInventoryItem {

		public String productName();

		public Integer quantityInStock();

	}
	
	public class ProductSelectionController {

		private final VendingMachineDisplay display;
		private final VendingMachineProductSelectionManager selection;
		private final FundsService fundsService;
		private final VendingMachineInventory inventory;

		public ProductSelectionController(
			VendingMachineDisplay display, 
			VendingMachineProductSelectionManager selection, 
			FundsService fundsService,
			VendingMachineInventory inventory
		) {
			this.display = display;
			this.selection = selection;
			this.fundsService = fundsService;
			this.inventory = inventory;
		}

		public void onRefund() {
			this.fundsService.refundFundsOnDeposit();
			this.selection.clearInputs();
			this.display.messageMakeSelection();
		}
		
		public void onClear() {
			Integer deposit = this.fundsService.sumOfFundsOnDeposit(); 

			this.selection.clearInputs(); 
			
			if( deposit > 0 ) {
				this.display.messageAmountOnDeposit(deposit);
			} else {
				this.display.messageMakeSelection();
			}
		}

		public void onInput(Integer input) {
			//the input comprises a complete product code
			if( this.selection.isCompleteProductCode(input) ) {
				//try to find the product in the inventory
				VendingMachineInventoryItem product = this.inventory.findProduct(input);
				
				//product was found and is in stock
				if( this.selection.isPurchasable( product ) ) {
					this.fundsService.makeChange();
					this.fundsService.addFundsToRespository();
					this.selection.clearInputs();
					this.display.promptProductDispensed(product.productName());
					
				//product was not found
				} else {
					this.display.messageInvalidSelection();
				}
			}
		}
	}
	
	/***
	 * TESTS BEGIN HERE
	 */
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelection() {
		final VendingMachineProductSelectionManager selection = context.mock(VendingMachineProductSelectionManager.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		final FundsService fundsService = context.mock(FundsService.class);
		final VendingMachineInventory inventory = context.mock(VendingMachineInventory.class);
		
		context.checking(new Expectations() {
			{
				//make sure that this method is always called
				oneOf(selection).isCompleteProductCode( with(any(Integer.class)) );
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService, inventory);
		productSelectionController.onInput(1);
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductFound() {
		final VendingMachineProductSelectionManager selection = context.mock(VendingMachineProductSelectionManager.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		final FundsService fundsService = context.mock(FundsService.class);
		final VendingMachineInventory inventory = context.mock(VendingMachineInventory.class);
		final VendingMachineInventoryItem product = context.mock(VendingMachineInventoryItem.class);
		
		final Integer input = 111;
		final String productName = "M&Ms";
		
		context.checking(new Expectations() {
			{
				oneOf(selection).isCompleteProductCode(input); 
					will( returnValue(true) );
				
				oneOf(inventory).findProduct( with(input) );
					will(returnValue(product));
					
				oneOf(selection).isPurchasable( with(any(VendingMachineInventoryItem.class)) );
					will( returnValue(true) );
				
				oneOf(product).productName();
					will( returnValue(productName) );
				
				oneOf(fundsService).makeChange();
				oneOf(fundsService).addFundsToRespository();
				oneOf(selection).clearInputs();
				oneOf(display).promptProductDispensed(with(productName));
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService, inventory);
		productSelectionController.onInput(111);
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductNotFound() {
		final VendingMachineProductSelectionManager selection = context.mock(VendingMachineProductSelectionManager.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		final FundsService fundsService = context.mock(FundsService.class);
		final VendingMachineInventory inventory = context.mock(VendingMachineInventory.class);
		
		final Integer input = 300;
		
		context.checking(new Expectations() {
			{
				oneOf(selection).isCompleteProductCode(input); 
					will( returnValue(true) );
				
				oneOf(inventory).findProduct( with(input) );
					will( returnValue(null) );
					
				oneOf(selection).isPurchasable( with(aNull(VendingMachineInventoryItem.class)) );
					//b/c product wasn't found
					will( returnValue(false) );
				
				oneOf(display).messageInvalidSelection();
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService, inventory);
		productSelectionController.onInput(input);
	}

	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductOutOfStock() {
		final VendingMachineProductSelectionManager selection = context.mock(VendingMachineProductSelectionManager.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		final FundsService fundsService = context.mock(FundsService.class);
		final VendingMachineInventory inventory = context.mock(VendingMachineInventory.class);
		final VendingMachineInventoryItem product = context.mock(VendingMachineInventoryItem.class);
		
		final Integer input = 300;
		
		context.checking(new Expectations() {
			{
				oneOf(selection).isCompleteProductCode(input); 
					will( returnValue(true) );
				
				oneOf(inventory).findProduct( with(input) );
					will(returnValue(product));
					
				oneOf(selection).isPurchasable( with(any(VendingMachineInventoryItem.class)) );
					//b/c quantity of product in inventory is 0
					will( returnValue(false) );
				
				oneOf(display).messageInvalidSelection();
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService, inventory);
		productSelectionController.onInput(input);
	}

	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testRefund() {
		final VendingMachineProductSelectionManager selection = context.mock(VendingMachineProductSelectionManager.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		final FundsService fundsService = context.mock(FundsService.class);
		final VendingMachineInventory inventory = context.mock(VendingMachineInventory.class);
		
		//same behavior regardless of whether or not there is money on deposit
		context.checking(new Expectations() {
			{
				oneOf(fundsService).refundFundsOnDeposit();
				oneOf(selection).clearInputs();
				oneOf(display).messageMakeSelection();
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService, inventory);
		productSelectionController.onRefund();
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testClearWhenNoFundsOnDeposit() {
		final VendingMachineProductSelectionManager selection = context.mock(VendingMachineProductSelectionManager.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		final FundsService fundsService = context.mock(FundsService.class);
		final VendingMachineInventory inventory = context.mock(VendingMachineInventory.class);
		
		context.checking(new Expectations() {
			{
				oneOf(fundsService).sumOfFundsOnDeposit(); will(returnValue(0));
				allowing(selection).clearInputs(); 
				oneOf(display).messageMakeSelection();
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService, inventory);
		productSelectionController.onClear();
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testClearWhenFundsOnDeposit() {
		final VendingMachineProductSelectionManager selection = context.mock(VendingMachineProductSelectionManager.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		final FundsService fundsService = context.mock(FundsService.class);
		final VendingMachineInventory inventory = context.mock(VendingMachineInventory.class);
		
		context.checking(new Expectations() {
			{
				final Integer depositAmount = 1;
					
				oneOf(fundsService).sumOfFundsOnDeposit(); will(returnValue(1));
				allowing(selection).clearInputs(); 
				oneOf(display).messageAmountOnDeposit(depositAmount); 
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService, inventory);
		productSelectionController.onClear();
	}
	
}
