package edu.wpi.cs.wpisuitetng.modules.cal.formulae;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.utils.Months;


public class MonthsTest {

	
	DateTime timeOne = 			new DateTime(2012, 10, 2, 1, 1);
	DateTime timeOnePointNine = new DateTime(2012, 11, 1, 1, 1);
	DateTime timeTwo = 			new DateTime(2012, 11, 2, 1, 1);
	DateTime timeTwoPointOne = 	new DateTime(2012, 11, 3, 1, 1);
	DateTime timeThree = 		new DateTime(2012, 12, 2, 1, 1);
	
	
	@Test
	public void testNextMonth() {
		assertEquals("nextMonth should return the month following the input DateTime", timeTwo, Months.nextMonth(timeOne));
		assertEquals("nextMonth should return the month following the input DateTime", timeThree, Months.nextMonth(timeTwo));
	}
	
	@Test
	public void testPrevMonth() {
		assertEquals("prevMonth should return the month preceeding the input DateTime", timeOne, Months.prevMonth(timeTwo));
		assertEquals("prevMonth should return the month preceeding the input DateTime", timeTwo, Months.prevMonth(timeThree));
	}
	
	@Test
	public void testNextDay() {
		assertEquals("nextDay should return the day following the input DateTime", timeTwo, Months.nextDay(timeOnePointNine));
		assertEquals("nextDay should return the day following the input DateTime", timeTwoPointOne, Months.nextDay(timeTwo));
	}
	
	@Test
	public void testPrevDay() {
		assertEquals("prevDay should return the day preceeding the input DateTime", timeTwo, Months.prevDay(timeTwoPointOne));
		assertEquals("prevDay should return the day preceeding the input DateTime", timeOnePointNine, Months.prevDay(timeTwo));
	}

}
