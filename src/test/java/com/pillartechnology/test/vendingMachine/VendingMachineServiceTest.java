package com.pillartechnology.test.vendingMachine;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * VendingMachineServiceTest
 */
public class VendingMachineServiceTest {
	private VendingMachineService subject;
	
	@Before
	public void setup () {
		subject = new VendingMachineService();
	}

    @Test
    public void testAddCoin()
    {
    	String result = subject.addCoin();
        assertEquals("",  result);
    }
}
