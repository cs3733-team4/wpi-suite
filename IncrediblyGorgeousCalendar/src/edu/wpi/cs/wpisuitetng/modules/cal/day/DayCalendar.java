package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JLabel;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.CalendarInterface;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class DayCalendar extends JPanel implements CalendarInterface {

	private JPanel inside                = new JPanel(), 
			       top                   = new JPanel(), 
			       navigationPanel       = new JPanel();
	
	private JButton nextButton   = new JButton(">"), 
	        previousButton       = new JButton("<"), 
	        todayButton          = new JButton("Today");
	
	private DateTime time;
	private MainPanel mainPanel;
	private DrawnDay current;
	
	public DayCalendar(DateTime on, MainPanel mainPanel)
	{
		this.mainPanel = mainPanel;
		this.time      = on;
		
		this.setLayout(new BorderLayout());
		this.add(navigationPanel, BorderLayout.NORTH);
		display(on);
	}
	
	private void generateDay(MutableDateTime current)
	{
		this.current = new DrawnDay(this.time);
	}
	
	@Override
	public void next()
	{
		MutableDateTime current = new MutableDateTime(this.time);
		current.addDays(1);
		this.time = current.toDateTime();
		generateDay(current);
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
	public void addEvents(List<Event> eventList) {
		this.current.addEvents(eventList);
		this.display(this.time);
	}

	@Override
	public void removeEvents(List<Event> eventList) {
		this.current.removeEvents(eventList);
		this.display(this.time);

	}
	@Override
	public void addEvent(Event event)
	{
		// Please remove this from the calendar interface if we're not going to use this.
	}
	@Override
	public void removeEvent(Event event)
	{
		// Same here.
	}
}
