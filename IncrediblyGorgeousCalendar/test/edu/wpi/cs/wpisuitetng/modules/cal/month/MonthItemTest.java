package edu.wpi.cs.wpisuitetng.modules.cal.month;

import static org.junit.Assert.*;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.month.MonthItem;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

/**
 * Tests for MonthItem class
 */


public class MonthItemTest {
	
	DateTime time = new DateTime(); 
	DateTime timeModified = new DateTime(2010, 12, 12, 1, 1);
	
	DateTime timeForSimpleTime  = new DateTime(2013, 01, 12, 1, 1);
	DateTime timeForSimpleTime2 = new DateTime(2013, 01, 12, 0, 1);
	DateTime timeForSimpleTime3 = new DateTime(2013, 01, 12, 11, 59);
	
	DateTime timeForSimpleTimePM  = new DateTime(2013, 01, 12, 22, 1);
	DateTime timeForSimpleTimePM2 = new DateTime(2013, 01, 12, 12, 1);
	DateTime timeForSimpleTimePM3 = new DateTime(2013, 01, 12, 23, 59);
	
	Event e=new Event().addName("Hello").addStartTime(timeModified);
	
	@Test
	public void testSimpleTimeAM() {
		MonthItem mItem = new MonthItem(null);
		assertEquals("simpleTime should return in hour:minute", "1:01", mItem.simpleTime(timeForSimpleTime));
		assertEquals("simpleTime should return in hour:minute", "12:01", mItem.simpleTime(timeForSimpleTime2));
		assertEquals("simpleTime should return in hour:minute", "11:59", mItem.simpleTime(timeForSimpleTime3));
	}
	
	@Test
	public void testSimpleTimePM() {
		MonthItem mItem = new MonthItem(null);
		assertEquals("simpleTime should return in hour:minute. If time is past 12:00, a p should be appended to the end", "10:01p", mItem.simpleTime(timeForSimpleTimePM));
		assertEquals("simpleTime should return in hour:minute. If time is past 12:00, a p should be appended to the end", "12:01p", mItem.simpleTime(timeForSimpleTimePM2));
		assertEquals("simpleTime should return in hour:minute. If time is past 12:00, a p should be appended to the end", "11:59p", mItem.simpleTime(timeForSimpleTimePM3));
	}

}
