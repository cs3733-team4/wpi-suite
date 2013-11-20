package edu.wpi.cs.wpisuitetng.modules.cal.month;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.month.MonthCalendar;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

/**
 * Tests for MonthsTest class
 *
 */

public class MonthCalendarTest{

	private EventModel dummyModel=new EventModel();
	
	private DateTime time =  new DateTime();
	private DateTime timeDecember = new DateTime(2012,12,1,1,1);
	private DateTime timeJanuary = new DateTime(2013,01,1,1,1);
	private DateTime timePlusOneMonth = time.plusMonths(1);
	private DateTime timeOneHourThirtyMinutesBeforeDST = new DateTime(2013, 11, 3, 0, 30);
	private DateTime timeOneHourThirtyMinutesBeforeDSTPlusOneMonth = new DateTime(2013, 12, 3, 0, 30);
	
	
	// Testing is currently broken: 
	
	
	
	@Test
	public void testgetTimeReturnsProvidedTime() {
		MonthCalendar mCal = new MonthCalendar(time, dummyModel);
		assertEquals("Function should return current time", time, mCal.getTime());
	}
	
	@Test
	public void testNextReturnsNextMonth() {
		MonthCalendar mCal = new MonthCalendar(time, dummyModel);
		mCal.next();
		assertEquals("Function should return current time plus 1 month", timePlusOneMonth, mCal.getTime());
	}
	
	@Test
	public void testNextReturnsNextMonthDecemberToJanuary() {
		MonthCalendar mCal = new MonthCalendar(timeDecember, dummyModel);
		mCal.next();
		assertEquals("Function should return January 2013", timeJanuary, mCal.getTime());
	}
	
	@Test
	public void testPreviousReturnsNextMonthJanuaryToDecember() {
		MonthCalendar mCal = new MonthCalendar(timeJanuary, dummyModel);
		mCal.previous();
		assertEquals("Function should return December 2012", timeDecember, mCal.getTime());
	}
	
	@Test
	public void testIsTodayFalse() {
		MonthCalendar mCal = new MonthCalendar(timeJanuary, dummyModel);
		assertFalse("January 2013 is not today", mCal.isToday(timeJanuary));
	}
	
	@Test
	public void testIsTodayTrue() {
		MonthCalendar mCal = new MonthCalendar(time, dummyModel);
		assertTrue("Today should be today", mCal.isToday(time));
	}
	
	@Test
	public void testIsTodayDaylightSavingsTime() {
		MonthCalendar mCal = new MonthCalendar(timeOneHourThirtyMinutesBeforeDST, dummyModel);
		mCal.next();
		assertEquals("Next month should be the same even if DST happened", timeOneHourThirtyMinutesBeforeDSTPlusOneMonth, mCal.getTime());
	}
	
	@Test
	public void testNextPrevious() {
		MonthCalendar mCal = new MonthCalendar(timeOneHourThirtyMinutesBeforeDST, dummyModel);
		mCal.next();
		mCal.previous();
		assertEquals("Time should be the same", timeOneHourThirtyMinutesBeforeDST, mCal.getTime());
	}
	
	@Test
	public void testNextPreviousIsToday() {
		MonthCalendar mCal = new MonthCalendar(time, dummyModel);
		mCal.next();
		mCal.previous();
		assertTrue("Time should be the same", mCal.isToday(time));
	}
	
	
	DateTime now=new DateTime(2000,10,10,0,0);
	Event eatIcecream=new Event().addName("Eat icecream")
								 .addDescription("Yummy!")
								 .addStartTime(new DateTime(2000, 1, 1, 1, 0))
								 .addEndTime(new DateTime(2000, 1, 1, 2, 0));
	
	Event throwUpIcecream=new Event().addName("Throw up icecream")
			                         .addDescription("Ugh!")
			                         .addStartTime(new DateTime(2000, 1, 1, 2, 0))
			                         .addEndTime(new DateTime(2000, 1, 1, 3, 0));
	
	Event seekRevenge=new Event().addName("Seek vengance")
			                     .addDescription("Try to sell me bad icecream, will he?")
			                     .addStartTime(new DateTime(2000, 1, 1, 3, 0))
			                     .addEndTime(new DateTime(2000, 1, 3, 3, 0));
	
	//@Test
	//public void testAddingAnEvent() {
	//	MonthCalendar mCal = new MonthCalendar(time, dummyModel);
	//	mCal.addEvent(throwUpIcecream);
	//	assertTrue("An event should be added to the monthDay it takes place on", mCal.getMonthDayForDay(1).hasEvent(eatIcecream));
	//}
}
