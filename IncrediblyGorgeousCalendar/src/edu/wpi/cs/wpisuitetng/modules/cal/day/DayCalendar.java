package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;

public class DayCalendar extends AbstractCalendar
{

	private DateTime time;
	private MainPanel mainPanel;
	private DrawnDay current;
	
	private JPanel holder = new JPanel();
	private JScrollPane scroll = new JScrollPane(holder);

	private EventModel eventModel;
	
	private int sizeW = 0;

	private DateTimeFormatter titleFmt = DateTimeFormat.forPattern("EEEE, MMM d, yyyy");

	public DayCalendar(DateTime on, EventModel emodel)
	{
		this.mainPanel = MainPanel.getInstance();
		this.time = on;
		eventModel = emodel;
		scroll.setBackground(Colors.TABLE_BACKGROUND);
		holder.setBackground(Colors.TABLE_BACKGROUND);
		
		this.setLayout(new BorderLayout());
		this.holder.setLayout(new BorderLayout());
		
		generateDay();
	}

	private void generateDay()
	{
		if (this.getWidth() > 0)this.sizeW = this.getWidth();
		this.holder.removeAll();
		this.removeAll();
		this.add(scroll, BorderLayout.CENTER);
		this.add(new JLabel(time.toString(titleFmt)), BorderLayout.NORTH);

		this.current = new DrawnDay(this.time, this.sizeW);
		this.current.addEvents(getVisibleEvents());

		this.holder.add(new DayGridLabel(), BorderLayout.WEST);
		this.holder.add(this.current, BorderLayout.CENTER);

		// notify mini-calendar to change
		mainPanel.miniMove(time);
	}
	
	private List<Event> getVisibleEvents()
	{
		MutableDateTime f = new MutableDateTime(time);
		f.setMillisOfDay(0);
		DateTime from = f.toDateTime();
		f.addDays(1);
		DateTime to = f.toDateTime();
		List<Event> temp = eventModel.getEvents(from, to);
		Collections.sort(temp, new Comparator<Event>() {

		        public int compare(Event e1, Event e2) {
		        	if(e1.isProjectEvent()&&!e2.isProjectEvent())
		        		return 1;
		        	else if(e2.isProjectEvent()&&!e1.isProjectEvent())
		        		return -1;
		        	else
		        		return e2.getStart().isBefore(e1.getStart())?1:-1;
		        }
		    });
		for(Event e : temp)
		{
			System.out.print(e.getStart().toString() + "\n");
		}
		return temp;
	}

	@Override
	public void next()
	{
		this.time = Months.nextDay(this.time);
		this.generateDay();
	}

	@Override
	public void previous()
	{
		this.time = Months.prevDay(this.time);
		this.generateDay();

	}

	@Override
	public void display(DateTime newTime)
	{
		this.time = newTime;
		this.generateDay();
	}

	@Override
	public void updateEvents(Event events, boolean added)
	{
		// at the moment, we don't care, and just re-pull from the DB. TODO: this should change
		this.generateDay();
	}
}
