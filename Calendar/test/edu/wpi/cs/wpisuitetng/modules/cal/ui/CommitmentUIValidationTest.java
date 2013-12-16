package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import static org.junit.Assert.*;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.ReflectUtils;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentStatus;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.DatePicker;

public class CommitmentUIValidationTest
{
	private AddCommitmentDisplay mCommitDisplay = new AddCommitmentDisplay();

	DatePicker dateBox;

	JTextField nameBox;
	
	JComboBox<String> statusPick;

	@Before
	public void setup() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
	{
		dateBox = ReflectUtils.getFieldValue(mCommitDisplay, "startTimeDatePicker");
		nameBox = ReflectUtils.getFieldValue(mCommitDisplay, "nameTextField");
		statusPick = ReflectUtils.getFieldValue(mCommitDisplay, "commitmentStatusPicker");
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
		dateBox.date.setValue("11/20/13");
		dateBox.time.setValue("03:00");
		dateBox.AMPM.setSelectedItem("PM");
		assertFalse("Commitment is not saveable with no Commitment name", mCommitDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsDisabledWithProperNameButNoDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		nameBox.setText("Test Commitment");

		assertTrue("Commitment date and time is set automatically", mCommitDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterNameDeletion() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		setUpAndTestValidCommitmentFields();
		nameBox.setText("");
		assertFalse("Commitment is no longer saveable", mCommitDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsNoLongerEnabledAfterDateDeletion() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		setUpAndTestValidCommitmentFields();
		dateBox.date.setValue("");
		assertFalse("Commitment is no longer saveable", mCommitDisplay.isSaveable());
	}

	@Test
	public void testIfSaveButtonIsDisabledWithOnlySpacesForCommitmentName() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		setUpAndTestValidCommitmentFields();
		nameBox.setText("    ");
		assertFalse("Commitment is no longer saveable", mCommitDisplay.isSaveable());
	}

	@Test
	public void testIfCommitmentCantBeSavedWithNondefaultStatus() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException
	{
		setUpAndTestValidCommitmentFields();
		statusPick.setSelectedItem(CommitmentStatus.InProgress.toString());
		assertTrue("Commitment is saveable with a new status", mCommitDisplay.isSaveable());
		
		statusPick.setSelectedItem(CommitmentStatus.Complete.toString());
		assertTrue("Commitment is saveable with a new status", mCommitDisplay.isSaveable());
		
		statusPick.setSelectedItem(CommitmentStatus.NotStarted.toString());
		assertTrue("Commitment is saveable with the default status (Not started)", mCommitDisplay.isSaveable());
	}
	
	private void setUpAndTestValidCommitmentFields() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		nameBox.setText("Test Commitment");
		dateBox.date.setValue("11/20/13");
		dateBox.time.setValue("03:00");
		dateBox.AMPM.setSelectedItem("PM");
		assertTrue("Commitment is saveable with proper input", mCommitDisplay.isSaveable());
	}

	@Test
	public void testAutoFillDate() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		DateTime today = new DateTime();

		nameBox.setText("Test Commitment");

		assertEquals(dateBox.getDate().getMonthOfYear(), today.getMonthOfYear());
		assertEquals(dateBox.getDate().getDayOfYear(), today.getDayOfYear());
		assertEquals(dateBox.getDate().getYear(), today.getYear());
		assertTrue("Commitment is saveable with proper input", mCommitDisplay.isSaveable());
	}
}
