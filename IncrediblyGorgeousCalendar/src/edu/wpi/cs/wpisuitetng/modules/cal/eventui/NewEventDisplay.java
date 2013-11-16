package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.joda.time.DateTime;


/**
 * @author anthonyjruffa
 */
public class NewEventDisplay extends JPanel {
	private JPanel nameEntry;
	private JLabel nameLabel;
	private JTextField nameField;
	private JLabel descriptionLabel;
	private JTextArea descriptionField;
	private DatePicker calDisplay;
	private JButton saveButton;
	//private MainPanel mainpanel;
	
	public NewEventDisplay() {
		//this.mainpanel = mainpanel;
		nameEntry = new JPanel();
		nameLabel = new JLabel("Event Name: ");
		nameField = new JTextField(20);
		descriptionLabel = new JLabel("Description: ", JLabel.CENTER);
		descriptionField = new JTextArea(15, 15);
		calDisplay = new DatePicker();
		saveButton = new JButton("Save Event");
	}
	
	/** Sets the displays and displays the GUI.
	 * @param newtime the date and time given
	 * @return void
	 */
	public void display(DateTime newtime) {
		// Set layouts.
		SpringLayout layout = new SpringLayout();
		nameEntry.setLayout(layout);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// Event Name Entry.
		nameEntry.add(nameLabel);
		nameEntry.add(nameField);
		
		//Adjust constraints for the label.
        layout.putConstraint(SpringLayout.WEST, nameLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.NORTH, this);
 
        //Adjust constraints for the text field.
        layout.putConstraint(SpringLayout.WEST, nameField, 5, SpringLayout.EAST, nameLabel);
        layout.putConstraint(SpringLayout.NORTH, nameField, 5, SpringLayout.NORTH, this);
 
		
		// Main UI Display
		this.add(nameEntry);
		this.add(calDisplay);
		this.add(descriptionLabel);
		this.add(descriptionField);
		this.add(saveButton);
		
		// Display the mini calendar.
		calDisplay.display(newtime);
	}
}
