package com.pillartechnology.vendingMachine.controller.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.pillartechnology.vendingMachine.controller.productSelection.ProductSelectionController;
import com.pillartechnology.vendingMachine.controller.productSelection.ProductSelectionHandler;
import com.pillartechnology.vendingMachine.model.FundsService;
import com.pillartechnology.vendingMachine.model.VendingMachineInventory;
import com.pillartechnology.vendingMachine.model.VendingMachineInventoryItem;
import com.pillartechnology.vendingMachine.model.VendingMachineProductSelectionManager;
import com.pillartechnology.vendingMachine.view.VendingMachineDisplay;

import org.jmock.integration.junit4.JUnitRuleMockery;

import org.jmock.Expectations;

public class ProductSelectionControllerTest {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	
	public class ProductSelectionLibrary {
		
		public final VendingMachineProductSelectionManager selection;
		public final VendingMachineDisplay display;
		public final FundsService fundsService;
		public final VendingMachineInventory inventory;
		
		public ProductSelectionLibrary(
			VendingMachineProductSelectionManager selection, 
			VendingMachineDisplay display, 
			FundsService fundsService, 
			VendingMachineInventory inventory
		) {
			this.display = display;
			this.selection = selection;
			this.fundsService = fundsService;
			this.inventory = inventory;
		}
		
	}
	
	private VendingMachineProductSelectionManager selection;
	private VendingMachineDisplay display;
	private FundsService fundsService;
	private VendingMachineInventory inventory;
	private VendingMachineInventoryItem product;
	private ProductSelectionHandler handler;
	private ProductSelectionController controller;
	private ProductSelectionLibrary library;
	
	@Before
	public final void setup() {
		selection = context.mock(VendingMachineProductSelectionManager.class);
		display = context.mock(VendingMachineDisplay.class);
		fundsService = context.mock(FundsService.class);
		inventory = context.mock(VendingMachineInventory.class);
		product = context.mock(VendingMachineInventoryItem.class);
		library = new ProductSelectionLibrary(selection, display, fundsService, inventory);
		handler = new ProductSelectionHandler(library);
		controller = new ProductSelectionController(library, handler);
	}
	

	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductInStockPriceExceedsDeposit() {
		final Integer input = 111;
		final String productName = "M&Ms";
		final Integer productPrice = 100;
		
		context.checking(new Expectations() {
			{
				oneOf(selection).isCompleteProductCode(input); 
					will( returnValue(true) );
				
				oneOf(inventory).findProduct( with(input) );
					will(returnValue(product));
					
				oneOf(selection).isPurchasable( with(any(VendingMachineInventoryItem.class)) );
					will( returnValue(true) );
				
				oneOf(product).productPrice();
					will( returnValue(productPrice) );
					
				oneOf(fundsService).sumOfFundsOnDeposit();
					will( returnValue(productPrice-10) );
				
				never(fundsService).makeChange();
				never(fundsService).addFundsToRespository(productPrice);
				never(selection).clearInputs();
				
				oneOf(display).messageProductCost( with(productPrice) );
			}
		});
		
		controller.onInput(111);
	}

	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductInStockFundsMatchPrice() {
		final Integer input = 111;
		final String productName = "M&Ms";
		final Integer productPrice = 100;
		
		context.checking(new Expectations() {
			{
				oneOf(selection).isCompleteProductCode(input); 
					will( returnValue(true) );
				
				oneOf(inventory).findProduct( with(input) );
					will(returnValue(product));
					
				oneOf(selection).isPurchasable( with(any(VendingMachineInventoryItem.class)) );
					will( returnValue(true) );

				oneOf(product).productPrice();
					will( returnValue(productPrice) );
					
				oneOf(fundsService).sumOfFundsOnDeposit();
					will( returnValue(productPrice) );
				
				oneOf(product).productName();
					will( returnValue(productName) );
					
				never(fundsService).makeChange();
				
				oneOf(fundsService).addFundsToRespository( productPrice );
				oneOf(selection).clearInputs();
				oneOf(display).promptProductDispensed( with(productName) );
			}
		});
		
		controller.onInput(111);
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductInStockFundsExceedPriceAbleToMakeChange() {
		final Integer input = 111;
		final String productName = "M&Ms";
		final Integer productPrice = 100;
		
		context.checking(new Expectations() {
			{
				oneOf(selection).isCompleteProductCode(input); 
					will( returnValue(true) );
				
				oneOf(inventory).findProduct( with(input) );
					will(returnValue(product));
				
				oneOf(selection).isPurchasable( with(any(VendingMachineInventoryItem.class)) );
					will( returnValue(true) );
				
				oneOf(product).productPrice();
					will( returnValue(productPrice) );
				
				oneOf(fundsService).sumOfFundsOnDeposit();
					will( returnValue(productPrice+10) );
					
				oneOf(fundsService).isAbleToMakeChange();
					will( returnValue(true) );
					
				oneOf(product).productName();
					will( returnValue(productName) );
				
				oneOf(fundsService).makeChange();
				oneOf(fundsService).addFundsToRespository( productPrice );
				oneOf(selection).clearInputs();
				oneOf(display).promptProductDispensed( with(productName) );
				
				never(fundsService).refundFundsOnDeposit();
			}
		});
		
		controller.onInput(111);
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductInStockFundsExceedPriceExactChangeOnly() {
		final Integer input = 111;
		final String productName = "M&Ms";
		final Integer productPrice = 100;
		
		context.checking(new Expectations() {
			{
				oneOf(selection).isCompleteProductCode(input); 
					will( returnValue(true) );
				
				oneOf(inventory).findProduct( with(input) );
					will(returnValue(product));
				
				oneOf(selection).isPurchasable( with(any(VendingMachineInventoryItem.class)) );
					will( returnValue(true) );
				
				oneOf(product).productPrice();
					will( returnValue(productPrice) );
				
				oneOf(fundsService).sumOfFundsOnDeposit();
					will( returnValue(productPrice+10) );
					
				oneOf(fundsService).isAbleToMakeChange();
					will( returnValue(false) );
				
				never(fundsService).makeChange();
				never(fundsService).addFundsToRespository( productPrice );
				never(display).promptProductDispensed( with(productName) );
				
				oneOf(fundsService).refundFundsOnDeposit();
				oneOf(selection).clearInputs();
				oneOf(display).messageExactChangeOnly();
			}
		});
		
		controller.onInput(111);
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductCodeIsIncomplete() {
		context.checking(new Expectations() {
			{
				//make sure that this method is always called
				oneOf(selection).isCompleteProductCode( with(1) );
					will( returnValue(false) );
			}
		});
		
		controller.onInput(1);
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductCodeIsComplete() {
		final Integer input = 123;
		
		context.checking(new Expectations() {
			{
				oneOf(selection).isCompleteProductCode( with(input) );
					will( returnValue(true) );
					
				oneOf(inventory).findProduct( with(input) );
					
				oneOf(selection).isPurchasable( with(any(VendingMachineInventoryItem.class)) );
				
				allowing(display);
			}
		});
		
		controller.onInput(123);
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductFound() {
		final Integer input = 111;
		final String productName = "M&Ms";
		
		context.checking(new Expectations() {
			{
				oneOf(selection).isCompleteProductCode(input); 
					will( returnValue(true) );
				
				oneOf(inventory).findProduct( with(input) );
					will(returnValue(product));
					
				oneOf(selection).isPurchasable( with(any(VendingMachineInventoryItem.class)) );
				
				allowing(product).productName();
				allowing(product).productPrice();
				allowing(fundsService).makeChange();
				allowing(fundsService).addFundsToRespository( 100 );
				allowing(selection).clearInputs();
				allowing(display).promptProductDispensed( with(productName) );
				allowing(display).messageInvalidSelection();
			}
		});
		
		controller.onInput(111);
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductNotFound() {
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
		
		controller.onInput(input);
	}

	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testMakeSelectionProductOutOfStock() {
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
		
		controller.onInput(input);
	}

	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testRefund() {
		//same behavior regardless of whether or not there is money on deposit
		context.checking(new Expectations() {
			{
				oneOf(fundsService).refundFundsOnDeposit();
				oneOf(selection).clearInputs();
				oneOf(display).messageMakeSelection();
			}
		});
		
		controller.onRefund();
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testClear() {
		context.checking(new Expectations() {
			{
				oneOf(fundsService).sumOfFundsOnDeposit(); 
					
				oneOf(selection).clearInputs(); 
				
				oneOf(display); 
			}
		});
		
		controller.onClear();
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testClearWhenNoFundsOnDeposit() {
		context.checking(new Expectations() {
			{
				oneOf(fundsService).sumOfFundsOnDeposit(); will(returnValue(0));
				oneOf(selection).clearInputs(); 
				oneOf(display).messageMakeSelection();
			}
		});
		
		controller.onClear();
	}
	
	/**********************************************************************************************
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testClearWhenFundsOnDeposit() {
		final Integer depositAmount = 1;
		
		context.checking(new Expectations() {
			{
				oneOf(fundsService).sumOfFundsOnDeposit(); 
					will(returnValue(1));
					
				oneOf(selection).clearInputs(); 
				
				oneOf(display).messageAmountOnDeposit(depositAmount); 
			}
		});

		controller.onClear();
	}
	
}
