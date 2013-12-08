package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import static org.junit.Assert.*;

import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.ReflectUtils;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.DatePicker;

public class CommitmentUIValidationTest
{
	private AddCommitmentDisplay mCommitDisplay = new AddCommitmentDisplay();

	DatePicker ff;

	JTextField fff;

	@Before
	public void setup() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
	{
		ff = ReflectUtils.getFieldValue(mCommitDisplay, "startTimeDatePicker");
		fff = ReflectUtils.getFieldValue(mCommitDisplay, "nameTextField");
	}

	@Test
	public void testIfCommitmentDisplayIsNonNull()
	{
		assertNotNull("Make sure it actually runs", mCommitDisplay);
	}

	@Test
	public void testIfSaveButtonStartsDisabled()
	{
		assertFalse("Commitment is not saveable by default", mCommitDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsEnabledWithProperInput() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		setUpAndTestValidCommitmentFields();
	}

	@Test
	public void testIfSaveButtonIsDisabledWithProperDateButNoName() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		ff.date.setValue("11/20/13");
		ff.time.setValue("03:00");
		ff.AMPM.setSelectedItem("PM");
		assertFalse("Commitment is not saveable with no Commitment name", mCommitDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsDisabledWithProperNameButNoDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		fff.setText("Test Commitment");
		assertFalse("Commitment is not saveable with no date and time", mCommitDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterNameDeletion() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		setUpAndTestValidCommitmentFields();
		fff.setText("");
		assertFalse("Commitment is no longer saveable", mCommitDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterDateDeletion() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		setUpAndTestValidCommitmentFields();
		ff.date.setValue("");
		assertFalse("Commitment is no longer saveable", mCommitDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsDisabledWithOnlySpacesForCommitmentName() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		setUpAndTestValidCommitmentFields();
		fff.setText("    ");
		assertFalse("Commitment is no longer saveable", mCommitDisplay.isSaveable());
	}

	private void setUpAndTestValidCommitmentFields() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		fff.setText("Test Commitment");
		ff.date.setValue("11/20/13");
		ff.time.setValue("03:00");
		ff.AMPM.setSelectedItem("PM");
		assertTrue("Commitment is saveable with proper input", mCommitDisplay.isSaveable());
	}
}
