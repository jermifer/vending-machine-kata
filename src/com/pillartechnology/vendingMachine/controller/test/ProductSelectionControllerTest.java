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

		void messageAmountOnDeposit(Integer fundsOnDepositAmount);

	}

	public interface VendingMachineProductSelectionManager {

		void isRefundCode(int with);

		void clearInputs();

	}
	
	public class VendingMachineInventoryItem {
		;
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
			;
		}
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
		
		context.checking(new Expectations() {
			{
				oneOf(fundsService).refundFundsOnDeposit();
				oneOf(selection).clearInputs();
				oneOf(display).messageMakeSelection();
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService);
		productSelectionController.onRefund();
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
				allowing(selection).clearInputs(); 
				oneOf(display).messageMakeSelection();
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService);
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
		
		context.checking(new Expectations() {
			{
				final Integer depositAmount = 1;
					
				oneOf(fundsService).sumOfFundsOnDeposit(); will(returnValue(1));
				allowing(selection).clearInputs(); 
				allowing(display).messageAmountOnDeposit(depositAmount); 
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(display, selection, fundsService);
		productSelectionController.onClear();
	}
	
}
