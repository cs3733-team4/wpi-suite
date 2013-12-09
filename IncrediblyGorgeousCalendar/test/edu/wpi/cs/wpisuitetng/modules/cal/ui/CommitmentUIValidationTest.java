package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.text.ParseException;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.DatePicker;

public class CommitmentUIValidationTest {
	private AddCommitmentDisplay mCommitDisplay = new AddCommitmentDisplay();
	
	@Test
	public void testIfCommitmentDisplayIsNonNull() {
		assertNotNull("Make sure it actually runs", mCommitDisplay);
	}
	
	@Test
	public void testIfSaveButtonStartsDisabled() {
		assertFalse("Commitment is not saveable by default", mCommitDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsEnabledWithProperInput() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setUpAndTestValidCommitmentFields();
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithProperDateButNoName() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Field f= mCommitDisplay.getClass().getDeclaredField("commitTimePicker");
		f.setAccessible(true);
			
		((DatePicker) f.get(mCommitDisplay)).date.setValue("11/20/13");
		//TODO: Uncomment these after commitment times are implemented
		//((DatePicker) f.get(mCommitDisplay)).time.setValue("03:00");
		//((DatePicker) f.get(mCommitDisplay)).AMPM.setSelectedItem("PM");
		
		assertFalse("Commitment is not saveable with no Commitment name", mCommitDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithProperNameButNoDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Field fff= mCommitDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mCommitDisplay)).setText("Test Commitment");
		
		assertTrue("Commitment date and time is set automatically", mCommitDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterNameDeletion() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setUpAndTestValidCommitmentFields();

		Field fff= mCommitDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mCommitDisplay)).setText("");
		
		assertFalse("Commitment is no longer saveable", mCommitDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterDateDeletion() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setUpAndTestValidCommitmentFields();
		
		Field f= mCommitDisplay.getClass().getDeclaredField("commitTimePicker");
		f.setAccessible(true);
		
		((DatePicker) f.get(mCommitDisplay)).date.setValue("");
		
		assertFalse("Commitment is no longer saveable", mCommitDisplay.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithOnlySpacesForCommitmentName() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setUpAndTestValidCommitmentFields();
		
		Field fff= mCommitDisplay.getClass().getDeclaredField("nameTextField");
		fff.setAccessible(true);
		
		((JTextField) fff.get(mCommitDisplay)).setText("    ");
		
		assertFalse("Commitment is no longer saveable", mCommitDisplay.isSaveable());
	}
	
	private void setUpAndTestValidCommitmentFields() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		
		Field f= mCommitDisplay.getClass().getDeclaredField("commitTimePicker");
		f.setAccessible(true);
				
		Field ff = mCommitDisplay.getClass().getDeclaredField("nameTextField");
		ff.setAccessible(true);
		
		((JTextField) ff.get(mCommitDisplay)).setText("Test Commitment");
		
		((DatePicker) f.get(mCommitDisplay)).date.setValue("11/20/13");
		//TODO: Once times are added, uncomment to test
		//((DatePicker) f.get(mCommitDisplay)).time.setValue("03:00");
		//((DatePicker) f.get(mCommitDisplay)).AMPM.setSelectedItem("PM");
				
		assertTrue("Commitment is saveable with proper input", mCommitDisplay.isSaveable());
	}
	
	@Test
	public void testAutoFillDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		DateTime today = new DateTime();
		
		AddCommitmentDisplay mCommitDisplay = new AddCommitmentDisplay();
		
		Field f= mCommitDisplay.getClass().getDeclaredField("commitTimePicker");
		f.setAccessible(true);
				
		Field ff = mCommitDisplay.getClass().getDeclaredField("nameTextField");
		ff.setAccessible(true);
		
		((JTextField) ff.get(mCommitDisplay)).setText("Test Commitment");
		
		
		assertEquals(((DatePicker)f.get(mCommitDisplay)).getDate().getMonthOfYear(), today.getMonthOfYear());
		assertEquals(((DatePicker)f.get(mCommitDisplay)).getDate().getDayOfYear(), today.getDayOfYear());
		assertEquals(((DatePicker)f.get(mCommitDisplay)).getDate().getYear(), today.getYear());
		assertTrue("Commitment is saveable with proper input", mCommitDisplay.isSaveable());
	}

}
