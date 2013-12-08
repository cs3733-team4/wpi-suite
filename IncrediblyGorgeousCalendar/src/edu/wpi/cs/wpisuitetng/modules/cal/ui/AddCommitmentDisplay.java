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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.DisplayableEditorView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCommitmentDisplay extends DisplayableEditorView
{
	private int tabid;
	private Commitment commitmentToEdit;
	private boolean isEditingCommitment;

	public AddCommitmentDisplay()
	{
		super(false);
		isEditingCommitment = false;
		init(new Commitment());
	}

	public AddCommitmentDisplay(Commitment oldCommitment)
	{
		super(false);
		isEditingCommitment = true;
		commitmentToEdit = oldCommitment;
		init(commitmentToEdit);
	}

	private void init(final Commitment oldCommitment)
	{
		nameTextField.setText(oldCommitment.getName());
		startTimeDatePicker.setDateTime(oldCommitment.getDate());
		participantsTextField.setText(oldCommitment.getParticipants());
		// TODO: categories and team/personal
		descriptionTextArea.setText(oldCommitment.getDescription());

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Commitment e = oldCommitment;
				e.setName(nameTextField.getText().trim());
				e.setDescription(descriptionTextArea.getText());
				e.setDate(startTimeDatePicker.getDateTime());

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

		saveButton.setEnabled(isSaveable());
		// TODO: autofocus
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
}
