package com.pillartechnology.vendingMachine.model;

public interface FundsService {

	void refundFundsOnDeposit();

	void makeChange();

	void addFundsToRespository(Integer amount);

	Integer sumOfFundsOnDeposit();

	Boolean isAbleToMakeChange();
}