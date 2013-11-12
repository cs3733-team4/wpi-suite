package edu.wpi.cs.wpisuitetng.modules.calendar;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;

public class MonthsTest{

	@Test
	public void testIsLeapYear2013() {
		assertFalse("2013 is not a leap year", Months.isLeapYear(2013));
	}

}
