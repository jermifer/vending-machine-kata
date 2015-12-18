package com.pillartechnology.vendingMachine.controller.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.pillartechnology.vendingMachine.controller.test.ProductSelectionControllerTest.ProductSelectionHandler;

import org.jmock.integration.junit4.JUnitRuleMockery;

import org.jmock.Expectations;

public class ProductSelectionControllerTest {
	public class ProductSelectionHandler {
		
		private final VendingMachineDisplay display;
		private final VendingMachineProductSelectionManager selection;
		private final FundsService fundsService;
		private final VendingMachineInventory inventory;
		
		public ProductSelectionHandler(
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

		void refundDeposit() {
			this.fundsService.refundFundsOnDeposit();
			this.selection.clearInputs();
			this.display.messageMakeSelection();
		}

	}

	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	
	public interface VendingMachineInventory {

		VendingMachineInventoryItem findProduct(Integer productCode);

	}

	public interface VendingMachineInventoryItem {

		public String productName();

		public Integer quantityInStock();

		public Integer productPrice();

	}

	public interface FundsService {

		void refundFundsOnDeposit();

		void makeChange();

		void addFundsToRespository(Integer amount);

		Integer sumOfFundsOnDeposit();

		Boolean isAbleToMakeChange();
	}

	public interface VendingMachineDisplay {

		void messageMakeSelection();

		void promptProductDispensed(String productName);

		void messageAmountOnDeposit(Integer fundsOnDepositAmount);

		void messageInvalidSelection();

		void messageProductCost(Integer productCost);

		void messageExactChangeOnly();
	}

	public interface VendingMachineProductSelectionManager {

		void isRefundCode(Integer input);

		void clearInputs();

		boolean isCompleteProductCode(Integer input);

		//product.quantityInStock() > 0
		boolean isPurchasable(VendingMachineInventoryItem product);

	}
	
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
					//get amount already deposited
					Integer deposit = this.fundsService.sumOfFundsOnDeposit();
					
					//get cost of product
					Integer productPrice = product.productPrice();
					
					//not enough funds to buy product
					if( deposit < productPrice ) {
						this.display.messageProductCost(productPrice);
						
					//deposit exceeds cost of product and machine is not able to make change
					} else if( deposit > productPrice && !fundsService.isAbleToMakeChange() ) {
						this.fundsService.refundFundsOnDeposit();
						this.selection.clearInputs();
						this.display.messageExactChangeOnly();
							
					//purchase can be completed
					} else {
						//move funds equaling the price of the product into the repository  
						this.fundsService.addFundsToRespository( productPrice );

						//clear out selection, so next one can proceed
						this.selection.clearInputs();

						//output product dispensed to console
						this.display.promptProductDispensed(product.productName());
						
						//amount of funds on deposit exceeds cost of product
						if( deposit > productPrice ) {
							this.fundsService.makeChange();
						}
					}
					
				//product was not found or is not in stock
				} else {
					this.display.messageInvalidSelection();
				}
			}
		}
	}
	
	/**********************************************************************************************
	 * ********************************************************************************************
	 * ********************************************************************************************
	 * TESTS BEGIN HERE
	 * @return 
	 */
	
	private VendingMachineProductSelectionManager selection;
	private VendingMachineDisplay display;
	private FundsService fundsService;
	private VendingMachineInventory inventory;
	private VendingMachineInventoryItem product;
	private ProductSelectionHandler handler;
	private ProductSelectionController controller;
	
	@Before
	public final void setup() {
		selection = context.mock(VendingMachineProductSelectionManager.class);
		display = context.mock(VendingMachineDisplay.class);
		fundsService = context.mock(FundsService.class);
		inventory = context.mock(VendingMachineInventory.class);
		product = context.mock(VendingMachineInventoryItem.class);
		handler = new ProductSelectionHandler(display, selection, fundsService, inventory);
		controller = new ProductSelectionController(display, selection, fundsService, inventory, handler);
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
