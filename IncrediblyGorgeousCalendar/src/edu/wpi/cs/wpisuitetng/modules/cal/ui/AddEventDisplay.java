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

import javax.swing.Box.Filler;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.UUID;

public class AddEventDisplay extends JPanel
{
	
	private int tabid;
	private JPanel nameLabelPanel;
	private JPanel dateAndTimeLabelPane;
	private JPanel dateAndTimePickerPane;
	private JPanel participantsLabelPanel;
	private JPanel participantsTextFieldPanel;
	private JPanel descriptionTextFieldPanel;
	private JPanel descriptionLabelPanel;
	private JPanel submissionPanel;
	private JLabel nameLabel;
	private JLabel dateAndTimeLabel;
	private JLabel participantsLabel;
	private JLabel descriptionLabel;
	private JPanel nameTextFieldPanel;
	private JTextField nameTextField = new JTextField();
	private JTextField participantsTextField = new JTextField();
	private JTextArea descriptionTextArea = new JTextArea(5,35);
	private DatePicker endTimeDatePicker = new DatePicker(true, null);
	private DatePicker startTimeDatePicker = new DatePicker(true, endTimeDatePicker);
	private JCheckBox teamProjectCheckBox = new JCheckBox("Project Event");
	private JLabel errorText;
	private JLabel nameErrorLabel;
	private JLabel dateErrorLabel;
	private JButton saveButton;
	private JButton cancelButton;
	private Event eventToEdit;
	private boolean isEditingEvent;
	private UUID existingEventID; // UUID of event being edited
	private JComboBox<Category> eventCategoryPicker;
	private DateTime currentTime;
	
	
	// Constructor for edit event.
	public AddEventDisplay(Event mEvent)
	{
		this.eventCategoryPicker = new JComboBox<Category>();
		this.eventToEdit = mEvent;
		this.isEditingEvent = true;
		this.existingEventID = eventToEdit.getEventID();
		setUpUI();
		populateEventFields(eventToEdit);
		setUpListeners();
		
	}
	
	// Constructor for create new event.
	public AddEventDisplay()
	{
		this.eventCategoryPicker = new JComboBox<Category>();
		this.isEditingEvent = false;
		this.currentTime = new DateTime();
		setUpUI();
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
		this.teamProjectCheckBox.setSelected(eventToEdit.isProjectEvent());
		this.startTimeDatePicker.setDateTime(eventToEdit.getStart());
		this.endTimeDatePicker.setDateTime(eventToEdit.getEnd());
		if (eventToEdit.getAssociatedCategory()!=null)
			this.eventCategoryPicker.setSelectedItem(eventToEdit.getAssociatedCategory());
		else
			this.eventCategoryPicker.setSelectedItem(Category.DEFUALT_CATEGORY);
	}
	
	/**
	 * Set up the UI layout for AddEventDisplay
	 */
	private void setUpUI()
	{
		
		// set up the combobox
		this.eventCategoryPicker.addItem(Category.DEFUALT_CATEGORY);
		for(Category c : CategoryModel.getInstance().getAllCategories())
		{
			this.eventCategoryPicker.addItem(c);
		}
		
		
		// Basic Layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		/** Name  Panel */
		
		// Panel
		this.nameLabelPanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) nameLabelPanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		
		// Label
		this.nameLabel = new JLabel("Name:");
		nameLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		nameLabelPanel.add(nameLabel);
		
		// Add panel to UI
		this.add(nameLabelPanel);
		
		/** Name text field */
		
		// Panel
		this.nameTextFieldPanel = new JPanel();
		FlowLayout fl_NamePane = (FlowLayout) nameTextFieldPanel.getLayout();
		fl_NamePane.setAlignment(FlowLayout.LEFT);
		
		nameErrorLabel = new JLabel();
		nameTextField.setColumns(25);
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
		// Text Field
		nameTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		nameTextField.setColumns(25);
		nameTextFieldPanel.add(nameTextField);
		
		nameErrorLabel.setForeground(Color.RED);
		validateText(nameTextField.getText(), nameErrorLabel);

		nameLabelPanel.add(nameTextField);
		nameLabelPanel.add(nameErrorLabel);
		
		JPanel DateandTimeLabelPane = new JPanel();
		add(DateandTimeLabelPane);
		DateandTimeLabelPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		/** Date and Time Panel */
		
		// Panel
		this.dateAndTimeLabelPane = new JPanel();
		dateAndTimeLabelPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		// Label
		
		this.dateAndTimeLabel = new JLabel("Date and Time:");
		dateAndTimeLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		dateAndTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		dateAndTimeLabelPane.add(dateAndTimeLabel);
		
		// Add panel to UI
		this.add(dateAndTimeLabelPane);
		
		/** Date and Time Picker */
		
		// Panel
		this.dateAndTimePickerPane = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) dateAndTimePickerPane.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		this.add(dateAndTimePickerPane);
		startTimeDatePicker.addChangeListener(new DatePickerListener() {
			
			@Override
			public void datePickerUpdate(DateTime mDateTime) {
				dateErrorLabel.setVisible(!validateDate(mDateTime, endTimeDatePicker.getDate(), dateErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
		});
		
		endTimeDatePicker.addChangeListener(new DatePickerListener() {
			@Override
			public void datePickerUpdate(DateTime mDateTime) {
				dateErrorLabel.setVisible(!validateDate(startTimeDatePicker.getDate(), mDateTime, dateErrorLabel));
				saveButton.setEnabled(isSaveable());
			}
		});

		dateAndTimePickerPane.add(new JLabel("From "));
		dateAndTimePickerPane.add(startTimeDatePicker);
		dateAndTimePickerPane.add(new JLabel(" to "));
		dateAndTimePickerPane.add(endTimeDatePicker);
		JCheckBox chckbxAllDayEvent = new JCheckBox("All Day Event");
		//dateAndTimePickerPane.add(chckbxAllDayEvent);
		dateErrorLabel = new JLabel();
		dateErrorLabel.setForeground(Color.RED);
		dateAndTimePickerPane.add(dateErrorLabel);
		
		// Add panel to UI
		this.add(dateAndTimePickerPane);
		
		/** Participants Panel */
		
		// Panel
		this.participantsLabelPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) participantsLabelPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		
		// Label
		this.participantsLabel = new JLabel("Participants:");
		participantsLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		participantsLabelPanel.add(participantsLabel);
		
		// Add panel to UI
		this.add(participantsLabelPanel);
		
		/** Participants Text Field */
		
		// Panel
		participantsTextFieldPanel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) participantsTextFieldPanel.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		
		// Text Field
		participantsTextFieldPanel.add(participantsTextField);
		participantsTextField.setColumns(30);
		
		// Add panel to UI
		this.add(participantsTextFieldPanel);
		
		/** Categories ui */
		
		JPanel comboHolder = new JPanel();
		FlowLayout fl_DescriptionLabelPane = (FlowLayout) comboHolder.getLayout();
		fl_DescriptionLabelPane.setAlignment(FlowLayout.LEFT);
		
		JLabel ljtest = new JLabel("Category:");
		ljtest.setVerticalAlignment(SwingConstants.BOTTOM);
		ljtest.setAlignmentX(Component.CENTER_ALIGNMENT);
		ljtest.setHorizontalAlignment(SwingConstants.LEFT);
		comboHolder.add(ljtest);
		add(comboHolder);
		
		//add the combobox for category
		comboHolder = new JPanel();
		fl_DescriptionLabelPane = (FlowLayout) comboHolder.getLayout();
		fl_DescriptionLabelPane.setAlignment(FlowLayout.LEFT);
		comboHolder.add(eventCategoryPicker);
		add(comboHolder);
		
		/** Description Panel */
		
		// Panel
		
		this.descriptionLabelPanel = new JPanel();
		fl_DescriptionLabelPane = (FlowLayout) descriptionLabelPanel.getLayout();
		fl_DescriptionLabelPane.setAlignment(FlowLayout.LEFT);
		
		// Label
		descriptionLabel = new JLabel("Description:");
		descriptionLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);
		descriptionLabelPanel.add(descriptionLabel);
		
		// Add panel to UI
		this.add(descriptionLabelPanel);
		
		/** Description Text Field */
		
		// Panel
		this.descriptionTextFieldPanel = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) descriptionTextFieldPanel.getLayout();
		flowLayout_6.setAlignment(FlowLayout.LEFT);
		descriptionTextFieldPanel.setMinimumSize(new Dimension(100, 100));
		
		// Text Area
		descriptionTextArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		
		JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
		descriptionScrollPane.setBorder(nameTextField.getBorder());
		descriptionTextFieldPanel.add(descriptionScrollPane);
		
		// Add panel to UI
		this.add(descriptionTextFieldPanel);
		
		// Format UI
		Filler filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767), new java.awt.Dimension(0, 32767));
        this.add(filler1);
		
        /** Submission panel */
       
        // Panel
		this.submissionPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) submissionPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		// CheckBox
		teamProjectCheckBox.setSelected(true);
		
		// Error label
		this.errorText = new JLabel();
		errorText.setForeground(Color.RED);
		
		// Save button
		this.saveButton = new JButton("Save");
		saveButton.setHorizontalAlignment(SwingConstants.LEFT);
		submissionPanel.add(saveButton);
		
		// Cancel Button
		this.cancelButton = new JButton("Cancel");
		submissionPanel.add(cancelButton);
		
		// Add checkbox and error text to panel
		submissionPanel.add(teamProjectCheckBox);
		submissionPanel.add(errorText);
		
		// Add panel to UI
		this.add(submissionPanel);
		
		saveButton.setEnabled(isSaveable());
	}
	
	/**
	 * Adds button listeners based on whether an event is being edited or created
	 */
	private void setUpListeners(){
		
		// Save Button
		
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

				startTimeDatePicker.getDate();
					endTimeDatePicker.getDate();
					errorText.setVisible(true);
			
					errorText.setVisible(false);
					Event e = new Event();
					e.setName(nameTextField.getText().trim());
					e.setDescription(descriptionTextArea.getText());
					e.setStart(startTimeDatePicker.getDate());
					e.setEnd(endTimeDatePicker.getDate());
					e.setProjectEvent(teamProjectCheckBox.isSelected());
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
				
				saveButton.setEnabled(false);
				saveButton.setText("Saved!");
				MainPanel.getInstance().closeTab(tabid);
				MainPanel.getInstance().refreshView();
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
		validateDate(startTimeDatePicker.getDate(), endTimeDatePicker.getDate(), dateErrorLabel);
		
		saveButton.setEnabled(isSaveable());
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
			mErrorLabel.setText("* Required Field");
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
			mErrorLabel.setText("* Invalid Date/Time");
		}//if properly formatted, error if startDate is a different day than the endDate
		//error if the start time is after the end time
		else if (!mEndTime.isAfter(mStartTime)) {
			mErrorLabel.setText("* Event has invalid duration");
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
				validateDate(startTimeDatePicker.getDate(), endTimeDatePicker.getDate(), dateErrorLabel);
	}
	
	
	/**
	 * Checks whether events match 
	 * 
	 * @param other the event to compare with the event to edit
	 * @return boolean indicating whether the events match
	 */
	public boolean matchingEvent(AddEventDisplay other)
	{
		return this.eventToEdit != null && this.eventToEdit.equals(other.eventToEdit);
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
}
