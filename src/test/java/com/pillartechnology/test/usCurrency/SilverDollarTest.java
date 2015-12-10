package com.pillartechnology.test.usCurrency;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.pillartechnology.currency.usCurrency.*;

public class SilverDollarTest {
	private SilverDollarEntity _subject;

	@Before
	public void setUp() throws Exception {
		_subject = new SilverDollarEntity();
	}

	@Test
	public final void instantiationTest() {
		assertEquals("", _subject);
	}

}
