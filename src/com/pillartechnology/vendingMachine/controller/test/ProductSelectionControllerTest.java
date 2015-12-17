package com.pillartechnology.vendingMachine.controller.test;

import org.junit.Rule;
import org.junit.Test;

import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.integration.junit4.JUnitRuleMockery;

import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.jmock.Expectations;

public class ProductSelectionControllerTest {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	
	public interface VendingMachineInventory {

		VendingMachineInventoryItem findProduct(int productCode);

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

		void isRefundCode(int with);

		void clearInputs();

	}
	
	public class VendingMachineInventoryItem {

		public String productName() {
			return "M&Ms";
		}

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

		public void onInput(int i) {
			VendingMachineInventoryItem product = this.inventory.findProduct(i);
			
			if( product == null ) {
				this.display.messageInvalidSelection();
				
			} else {
				this.fundsService.makeChange();
				this.fundsService.addFundsToRespository();
				this.selection.clearInputs();
				this.display.promptProductDispensed(product.productName());
			}
		}
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
		
		context.checking(new Expectations() {
			{
				oneOf(inventory).findProduct(with(111));
				will(returnValue(new VendingMachineInventoryItem()));
				
				allowing(fundsService).makeChange();
				allowing(fundsService).addFundsToRespository();
				allowing(selection).clearInputs();
				allowing(display).promptProductDispensed(with("M&Ms"));
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
		
		context.checking(new Expectations() {
			{
				oneOf(inventory).findProduct(with(300));
				will(returnValue(null));
				
				oneOf(display).messageInvalidSelection();
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService, inventory);
		productSelectionController.onInput(300);
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
