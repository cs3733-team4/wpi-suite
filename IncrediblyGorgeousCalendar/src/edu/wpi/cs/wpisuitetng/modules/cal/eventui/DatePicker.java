package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.year.MiniCalendarHostIface;
import edu.wpi.cs.wpisuitetng.modules.cal.year.YearCalendarHolder;
/**
 * @author anthonyjruffa
 */
public class DatePicker extends JPanel implements MiniCalendarHostIface {
	private JPanel datePanel;
	private JPanel calViewer;
	private JLabel dateLabel;
	private JLabel startLabel;
	private JLabel endLabel;
	private JCheckBox isAllDay;
	private YearCalendarHolder viewCal;
	final private static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("M/d/yy");
	final private static DateTimeFormatter timeFormat = DateTimeFormat.forPattern("h:mm a");
	// Declare text fields for start and end dates/times.
	final JTextField startDate = new JTextField(DateTime.now().toString(dateFormat));
	final JTextField endDate = new JTextField(DateTime.now().toString(dateFormat));
	final JTextField startTime = new JTextField(DateTime.now().toString(timeFormat));
	final JTextField endTime = new JTextField(DateTime.now().toString(timeFormat));
	
	public DatePicker() {
		this.setPreferredSize(new Dimension(200, 250));
		calViewer = new JPanel();
		viewCal = new YearCalendarHolder(DateTime.now(), this, false); // May need to fix later.
		dateLabel = new JLabel("Select Date");
		//isAllDay = new JCheckBox("All Day Event: ");
		startLabel = new JLabel("From: ");
		endLabel = new JLabel("To: ");
		
		/*dateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		startDate.setHorizontalAlignment(SwingConstants.TRAILING);
		startTime.setHorizontalAlignment(SwingConstants.TRAILING);	
		endDate.setHorizontalAlignment(SwingConstants.TRAILING);*/

		GroupLayout gl_dateDisplay = new GroupLayout(calViewer);
		gl_dateDisplay.setHorizontalGroup(
			gl_dateDisplay.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_dateDisplay.createSequentialGroup()
					.addGroup(gl_dateDisplay.createParallelGroup(Alignment.LEADING)
						.addComponent(dateLabel)
						.addGroup(gl_dateDisplay.createSequentialGroup()
							.addComponent(startLabel)
							.addGap(4)
							.addComponent(startDate, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(startTime, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(endLabel)
							.addGap(6)
							.addComponent(endDate, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(endTime, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
						.addComponent(viewCal, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_dateDisplay.setVerticalGroup(
			gl_dateDisplay.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_dateDisplay.createSequentialGroup()
					.addComponent(dateLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(viewCal, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_dateDisplay.createParallelGroup(Alignment.BASELINE)
						.addComponent(startLabel)
						.addComponent(startDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(startTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(endDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(endLabel)
						.addComponent(endTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		calViewer.setLayout(gl_dateDisplay);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(calViewer, GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(calViewer, GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
					.addContainerGap())
		);
		setLayout(groupLayout);
	}

	@Override
	public void display(DateTime newtime) {
		// Change the text to correspond to the DateTime parameter.
		endDate.setText(newtime.toString(dateFormat));
		startTime.setText(newtime.toString(timeFormat));
		endTime.setText(newtime.toString(timeFormat));
		
		// Calendar Viewer
		/*calViewer.setLayout(new BoxLayout(calViewer, BoxLayout.Y_AXIS));
		calViewer.add(dateLabel);
		dateLabel.setLabelFor(viewCal);
		calViewer.add(viewCal);
		
		// Start Date Entry Info
		SpringLayout datepanellayout = new SpringLayout();
		datePanel.setLayout(datepanellayout);
		datePanel.add(startLabel);
		datePanel.add(startDate);
		datePanel.add(startTime);
		datePanel.add(endLabel);
		datePanel.add(endDate);
		datePanel.add(endTime);
		
		//Adjust constraints for the start date label.
		datepanellayout.putConstraint(SpringLayout.WEST, startLabel, 5, SpringLayout.WEST, calViewer);
		datepanellayout.putConstraint(SpringLayout.NORTH, startLabel, 10, SpringLayout.NORTH, calViewer);
 
        //Adjust constraints for the start date text field.
		datepanellayout.putConstraint(SpringLayout.WEST, startDate, 5, SpringLayout.EAST, startLabel);
		datepanellayout.putConstraint(SpringLayout.NORTH, startDate, 5, SpringLayout.NORTH, calViewer);
		
		//Adjust constraints for the start time text field.
		datepanellayout.putConstraint(SpringLayout.WEST, startTime, 5, SpringLayout.EAST, startDate);
		datepanellayout.putConstraint(SpringLayout.NORTH, startTime, 5, SpringLayout.NORTH, calViewer);
		
		//Adjust constraints for the end date label.
		datepanellayout.putConstraint(SpringLayout.WEST, endLabel, 5, SpringLayout.EAST, startTime);
		datepanellayout.putConstraint(SpringLayout.NORTH, endLabel, 10, SpringLayout.NORTH, calViewer);
		
		//Adjust constraints for the end date text field.
		datepanellayout.putConstraint(SpringLayout.WEST, endDate, 5, SpringLayout.EAST, endLabel);
		datepanellayout.putConstraint(SpringLayout.NORTH, endDate, 5, SpringLayout.NORTH, calViewer);
		
		//Adjust constraints for the end time text field.
		datepanellayout.putConstraint(SpringLayout.WEST, endTime, 5, SpringLayout.EAST, endDate);
		datepanellayout.putConstraint(SpringLayout.NORTH, endTime, 5, SpringLayout.NORTH, calViewer);*/
				
		// Add everything to the main panel.
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(calViewer);
		//this.add(datePanel);
	}
}
