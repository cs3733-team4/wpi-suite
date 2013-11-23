package edu.wpi.cs.wpisuitetng.modules.cal.week;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.day.DayGridLabel;
import edu.wpi.cs.wpisuitetng.modules.cal.day.DrawnDay;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Events;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;

public class WeekCalendar extends AbstractCalendar {

	JPanel weekHolder = new JPanel();
	JPanel weekDaysHolder = new JPanel();
	JPanel weekTimeLabel = new JPanel();
	
	JScrollPane fullWeek = new JScrollPane(weekHolder);
	
	DrawnDay[] weekDays = new DrawnDay[7]; 
	
	DateTime time;
	EventModel emodel;
	
	public WeekCalendar(DateTime time, EventModel emodel)
	{
		updateUI();
		this.emodel = emodel;
		
		this.updateDays(Months.getWeekStart(time));
	}
	
	public void updateUI()
	{
		this.removeAll();
		this.setLayout(new GridLayout(1,1));
		this.weekHolder.setLayout(new BorderLayout());
		this.weekDaysHolder.setLayout(new GridLayout(1,7));
		this.weekTimeLabel.setLayout(new GridLayout(1,1));
		
		this.weekHolder.add(this.weekDaysHolder, BorderLayout.CENTER);
		this.weekHolder.add(this.weekTimeLabel, BorderLayout.WEST);
		
		this.weekTimeLabel.add(DayGridLabel.getInstance());
		
		this.add(fullWeek);
	}
	
	
	public void updateDays(DateTime startOfWeek)
	{
		this.updateUI();
		this.time = startOfWeek;
		MutableDateTime mdt = new MutableDateTime(startOfWeek);
		this.weekDaysHolder.removeAll();
		
		List<Event> events = Events.getEventsForWeek(emodel, time);
		List<List<Event>> eventsByDay = new ArrayList<List<Event>>();
		
		
		for(int i = 0; i < 7; i++)
		{
			eventsByDay.add(new ArrayList<Event>());
		}
		
		for(Event e : events)
		{
			int dayOfWeek = e.getStart().getDayOfWeek() % 7;
			eventsByDay.get(dayOfWeek).add(e);
		}
		
		
		
		for(int i = 0; i < 7; i++)
		{
			mdt.addDays(1);
			DateTime thisDate = mdt.toDateTime();
			DrawnDay thisDay = new DrawnDay(thisDate, 50);
			thisDay.addEvents(eventsByDay.get(i));
			
			JPanel day = new JPanel(new BorderLayout());
			day.add(thisDay, BorderLayout.CENTER);
			day.add(new JLabel(thisDate.dayOfWeek().getAsText()), BorderLayout.NORTH);
			
			this.weekDaysHolder.add(day);
			this.weekDays[i] = thisDay;
		}
	}
	
	
	@Override
	public void next() {
		this.updateDays(Months.nextWeek(time));
	}

	@Override
	public void previous() {
		this.updateDays(Months.prevWeek(time));
	}

	@Override
	public void display(DateTime newTime) {
		this.updateDays(Months.getWeekStart(newTime));
	}

	@Override
	public void updateEvents(Event event, boolean added) {
		this.updateDays(Months.getWeekStart(time));
	}
	
}
