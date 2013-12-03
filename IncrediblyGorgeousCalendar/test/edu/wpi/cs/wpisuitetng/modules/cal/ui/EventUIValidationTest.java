package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import static org.junit.Assert.*;

import java.text.ParseException;

import javax.swing.JCheckBox;

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
	public void testIfSaveButtonIsEnabledWithProperInput() {
		setUpAndTestValidEventFields();
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithProperDateButNoName() {
		mEventDisplay.getStartTimePicker().date.setValue("11/20/13");
		mEventDisplay.getStartTimePicker().time.setValue("03:00");
		mEventDisplay.getStartTimePicker().AMPM.setSelectedItem("PM");
		
		mEventDisplay.getEndTimePicker().date.setValue("11/20/13");
		mEventDisplay.getEndTimePicker().time.setValue("04:00");
		mEventDisplay.getEndTimePicker().AMPM.setSelectedItem("PM");
		
		assertFalse("Event is not saveable with no event name", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithProperNameButNoStartDate() {
		mEventDisplay.getEventNameField().setText("Test Event");
		
		mEventDisplay.getEndTimePicker().date.setValue("11/20/13");
		mEventDisplay.getEndTimePicker().time.setValue("04:00");
		mEventDisplay.getEndTimePicker().AMPM.setSelectedItem("PM");
		
		assertFalse("Event is not saveable with no start date", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithProperNameButNoEndDate() {
		mEventDisplay.getEventNameField().setText("Test Event");
		
		mEventDisplay.getStartTimePicker().date.setValue("11/20/13");
		mEventDisplay.getStartTimePicker().time.setValue("03:00");
		mEventDisplay.getStartTimePicker().AMPM.setSelectedItem("PM");
		
		assertFalse("Event is not saveable with no end date", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithStartDateAfterEndDate() {
		mEventDisplay.getEventNameField().setText("Test Event");
		
		mEventDisplay.getStartTimePicker().date.setValue("11/20/13");
		mEventDisplay.getStartTimePicker().time.setValue("05:00");
		mEventDisplay.getStartTimePicker().AMPM.setSelectedItem("PM");
		
		mEventDisplay.getEndTimePicker().date.setValue("11/20/13");
		mEventDisplay.getEndTimePicker().time.setValue("04:00");
		mEventDisplay.getEndTimePicker().AMPM.setSelectedItem("PM");
		
		assertFalse("Event is not saveable with start date after end date", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledOnMultidayEvent() {
		mEventDisplay.getEventNameField().setText("Test Event");
		
		mEventDisplay.getStartTimePicker().date.setValue("11/20/13");
		mEventDisplay.getStartTimePicker().time.setValue("05:00");
		mEventDisplay.getStartTimePicker().AMPM.setSelectedItem("PM");
		
		mEventDisplay.getEndTimePicker().date.setValue("11/21/13");
		mEventDisplay.getEndTimePicker().time.setValue("04:00");
		mEventDisplay.getEndTimePicker().AMPM.setSelectedItem("PM");
		
		assertFalse("Event is not saveable with multiday event", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterNameDeletion() {
		setUpAndTestValidEventFields();

		mEventDisplay.getEventNameField().setText("");
		
		assertFalse("Event is no longer saveable", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterDateDeletion() {
		setUpAndTestValidEventFields();
		
		mEventDisplay.getStartTimePicker().date.setValue("11/21/13");
		
		assertFalse("Event is no longer saveable", mEventDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithOnlySpacesForEventName() {
		setUpAndTestValidEventFields();
		
		mEventDisplay.getEventNameField().setText("    ");
		
		assertFalse("Event is no longer saveable", mEventDisplay.isSaveable());
	}
	
	private void setUpAndTestValidEventFields()
	{
		mEventDisplay.getEventNameField().setText("Test Event");
		
		mEventDisplay.getStartTimePicker().date.setValue("11/20/13");
		mEventDisplay.getStartTimePicker().time.setValue("03:00");
		mEventDisplay.getStartTimePicker().AMPM.setSelectedItem("PM");
		
		mEventDisplay.getEndTimePicker().date.setValue("11/20/13");
		mEventDisplay.getEndTimePicker().time.setValue("11:00");
		mEventDisplay.getEndTimePicker().AMPM.setSelectedItem("PM");
		
		assertTrue("Event is saveable with proper input", mEventDisplay.isSaveable());
	}
}
