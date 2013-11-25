package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

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

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEventDisplay extends JPanel
{
	private JTextField eventNameField;
	private JTextField participantField;
	private JButton saveEventButton;
	private int tabid;
	private JLabel nameErrorLabel;
	private JLabel dateErrorLabel;
	private DatePicker startTimePicker;
	private DatePicker endTimePicker;
	
	public AddEventDisplay()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		final JPanel me = this;
		JPanel NameLabelPanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) NameLabelPanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		add(NameLabelPanel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setVerticalAlignment(SwingConstants.BOTTOM);
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		NameLabelPanel.add(lblName);
		
		JPanel NamePane = new JPanel();
		FlowLayout fl_NamePane = (FlowLayout) NamePane.getLayout();
		fl_NamePane.setAlignment(FlowLayout.LEFT);
		add(NamePane);
		
		eventNameField = new JTextField();
		nameErrorLabel = new JLabel();
		eventNameField.setColumns(25);
		eventNameField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				nameErrorLabel.setVisible(!validateText(eventNameField.getText(), nameErrorLabel));
				saveEventButton.setEnabled(isSaveable());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				nameErrorLabel.setVisible(!validateText(eventNameField.getText(), nameErrorLabel));
				saveEventButton.setEnabled(isSaveable());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				//Not triggered by plaintext fields
			}
		});
		
		nameErrorLabel.setForeground(Color.RED);
		validateText(eventNameField.getText(), nameErrorLabel);
		
		NamePane.add(eventNameField);
		NamePane.add(nameErrorLabel);
		
		JPanel DateandTimeLabelPane = new JPanel();
		add(DateandTimeLabelPane);
		DateandTimeLabelPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblDateTime = new JLabel("Date and Time:");
		lblDateTime.setVerticalAlignment(SwingConstants.BOTTOM);
		lblDateTime.setHorizontalAlignment(SwingConstants.LEFT);
		DateandTimeLabelPane.add(lblDateTime);
		
		JPanel DatePickerPanel = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) DatePickerPanel.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		add(DatePickerPanel);
		endTimePicker = new DatePicker(true, null);
		startTimePicker = new DatePicker(true, endTimePicker);
		startTimePicker.addChangeListener(new DatePickerListener() {
			
			@Override
			public void datePickerUpdate(DateTime mDateTime) {
				dateErrorLabel.setVisible(!validateDate(mDateTime, endTimePicker.getDate(), dateErrorLabel));
				saveEventButton.setEnabled(isSaveable());
			}
		});
		
		endTimePicker.addChangeListener(new DatePickerListener() {
			@Override
			public void datePickerUpdate(DateTime mDateTime) {
				dateErrorLabel.setVisible(!validateDate(startTimePicker.getDate(), mDateTime, dateErrorLabel));
				saveEventButton.setEnabled(isSaveable());
			}
		});

		DatePickerPanel.add(new JLabel("From "));
		DatePickerPanel.add(startTimePicker);
		DatePickerPanel.add(new JLabel(" to "));
		DatePickerPanel.add(endTimePicker);
		JCheckBox chckbxAllDayEvent = new JCheckBox("All Day Event");
		DatePickerPanel.add(chckbxAllDayEvent);
		dateErrorLabel = new JLabel();
		dateErrorLabel.setForeground(Color.RED);
		DatePickerPanel.add(dateErrorLabel);
		
		JPanel ParticipantsLabelPane = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) ParticipantsLabelPane.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(ParticipantsLabelPane);
		
		JLabel lblParticipants = new JLabel("Participants:");
		lblParticipants.setVerticalAlignment(SwingConstants.BOTTOM);
		ParticipantsLabelPane.add(lblParticipants);
		
		JPanel ParticipantsPanel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) ParticipantsPanel.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		add(ParticipantsPanel);
		
		participantField = new JTextField();
		ParticipantsPanel.add(participantField);
		participantField.setColumns(30);
		
		JPanel DescriptionLabelPane = new JPanel();
		FlowLayout fl_DescriptionLabelPane = (FlowLayout) DescriptionLabelPane.getLayout();
		fl_DescriptionLabelPane.setAlignment(FlowLayout.LEFT);
		add(DescriptionLabelPane);
		final JCheckBox chckbxProjectEvent = new JCheckBox("Project Event");
		chckbxProjectEvent.setSelected(true);
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setVerticalAlignment(SwingConstants.BOTTOM);
		DescriptionLabelPane.add(lblDescription);
		lblDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblDescription.setHorizontalAlignment(SwingConstants.LEFT);
		
		JPanel DescriptionPanel = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) DescriptionPanel.getLayout();
		flowLayout_6.setAlignment(FlowLayout.LEFT);
		DescriptionPanel.setMinimumSize(new Dimension(100, 100));
		add(DescriptionPanel);
        Filler filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767), new java.awt.Dimension(0, 32767));
        
        add(filler1);
		
		final JTextArea Description = new JTextArea(5,35);
		Description.setLineWrap(true);
		Description.setWrapStyleWord(true);
		
		JScrollPane descriptionScrollPane = new JScrollPane(Description);
		DescriptionPanel.add(descriptionScrollPane);
		
		JPanel SubmitPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) SubmitPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(SubmitPanel);
		
		final JLabel errorText = new JLabel();
		errorText.setForeground(Color.RED);
		
		saveEventButton = new JButton("Save");
		saveEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Event e = new Event();
				MainPanel mMainPanel = MainPanel.getInstance();
				e.setName(eventNameField.getText().trim());
				e.setDescription(Description.getText());
				e.setStart(startTimePicker.getDate());
				e.setEnd(endTimePicker.getDate());
				e.setProjectEvent(chckbxProjectEvent.isSelected());
				mMainPanel.addEvent(e);
				saveEventButton.setEnabled(false);
				saveEventButton.setText("Saved!");
				mMainPanel.closeTab(tabid);
				mMainPanel.refreshView();
			}
		});
		
		saveEventButton.setHorizontalAlignment(SwingConstants.LEFT);
		SubmitPanel.add(saveEventButton);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				MainPanel.getInstance().closeTab(tabid);
			}
		});
		SubmitPanel.add(btnCancel);
		SubmitPanel.add(chckbxProjectEvent);
		SubmitPanel.add(errorText);
		
		//this should be called in updateSaveable() and thus isnt necessary here
		//but error msg didn't start visible unless I called it directly
		validateDate(startTimePicker.getDate(), endTimePicker.getDate(), dateErrorLabel);
		
		saveEventButton.setEnabled(isSaveable());
	}
	
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
		/*will be handled when parsed
		}else if(mText.matches("^.*[^a-zA-Z0-9.,()$ ].*$"))
		{
			
			mErrorLabel.setText("* Invalid Name/Characters");
		*/
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
		else if (!(mStartTime.getDayOfYear() == mEndTime.getDayOfYear() &&
			mStartTime.getYear() == mEndTime.getYear()))
		{
			mErrorLabel.setText("* Multi-day events not supported");
		}//error if the start time is after the end time
		else if (mEndTime.isBefore(mStartTime)) {
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
		return validateText(eventNameField.getText(), nameErrorLabel) && 
				validateDate(startTimePicker.getDate(), endTimePicker.getDate(), dateErrorLabel);
	}

	public JTextField getEventNameField() {
		return eventNameField;
	}

	public DatePicker getStartTimePicker() {
		return startTimePicker;
	}

	public DatePicker getEndTimePicker() {
		return endTimePicker;
	}
}
