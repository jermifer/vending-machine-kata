package com.pillartechnology.vendingMachine.model;

/**********************************************************************************************
 * PRICE CLASS
 * @author jennifer.mankin
 *
 */
public class Price {
	
	private int usCents;

	public Price(int usCents) {
		this.usCents = usCents;
	}
	
	public static Price usCents(int usCents) {
		return new Price(usCents);
	}
	
	public double getUSDollar() {
		return this.usCents / 100.0d;
	}
	
	@Override
	public boolean equals(Object other) {
		if( other instanceof Price ) {
			Price that = (Price) other;
			return ( this.usCents == that.usCents );
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return usCents;
	}
	
	@Override
	public String toString() {
		return "$" + this.getUSDollar();
	}
	
}