package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
	DatePicker linked;
	
	public DatePicker(boolean showTime, DatePicker mLinked) {
		super();
		linked = mLinked;
		
		dateFmt = DateTimeFormat.forPattern("MM/dd/yy");
		
		dateTimeFmt = DateTimeFormat.forPattern("MM/dd/yy h:mmaa");
		final MiniCalendarHostIface me = this;
		try {
			date = new JFormattedTextField(new MaskFormatter("##/##/##"));
			date.setFont(new Font("Monospaced", Font.PLAIN, 13));
			this.add(date);
			if (showTime) {
				MaskFormatter mask = new MaskFormatter("##:##");
				mask.setPlaceholder("00:00");
				time = new JFormattedTextField(mask);
				time.setFont(new Font("Monospaced", Font.PLAIN, 13));				
				time.addFocusListener(new FocusListener() {
					
					@Override
					public void focusLost(FocusEvent arg0) {
						switch(time.getText().replace(":", "").trim().length())
						{
						case 4:
							break;
							
						case 3:
							time.setValue("0" + time.getText().replace(":", "").trim().charAt(0)+":"+time.getText().replace(":", "").trim().substring(1));
							break;
							
						case 2:
							if(Integer.valueOf(time.getText().replace(":", "").trim())<13)
								time.setValue(time.getText().replace(":", "").trim()+":00");
							else
								time.setValue("00:00");	
							break;
							
						case 1:
							time.setValue("0"+time.getText().replace(":", "").trim()+":00");
							break;
							
						default:
							time.setValue("00:00");	
						}
					}
					
					@Override
					public void focusGained(FocusEvent arg0) {
						 SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								time.selectAll();
							}
						});
					}
				});
				
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
		date.setText(value.toString(dateFmt));
		if(linked!=null)
			linked.display(value);
	}
	
	public DateTime getDate(){
		return dateTimeFmt.parseDateTime(date.getText()
				+ " " + time.getText() + AMPM.getSelectedItem());
	}
}
