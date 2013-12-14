/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.DatePickerListener;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.UUID;

public class AddEventDisplay extends DisplayableEditorView
{
	
	private int tabid;
	private Event eventToEdit;
	private boolean isEditingEvent;
	private UUID existingEventID; // UUID of event being edited
	private DateTime currentTime;
	
	// Constructor for edit event.
	public AddEventDisplay(Event mEvent)
	{
		super(true);
		this.eventToEdit = mEvent;
		this.isEditingEvent = true;
		this.existingEventID = eventToEdit.getIdentification();
		populateEventFields(eventToEdit);
		setUpListeners();
		
	}
	
	// Constructor for create new event.
	public AddEventDisplay()
	{
		super(true);
		this.isEditingEvent = false;
		this.currentTime = new DateTime();
		setCurrentDateAndTime();
		setUpListeners();
	}
	
	/**
	 * Populates the events field if the class was invoked with an existing event.
	 * Allows for the edition of events 
	 */
	private void populateEventFields(Event eventToEdit)
	{
		this.participantsTextField.setText(eventToEdit.getParticipants());
		this.nameTextField.setText(eventToEdit.getName());
		this.descriptionTextArea.setText(eventToEdit.getDescription());
		this.rdbtnPersonal.setSelected(!eventToEdit.isProjectEvent());
		this.rdbtnTeam.setSelected(eventToEdit.isProjectEvent());
		this.startTimeDatePicker.setDateTime(eventToEdit.getStart());
		this.endTimeDatePicker.setDateTime(eventToEdit.getEnd());
		if (eventToEdit.getAssociatedCategory()!=null)
			this.eventCategoryPicker.setSelectedItem(eventToEdit.getAssociatedCategory());
		else
			this.eventCategoryPicker.setSelectedItem(Category.DEFAULT_CATEGORY);
	}
	
	/**
	 * Adds button listeners based on whether an event is being edited or created
	 */
	private void setUpListeners(){
		eventCategoryPicker.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(FocusEvent e) {
				refreshCategories();
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}	
		});
		nameTextField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e)
			{
				nameErrorLabel.setVisible(!validateText(nameTextField.getText(), nameErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				// TODO Auto-generated method stub
				
			}
		});

		nameTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				nameErrorLabel.setVisible(!validateText(nameTextField.getText(), nameErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				nameErrorLabel.setVisible(!validateText(nameTextField.getText(), nameErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				//Not triggered by plaintext fields
			}
		});

		startTimeDatePicker.addChangeListener(new DatePickerListener() {
			
			@Override
			public void datePickerUpdate(DateTime mDateTime) {
				dateErrorLabel.setVisible(!validateDate(mDateTime, endTimeDatePicker.getDateTime(), dateErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
		});
		
		endTimeDatePicker.addChangeListener(new DatePickerListener() {
			@Override
			public void datePickerUpdate(DateTime mDateTime) {
				dateErrorLabel.setVisible(!validateDate(startTimeDatePicker.getDateTime(), mDateTime, dateErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
		});
		
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
					attemptSave();
			}
		});
		
		// Cancel Button
		
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				MainPanel.getInstance().closeTab(tabid);
			}
		});
		
		//this should be called in updateSaveable() and thus isnt necessary here
		//but error msg didn't start visible unless I called it directly
		validateDate(startTimeDatePicker.getDateTime(), endTimeDatePicker.getDateTime(), dateErrorLabel);
		saveButton.setEnabled(isSaveable());
	}
	public void attemptSave()
	{
		if (!isSaveable())
			return;
		startTimeDatePicker.getDateTime();
		endTimeDatePicker.getDateTime();
		
		Event e = new Event();
		e.setName(nameTextField.getText().trim());
		e.setDescription(descriptionTextArea.getText());
		e.setStart(startTimeDatePicker.getDateTime());
		e.setEnd(endTimeDatePicker.getDateTime());
		e.setProjectEvent(rdbtnTeam.isSelected());
		e.setParticipants(participantsTextField.getText().trim());
		e.setCategory(((Category)eventCategoryPicker.getSelectedItem()).getCategoryID());
		
		if (isEditingEvent){
			e.setEventID(existingEventID);
			MainPanel.getInstance().updateEvent(e);
		} else {
			MainPanel.getInstance().addEvent(e);
		}
		
		saveButton.setEnabled(false);
		saveButton.setText("Saved!");
		MainPanel.getInstance().closeTab(tabid);
		MainPanel.getInstance().refreshView();
	}
	/**
	 * Set tab id for the created event view
	 * @param id value to set id to
	 */
	public void setTabId(int id)
	{
		tabid = id;
	}
	
	/**
	 * Validates text has been entered (used for event name)
	 * @param mText text to be validated
	 * @param mErrorLabel JLabel to display resulting error message
	 * @return true if all pass, else return true
	 */
	private boolean validateText(String mText, JLabel mErrorLabel)
	{
		if(mText==null || mText.trim().length()==0)
		{
			mErrorLabel.setText("This field is required");
			return false;
		}
		return true;
	}
	
	/**
	 * Validates that an event has valid start and end dates and times
	 * @param mStartTime starting DateTime to validate and compare
	 * @param mEndTime ending DateTime to validate and compare
	 * @param mErrorLabel text field to be updated with any error message
	 * @return true if all validation checks pass, else returns false
	 */
	private boolean validateDate(DateTime mStartTime, DateTime mEndTime, JLabel mErrorLabel)
	{
		if(mStartTime == null || mEndTime == null)
		{
			mErrorLabel.setText("That does not look like a valid date & time");
		}//if properly formatted, error if startDate is a different day than the endDate
		//error if the start time is after the end time
		else if (!mEndTime.isAfter(mStartTime)) {
			mErrorLabel.setText("Event can't start after it ends");
		}else
		{
			//no errors found
			return true;
		}
		
		//error found
		return false;
	}
	
	/**
	 * checks if all validation tests pass
	 * @return true if all pass, else return false
	 */
	public boolean isSaveable()
	{
		return validateText(nameTextField.getText(), nameErrorLabel) && 
				validateDate(startTimeDatePicker.getDateTime(), endTimeDatePicker.getDateTime(), dateErrorLabel);
	}
	
	
	/**
	 * Checks whether events match 
	 * 
	 * @param other the event to compare with the event to edit
	 * @return boolean indicating whether the events match
	 */
	public boolean matchingEvent(AddEventDisplay other)
	{
		return this.eventToEdit != null && this.eventToEdit.getEventID().equals(other.eventToEdit.getEventID());
	}
	
	
	/**
	 * Sets the default date and time text fields to the current date and time
	 * 
	 * Should be only called if creating a new event, not when editing since edit
	 * event already has a date and time to fill the text fields with
	 */
	public void setCurrentDateAndTime()
	{
		this.startTimeDatePicker.setDateTime(currentTime);
		this.endTimeDatePicker.setDateTime(currentTime.plusHours(1));
	}
	
	public boolean editingEvent()
	{
		return this.isEditingEvent;
	}
	public void refreshCategories()
	{
		this.eventCategoryPicker.removeAllItems();
		this.eventCategoryPicker.addItem(Category.DEFAULT_CATEGORY);
		for (Category c : CategoryModel.getInstance().getAllCategories())
		{
			this.eventCategoryPicker.addItem(c);
		}
	}
}
