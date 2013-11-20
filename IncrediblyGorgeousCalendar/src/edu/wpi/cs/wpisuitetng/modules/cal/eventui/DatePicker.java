package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MiniCalendarHostIface;

@SuppressWarnings("serial")
public class DatePicker extends JPanel implements MiniCalendarHostIface {
	JFormattedTextField date;
	DateTimeFormatter dateFmt;
	DateTimeFormatter dateTimeFmt;
	JComboBox<String> AMPM;
	JFormattedTextField time;
	
	public DatePicker(boolean showTime) {
		super();
		dateFmt = new DateTimeFormatterBuilder()
        .appendMonthOfYear(2)
        .appendLiteral('/')
        .appendDayOfMonth(2)
        .appendLiteral('/')
        .appendYearOfCentury(2,2)
        .toFormatter();
		
		dateTimeFmt = new DateTimeFormatterBuilder()
		.appendMonthOfYear(2)
        .appendLiteral('/')
        .appendDayOfMonth(2)
        .appendLiteral('/')
        .appendYearOfCentury(2,2)
        .appendLiteral(' ')
        .appendHourOfDay(2)
        .appendLiteral(':')
        .appendMinuteOfHour(2)
        .appendHalfdayOfDayText()
        .toFormatter();
		final MiniCalendarHostIface me = this;
		try {
			date = new JFormattedTextField(new MaskFormatter("##/##/##"));
			date.setFont(new Font("Monospaced", Font.PLAIN, 13));
			this.add(date);
			if (showTime) {
				time = new JFormattedTextField(new MaskFormatter("##:##"));
				time.setFont(new Font("Monospaced", Font.PLAIN, 13));
				this.add(time);
				AMPM = new JComboBox<>();
				AMPM.addItem("AM");
				AMPM.addItem("PM");
				this.add(AMPM);
			}
			date.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent e) {

				}

				public void mousePressed(MouseEvent e) {

				}

				public void mouseReleased(MouseEvent e) {
					JFrame cal = new PopupCalendar(DateTime.now(), me);
				    cal.setUndecorated(true);
				    cal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				    Point loc = date.getLocationOnScreen();
				    loc.setLocation(loc.x, loc.y+26);
					cal.setLocation(loc);
					cal.setSize(220, 220);
				    cal.setVisible(true);
				}

				public void mouseEntered(MouseEvent e) {

				}

				public void mouseExited(MouseEvent e) {

				}

			});

		} catch (java.text.ParseException e) {
			System.err.println(e.toString());
		}
	}

	public void display(DateTime value) {		
		date.setText(value.toString(fmt));
	}
	
	public DateTime getDate(){
		return dateTimeFmt.parseDateTime(date.getText()
				+ " " + time.getText() + AMPM.getSelectedItem());
	}
}
