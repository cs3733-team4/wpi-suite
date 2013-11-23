package edu.wpi.cs.wpisuitetng.modules.cal.month;

import static org.junit.Assert.*;
import edu.wpi.cs.wpisuitetng.modules.cal.month.MonthItem;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Tests for MonthItem class
 *
 */


public class MonthItemTest {

	DateTime time = new DateTime(); 
	DateTime timeModified = new DateTime(2010, 12, 12, 1, 1);
	DateTime timeForSimpleTime = new DateTime(2013, 01, 12, 1, 1);
	DateTime timeForSimpleTimePM = new DateTime(2013, 01, 12, 22, 1);
	
	@Test
	public void testGetWhen() {
		MonthItem mItem = new MonthItem(time, "Test");
		assertEquals("getWhen should return the DateTime provided", time, mItem.getWhen());
	}
	
	@Test
	public void testSetWhen() {
		MonthItem mItem = new MonthItem(time, "Test");
		mItem.setWhen(timeModified);
		assertEquals("getWhen should return the modified DateTime by setWhen", timeModified, mItem.getWhen());
	}
	
	@Test
	public void testSimpleTimeAM() {
		MonthItem mItem = new MonthItem(timeForSimpleTime, "Test");
		assertEquals("simpleTime should return in hour:minute", "1:1", mItem.simpleTime(timeForSimpleTime));
	}
	
	@Test
	public void testSimpleTimePM() {
		MonthItem mItem = new MonthItem(timeForSimpleTimePM, "Test");
		assertEquals("simpleTime should return in hour:minute. If time is past 12:00, a p should be appended to the end",
				"10:1p", mItem.simpleTime(timeForSimpleTimePM));
	}

}
