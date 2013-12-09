package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.week;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.MockNetwork;
import edu.wpi.cs.wpisuitetng.network.Network;

/**
 * Tests for MonthsTest class
 *
 */

public class WeekCalendarTest{
	
	private EventModel dummyModel = EventModel.getInstance();
	private CommitmentModel dummyModel2 = CommitmentModel.getInstance();
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		((MockNetwork)Network.getInstance()).clearCache();
	}
	
	private DateTime time =  new DateTime();
	private DateTime timeDecember = new DateTime(2012,12,31,1,1);
	private DateTime timeJanuary = timeDecember.plusWeeks(1);
	private DateTime timePlusOneWeek = time.plusWeeks(1);
	private DateTime timeOneHourThirtyMinutesBeforeDST = new DateTime(2013, 11, 3, 0, 30);
	private DateTime timeOneHourThirtyMinutesBeforeDSTPlusOneWeek = timeOneHourThirtyMinutesBeforeDST.plusWeeks(1);
	
	@Test
	public void testgetTimeReturnsProvidedTime() {
		WeekCalendar mCal = new WeekCalendar(time, dummyModel);
		assertEquals("Function should return current time", time, mCal.getTime());
	}
	
	@Test
	public void testNextReturnsNextWeek() {
		WeekCalendar mCal = new WeekCalendar(time, dummyModel);
		mCal.next();
		assertEquals("Function should return current time plus 1 month", timePlusOneWeek, mCal.getTime());
	}
	
	@Test
	public void testNextReturnsNextMonthDecemberToJanuary() {
		WeekCalendar mCal = new WeekCalendar(timeDecember, dummyModel);
		mCal.next();
		assertEquals("Function should return January 2013", timeJanuary, mCal.getTime());
	}
	
	@Test
	public void testPreviousReturnsNextMonthJanuaryToDecember() {
		WeekCalendar mCal = new WeekCalendar(timeJanuary, dummyModel);
		mCal.previous();
		assertEquals("Function should return December 2012", timeDecember, mCal.getTime());
	}
	
	@Test
	public void testIsTodayDaylightSavingsTime() {
		WeekCalendar mCal = new WeekCalendar(timeOneHourThirtyMinutesBeforeDST, dummyModel);
		mCal.next();
		assertEquals("Next month should be the same even if DST happened", timeOneHourThirtyMinutesBeforeDSTPlusOneWeek, mCal.getTime());
	}
	
	@Test
	public void testNextPrevious() {
		WeekCalendar mCal = new WeekCalendar(timeOneHourThirtyMinutesBeforeDST, dummyModel);
		mCal.next();
		mCal.previous();
		assertEquals("Time should be the same", timeOneHourThirtyMinutesBeforeDST, mCal.getTime());
	}
	
	@Test
	public void testNextPreviousIsToday() {
		WeekCalendar mCal = new WeekCalendar(time, dummyModel);
		mCal.next();
		mCal.previous();
		assertEquals("Time should be the same", time, mCal.getTime());
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
