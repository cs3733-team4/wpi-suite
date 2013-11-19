package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerDateModel;

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
	final private static DateTimeFormatter timeFormat = DateTimeFormat.forPattern("h:mm");
	// AM/PM selectors formatting.
	private SpinnerDateModel startampm = new SpinnerDateModel();
	private JSpinner startampmSelect = new JSpinner();
	private SpinnerDateModel endampm = new SpinnerDateModel();
	private JSpinner endampmSelect = new JSpinner();
	
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
		
		// Set AM/PM Selectors.
		startampm.setCalendarField(Calendar.AM_PM);
		startampmSelect.setModel(startampm);
		startampmSelect.setEditor(new JSpinner.DateEditor(startampmSelect, "a"));
		endampm.setCalendarField(Calendar.AM_PM);
		endampmSelect.setModel(endampm);
		endampmSelect.setEditor(new JSpinner.DateEditor(endampmSelect, "a"));
		
		/*FontMetrics metrics = getFontMetrics(getFont());
		startDate.setSize(metrics.stringWidth(startDate.getText()), metrics.getHeight());
		endDate.setSize(metrics.stringWidth(endDate.getText()), metrics.getHeight());
		startTime.setSize(metrics.stringWidth(startTime.getText()), metrics.getHeight());
		endTime.setSize(metrics.stringWidth(endTime.getText()), metrics.getHeight());*/
		
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
							.addComponent(startDate, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(startTime, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(startampmSelect)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(endLabel)
							.addGap(6)
							.addComponent(endDate, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(endTime, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(endampmSelect)
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
						.addComponent(startampmSelect)
						.addComponent(endDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(endLabel)
						.addComponent(endTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(endampmSelect)
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
		ItemListener il = new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		// Check if the all day checkbox is checked, and toggle whether or not time can be edited.
		isAllDay.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(isAllDay.isSelected()) {
					// Disallow editing of the start and end times if the event is all day.
					startTime.setEditable(false);
					endTime.setEditable(false);
					startTime.setEnabled(false);
					endTime.setEnabled(false);
					startampmSelect.setEnabled(false);
					endampmSelect.setEnabled(false);
				} else {
					// Allow editing of the start and end times if the event is not all day.
					startTime.setEditable(true);
					endTime.setEditable(true);
					startTime.setEnabled(true);
					endTime.setEnabled(true);
					startampmSelect.setEnabled(true);
					endampmSelect.setEnabled(true);
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
	
	public JSpinner getStartAMPM() {
		return this.startampmSelect;
	}
	
	public JSpinner getEndAMPM() {
		return this.endampmSelect;
	}
	
	// This is for testing purposes for the checkbox.
	public JCheckBox getIsAllDay(){
		return this.isAllDay;
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