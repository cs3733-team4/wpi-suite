package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import javax.swing.Box.Filler;
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
	private JLabel partiipantsLabel;
	private JLabel descriptionLabel;
	private JPanel nameTextFieldPanel;
	private JTextField nameTextField;
	private JTextField participantsTextField;
	private JTextArea descriptionTextArea;
	private DatePicker startTime;
	private DatePicker endTime;
	private JCheckBox teamProjectCheckBox;
	private JLabel errorText;
	private JButton saveButton;
	private JButton cancelButton;
	private Event eventToEdit;
	
	
	// Constructor for edit event.
	public AddEventDisplay(Event mEvent){
		
		this.eventToEdit = mEvent;
		setUpUI();
		populateEventFields(eventToEdit);
		setUpListenersEdit();
	}
	
	// Constructor for create new event.
	public AddEventDisplay()
	{
		setUpUI();
		setUpListenersAdd();
		
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
		
		// Text Field
		
		this.nameTextField = new JTextField();
		nameTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		nameTextField.setColumns(25);
		nameTextFieldPanel.add(nameTextField);
		
		// Add panel to UI
		this.add(nameTextFieldPanel);
		
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
		
		// Picker
		this.endTime = new DatePicker(true, null);
		this.startTime = new DatePicker(true, endTime);
		dateAndTimePickerPane.add(new JLabel("From "));
		dateAndTimePickerPane.add(startTime);
		dateAndTimePickerPane.add(new JLabel(" To "));
		dateAndTimePickerPane.add(endTime);
		
		//JCheckBox chckbxAllDayEvent = new JCheckBox("All Day Event");
		//DatePickerPanel.add(chckbxAllDayEvent);
		
		// Add panel to UI
		this.add(dateAndTimePickerPane);
		
		/** Participants Panel */
		
		// Panel
		this.participantsLabelPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) participantsLabelPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		
		// Label
		this.partiipantsLabel = new JLabel("Participants:");
		partiipantsLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		participantsLabelPanel.add(partiipantsLabel);
		
		// Add panel to UI
		this.add(participantsLabelPanel);
		
		/** Participants Text Field */
		
		// Panel
		participantsTextFieldPanel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) participantsTextFieldPanel.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		
		// Text Field
		this.participantsTextField = new JTextField();
		participantsTextFieldPanel.add(participantsTextField);
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
		this.descriptionTextArea = new JTextArea(5,35);
		descriptionTextArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		
		JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
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
		teamProjectCheckBox = new JCheckBox("Project Event");
		teamProjectCheckBox.setSelected(true);
		
		// Error label
		this.errorText = new JLabel();
		errorText.setForeground(Color.RED);
		errorText.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 13));
		
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
		
		//TODO start date, end date, start time, end time
		
	}
	
	/**
	 * Adds button listeners for creating new events
	 */
	private void setUpListenersAdd(){
		
		// Save Button
		
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					startTime.getDate();
					endTime.getDate();
					errorText.setVisible(true);
					
					if (nameTextField.getText() == null || nameTextField.getText().trim().length() == 0)
					{
						errorText.setText("* Please enter an event title");
					}
					else if (!(startTime.getDate().getDayOfYear() == endTime.getDate().getDayOfYear() &&
						startTime.getDate().getYear() == endTime.getDate().getYear()))
					{
						errorText.setText("* Event must start and end on the same date");
					}
					else if (startTime.getDate().isAfter(endTime.getDate())) {
						errorText.setText("* Event start date must be before end date");
					}
					else
					{
						errorText.setVisible(false);
						Event e = new Event();
						e.setName(nameTextField.getText().trim());
						e.setDescription(descriptionTextArea.getText());
						e.setStart(startTime.getDate());
						e.setEnd(endTime.getDate());
						e.setProjectEvent(teamProjectCheckBox.isSelected());
						e.setParticipants(participantsTextField.getText().trim());
						MainPanel.getInstance().addEvent(e);
						saveButton.setEnabled(false);
						saveButton.setText("Saved!");
						MainPanel.getInstance().closeTab(tabid);
						MainPanel.getInstance().refreshView();
					}
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
	}
	
	private void setUpListenersEdit(){
		
		// Save Button
		
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					startTime.getDate();
					endTime.getDate();
					errorText.setVisible(true);
					
					if (nameTextField.getText() == null || nameTextField.getText().trim().length() == 0)
					{
						errorText.setText("* Please enter an event title");
					}
					else if (!(startTime.getDate().getDayOfYear() == endTime.getDate().getDayOfYear() &&
						startTime.getDate().getYear() == endTime.getDate().getYear()))
					{
						errorText.setText("* Event must start and end on the same date");
					}
					else if (startTime.getDate().isAfter(endTime.getDate())) {
						errorText.setText("* Event start date must be before end date");
					}
					else
					{
						errorText.setVisible(false);
						Event e = new Event();
						e.setName(nameTextField.getText().trim());
						e.setDescription(descriptionTextArea.getText());
						e.setStart(startTime.getDate());
						e.setEnd(endTime.getDate());
						e.setProjectEvent(teamProjectCheckBox.isSelected());
						MainPanel.getInstance().addEvent(e);
						saveButton.setEnabled(false);
						saveButton.setText("Saved!");
						MainPanel.getInstance().closeTab(tabid);
						MainPanel.getInstance().refreshView();
					}
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
	}
	
	public void setTabId(int id)
	{
		tabid = id;
	}
}
