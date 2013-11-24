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

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEventDisplay extends JPanel
{
	private JTextField Name;
	private JTextField Participants;
	private JButton saveEventButton;
	private int tabid;
	private JLabel nameErrorLabel;
	private JLabel dateErrorLabel;
	private JLabel descriptionErrorLabel;
	private DatePicker startTime;
	private DatePicker endTime;
	
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
		
		Name = new JTextField();
		nameErrorLabel = new JLabel();
		Name.setFont(new Font("Tahoma", Font.PLAIN, 13));
		Name.setColumns(25);
		Name.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				nameErrorLabel.setVisible(!validateText(Name.getText(), nameErrorLabel));
				saveEventButton.setEnabled(isSaveable());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				nameErrorLabel.setVisible(!validateText(Name.getText(), nameErrorLabel));
				saveEventButton.setEnabled(isSaveable());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				//Not triggered by plaintext fields
			}
		});
		
		nameErrorLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		nameErrorLabel.setForeground(Color.RED);
		validateText(Name.getText(), nameErrorLabel);
		
		NamePane.add(Name);
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
		endTime = new DatePicker(true, null);
		startTime = new DatePicker(true, endTime);
		
		startTime.time.getDocument().addDocumentListener(dateValidationDocListener);
		startTime.date.getDocument().addDocumentListener(dateValidationDocListener);
		startTime.AMPM.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dateErrorLabel.setVisible(!validateDate(startTime, endTime, dateErrorLabel));
				saveEventButton.setEnabled(isSaveable());
			}
		});
		
		endTime.time.getDocument().addDocumentListener(dateValidationDocListener);
		endTime.date.getDocument().addDocumentListener(dateValidationDocListener);
		endTime.AMPM.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dateErrorLabel.setVisible(!validateDate(startTime, endTime, dateErrorLabel));
				saveEventButton.setEnabled(isSaveable());
			}
		});

		DatePickerPanel.add(new JLabel("From "));
		DatePickerPanel.add(startTime);
		DatePickerPanel.add(new JLabel(" To "));
		DatePickerPanel.add(endTime);
		JCheckBox chckbxAllDayEvent = new JCheckBox("All Day Event");
		DatePickerPanel.add(chckbxAllDayEvent);
		dateErrorLabel = new JLabel();
		dateErrorLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
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
		
		Participants = new JTextField();
		ParticipantsPanel.add(Participants);
		Participants.setColumns(30);
		
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
		Description.setFont(new Font("Tahoma", Font.PLAIN, 13));
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
		errorText.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 13));
		
		saveEventButton = new JButton("Save");
		saveEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Event e = new Event();
				e.setName(Name.getText().trim());
				e.setDescription(Description.getText());
				e.setStart(startTime.getDate());
				e.setEnd(endTime.getDate());
				e.setProjectEvent(chckbxProjectEvent.isSelected());
				MainPanel.getInstance().addEvent(e);
				saveEventButton.setEnabled(false);
				saveEventButton.setText("Saved!");
				MainPanel.getInstance().closeTab(tabid);
				MainPanel.getInstance().refreshView();
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
		validateDate(startTime, endTime, dateErrorLabel);
		
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
	
	private boolean validateDate(DatePicker mStartTime, DatePicker mEndTime, JLabel mErrorLabel)
	{
		try
		{
			mStartTime.getDate();
			mEndTime.getDate();
			
			if (!(mStartTime.getDate().getDayOfYear() == mEndTime.getDate().getDayOfYear() &&
				mStartTime.getDate().getYear() == mEndTime.getDate().getYear()))
			{
				mErrorLabel.setText("* Multi-day events not supported");
			}
			else if (mStartTime.getDate().isAfter(mEndTime.getDate())) {
				mErrorLabel.setText("* Event has invalid duration");
			}else
			{
				return true;
			}
		}
		catch (IllegalArgumentException exception)
		{
			mErrorLabel.setText("* Invalid Date/Time");
		}
		return false;
	}
	
	/**
	 * checks if all validation tests pass
	 * @return true if all pass, else return true
	 */
	private boolean isSaveable()
	{
		return validateText(Name.getText(), nameErrorLabel) && validateDate(startTime, endTime, dateErrorLabel);
	}
	
	private DocumentListener dateValidationDocListener = new DocumentListener() {
		
		@Override
		public void removeUpdate(DocumentEvent e) {
			dateErrorLabel.setVisible(!validateDate(startTime, endTime, dateErrorLabel));
			saveEventButton.setEnabled(isSaveable());
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			dateErrorLabel.setVisible(!validateDate(startTime, endTime, dateErrorLabel));
			saveEventButton.setEnabled(isSaveable());
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			dateErrorLabel.setVisible(!validateDate(startTime, endTime, dateErrorLabel));
			saveEventButton.setEnabled(isSaveable());
		}
	};
}
