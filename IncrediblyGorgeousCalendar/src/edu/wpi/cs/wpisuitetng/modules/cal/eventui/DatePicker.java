package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import java.awt.BorderLayout;
import java.awt.Dimension;

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
	private JLabel dateLabel;
	private JLabel startLabel;
	private JLabel endLabel;
	private JCheckBox isAllDay;
	private YearCalendarHolder viewCal;
	final private static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("M/d/yy");
	final private static DateTimeFormatter timeFormat = DateTimeFormat.forPattern("h:m a");
	
	public DatePicker() {
		this.setPreferredSize(new Dimension(200, 250));
		viewCal = new YearCalendarHolder(DateTime.now(), this, false); // May need to fix later.
		dateLabel = new JLabel("Select Date", JLabel.CENTER);
		isAllDay = new JCheckBox("All Day Event: ");
		startLabel = new JLabel("Start Time: ");
		endLabel = new JLabel("End Time: ");
		startDatePanel = new JPanel();
		endDatePanel = new JPanel();
	}

	@Override
	public void display(DateTime newtime) {
		// Set default texts of input fields.
		/*String currDate = Integer.toString(newtime.getMonthOfYear()) + "/" 
				+ Integer.toString(newtime.getDayOfMonth()) 
				+ "/" + Integer.toString(newtime.getYear());
		String currTime = Integer.toString(newtime.getHourOfDay()) 
				+ ":" + Integer.toString(newtime.getMinuteOfHour());*/
		final JTextField startDate = new JTextField(newtime.toString(dateFormat));
		final JTextField endDate = new JTextField(newtime.toString(dateFormat));
		final JTextField startTime = new JTextField(newtime.toString(timeFormat));
		final JTextField endTime = new JTextField(newtime.toString(timeFormat));
		endDate.setText(newtime.toString(dateFormat));
		startTime.setText(newtime.toString(timeFormat));
		endTime.setText(newtime.toString(timeFormat));
		
		// Start Date Entry Info
		startDatePanel.setLayout(new BorderLayout());
		startDatePanel.add(startLabel, BorderLayout.WEST);
		startDatePanel.add(startDate, BorderLayout.CENTER);
		startDatePanel.add(startTime, BorderLayout.EAST);
		
		// End Date Entry Info
		endDatePanel.setLayout(new BorderLayout());
		endDatePanel.add(endLabel, BorderLayout.WEST);
		endDatePanel.add(endDate, BorderLayout.CENTER);
		endDatePanel.add(endTime, BorderLayout.EAST);
		
		// Add everything to the main panel.
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(dateLabel);
		this.add(viewCal);
		this.add(startDatePanel);
		this.add(endDatePanel);
	}
}
