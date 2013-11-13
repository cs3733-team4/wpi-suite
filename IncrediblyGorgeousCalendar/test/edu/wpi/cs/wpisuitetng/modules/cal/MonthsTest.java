package edu.wpi.cs.wpisuitetng.modules.cal;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.MonthCalendar;

/**
 * Tests for MonthsTest class
 *
 */

public class MonthsTest{

	private DateTime time =  new DateTime();
	private DateTime timeDecember = new DateTime(2012,12,1,1,1);
	private DateTime timeJanuary = new DateTime(2013,01,1,1,1);
	private DateTime timePlusOneMonth = time.plusMonths(1);
	private DateTime timeOneHourThirtyMinutesBeforeDST = new DateTime(2013, 11, 3, 0, 30);
	private DateTime timeOneHourThirtyMinutesBeforeDSTPlusOneMonth = new DateTime(2013, 12, 3, 0, 30);
	
	@Test
	public void testgetTimeReturnsProvidedTime() {
		MonthCalendar mCal = new MonthCalendar(time);
		assertEquals("Function should return current time", time, mCal.getTime());
	}
	
	@Test
	public void testNextReturnsNextMonth() {
		MonthCalendar mCal = new MonthCalendar(time);
		mCal.next();
		assertEquals("Function should return current time plus 1 month", timePlusOneMonth, mCal.getTime());
	}
	
	@Test
	public void testNextReturnsNextMonthDecemberToJanuary() {
		MonthCalendar mCal = new MonthCalendar(timeDecember);
		mCal.next();
		assertEquals("Function should return January 2013", timeJanuary, mCal.getTime());
	}
	
	@Test
	public void testPreviousReturnsNextMonthJanuaryToDecember() {
		MonthCalendar mCal = new MonthCalendar(timeJanuary);
		mCal.previous();
		assertEquals("Function should return December 2012", timeDecember, mCal.getTime());
	}
	
	@Test
	public void testIsTodayFalse() {
		MonthCalendar mCal = new MonthCalendar(timeJanuary);
		assertFalse("January 2013 is not today", mCal.isToday(timeJanuary));
	}
	
	@Test
	public void testIsTodayTrue() {
		MonthCalendar mCal = new MonthCalendar(time);
		assertTrue("Today should be today", mCal.isToday(time));
	}
	
	@Test
	public void testIsTodayDaylightSavingsTime() {
		MonthCalendar mCal = new MonthCalendar(timeOneHourThirtyMinutesBeforeDST);
		mCal.next();
		assertEquals("Next month should be the same even if DST happened", timeOneHourThirtyMinutesBeforeDSTPlusOneMonth, mCal.getTime());
	}
	
	@Test
	public void testNextPrevious() {
		MonthCalendar mCal = new MonthCalendar(timeOneHourThirtyMinutesBeforeDST);
		mCal.next();
		mCal.previous();
		assertEquals("Time should be the same", timeOneHourThirtyMinutesBeforeDST, mCal.getTime());
	}
	
	@Test
	public void testNextPreviousIsToday() {
		MonthCalendar mCal = new MonthCalendar(time);
		mCal.next();
		mCal.previous();
		assertTrue("Time should be the same", mCal.isToday(time));
	}
}
