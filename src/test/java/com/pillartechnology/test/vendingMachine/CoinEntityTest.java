package com.pillartechnology.test.vendingMachine;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.pillartechnology.vendingMachine.CoinEntity;

public class CoinEntityTest {
	private CoinEntity _subject;

	@Before
	public void setUp() throws Exception {
		_subject = new CoinEntity();
	}

	@Test
	public void testCoinAmount() {
		Double result = _subject.coinAmount();
		assertEquals(new Double(0), result);
	}

}
