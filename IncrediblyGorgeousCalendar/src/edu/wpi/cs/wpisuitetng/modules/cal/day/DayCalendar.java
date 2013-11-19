package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;

public class DayCalendar extends AbstractCalendar
{

	private JPanel inside = new JPanel(), top = new JPanel(), navigationPanel = new JPanel(), title = new JPanel();

	private JButton nextButton = new JButton(">"), previousButton = new JButton("<"), todayButton = new JButton("Today");

	private DateTime time;
	private MainPanel mainPanel;
	private DrawnDay current;

	private EventModel eventModel;

	public DayCalendar(DateTime on, EventModel emodel)
	{
		this.mainPanel = MainPanel.getInstance();
		this.time = on;
		eventModel = emodel;
		this.setLayout(new BorderLayout());

		this.navigationPanel.add(this.previousButton);
		this.navigationPanel.add(this.todayButton);
		this.navigationPanel.add(this.nextButton);

		this.title.add(new JLabel(String.valueOf(time.getDayOfMonth())));

		this.top.add(this.navigationPanel);
		this.top.add(this.title);

		this.add(top, BorderLayout.NORTH);
		this.add(this.inside, BorderLayout.CENTER);
		
		generateDay();
	}

	private void generateDay()
	{
		this.current = new DrawnDay(this.time);
		this.current.addEvents(getVisibleEvents());

		this.inside.removeAll();

		this.inside.add(new DayGridLabel(), BorderLayout.WEST);
		this.inside.add(this.current, BorderLayout.CENTER);

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
		// TODO: this is where filtering should go
		return Arrays.asList(eventModel.getEvents(from, to));
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
