package com.pillartechnology.vendingMachine.model.funds;

public interface FundsService {

	void refundFundsOnDeposit();

	void makeChange();

	void addFundsToRespository(Integer amount);

	Integer sumOfFundsOnDeposit();

	Boolean isAbleToMakeChange();
}