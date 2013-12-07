package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.text.ParseException;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.DatePicker;

public class EventUIValidationTest {
private AddEventDisplay mEventDisplay = new AddEventDisplay();
	
	@Test
	public void testIfEventDisplayIsNonNull() {
		assertNotNull("Make sure it actually runs", mEventDisplay);
	}
	
	@Test
	public void testIfSaveButtonStartsDisabled() {
		assertFalse("Event is not saveable by default", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsEnabledWithProperInput() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setUpAndTestValidEventFields();
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithProperDateButNoName() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Field f= mEventDisplay.getClass().getDeclaredField("startTimeDatePicker");
		f.setAccessible(true);
		
		Field ff= mEventDisplay.getClass().getDeclaredField("endTimeDatePicker");
		ff.setAccessible(true);
		
		((DatePicker) f.get(mEventDisplay)).date.setValue("11/20/13");
		((DatePicker) f.get(mEventDisplay)).time.setValue("03:00");
		((DatePicker) f.get(mEventDisplay)).AMPM.setSelectedItem("PM");
		
		((DatePicker) ff.get(mEventDisplay)).date.setValue("11/20/13");
		((DatePicker) ff.get(mEventDisplay)).time.setValue("04:00");
		((DatePicker) ff.get(mEventDisplay)).AMPM.setSelectedItem("PM");
		
		assertFalse("Event is not saveable with no event name", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithProperNameButNoStartDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Field ff= mEventDisplay.getClass().getDeclaredField("endTimeDatePicker");
		ff.setAccessible(true);
		
		Field fff= mEventDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mEventDisplay)).setText("Test Event");
		
		((DatePicker) ff.get(mEventDisplay)).date.setValue("11/20/13");
		((DatePicker) ff.get(mEventDisplay)).time.setValue("04:00");
		((DatePicker) ff.get(mEventDisplay)).AMPM.setSelectedItem("PM");
		
		assertFalse("Event is not saveable with no start date", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithProperNameButNoEndDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Field f= mEventDisplay.getClass().getDeclaredField("startTimeDatePicker");
		f.setAccessible(true);
		
		Field fff= mEventDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mEventDisplay)).setText("Test Event");
		
		((DatePicker) f.get(mEventDisplay)).date.setValue("11/20/13");
		((DatePicker) f.get(mEventDisplay)).time.setValue("03:00");
		((DatePicker) f.get(mEventDisplay)).AMPM.setSelectedItem("PM");
		
		assertFalse("Event is not saveable with no end date", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithStartDateAfterEndDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Field f= mEventDisplay.getClass().getDeclaredField("startTimeDatePicker");
		f.setAccessible(true);
		
		Field ff= mEventDisplay.getClass().getDeclaredField("endTimeDatePicker");
		ff.setAccessible(true);
		
		Field fff= mEventDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mEventDisplay)).setText("Test Event");
		
		((DatePicker) f.get(mEventDisplay)).date.setValue("11/20/13");
		((DatePicker) f.get(mEventDisplay)).time.setValue("05:00");
		((DatePicker) f.get(mEventDisplay)).AMPM.setSelectedItem("PM");
		
		((DatePicker) ff.get(mEventDisplay)).date.setValue("11/20/13");
		((DatePicker) ff.get(mEventDisplay)).time.setValue("04:00");
		((DatePicker) ff.get(mEventDisplay)).AMPM.setSelectedItem("PM");
		
		assertFalse("Event is not saveable with start date after end date", mEventDisplay.isSaveable());
	}
	
	
	
	
	// We should change / remove this test as soon as they implement multi-day events
	@Test
	public void testIfSaveButtonIsDisabledOnMultidayEvent() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Field f= mEventDisplay.getClass().getDeclaredField("startTimeDatePicker");
		f.setAccessible(true);
		
		Field ff= mEventDisplay.getClass().getDeclaredField("endTimeDatePicker");
		ff.setAccessible(true);
		
		Field fff= mEventDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mEventDisplay)).setText("Test Event");
		
		((DatePicker) f.get(mEventDisplay)).date.setValue("11/20/13");
		((DatePicker) f.get(mEventDisplay)).time.setValue("05:00");
		((DatePicker) f.get(mEventDisplay)).AMPM.setSelectedItem("PM");
		
		((DatePicker) ff.get(mEventDisplay)).date.setValue("11/21/13");
		((DatePicker) ff.get(mEventDisplay)).time.setValue("04:00");
		((DatePicker) ff.get(mEventDisplay)).AMPM.setSelectedItem("PM");
		
		assertFalse("Event is not saveable with multiday event", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterNameDeletion() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setUpAndTestValidEventFields();

		Field fff= mEventDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mEventDisplay)).setText("");
		
		assertFalse("Event is no longer saveable", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterDateDeletion() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setUpAndTestValidEventFields();
		
		Field f= mEventDisplay.getClass().getDeclaredField("startTimeDatePicker");
		f.setAccessible(true);
		
		((DatePicker) f.get(mEventDisplay)).date.setValue("11/21/13");
		
		assertFalse("Event is no longer saveable", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithOnlySpacesForEventName() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setUpAndTestValidEventFields();
		
		Field fff= mEventDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mEventDisplay)).setText("    ");
		
		assertFalse("Event is no longer saveable", mEventDisplay.isSaveable());
	}
	
	private void setUpAndTestValidEventFields() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		
		Field f= mEventDisplay.getClass().getDeclaredField("startTimeDatePicker");
		f.setAccessible(true);
		
		Field ff= mEventDisplay.getClass().getDeclaredField("endTimeDatePicker");
		ff.setAccessible(true);
		
		Field fff= mEventDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mEventDisplay)).setText("Test Event");
		
		((DatePicker) f.get(mEventDisplay)).date.setValue("11/20/13");
		((DatePicker) f.get(mEventDisplay)).time.setValue("03:00");
		((DatePicker) f.get(mEventDisplay)).AMPM.setSelectedItem("PM");
		
		((DatePicker) ff.get(mEventDisplay)).date.setValue("11/20/13");
		((DatePicker) ff.get(mEventDisplay)).time.setValue("11:00");
		((DatePicker) ff.get(mEventDisplay)).AMPM.setSelectedItem("PM");
		
		assertTrue("Event is saveable with proper input", mEventDisplay.isSaveable());
	}
	
	@Test
	public void autoFillDateAndTimeIsSavable() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		AddEventDisplay mEventDisplay = new AddEventDisplay();
		
		Field f= mEventDisplay.getClass().getDeclaredField("startTimeDatePicker");
		f.setAccessible(true);
		
		Field ff= mEventDisplay.getClass().getDeclaredField("endTimeDatePicker");
		ff.setAccessible(true);
		
		Field fff= mEventDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mEventDisplay)).setText("Test Event");
		
		assertTrue("Event is saveable with proper input", mEventDisplay.isSaveable());
	}
}
