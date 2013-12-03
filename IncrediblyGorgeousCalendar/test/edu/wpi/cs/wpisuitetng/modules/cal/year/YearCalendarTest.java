package edu.wpi.cs.wpisuitetng.modules.cal.year;

import static org.junit.Assert.*;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.year.YearCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthItem;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;

/**
 * Tests for MonthItem class
 */


public class YearCalendarTest {
	
	EventModel events=new EventModel();
	DateTime time2000 = new DateTime(2000, 1, 1, 1, 1);
	
	@Test
	public void testExists() {
		YearCalendar YC = new YearCalendar(time2000,events);
		assertNotNull("A Year calendar can be created", YC);
	}
	
	@Test
	public void testCorrectDate() {
		YearCalendar YC = new YearCalendar(time2000,events);
		assertNotNull("A Year calendar will display the year it was given on initialization", YC);
	}
	
	@Test
	public void testNext() {
		YearCalendar YC = new YearCalendar(time2000,events);
	}
	
	@Test
	public void testPrev() {
		YearCalendar YC = new YearCalendar(time2000,events);
	}

}
