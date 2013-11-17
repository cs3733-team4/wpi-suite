package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.joda.time.DateTime;


/**
 * @author anthonyjruffa
 */
public class NewEventDisplay extends JPanel {
	private JPanel nameEntry;
	private JLabel nameLabel;
	private JPanel descriptionEntry;
	private JPanel miniCalDisplay;
	private JPanel saveEvent;
	private JTextField nameField;
	private JLabel descriptionLabel;
	private JTextArea descriptionField;
	private DatePicker calDisplay;
	private JButton saveButton;
	
	public NewEventDisplay() {
		Font mainfont = new Font("DejaVu Sans", Font.BOLD, 15);
		saveEvent = new JPanel();
		nameEntry = new JPanel();
		miniCalDisplay = new JPanel();
		descriptionEntry = new JPanel();
		nameLabel = new JLabel("Event Name: ");
		nameLabel.setFont(mainfont);
		nameField = new JTextField(20);
		descriptionLabel = new JLabel("Description: ");
		descriptionLabel.setFont(mainfont);
		descriptionField = new JTextArea(5, 30);
		// Set word wrap settings for the description text area.
		descriptionField.setWrapStyleWord(true);
		descriptionField.setLineWrap(true);
		calDisplay = new DatePicker();
		saveButton = new JButton("Save Event");
		saveButton.setFont(mainfont);
		// Set the size of the button to wrap its inner text.
		FontMetrics metrics = getFontMetrics(getFont()); 
		saveButton.setSize(metrics.stringWidth(saveButton.getText()), metrics.getHeight());
		
		//nameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		// Set the group layouts for the event name entry label and field.
		GroupLayout gl_nameEntry = new GroupLayout(nameEntry);
		gl_nameEntry.setHorizontalGroup(
			gl_nameEntry.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_nameEntry.createSequentialGroup()
					.addComponent(nameLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(166, Short.MAX_VALUE))
		);
		gl_nameEntry.setVerticalGroup(
			gl_nameEntry.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_nameEntry.createSequentialGroup()
					.addGroup(gl_nameEntry.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameLabel)
						.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(43, Short.MAX_VALUE))
		);
		nameEntry.setLayout(gl_nameEntry);
	}
	
	/** Sets the displays and displays the GUI.
	 * @param newtime the date and time given
	 * @return void
	 */
	public void display(DateTime newtime) {
		// Set layouts.
		//SpringLayout layout = new SpringLayout();
		//nameEntry.setLayout(layout);
		//descriptionEntry.setLayout(new BoxLayout(descriptionEntry, BoxLayout.Y_AXIS));
		descriptionEntry.setLayout(new BorderLayout(5, 5));
		miniCalDisplay.setLayout(new BoxLayout(miniCalDisplay, BoxLayout.Y_AXIS));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		/*// Event Name Entry.
		nameEntry.add(nameLabel);
		nameLabel.setLabelFor(nameField);
		nameEntry.add(nameField);
		
		//Adjust constraints for the label.
        layout.putConstraint(SpringLayout.WEST, nameLabel, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.NORTH, this);
 
        //Adjust constraints for the text field.
        layout.putConstraint(SpringLayout.WEST, nameField, 5, SpringLayout.EAST, nameLabel);
        layout.putConstraint(SpringLayout.NORTH, nameField, 5, SpringLayout.NORTH, this);*/
        
        // Description field display and Save Event button.
        descriptionEntry.add(descriptionLabel, BorderLayout.NORTH);
        descriptionLabel.setLabelFor(descriptionField);
        descriptionEntry.add(descriptionField, BorderLayout.LINE_START);
        //descriptionEntry.setAlignmentX(Component.CENTER_ALIGNMENT);
		
        miniCalDisplay.add(calDisplay);
        //miniCalDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        saveEvent.setLayout(new FlowLayout(FlowLayout.LEADING));
        saveEvent.add(saveButton);
        
		// Main UI Display
		this.add(nameEntry);
		this.add(miniCalDisplay);
		this.add(descriptionEntry);
		this.add(saveEvent);
		//this.add(saveButton);
		
		// Display the mini calendar.
		calDisplay.display(newtime);
	}
}