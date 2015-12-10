package com.pillartechnology.currency;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

public class CoinFactory {
	public static final Vector<Hashtable<String, ?>> COINS = {
			{
				type = new String("Dime"),
				country = new String("US"),
				value = new BigDecimal("0.10"),
				diameter = new Double(17.91);
			}
	};
}


