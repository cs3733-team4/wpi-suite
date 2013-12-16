package edu.wpi.cs.wpisuitetng.modules.cal.year;

import static org.junit.Assert.*;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.year.YearCalendar;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.junit.Test;

import java.lang.reflect.Field;

import edu.wpi.cs.wpisuitetng.modules.cal.models.client.EventModel;

/**
 * Tests for MonthItem class
 */


public class YearCalendarTest {
	
	EventModel events=EventModel.getInstance();
	DateTime time2000 = new DateTime(2000, 1, 1, 1, 1);
	MutableDateTime Mtime2000 = new MutableDateTime(2000, 1, 1, 1, 1, 0, 0);
	MutableDateTime Mtime2001 = new MutableDateTime(2001, 1, 1, 1, 1, 0, 0);
	MutableDateTime Mtime2002 = new MutableDateTime(2002, 1, 1, 1, 1, 0, 0);
	
	@Test
	public void testExists() {
		YearCalendar YC = new YearCalendar(time2000,events);
		assertNotNull("A Year calendar can be created", YC);
	}
	
	@Test
	public void testCorrectDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		YearCalendar YC = new YearCalendar(time2000,events);
		
		Field f= YC.getClass().getDeclaredField("calendarStart");
		f.setAccessible(true);
		
		// Ted can probably explain it better, but when a yearCalendar is initialized, it automatically displays. The display function takes the input datetime and increments it daily as it 
		// creates yeadDayHolders JPanels, eventually reaching the end of December and incrementing to the next year. Testing for the input datetime to become the next year is proof that the 
		// display function is working when the calendar initializes.
		assertEquals("A Year calendar will display the year it was given on initialization, incrementing the saved date to the next year when finished", Mtime2001.getYear(), ((MutableDateTime)f.get(YC)).getYear());
	}
	
	@Test
	public void testNext() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		YearCalendar YC = new YearCalendar(time2000,events);
		
		
		// Testing is broken: same null network problems as MonthCalendarTest
		YC.next();
		
		Field f= YC.getClass().getDeclaredField("calendarStart");
		f.setAccessible(true);
		
		assertEquals("A Year calendar will display the next year and increment the stored year when next() is used", Mtime2002.getYear(), ((MutableDateTime)f.get(YC)).getYear());

	}
	
	@Test
	public void testPrevious() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		YearCalendar YC = new YearCalendar(time2000,events);
		YC.previous();
		
		Field f= YC.getClass().getDeclaredField("calendarStart");
		f.setAccessible(true);
		
		assertEquals("A Year calendar will display the previous year and decrement the stored year when previous() is used", Mtime2000.getYear(), ((MutableDateTime)f.get(YC)).getYear());

	}

}
