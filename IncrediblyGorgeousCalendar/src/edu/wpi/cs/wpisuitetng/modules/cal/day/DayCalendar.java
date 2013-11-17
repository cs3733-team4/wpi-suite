package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JLabel;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.CalendarInterface;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class DayCalendar implements CalendarInterface {

	private JPanel inside                = new JPanel(), 
			       top                   = new JPanel(), 
			       mainCalendarView      = new JPanel(), 
			       navigationPanel       = new JPanel(), 
			       navigationButtonPanel = new JPanel();
	
	private JButton nextButton   = new JButton(">"), 
	        previousButton       = new JButton("<"), 
	        todayButton          = new JButton("Today");
	
	private JLabel monthLabel = new JLabel();
	private DateTime time;
	private MainPanel mainPanel;
	
	public DayCalendar(DateTime on, MainPanel mainPanel)
	{
		this.mainPanel = mainPanel;
		this.time      = on;
	}
	
	@Override
	public void next()
	{
		MutableDateTime current = new MutableDateTime(this.time);
		current.addDays(1);
		this.time = current.toDateTime();
		generateDay(current);
	}

	private void generateDay(MutableDateTime current)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void previous() {
		MutableDateTime current = new MutableDateTime(this.time);
		current.addDays(-1);
		this.time = current.toDateTime();
		generateDay(current);

	}

	@Override
	public void display(DateTime newTime) {
		this.time = newTime;
		generateDay(new MutableDateTime(newTime));
	}

	@Override
	public void addEvent(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEvents(List<Event> eventList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEvent(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEvents(List<Event> event) {
		// TODO Auto-generated method stub

	}

}
