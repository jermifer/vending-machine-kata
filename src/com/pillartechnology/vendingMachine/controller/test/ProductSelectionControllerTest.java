package com.pillartechnology.vendingMachine.controller.test;

import org.junit.Rule;
import org.junit.Test;

import com.pillartechnology.vendingMachine.controller.test.PushButtonOnVendingMachineTest.FundsService;
import com.pillartechnology.vendingMachine.controller.test.PushButtonOnVendingMachineTest.ProductSelectionController;
import com.pillartechnology.vendingMachine.controller.test.PushButtonOnVendingMachineTest.VendingMachineDisplay;
import com.pillartechnology.vendingMachine.controller.test.PushButtonOnVendingMachineTest.VendingMachineInventory;
import com.pillartechnology.vendingMachine.controller.test.PushButtonOnVendingMachineTest.VendingMachineProductSelectionManager;

import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.integration.junit4.JUnitRuleMockery;

import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.jmock.Expectations;

public class ProductSelectionControllerTest {
	public interface VendingMachineInventory {

		void findProduct(int productCode);

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

		void messageAmountOnDeposit(String formattedFundsOnDepositAmount);

	}

	public interface VendingMachineProductSelectionManager {

		void isRefundCode(int with);

		void clearInputs();

	}
	
	public class VendingMachineInventoryItem {
		
	}
	
	public class ProductSelectionController {

		private final VendingMachineDisplay display;
		private final VendingMachineProductSelectionManager selection;
		private final FundsService fundsService;

		public ProductSelectionController(
			VendingMachineDisplay display, 
			VendingMachineProductSelectionManager selection, 
			FundsService fundsService
		) {
			this.display = display;
			this.selection = selection;
			this.fundsService = fundsService;
		}

		public void refundFundsOnDeposit() {
			this.fundsService.refundFundsOnDeposit();
			this.selection.clearInputs();
			this.display.messageMakeSelection();
		}
		
		public void clearInputs() {
			Integer deposit = this.fundsService.sumOfFundsOnDeposit(); 
			
			this.selection.clearInputs(); 
			this.display.messageAmountOnDeposit(deposit.toString());
			}
		}
	}

	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	
	/*
	 *	- ProductSelectionController::onInput(int codeInput) 
	 *		- VendingMachineProductSelectionManager::isRefundCode
	 *	  		- FundsService::returnFundsOnDeposit
	 *  	- VendingMachineProductSelectionManager::isClearInputsCode
	 *  		- VendingMachineProductSelectionManager::clearInputs
	 *  	- VendingMachineProductSelectionManager::isReadyToFindItem
	 *  		- True ? (item = Inventory::findItem) == null
	 *  			- True ? Display::messageInvalidSelection
	 *  			- False ? FundsService::canCompletePurchase
	 *  				- True ? 
	 *  					- FundsService::makeChange
	 *  					- FundsService::addFundsToRespository
	 *  					- Display::promptProductDispensed
	 *  				- False ? Display::messageItemAmount
	 *  		- False ? 
	 *  			- VendingMachineProductSelectionManager::addInput
	 *  			- Display::messageCurrentInputs
	 */

	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testRefund() {
		final VendingMachineProductSelectionManager selection = context.mock(VendingMachineProductSelectionManager.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		final FundsService fundsService = context.mock(FundsService.class);
		
		context.checking(new Expectations() {
			{
				oneOf(fundsService).refundFundsOnDeposit();
				oneOf(selection).clearInputs();
				oneOf(display).messageMakeSelection();
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService);
		productSelectionController.refundFundsOnDeposit();
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
				allowing(inventory).findProduct(with(111));
				will(returnValue(new VendingMachineInventoryItem()));
				
				oneOf(fundsService).makeChange();
				oneOf(fundsService).addFundsToRespository();
				oneOf(selection).clearInputs();
				oneOf(display).promptProductDispensed(with(inventory.productName()));
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService);
		productSelectionController.onInput(111);
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
		
		context.checking(new Expectations() {
			{
				oneOf(fundsService).sumOfFundsOnDeposit(); will(returnValue(0));
				oneOf(display).messageMakeSelection();
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService);
		productSelectionController.clearInputs();
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
		
		context.checking(new Expectations() {
			{
				final String depositAmountFormatted = "$1";
					
				oneOf(fundsService).sumOfFundsOnDeposit(); will(returnValue(1));
				allowing(selection).clearInputs(); 
				allowing(display).messageAmountOnDeposit(depositAmountFormatted); 
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService);
		productSelectionController.clearInputs();
	}
	
}
