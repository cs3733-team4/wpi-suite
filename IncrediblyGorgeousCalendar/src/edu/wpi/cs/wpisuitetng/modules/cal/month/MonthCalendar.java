package edu.wpi.cs.wpisuitetng.modules.cal.month;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.joda.time.*;

import com.lowagie.text.Font;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.DayStyle;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;

public class MonthCalendar extends AbstractCalendar
{

	private JPanel inside = new JPanel(),
			top = new JPanel(),
			mainCalendarView = new JPanel(),
			calendarTitlePanel = new JPanel();
	
	private JLabel monthLabel = new JLabel();
	private DateTime time;
	private MainPanel mainPanel;

	private HashMap<Integer, MonthDay> days = new HashMap<Integer, MonthDay>();

	private EventModel eventModel;

	public MonthCalendar(DateTime on, EventModel emodel)
	{
		this.eventModel = emodel;
		this.mainPanel = MainPanel.getInstance();
		this.time = on;

		this.setLayout(new BorderLayout());
		this.add(calendarTitlePanel, BorderLayout.NORTH);

		generateDays(new MutableDateTime(on));
		generateHeaders(new MutableDateTime(on));

	}

	/**
	 * 
	 * @param fom
	 *            the mutable date time
	 */
	public void generateHeaders(MutableDateTime fom)
	{
		// Set up label for month title
		monthLabel.setHorizontalAlignment(JLabel.CENTER);
		monthLabel.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 25));

		// Set up the container title panel (only holds monthLabel for now)
		calendarTitlePanel.setLayout(new BorderLayout());
		calendarTitlePanel.add(monthLabel, BorderLayout.CENTER);

		// layout code
		mainCalendarView.setBackground(Colors.TABLE_BACKGROUND);
		mainCalendarView.setLayout(new BorderLayout());
		top.setLayout(new GridLayout(1, 7));

		mainCalendarView.add(top, BorderLayout.NORTH);
		mainCalendarView.add(inside, BorderLayout.CENTER);

		this.add(mainCalendarView, BorderLayout.CENTER);
		this.add(calendarTitlePanel, BorderLayout.NORTH);
		// end layout code

		fom.setDayOfMonth(1);
		fom.setMillisOfDay(0);
		int first = (fom.getDayOfWeek() % 7);
		fom.addDays(-first);

		// generate days on top
		for (int i = 0; i < 7; i++)
		{
			JLabel jl = new JLabel(fom.dayOfWeek().getAsText());
			jl.setHorizontalAlignment(SwingConstants.CENTER);
			fom.addDays(1);
			top.add(jl);
		}
	}

	/**
	 * clears and sets a list of events
	 * 
	 * @param events
	 */
	void setEvents(List<Event> events)
	{
		clearEvents();
		for (Event e : events)
		{
			this.addEvent(e);
		}
	}

	/**
	 * Add an event
	 * 
	 * @param e
	 */
	void addEvent(Event e)
	{
		MonthDay md = this.days.get(e.getStart().getDayOfYear());
		md.addEvent(e);
	}

	/**
	 * Remove a single event
	 * 
	 * @param e
	 */
	void removeEvent(Event e)
	{
		MonthDay md = this.days.get(e.getStart().getDayOfYear());
		md.removeEvent(e);
	}
	
	void clearEvents()
	{
		for (Component i : inside.getComponents())
		{
			((MonthDay)i).clear();
		}
	}

	public boolean isToday(ReadableDateTime fom)
	{
		DateTime now = DateTime.now();
		return fom.getYear() == now.getYear() && fom.getDayOfYear() == now.getDayOfYear();
	}

	public void display(DateTime newtime)
	{
		if (time.getMonthOfYear() == newtime.getMonthOfYear() && time.getYear() == newtime.getYear())
			return; // nothing changed. don't update
		time = newtime;
		generateDays(new MutableDateTime(time));
	}

	public void next()
	{
		MutableDateTime fom = new MutableDateTime(time);
		fom.addMonths(1);
		time = fom.toDateTime();
		generateDays(fom);
	}

	public void previous()
	{
		MutableDateTime fom = new MutableDateTime(time);
		fom.addMonths(-1);
		time = fom.toDateTime();
		generateDays(fom);
	}

	/**
	 * Fill calendar with month in referenceDay
	 * 
	 * @param referenceDay
	 *            what month should we display
	 */
	protected void generateDays(MutableDateTime referenceDay)
	{
		// reset to the first of the month at midnight, then find Sunday
		referenceDay.setDayOfMonth(1);
		referenceDay.setMillisOfDay(0);
		int first = (referenceDay.getDayOfWeek() % 7);
		int daysInView = first + referenceDay.dayOfMonth().getMaximumValue();
		int weeks = (int) Math.ceil(daysInView / 7.0);

		inside.setLayout(new java.awt.GridLayout(weeks, 7));
		referenceDay.addDays(-first);

		// remove all old days
		inside.removeAll();
		
		DateTime from = referenceDay.toDateTime();

		// generate days, weeks*7 covers all possible months, so we just loop
		// through and add each day
		for (int i = 0; i < (weeks * 7); i++)
		{
			MonthDay md = new MonthDay(referenceDay.toDateTime(), getMarker(referenceDay));
			inside.add(md);
			md.reBorder(i < 7, (i % 7) == 0, i >= 5 * 7);
			this.days.put(referenceDay.getDayOfYear(), md);
			referenceDay.addDays(1); // go to next day
		}
		
		referenceDay.addDays(-1);// go back one to counteract last add one
		
		setEvents(getVisibleEvents(from, referenceDay.toDateTime()));

		monthLabel.setText(this.getTime().toString(Months.monthLblFormat));

		// notify mini-calendar to change
		mainPanel.miniMove(time);

		// repaint when changed
		inside.revalidate();
	}

	private List<Event> getVisibleEvents(DateTime from, DateTime to)
	{
		// TODO: this is where filtering should go
		return Arrays.asList(eventModel.getEvents(from, to));
	}

	/**
	 * Gets the DayStyle of given date
	 * 
	 * @param date
	 * @return
	 */
	protected DayStyle getMarker(ReadableDateTime date)
	{
		if (date.getMonthOfYear() == time.getMonthOfYear())
		{
			return (isToday(date) ? DayStyle.Today : DayStyle.Normal);
		} else
			return DayStyle.OutOfMonth;
	}

	// Added for testing purposes
	public DateTime getTime()
	{
		return time;
	}

	@Override
	public void updateEvents(Event events, boolean addOrRemove)
	{
		// TODO Auto-generated method stub
	}
}
