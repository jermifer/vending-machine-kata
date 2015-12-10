package com.pillartechnology.test.usCurrency;

import static org.junit.Assert.*;

import com.pillartechnology.currency.usCurrency.*;

import org.junit.Before;
import org.junit.Test;

public class USCoinsTest {
	
	private DimeEntity _dime;
	private GoldDollarEntity _gold_dollar;
	private HalfDollarEntity _half_dollar;

	@Before
	public void setUp() throws Exception {
		_dime = new DimeEntity();
		_gold_dollar = new GoldDollarEntity();
		_half_dollar = new HalfDollarEntity();
	}

	@Test
	public final void dime() {
		System.out.println("\nDIME");
		System.out.println(_dime.value());
		System.out.println(_dime.diameterInMillimeters());
		System.out.println(_dime.thicknessInMillimeters());
		System.out.println(_dime.massInGrams());
	}
	
	@Test
	public final void goldDollar() {
		System.out.println("\nGOLD DOLLAR");
		System.out.println(_gold_dollar.value());
		System.out.println(_gold_dollar.diameterInMillimeters());
		System.out.println(_gold_dollar.thicknessInMillimeters());
		System.out.println(_gold_dollar.massInGrams());
	}
	
	@Test
	public final void halfDollar() {
		System.out.println("\nHALF DOLLAR");
		System.out.println(_half_dollar.value());
		System.out.println(_half_dollar.diameterInMillimeters());
		System.out.println(_half_dollar.thicknessInMillimeters());
		System.out.println(_half_dollar.massInGrams());
	}

}
