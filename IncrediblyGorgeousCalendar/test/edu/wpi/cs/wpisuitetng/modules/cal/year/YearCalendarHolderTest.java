package edu.wpi.cs.wpisuitetng.modules.cal.year;

import static org.junit.Assert.*;

import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.month.MonthCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.year.YearCalendarHolder;

import org.joda.time.DateTime;
import org.junit.Test;

public class YearCalendarHolderTest {

	MainPanel dummyPanel=new MainPanel();
	DateTime now =new DateTime(2000, 6, 1, 0, 0);
	DateTime then =new DateTime(2000, 7, 1, 0, 0);
	DateTime before =new DateTime(1900, 7, 1, 0, 0);
	DateTime after =new DateTime(2100, 7, 1, 0, 0);
	
	// Testing is currently broken: errors initializing MainPanel?
	// testGotoErrorLate seems to work fine for some reason
	// teatGotoErrorEarly is broken, but almost functionally the same as testGotoErrorLate
	
	
	
	@Test
	public void testExists() {
		YearCalendarHolder YCH=new YearCalendarHolder(now, dummyPanel);
		
		assertNotNull("A YearCalendarHolder can be created", YCH);
		assertEquals("It stores the correct daytime when initialized", YCH.getDate(), now);
	}
	
	@Test
	public void testGoto() {
		YearCalendarHolder YCH=new YearCalendarHolder(now, dummyPanel);
		YCH.parseGoto("7/1/2000");
		assertEquals("If the input is acceptable, the gotoDate box function will cause the calendar at mainPanel to display the designated month on the main screen", ((MonthCalendar)dummyPanel.getMOCA()).getTime().getMonthOfYear(), then.getMonthOfYear());
		YCH.parseGoto("7/1/1900");
		assertEquals("If the input is acceptable, the gotoDate box function will cause the calendar at mainPanel to display the designated month on the main screen", ((MonthCalendar)dummyPanel.getMOCA()).getTime().getMonthOfYear(), before.getMonthOfYear());
		YCH.parseGoto("7/1/2100");
		assertEquals("If the input is acceptable, the gotoDate box function will cause the calendar at mainPanel to display the designated month on the main screen", ((MonthCalendar)dummyPanel.getMOCA()).getTime().getMonthOfYear(), after.getMonthOfYear());
		YCH.parseGoto("7/1/00");
		assertEquals("If the input is acceptable, the gotoDate box function will cause the calendar at mainPanel to display the designated month on the main screen", ((MonthCalendar)dummyPanel.getMOCA()).getTime().getMonthOfYear(), before.getMonthOfYear());
		
	}
	
	@Test
	public void testGotoErrorEarly() {
		YearCalendarHolder YCH=new YearCalendarHolder(now, dummyPanel);
		YCH.parseGoto("6/1/1800");
		assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH.getError(), "* Year out of range (1900-2100)");
		YCH.parseGoto("6/1/1899");
		assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH.getError(), "* Year out of range (1900-2100)");
		YCH.parseGoto("12/31/1899");
		assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH.getError(), "* Year out of range (1900-2100)");
		YCH.parseGoto("12/12/-1000");
		assertEquals("If the input year is below 1900, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH.getError(), "* Year out of range (1900-2100)");
	}
	
	@Test
	public void testGotoErrorLate() {
		YearCalendarHolder YCH=new YearCalendarHolder(now, dummyPanel);
		YCH.parseGoto("6/1/2200");
		assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH.getError(), "* Year out of range (1900-2100)");
		YCH.parseGoto("6/1/2101");
		assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH.getError(), "* Year out of range (1900-2100)");
		YCH.parseGoto("1/1/2101");
		assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH.getError(), "* Year out of range (1900-2100)");
		YCH.parseGoto("12/12/99999");
		assertEquals("If the input year is above 2100, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH.getError(), "* Year out of range (1900-2100)");
	}
	
	@Test
	public void testGotoErrorFormat() {
		YearCalendarHolder YCH=new YearCalendarHolder(now, dummyPanel);
		YCH.parseGoto("12 1 2000");
		assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH.getError(), "* Invalid format, use: mm/dd/yyyy");
		
		// Note: the error message displayed is saved in the yearCalendarHolder, but refreshing the display for a correct input will remove the error message display.
		// This means we have to test with a new YCH each time.
		
		YearCalendarHolder YCH1=new YearCalendarHolder(now, dummyPanel);
		YCH1.parseGoto("12.12.2000");
		assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH1.getError(), "* Invalid format, use: mm/dd/yyyy");
		
		YearCalendarHolder YCH2=new YearCalendarHolder(now, dummyPanel);
		YCH2.parseGoto("August Nineteenth, Two-thousand Thirteen");
		assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH2.getError(), "* Invalid format, use: mm/dd/yyyy");
		
		YearCalendarHolder YCH3=new YearCalendarHolder(now, dummyPanel);
		YCH3.parseGoto("12/2000");
		assertEquals("If the input is in the incorrect format, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH3.getError(), "* Invalid format, use: mm/dd/yyyy");
	}
	
	@Test
	public void testGotoErrorInput() {
		YearCalendarHolder YCH=new YearCalendarHolder(now, dummyPanel);
		YCH.parseGoto("12/0/2000");
		assertEquals("If the input isn't an acceptable mm/dd/yyyy , the gotoDate box function will cause an error to display on the gotoErrorText box", YCH.getError(), "* Invalid format, use: mm/dd/yyyy");
		
		// Note: the error message displayed is saved in the yearCalendarHolder, but refreshing the display for a correct input will remove the error message display.
		// This means we have to test with a new YCH each time.
		
		YearCalendarHolder YCH1=new YearCalendarHolder(now, dummyPanel);
		YCH1.parseGoto("0/31/2000");
		assertEquals("If the input isn't an acceptable mm/dd/yyyy, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH1.getError(), "* Invalid format, use: mm/dd/yyyy");
		
		YearCalendarHolder YCH2=new YearCalendarHolder(now, dummyPanel);
		YCH2.parseGoto("-1/-1/2000");
		assertEquals("If the input isn't an acceptable mm/dd/yyyy, the gotoDate box function will cause an error to display on the gotoErrorText box", YCH2.getError(), "* Invalid format, use: mm/dd/yyyy");
		
		YearCalendarHolder YCH3=new YearCalendarHolder(now, dummyPanel);
		YCH3.parseGoto("31/12/2000");
		assertEquals("WE do not accept the English version of dd/m/yyyy, regardless of how amazingly logical it is", YCH3.getError(), "* Invalid format, use: mm/dd/yyyy");
	}

}
