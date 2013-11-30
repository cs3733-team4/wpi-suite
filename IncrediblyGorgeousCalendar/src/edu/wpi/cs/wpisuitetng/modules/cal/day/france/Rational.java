/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.day.france;

/**
 * Used for scalable fractions in day panel. Non-simplifying
 */
public class Rational
{
	private int numerator;
	private int denominator;

	public Rational(int num, int denom)
	{
		numerator = num;
		denominator = denom;
	}
	
	public int toInt(int width)
	{
		return (int) Math.round(((double)width * numerator) / (double) denominator);
	}

	public int getNumerator()
	{
		return numerator;
	}
	
	public int getDenominator()
	{
		return denominator;
	}

	public Rational multiply(Rational rational)
	{
		return new Rational(numerator * rational.numerator, denominator * rational.denominator);
	}
	
	public Rational addition(Rational rational)
	{
		return new Rational(rational.denominator * numerator + rational.numerator * denominator, denominator * rational.denominator);
	}
	
	public Rational subtract(Rational rational)
	{
		return this.addition(rational.negate());
	}

	public Rational divide(Rational rational)
	{
		return this.multiply(rational.inverse());
	}

	public Rational inverse()
	{
		return new Rational(denominator, numerator);
	}
	
	public Rational negate()
	{
		return new Rational(-numerator, denominator);
	}
	
	@Override
	public String toString()
	{
		return String.format("{%d/%d}", numerator, denominator);
	}

}
