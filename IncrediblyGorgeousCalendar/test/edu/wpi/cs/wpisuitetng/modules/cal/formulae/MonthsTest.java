package edu.wpi.cs.wpisuitetng.modules.cal.formulae;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.CalendarNavigationModule;

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
	
	@Test
	public void testIsLeapYear() {
		assertFalse("Testing for basic leap year recognition", Months.isLeapYear(1991));
		assertFalse("Testing for basic leap year recognition", Months.isLeapYear(1993));
		assertTrue("Testing for basic leap year recognition", Months.isLeapYear(1992));
		assertTrue("Testing for basic leap year recognition", Months.isLeapYear(1996));
		
		assertFalse("Testing that multiple of 100 that arn't multiple of 400 are considered leap years", Months.isLeapYear(1900));
		assertFalse("Testing that multiple of 100 that arn't multiple of 400 are considered leap years", Months.isLeapYear(1800));
		assertFalse("Testing that multiple of 100 that arn't multiple of 400 are considered leap years", Months.isLeapYear(2600));
		assertFalse("Testing that multiple of 100 that arn't multiple of 400 are considered leap years", Months.isLeapYear(3300));
		
		assertTrue("Testing that multiples of 400 are considered leap years", Months.isLeapYear(2000));
		assertTrue("Testing that multiples of 400 are considered leap years", Months.isLeapYear(2400));
		assertTrue("Testing that multiples of 400 are considered leap years", Months.isLeapYear(1200));
	}
	
	@Test
	public void testGetDaysInMonth() {
		assertEquals("Testing for normal months", Months.getDaysInMonth(1,2013),31);
		assertEquals("Testing for normal months", Months.getDaysInMonth(2,2013),28);
		assertEquals("Testing for normal months", Months.getDaysInMonth(3,2013),31);
		assertEquals("Testing for normal months", Months.getDaysInMonth(4,2013),30);
		assertEquals("Testing for normal months", Months.getDaysInMonth(5,2013),31);
		assertEquals("Testing for normal months", Months.getDaysInMonth(6,2013),30);
		assertEquals("Testing for normal months", Months.getDaysInMonth(7,2013),31);
		assertEquals("Testing for normal months", Months.getDaysInMonth(8,2013),31);
		assertEquals("Testing for normal months", Months.getDaysInMonth(9,2013),30);
		assertEquals("Testing for normal months", Months.getDaysInMonth(10,2013),31);
		assertEquals("Testing for normal months", Months.getDaysInMonth(11,2013),30);
		assertEquals("Testing for normal months", Months.getDaysInMonth(12,2013),31);
		
		assertEquals("Testing for March on leap year", Months.getDaysInMonth(2,2012), 29);
		assertEquals("Testing for March on leap year", Months.getDaysInMonth(2,1416), 29);
		
		//Function will return months less than 1 / more than 12 with no error: 1-12 for the month stated in function parameters
	}
	
	@Test
	public void testGetDayOfMonth() {
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(2013,11,3),1);
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(2013,11,4),2);
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(2013,11,5),3);
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(2013,11,6),4);
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(2013,11,7),5);
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(2013,11,8),6);
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(2013,11,9),7);
		
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(2009,1,1),5);
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(2000,1,1),7);
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(1700,1,1),6);
		assertEquals("Testing for basic day retrieval (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(3000,1,1),4);
		
		assertEquals("Testing for March 29th on leap years (1-7 = Sunday-Saturday for expected values)", Months.getDayOfMonth(2004,2,29),1);
		
		// Function will return a day for March 29th on non-leap years with no error; Input is assumed correct / possible in function parameters
	}
	
	
	

}
