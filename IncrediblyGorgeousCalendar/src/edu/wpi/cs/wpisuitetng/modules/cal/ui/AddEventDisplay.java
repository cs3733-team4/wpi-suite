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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private boolean editEvent;
	private UUID existingEventID; // UUID of event being edited
	
	
	// Constructor for edit event.
	public AddEventDisplay(Event mEvent){
		
		this.eventToEdit = mEvent;
		this.editEvent = true;
		this.existingEventID = eventToEdit.getEventID();
		setUpUI();
		populateEventFields(eventToEdit);
		setUpListeners();
	}
	
	// Constructor for create new event.
	public AddEventDisplay()
	{
		this.editEvent = false;
		setUpUI();
		setUpListeners();
		
	}
	
	/**
	 * Populates the events field if the class was invoked with an existing event.
	 * Allows for the edition of events 
	 */
	private void populateEventFields(Event eventToEdit){
		
		this.participantsTextField.setText(eventToEdit.getParticipants());
		this.nameTextField.setText(eventToEdit.getName());
		this.descriptionTextArea.setText(eventToEdit.getDescription());
		this.teamProjectCheckBox.setSelected(eventToEdit.isProjectEvent());
		this.startTimeDatePicker.setDateTime(eventToEdit.getStart());
		this.endTimeDatePicker.setDateTime(eventToEdit.getEnd());
		
	}
	
	/**
	 * Set up the UI layout for AddEventDisplay
	 */
	private void setUpUI(){
		
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
		nameTextField.setBorder( new BevelBorder(BevelBorder.LOWERED));
		nameTextFieldPanel.add(nameTextField);
		
		nameErrorLabel.setForeground(Color.RED);
		validateText(nameTextField.getText(), nameErrorLabel);

		nameLabelPanel.add(nameErrorLabel);
		nameLabelPanel.add(nameTextField);
		
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
		dateAndTimePickerPane.add(chckbxAllDayEvent);
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
		participantsTextField.setBorder( new BevelBorder(BevelBorder.LOWERED));
		participantsTextField.setColumns(30);
		
		// Add panel to UI
		this.add(participantsTextFieldPanel);
		
		/** Description Panel */
		
		// Panel
		
		this.descriptionLabelPanel = new JPanel();
		FlowLayout fl_DescriptionLabelPane = (FlowLayout) descriptionLabelPanel.getLayout();
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
		descriptionTextArea.setBorder( new BevelBorder(BevelBorder.LOWERED));
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		
		JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
		descriptionScrollPane.setBorder(null);
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
				try
				{
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
						
						if (editEvent){
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
				catch (IllegalArgumentException exception)
				{
					errorText.setText("* Invalid Date/Time");
					errorText.setVisible(true);
				}
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
	 * 
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
	 * 
	 * @param mStartTime first DatePicker to validate and compare
	 * @param mEndTime second DatePicker to validate and compare
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

	public JTextField getEventNameField() {
		return nameTextField;
	}

	public DatePicker getStartTimePicker() {
		return startTimeDatePicker;
	}

	public DatePicker getEndTimePicker() {
		return endTimeDatePicker;
	}
	
	public boolean matchingEvent(AddEventDisplay other)
	{
		return this.eventToEdit != null && this.eventToEdit.equals(other.eventToEdit);
	}
}
