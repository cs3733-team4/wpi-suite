package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.year.MiniCalendarHostIface;
import edu.wpi.cs.wpisuitetng.modules.cal.year.YearCalendarHolder;
/**
 * @author anthonyjruffa
 */
public class DatePicker extends JPanel implements MiniCalendarHostIface {
	private JPanel startDatePanel;
	private JPanel endDatePanel;
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
		isAllDay = new JCheckBox("All Day Event: ");
		startLabel = new JLabel("Start Time: ");
		endLabel = new JLabel("End Time: ");
		startDatePanel = new JPanel();
		endDatePanel = new JPanel();
	}

	@Override
	public void display(DateTime newtime) {
		// Change the text to correspond to the DateTime parameter.
		endDate.setText(newtime.toString(dateFormat));
		startTime.setText(newtime.toString(timeFormat));
		endTime.setText(newtime.toString(timeFormat));
		
		// Calendar Viewer
		calViewer.setLayout(new BoxLayout(calViewer, BoxLayout.Y_AXIS));
		calViewer.add(dateLabel);
		calViewer.add(viewCal);
		dateLabel.setLabelFor(viewCal);
		
		// Start Date Entry Info
		startDatePanel.setLayout(new BoxLayout(startDatePanel, BoxLayout.X_AXIS));
		startDatePanel.add(startLabel);
		startDatePanel.add(startDate);
		startDatePanel.add(startTime);
		
		// End Date Entry Info
		endDatePanel.setLayout(new BoxLayout(endDatePanel, BoxLayout.X_AXIS));
		endDatePanel.add(endLabel);
		endDatePanel.add(endDate);
		endDatePanel.add(endTime);
		
		// Add everything to the main panel.
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(calViewer);
		this.add(startDatePanel);
		this.add(endDatePanel);
	}
}
