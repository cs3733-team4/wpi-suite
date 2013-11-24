package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MiniCalendarHostIface;

@SuppressWarnings("serial")
public class DatePicker extends JPanel implements MiniCalendarHostIface {
	DateTimeFormatter dateFmt;
	DateTimeFormatter dateTimeFmt;
	public JComboBox<String> AMPM;
	public JFormattedTextField date;
	public JFormattedTextField time;
	DateTimeFormatter hourFmt;
	DateTimeFormatter minFmt;
	//JComboBox<String> hrs;
	//JComboBox<String> mins;
	SpinnerDateModel hourmodel;
	SpinnerDateModel minmodel;
	JSpinner hrs;
	JSpinner mins;
	JSpinner.DateEditor houreditor;
	JSpinner.DateEditor minuteeditor;
	//JFormattedTextField time;
	DatePicker linked;
	
	public DatePicker(boolean showTime, DatePicker mLinked) {
		super();
		linked = mLinked;
		
		dateFmt = DateTimeFormat.forPattern("MM/dd/yy");
		dateTimeFmt = DateTimeFormat.forPattern("MM/dd/yy hh:mm aa");
		hourFmt = DateTimeFormat.forPattern("hh");
		minFmt = DateTimeFormat.forPattern("mm");
		final MiniCalendarHostIface me = this;
		try {
			date = new JFormattedTextField(new MaskFormatter("##/##/##"));
			date.setFont(new Font("Monospaced", Font.PLAIN, 13));
			this.add(date);
			if (showTime) {
				//time = new JFormattedTextField(new MaskFormatter("##:##"));
				//time.setFont(new Font("Monospaced", Font.PLAIN, 13));
				//this.add(time);
				//
				Date date = new Date();
				hourmodel = new SpinnerDateModel(date, null, null, Calendar.HOUR);
				minmodel = new SpinnerDateModel(date, null, null, Calendar.MINUTE);
				hrs = new JSpinner(hourmodel);
				mins = new JSpinner(minmodel);
				houreditor = new JSpinner.DateEditor(hrs, "hh");
				minuteeditor = new JSpinner.DateEditor(mins, "mm");
				hrs.setEditor(houreditor);
				mins.setEditor(minuteeditor);
				
				// Add the numbers for the hours and minutes.
				//hrs = new JComboBox<>();
				/*for (int i = 1; i <= 12; i++) {
					String num = (i < 10) ? "0" + Integer.toString(i) : Integer.toString(i);
					hrs.addItem(num);
				}
				mins = new JComboBox<>();
				for (int j = 1; j < 60; j++) {
					String num = (j < 10) ? "0" + Integer.toString(j) : Integer.toString(j);
					mins.addItem(num);
				}*/
				// Add everything into the display panel.
				this.add(hrs);
				this.add(new JLabel(":"));
				this.add(mins);
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
				    loc.setLocation(loc.x, loc.y + 26);
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
		date.setText(value.toString(dateFmt));
		if(linked != null)
			linked.display(value);
	}
	
	public DateTime getDate() {
	      System.out.println(date);
		return dateTimeFmt.parseDateTime(date.getText()
				//+ " " + hrs.getSelectedItem() + ":" + mins.getSelectedItem() + " " + AMPM.getSelectedItem());
				//+ " " + ((Date) hrs.getValue()).getHours() + ":" + ((Date) mins.getValue()).getMinutes() + " " + AMPM.getSelectedItem());
				+ " " + houreditor.getTextField().getText() + ":" + minuteeditor.getTextField().getText() + " " + AMPM.getSelectedItem());
	}
}
