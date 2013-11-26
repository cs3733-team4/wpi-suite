package edu.wpi.cs.wpisuitetng.modules.cal.eventui;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MiniCalendarHostIface;

@SuppressWarnings("serial")
public class DatePicker extends JPanel implements MiniCalendarHostIface {
	DateTimeFormatter dateFmt;
	DateTimeFormatter timeFmt;
	DateTimeFormatter dateTimeFmt;
	JComboBox<String> AMPM;
	JFormattedTextField date;
	JFormattedTextField time;
	DatePicker linked;
	ArrayList<DatePickerListener> changeListeners = new ArrayList<DatePickerListener>();
	
	public DatePicker(boolean showTime, DatePicker mLinked) {
		super();
		linked = mLinked;
		
		dateFmt = DateTimeFormat.forPattern("MM/dd/yy");
		timeFmt = DateTimeFormat.forPattern("hh:mm");
		dateTimeFmt = DateTimeFormat.forPattern("MM/dd/yy hh:mm aa");
		final MiniCalendarHostIface me = this;
		try {
			date = new JFormattedTextField(new MaskFormatter("##/##/##"));
			date.setFont(new Font("Monospaced", Font.PLAIN, 12));
			date.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					notifyListeners();
				}
				
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					notifyListeners();						
				}
				
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					notifyListeners();
				}
			});
			this.add(date);
			
			if (showTime) {
				MaskFormatter mask = new MaskFormatter("##:##");
				time = new JFormattedTextField(mask);
				time.setFont(new Font("Monospaced", Font.PLAIN, 12));
				time.getDocument().addDocumentListener(new DocumentListener() {
					
					@Override
					public void removeUpdate(DocumentEvent arg0) {
						notifyListeners();
					}
					
					@Override
					public void insertUpdate(DocumentEvent arg0) {
						notifyListeners();						
					}
					
					@Override
					public void changedUpdate(DocumentEvent arg0) {
						notifyListeners();
					}
				});
				
				time.addFocusListener(new FocusListener() {
					
					@Override
					public void focusLost(FocusEvent arg0) {
						String input = time.getText().replace(":", "").trim();
						//If less than 4 characters are entered, intelligently fill textbox to fit format
						switch(input.length())
						{
						case 4:
							break;
							
						case 3:
							time.setValue("0" + input.charAt(0)+":"+input.substring(1));
							break;
							
						case 2:
							if(Integer.valueOf(input)<13)
								time.setValue(input+":00");
							else
								time.setValue("00:00");	
							break;
							
						case 1:
							time.setValue("0"+input+":00");
							break;
							
						default:
						}
					}
					
					//Select all when user clicks text box
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
				AMPM.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						notifyListeners();						
					}
				});
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
		try
		{
			return dateTimeFmt.parseDateTime(date.getText()+" "+time.getText()+" "+AMPM.getSelectedItem());
		}catch(IllegalArgumentException e)
		{
			return null;
		}
	}
	
	
	/**
	 * Sets date and time of DatePicker to specified value
	 * @param previous the DateTime object from which to obtain values
	 */
	public void setDateTime(DateTime previous) {
			this.date.setText(previous.toString(dateFmt));
			this.time.setText(previous.toString(timeFmt));
			if (previous.getHourOfDay() > 12){
				this.AMPM.setSelectedIndex(1);
			}
			else{
				this.AMPM.setSelectedIndex(0);
			}
	}
	
	public void addChangeListener(DatePickerListener newListener)
	{
		changeListeners.add(newListener);
	}
	
	public void notifyListeners()
	{
		for(DatePickerListener d : changeListeners)
		{
			d.datePickerUpdate(this.getDate());
		}
	}
}
