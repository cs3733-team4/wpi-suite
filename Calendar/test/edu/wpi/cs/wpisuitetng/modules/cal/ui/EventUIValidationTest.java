package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import static org.junit.Assert.*;

import javax.swing.JTextField;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.ReflectUtils;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs.AddEventDisplay;

public class EventUIValidationTest
{
	private AddEventDisplay mEventDisplay = new AddEventDisplay();

	DatePicker f;

	DatePicker ff;

	JTextField fff;

	@Before
	public void setup() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
	{
		f = ReflectUtils.getFieldValue(mEventDisplay, "startTimeDatePicker");
		ff = ReflectUtils.getFieldValue(mEventDisplay, "endTimeDatePicker");
		fff = ReflectUtils.getFieldValue(mEventDisplay, "nameTextField");
	}

	@Test
	public void testIfEventDisplayIsNonNull()
	{
		assertNotNull("Make sure it actually runs", mEventDisplay);
	}

	@Test
	public void testIfSaveButtonStartsDisabled()
	{
		assertFalse("Event is not saveable by default", mEventDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsEnabledWithProperInput() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		setUpAndTestValidEventFields();
	}

	@Test
	public void testIfSaveButtonIsDisabledWithProperDateButNoName() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{

		f.date.setValue("11/20/13");
		f.time.setValue("03:00");
		f.AMPM.setSelectedItem("PM");

		ff.date.setValue("11/20/13");
		ff.time.setValue("04:00");
		ff.AMPM.setSelectedItem("PM");

		assertFalse("Event is not saveable with no event name", mEventDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsDisabledWithProperNameButNoStartDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{

		fff.setText("Test Event");

		ff.date.setValue("11/20/13");
		ff.time.setValue("04:00");
		ff.AMPM.setSelectedItem("PM");

		assertFalse("Event is not saveable with no start date", mEventDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsDisabledWithProperNameButNoEndDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{

		fff.setText("Test Event");

		f.date.setValue("11/20/13");
		f.time.setValue("03:00");
		f.AMPM.setSelectedItem("PM");
		
		ff.date.setValue("");

		assertFalse("Event is not saveable with no end date", mEventDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsDisabledWithStartDateAfterEndDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{

		fff.setText("Test Event");

		f.date.setValue("11/20/13");
		f.time.setValue("05:00");
		f.AMPM.setSelectedItem("PM");

		ff.date.setValue("11/20/13");
		ff.time.setValue("04:00");
		ff.AMPM.setSelectedItem("PM");

		assertFalse("Event is not saveable with start date after end date", mEventDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsDisabledOnMultidayEvent() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{

		fff.setText("Test Event");

		f.date.setValue("11/20/13");
		f.time.setValue("05:00");
		f.AMPM.setSelectedItem("PM");

		ff.date.setValue("11/21/13");
		ff.time.setValue("04:00");
		ff.AMPM.setSelectedItem("PM");

		assertTrue("Event is saveable with multiday event", mEventDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterNameDeletion() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		setUpAndTestValidEventFields();

		fff.setText("");

		assertFalse("Event is no longer saveable", mEventDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterDateDeletion() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		setUpAndTestValidEventFields();

		f.date.setValue("11/21/13");

		assertFalse("Event is no longer saveable", mEventDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsDisabledWithOnlySpacesForEventName() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		setUpAndTestValidEventFields();

		fff.setText("    ");

		assertFalse("Event is no longer saveable", mEventDisplay.isSaveable());
	}

	private void setUpAndTestValidEventFields() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{

		fff.setText("Test Event");

		f.date.setValue("11/20/13");
		f.time.setValue("03:00");
		f.AMPM.setSelectedItem("PM");

		ff.date.setValue("11/20/13");
		ff.time.setValue("11:00");
		ff.AMPM.setSelectedItem("PM");

		assertTrue("Event is saveable with proper input", mEventDisplay.isSaveable());
	}
	
	@Test
	public void autoFillDateAndTimeIsSavable() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		fff.setText("Test Event");
		
		assertTrue("Event is saveable with proper input", mEventDisplay.isSaveable());
	}
	
	@Test
	public void autoFillDateAndTimeIsNow() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{	
		DateTime today = new DateTime();
		DateTime todayPlusOneHour = today.plusHours(1);
	
		fff.setText("Test Event");
		
		assertEquals(f.getDate().getMonthOfYear(), today.getMonthOfYear());
		assertEquals(f.getDate().getDayOfMonth(), today.getDayOfMonth());
		assertEquals(ff.getDate().getMonthOfYear(), todayPlusOneHour.getMonthOfYear());
		assertEquals(ff.getDate().getDayOfMonth(), todayPlusOneHour.getDayOfMonth());
		assertTrue("Event is saveable with proper input", mEventDisplay.isSaveable());
	}
}
