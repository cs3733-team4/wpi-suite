/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.DisplayableEditorView;

public class AddCommitmentDisplay extends DisplayableEditorView
{
	private int tabid;
	private Commitment commitmentToEdit;
	private boolean isEditingCommitment;
	private DateTime currentTime;

	// For a new commitment.
	public AddCommitmentDisplay()
	{
		super(false);
		isEditingCommitment = false;
		currentTime = new DateTime();
		populateCommitmentFields(new Commitment());
		setCurrentDateAndTime();
		setUpListeners();
	}

	// For editing a commitment.
	public AddCommitmentDisplay(Commitment mCommitment)
	{
		super(false);
		isEditingCommitment = true;
		commitmentToEdit = mCommitment;
		populateCommitmentFields(commitmentToEdit);
		setUpListeners();
	}
	
	private void populateCommitmentFields(Commitment mCommitment)
	{
		nameTextField.setText(mCommitment.getName());
		startTimeDatePicker.setDateTime(mCommitment.getDate());
		participantsTextField.setText(mCommitment.getParticipants());
		// TODO: categories and team/personal
		System.out.println("Project Commitment: " + mCommitment.isProjectCommitment());
		this.rdbtnPersonal.setSelected(!mCommitment.isProjectCommitment());
		this.rdbtnTeam.setSelected(mCommitment.isProjectCommitment());
		descriptionTextArea.setText(mCommitment.getDescription());
		saveButton.setEnabled(isSaveable());
	}
	
	private void setUpListeners()
	{
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainPanel.getInstance().closeTab(tabid);
			}
		});

		nameTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				nameErrorLabel.setVisible(!validateText(nameTextField.getText(), nameErrorLabel));
				saveButton.setEnabled(isSaveable());
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				nameErrorLabel.setVisible(!validateText(nameTextField.getText(), nameErrorLabel));
				saveButton.setEnabled(isSaveable());
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				// Not triggered by plaintext fields
			}
		});
		
		startTimeDatePicker.addChangeListener(new DatePickerListener() {

			@Override
			public void datePickerUpdate(DateTime mDateTime)
			{
				dateErrorLabel.setVisible(!validateDate(startTimeDatePicker.getDateTime(), dateErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
		});
		
		saveButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0){
				Commitment e = commitmentToEdit;
				e.setName(nameTextField.getText().trim());
				e.setDescription(descriptionTextArea.getText());
				e.setDate(startTimeDatePicker.getDateTime());
				e.setProjectCommitment(rdbtnTeam.isSelected());
				e.setParticipants(participantsTextField.getText().trim());
				e.setCategory(((Category)eventCategoryPicker.getSelectedItem()).getCategoryID());

				if (isEditingCommitment)
					MainPanel.getInstance().updateCommitment(e);
				else
					MainPanel.getInstance().addCommitment(e);

				saveButton.setEnabled(false);
				saveButton.setText("Saved!");
				MainPanel.getInstance().closeTab(tabid);
				MainPanel.getInstance().refreshView();
			}
		});
	}

	public boolean isSaveable()
	{
		return validateText(nameTextField.getText(), nameErrorLabel) && validateDate(startTimeDatePicker.getDateTime(), dateErrorLabel);
	}

	/**
	 * 
	 * @param dueDate
	 *            first DatePicker to validate and compare
	 * @param mEndTime
	 *            second DatePicker to validate and compare
	 * @param mErrorLabel
	 *            text field to be updated with any error message
	 * @return true if all validation checks pass, else returns false
	 */
	private boolean validateDate(DateTime dueDate, JLabel mErrorLabel)
	{
		if (dueDate == null)// Make sure that a date has been selected
		{
			mErrorLabel.setText("That does not look like a valid date & time");
			return false;
		}
		// no errors found
		return true;

	}

	/**
	 * 
	 * @param mText
	 *            text to be validated
	 * @param mErrorLabel
	 *            JLabel to display resulting error message
	 * @return true if all pass, else return true
	 */
	private boolean validateText(String mText, JLabel mErrorLabel)
	{
		if (mText == null || mText.trim().length() == 0)
		{
			mErrorLabel.setText("This field is required");
			return false;

		}
		return true;
	}

	public void setTabId(int id)
	{
		tabid = id;
	}

	public boolean matchingCommitment(AddCommitmentDisplay other)
	{
		return this.commitmentToEdit != null && this.commitmentToEdit.equals(other.commitmentToEdit);
	}
	
	/**
	 * Sets the default date and time text fields to the current date and time
	 * 
	 * Should be only called if creating a new commitment, not when editing since edit
	 * event already has a date and time to fill the text fields with
	 */
	public void setCurrentDateAndTime()
	{
		this.startTimeDatePicker.setDateTime(currentTime);
	}
}
