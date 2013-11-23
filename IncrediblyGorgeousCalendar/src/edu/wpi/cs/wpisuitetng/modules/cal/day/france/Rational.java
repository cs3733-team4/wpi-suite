package edu.wpi.cs.wpisuitetng.modules.cal.day.france;

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
}
