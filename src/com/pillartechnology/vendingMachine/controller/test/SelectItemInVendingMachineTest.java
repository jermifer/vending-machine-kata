package com.pillartechnology.vendingMachine.controller.test;

import org.junit.Test;

import com.pillartechnology.vendingMachine.controller.ProductSelectionController;
import com.pillartechnology.vendingMachine.controller.test.SelectItemInVendingMachineTest.CommandController;
import com.pillartechnology.vendingMachine.model.Catalog;
import com.pillartechnology.vendingMachine.model.VendingMachineDisplay;
import com.pillartechnology.vendingMachine.model.Price;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Expectations;

/*
 * |	M&Ms	|	Starburst	|	Ruffles	| Popcorn	||	------------------
 * |	$1.00	|	$0.60		|	$0.60	|	$1.25	||	| Insert Change	 |
 * -----------------------------------------------------||	------------------
 * |			|				|			|			||		1 | 2 | 3
 * |			|				|			|			||		4 | 5 | 6
 * -----------------------------------------------------||		7 | 8 | 9
 * |			|				|			|			||			0 | X
 * |			|				|			|			||	-----			   ---
 * -----------------------------------------------------||	|	|  ----------  |||
 * |			|				|			|			||	|	|  | Refund |  ---
 * |			|				|			|			||	-----  ----------
 * ===============================================================================
 * ||	|											|	||						||	
 * ||	|											|	||						||
 * ||	|-------------------------------------------|	||						||
 * ===============================================================================
 *
 * Events
 * 	- FrontController::onLoad
 * 		- VendingMachineFactory::createSnackMachineExpectingThreeDigitCodeAcceptingOnlyUSCoins
 * 		- FundsService::isAbleToMakeChange
 * 			- True ? Display::messageInsertFunds
 * 			- False ? Display::messageExactAmountOnly
 * 		- Display::displayItemsAvailableForPurchase
 * 		- Display::displayFundsChoices
 * 		- Display::displayCommandInputDevice
 * 
 * - FrontController::onDeposit(String coinageType)
 * 		- coinageEntity = CoinageService::getCoinageByType
 * 		- coinageEntity == null
 * 			- True ? FundsService::returnMostRecentDeposit
 * 			- False ? FundsController::onFunds(coinageEntity.coinageSpecification)
 * 
 *	- CommandController::onInput(int commandInput) 
 *		- VendingMachineCommandManager::isRefundCommand
 *	  		- FundsService::returnFundsOnDeposit
 *  	- VendingMachineCommandManager::isClearInputsCommand
 *  		- VendingMachineCommandManager::clearInputs
 *  	- VendingMachineCommandManager::isReadyToFindItem
 *  		- True ? (item = Inventory::findItem) == null
 *  			- True ? Display::messageInvalidSelection
 *  			- False ? FundsService::canCompletePurchase
 *  				- True ? 
 *  					- FundsService::makeChange
 *  					- FundsService::addFundsToVault
 *  					- Display::promptProductDispensed
 *  				- False ? Display::messageItemAmount
 *  		- False ? 
 *  			- VendingMachineCommandManager::addInput
 *  			- Display::messageCurrentInputs
 *  
 *  - FundsController::onFunds(CoinageSpecification)
 *  	- isSuccessful = FundsService::depositFunds
 *  		- True ? Display::messageAmountOnDeposit
 *  		- False ? FundsService::returnMostRecentDeposit
 *  
 *  Model  
 *	- InventoryItem|null <VendingMachineInventory>::findItem(int commandInput)
 *	- InventoryItemsCollection|null <VendingMachineInventory>::getAllItems()
 *	- InventoryItemsCollection|null <VendingMachineInventory>::removeItem()
 *
 *	- String <VendingMachineDisplay>::messageInsertFunds()
 *	- String <VendingMachineDisplay>::messageItemAmount(int Inventory::itemAmount)
 *	- String <VendingMachineDisplay>::messageExactAmountOnly()
 *	- String <VendingMachineDisplay>::messageAmountOnDeposit(int FundsService:getAmountOnDeposit)
 *	- String <VendingMachineDisplay>::messageInvalidSelection()
 *	- String <VendingMachineDisplay>::messageCurrentInputs(int VendingMachineCommandManager::inputs) 
 *	- String <VendingMachineDisplay>::displayFundsChoices(CoinageService::getAllCoinage)
 *	- String <VendingMachineDisplay>::displayItemsAvailableForPurchase(InventoryItemsCollection VendingMachineInventory::getAllItems)
 *	- String <VendingMachineDisplay>::displayCommandInputDevice()
 *	- String <VendingMachineDisplay>::promptProductDispensed()
 *
 *	- int FundsService::makeChange()
 *	- int FundsService::returnAllFundsOnDeposit()
 *	- int FundsService::returnMostRecentDeposit()
 *	- int FundsServcie::getSumOfFundsInVault()
 *	- Boolean FundsService::depositFunds(CoinageEntity)
 *	- void FundsService::addFundsToVault(CoinageCollection)
 *	- Boolean FundsService::isAbleToMakeChange()
 *	- Boolean FundsService::canCompletePurchase()
 *
 *	- void FundsVault(CoinageCollection)
 *	- void FundsVault::addFunds(CoinageEntity)
 *	- int FundsVault::getFundsAmount()
 *	- void FundsVault::removeFunds(int amount)
 *
 *	- void FundsDepository(CoinageCollection)
 *	- void FundsDepository::addFunds(CoinageEntity)
 *	- int FundsDepository::getFundsOnDeposit()
 *	- void FundsDepository::removeFunds(int amount)
 *
 *	- int CoinageEntity::value()
 *	- String CoinageEntity::format()
 *	- String CoinageEntity::type()
 *	- String CoinageEntity::countryCode()
 *	- CoinageSpecification CoinageEntity::coinageSpecification()
 *
 *	- CoinageSpecification::diameterInMillimeters()
 *	- CoinageSpecification::thicknessInMillimeters()
 *	- CoinageSpecification::massInGrams()
 *	- CoinageSpecification::materialCompositionCode()
 *
 *	- CoinageCollection CoinageFactory::createUSCoinageWithCoins()
 *	- CoinageCollection CoinageFactory::createUSCoinageWithCoinsAndDollars()
 *	- CoinageCollection CoinageFactory::createUSCoinageWithDollars()
 *
 *	- CoinageCollection <CoinageCollection>::getAll()
 *	- void <CoinageCollection>::addCoinage()
 *
 *	- CoinageEntity CoinageService::getCoinageBySpec(CoinageCollection, CoinageSpecification)
 *	- CoinageEntity CoinageService::getCoinageByType(CoinageCollection, String coinageType)
 *	- CoinageEntity CoinageService::getCoinageByValue(CoinageCollection, int coinageValue)
 *	- Boolean CoinageService::isValidCoinage(CoinageCollection, CoinageEntity)
 *	- String CoinageService::format(value)
 *
 *	- int <VendingMachineCommandManager>::inputs()
 *	- void <VendingMachineCommandManager>::addInput(int commandInput)
 *	- void <VendingMachineCommandManager>::clearInputs()
 *	- Boolean <VendingMachineCommandManager>::isReadyToFindItem()
 *	- Boolean <VendingMachineCommandManager>::isRefundCommand()
 *	- Boolean <VendingMachineCommandManager>::isClearInputsCommand()
 *
 *	- VendingMachine VendingMachineFactory::createSnackMachineExpectingThreeDigitCodeAcceptingOnlyUSCoins
 *
 *	- <VendingMachine>::<VendingMachineInventory>
 *	- <VendingMachine>::<VendingMachineDisplay>
 *	- <VendingMachine>::<CoinageCollection>
 *	- <VendingMachine>::<VendingMachineCommandManager>
 */

/* Inventory Catalog Sample
 * Command	Location(x,y)	Name		Price(us cents)	Remaining
 * 001		1,1				M&Ms		100				10
 * 002		1,2				Starburst	60			1
 */

public class SelectItemInVendingMachineTest {
	public class CommandController {

		public CommandController(Command button, VendingMachineDisplay display) {
			// TODO Auto-generated constructor stub
		}

		public void onInput(int i) {
			// TODO Auto-generated method stub
			
		}

	}

	public interface Command {

		void onButton(int commandInput);
		
	}

	private Mockery context = new JUnit4Mockery();

	/**********************************************************************************************
	 * TESTPRODUCTEXISTSININVENTORY
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testproductExistsInInventory() {
		final Catalog inventory = context.mock(Catalog.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		
		context.checking(new Expectations() {
			{
				allowing(inventory).findPrice(with("M&Ms"));
				will(returnValue(Price.usCents(100)));
				oneOf(display).displayInventoryItemPrice(Price.usCents(100));
			}
		});
		
		ProductSelectionController productSelectionController = new ProductSelectionController(inventory, display);
		productSelectionController.onSelectItem("M&Ms");
	}
	
	/**********************************************************************************************
	 * TESTPRODUCTDOESNOTEXISTININVENTORY
	 * @author jennifer.mankin
	 *
	 */
	@Test
	public final void testproductDoesNotExistInInventory() throws Exception {
		final Catalog inventory = context.mock(Catalog.class);
		final VendingMachineDisplay display = context.mock(VendingMachineDisplay.class);
		final Command button = context.mock(Command.class);
		
		context.checking(new Expectations() {
			{
				allowing(inventory).findPrice(with("M&Ms"));
				will(returnValue(null));
				
				oneOf(display).displayProductNotFoundMessage("M&Ms");
			}
		});
		
		CommandController commandController = new CommandController(button, display);
		commandController.onInput(1);
		commandController.onInput(0);
		commandController.onInput(7);
		
		ProductSelectionController productSelectionController = new ProductSelectionController(inventory, display);
		productSelectionController.onSelectItem("M&Ms");
	}
}
