package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
<<<<<<< HEAD
=======
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
>>>>>>> b32e343cd8d9d5ec96d82fcff4843054e288c257

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.year.MiniCalendarHostIface;
import edu.wpi.cs.wpisuitetng.modules.cal.year.YearCalendarHolder;
/**
 * @author anthonyjruffa
 */
public class DatePicker extends JPanel implements MiniCalendarHostIface {
	private JPanel calViewer;
	private JLabel dateLabel;
	private JLabel startLabel;
	private JLabel endLabel;
	private JCheckBox isAllDay;
	private YearCalendarHolder viewCal;
	final private static DateTimeFormatter dateFormat = DateTimeFormat.forPattern("M/d/yy");
	final private static DateTimeFormatter timeFormat = DateTimeFormat.forPattern("h:mm a");
	// Declare text fields for start and end dates/times.
	final private JTextField startDate = new JTextField(DateTime.now().toString(dateFormat));
	final private JTextField endDate = new JTextField(DateTime.now().toString(dateFormat));
	final private JTextField startTime = new JTextField(DateTime.now().toString(timeFormat));
	final private JTextField endTime = new JTextField(DateTime.now().toString(timeFormat));
	
	public DatePicker() {
		Font mainfont = new Font("DejaVu Sans", Font.BOLD, 12);
		this.setPreferredSize(new Dimension(200, 250));
		calViewer = new JPanel();
		viewCal = new YearCalendarHolder(DateTime.now(), this, false); // May need to fix later.
		dateLabel = new JLabel("Select Date:");
		dateLabel.setFont(mainfont);
		isAllDay = new JCheckBox("All Day Event");
		startLabel = new JLabel("From: ");
		endLabel = new JLabel("To: ");
		startLabel.setFont(mainfont);
		endLabel.setFont(mainfont);
		
		/*dateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		startDate.setHorizontalAlignment(SwingConstants.TRAILING);
		startTime.setHorizontalAlignment(SwingConstants.TRAILING);
		endDate.setHorizontalAlignment(SwingConstants.TRAILING);*/

		// Set up the group layouts for the date display.
		GroupLayout gl_dateDisplay = new GroupLayout(calViewer);
		gl_dateDisplay.setHorizontalGroup(
			gl_dateDisplay.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_dateDisplay.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_dateDisplay.createParallelGroup(Alignment.LEADING)
						.addComponent(dateLabel)
						.addGroup(gl_dateDisplay.createSequentialGroup()
							.addContainerGap()
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
							.addComponent(endTime, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(isAllDay))
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
						.addComponent(endTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(isAllDay)))
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
		
		isAllDay.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					// Disallow editing of the start and end times if the event is all day.
					startTime.setEditable(false);
					endTime.setEditable(false);
					// Change the text color to gray to show it cannot be edited.
					startTime.setForeground(Color.LIGHT_GRAY);
					endTime.setForeground(Color.LIGHT_GRAY);
				} else {
					// Allow editing of the start and end times if the event is not all day.
					startTime.setEditable(true);
					endTime.setEditable(true);
					// Change the text color back to black to show it can be edited.
					startTime.setForeground(Color.BLACK);
					endTime.setForeground(Color.BLACK);
				}
				
			}
		});
	}

	// Getter methods for dates and times.
	public JTextField getStartDate() {
		return this.startDate;
	}
	
	public JTextField getStartTime() {
		return this.startTime;
	}
	
	public JTextField getEndDate() {
		return this.endDate;
	}
	
	public JTextField getEndTime() {
		return this.endTime;
	}
	
	//this function will eventually be used for quick adding events that default to 1 hour length
	public void displayTime(DateTime newTime){
		startTime.setText(newTime.toString(timeFormat));
		MutableDateTime newMutTime = newTime.toMutableDateTime();
		newMutTime.addHours(1);
		endTime.setText(newMutTime.toString(timeFormat));
	}
	
	@Override
	public void display(DateTime newTime) {
		// Change the text to correspond to the DateTime parameter.
		endDate.setText(newTime.toString(dateFormat));
		startDate.setText(newTime.toString(dateFormat));
		
		// Add everything to the main panel.
		this.setLayout(new BorderLayout(5, 5));
		this.add(calViewer, BorderLayout.LINE_START);
		viewCal.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
	}
}