///*
//package edu.wpi.cs.wpisuitetng.modules.cal.eventui;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.FontMetrics;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Calendar;
//import java.util.Date;
//
//import javax.swing.BorderFactory;
//import javax.swing.BoxLayout;
//import javax.swing.GroupLayout;
//import javax.swing.GroupLayout.Alignment;
//import javax.swing.JButton;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JSpinner;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.LayoutStyle.ComponentPlacement;
//import javax.swing.SwingConstants;
//
//import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
//
//import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
//import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
//
//public class NewEventDisplay extends JPanel {
//	private JPanel nameEntry;
//	private JLabel nameLabel;
//	private JPanel descriptionEntry;
//	private JPanel miniCalDisplay;
//	private JPanel saveEvent;
//	private JTextField nameField;
//	private JLabel errorMess;
//	private JLabel descriptionLabel;
//	private JTextArea descriptionField;
//	private DatePicker calDisplay;
//	private JButton saveButton;
//	final private static DateTimeFormatter datetimeFormat = DateTimeFormat.forPattern("M/d/yy h:mm a");
//	
//	public NewEventDisplay() {
//		Font mainfont = new Font("DejaVu Sans", Font.BOLD, 12);
//		saveEvent = new JPanel();
//		nameEntry = new JPanel();
//		errorMess = new JLabel("Invalid date input.");
//		errorMess.setForeground(Color.RED);
//		miniCalDisplay = new JPanel();
//		descriptionEntry = new JPanel();
//		nameLabel = new JLabel("Event Name: ");
//		nameLabel.setFont(mainfont);
//		nameField = new JTextField(20);
//		descriptionLabel = new JLabel("Description: ");
//		descriptionLabel.setFont(mainfont);
//		descriptionField = new JTextArea(5, 30);
//		// Set word wrap settings for the description text area.
//		descriptionField.setWrapStyleWord(true);
//		descriptionField.setLineWrap(true);
//	//	calDisplay = new DatePicker();
//		saveButton = new JButton("Save Event");
//		saveButton.setFont(mainfont);
//		// Set the size of the button to wrap its inner text.
//		FontMetrics metrics = getFontMetrics(getFont()); 
//		saveButton.setSize(metrics.stringWidth(saveButton.getText()), metrics.getHeight());
//		
//		// Set the group layouts for the event name entry label and field.
//		GroupLayout gl_nameEntry = new GroupLayout(nameEntry);
//		gl_nameEntry.setHorizontalGroup(
//			gl_nameEntry.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_nameEntry.createSequentialGroup()
//					.addContainerGap()
//					.addComponent(nameLabel)
//					.addPreferredGap(ComponentPlacement.RELATED)
//					.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//					.addContainerGap(119, Short.MAX_VALUE))
//		);
//		gl_nameEntry.setVerticalGroup(
//			gl_nameEntry.createParallelGroup(Alignment.TRAILING)
//				.addGroup(gl_nameEntry.createSequentialGroup()
//					.addContainerGap(19, Short.MAX_VALUE)
//					.addGroup(gl_nameEntry.createParallelGroup(Alignment.BASELINE)
//						.addComponent(nameLabel)
//						.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//					.addContainerGap())
//		);
//		nameEntry.setLayout(gl_nameEntry);
//		
//		// Set the group layout for the description entry components.
//		descriptionLabel.setHorizontalAlignment(SwingConstants.TRAILING);
//		GroupLayout gl_descriptionEntry = new GroupLayout(descriptionEntry);
//		gl_descriptionEntry.setHorizontalGroup(
//			gl_descriptionEntry.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_descriptionEntry.createSequentialGroup()
//					.addContainerGap()
//					.addGroup(gl_descriptionEntry.createParallelGroup(Alignment.LEADING)
//						.addComponent(descriptionField, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
//						.addComponent(descriptionLabel))
//					.addContainerGap())
//		);
//		gl_descriptionEntry.setVerticalGroup(
//			gl_descriptionEntry.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_descriptionEntry.createSequentialGroup()
//					.addContainerGap()
//					.addComponent(descriptionLabel)
//					.addPreferredGap(ComponentPlacement.RELATED)
//					.addComponent(descriptionField, GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
//		
//		);
//		descriptionEntry.setLayout(gl_descriptionEntry);
//		descriptionEntry.setMaximumSize(new Dimension(800, 400));
//		// Save Button Event Listener.
//		saveButton.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// Create a new Event instance when the save button is clicked.
//				try
//				{
//					DateTime sdatetime = datetimeFormat.parseDateTime(
//							calDisplay.getStartDate().getText()
//							+ " " + calDisplay.getStartTime().getText() + " " + 
//							amPM(calDisplay.getStartAMPM()));
//					DateTime edatetime = datetimeFormat.parseDateTime(
//							calDisplay.getEndDate().getText() 
//							+ " " + calDisplay.getEndTime().getText() + " " + 
//							amPM(calDisplay.getEndAMPM()));
//					errorMess.setVisible(false);
//					
//					if (nameField.getText() == null || nameField.getText().length() == 0)
//					{
//						errorMess.setText("Event name cannot be empty.");
//						errorMess.setVisible(true);
//					}
//					else
//					{
//						Event event = new Event();
//						event.setName(nameField.getText());
//						event.setDescription(descriptionField.getText());
//						event.setStart(sdatetime);
//						event.setEnd(edatetime);
//						event.setProjectEvent(true);
//						MainPanel.getInstance().addEvent(event);
//					}
//				}
//				catch (IllegalArgumentException exception)
//				{
//					exception.printStackTrace();
//					errorMess.setVisible(true);
//				}
//			}
//		});
//	}
//	
//	String amPM(JSpinner spinner)
//	{
//		return new DateTime((Date)spinner.getValue()).getHourOfDay() == 0 ? "AM" : "PM";
//	}
//	
//	/** Sets the displays and displays the GUI.
//	 * @param newtime the date and time given
//	 * @return void
//	 */
//	public void display(DateTime newtime) {
//			
//		miniCalDisplay.setLayout(new BoxLayout(miniCalDisplay, BoxLayout.Y_AXIS));
//		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        
//        miniCalDisplay.add(calDisplay);
//        
//        // For the Save Event Button, in order to align it properly.
//        saveEvent.setLayout(new FlowLayout(FlowLayout.LEADING));
//        saveEvent.add(saveButton);
//        saveEvent.add(errorMess);
//        
//		// Main UI Display
//		this.add(nameEntry);
//		this.add(miniCalDisplay);
//		this.add(descriptionEntry);
//		this.add(saveEvent);
//		errorMess.setVisible(false);
//		
//		// Set a black border around the description field.
//		descriptionField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//		
//		// Display the mini calendar.
//		calDisplay.display(newtime);
//	}
//}
//*/