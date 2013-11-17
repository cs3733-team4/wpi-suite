package edu.wpi.cs.wpisuitetng.modules.cal.month;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import edu.wpi.cs.wpisuitetng.modules.cal.CalendarInterface;
import edu.wpi.cs.wpisuitetng.modules.cal.DayStyle;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;


public class MonthCalendar extends JPanel implements CalendarInterface
{

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
	
	
	private HashMap<Integer, MonthDay> days = new HashMap<Integer, MonthDay>();
	

	public MonthCalendar(DateTime on, MainPanel mainPanel)
	{
		this.mainPanel = mainPanel;
		this.time      = on;
		
		this.setLayout(new BorderLayout());
		this.add(navigationPanel, BorderLayout.NORTH);
		
		generateDays(new MutableDateTime(on));
		generateHeaders(new MutableDateTime(on));
		
	}
	
	/**
	 * 
	 * @param fom the mutable date time
	 */
	public void generateHeaders(MutableDateTime fom)
	{
		navigationPanel.setLayout(new BorderLayout());
		monthLabel.setHorizontalAlignment(JLabel.CENTER);	
		monthLabel.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 25));

		navigationButtonPanel.setLayout(new BorderLayout());
		navigationButtonPanel.add(nextButton, BorderLayout.EAST);
		navigationButtonPanel.add(todayButton, BorderLayout.CENTER);
		navigationButtonPanel.add(previousButton, BorderLayout.WEST);
		
		
		//placeholder panel to center title panel
		JPanel navigationTopRightPanel = new JPanel();
		navigationTopRightPanel.setPreferredSize(navigationButtonPanel.getPreferredSize());
		
		//unnecessary if arrows are used because both are same size
		//nextButton.setPreferredSize(previousButton.getPreferredSize());
		
		navigationPanel.add(monthLabel, BorderLayout.CENTER);
		navigationPanel.add(navigationButtonPanel, BorderLayout.WEST);
		navigationPanel.add(navigationTopRightPanel, BorderLayout.EAST);
		
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}
		});
		previousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previous();
				
			}
		});
		todayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				display(DateTime.now());
			}
		});
		
		

		// layout code
		mainCalendarView.setBackground(Colors.TABLE_BACKGROUND);
		mainCalendarView.setLayout(new BorderLayout());
		top.setLayout(new GridLayout(1, 7));
		
		mainCalendarView.add(top, BorderLayout.NORTH);
		mainCalendarView.add(inside, BorderLayout.CENTER);
		
		this.add(mainCalendarView, BorderLayout.CENTER);
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
	 * Add a list of events
	 * @param events
	 */
	public void addEvents(List<Event> events)
	{
		Collections.sort(events, new Comparator<Event>(){
			@Override
			public int compare(Event e, Event e2)
			{
				return e.getStartTime().compareTo(e2.getStartTime());
			}
		});
		
		for(Event e : events)
		{
			this.addEvent(e);
		}
	}
	
	/**
	 * Add an event
	 * @param e
	 */
	public void addEvent(Event e)
	{
		MonthDay md = this.days.get(e.getStartTime().getDayOfYear());
		md.addEvent(e);
	}
	/**
	 * Remove a single event
	 * @param e
	 */
	public void removeEvent(Event e)
	{
		MonthDay md = this.days.get(e.getStartTime().getDayOfYear());
		md.removeEvent(e);
	}
	/**
	 * Remove a list of events
	 * @param events
	 */
	public void removeEvents(List<Event> events)
	{
		for(Event e : events)
		{
			this.removeEvent(e);
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
	 * @param referenceDay what month should we display
	 */
	protected void generateDays(MutableDateTime referenceDay)
	{
		// reset to the first of the month at midnight, then find Sunday
		referenceDay.setDayOfMonth(1);
		referenceDay.setMillisOfDay(0);
		int first = (referenceDay.getDayOfWeek() % 7);
		int daysInView = first + referenceDay.dayOfMonth().getMaximumValue();
		int weeks = (int)Math.ceil(daysInView / 7.0);
		
		inside.setLayout(new java.awt.GridLayout(weeks, 7));
		referenceDay.addDays(-first);

		// remove all old days
		inside.removeAll();

		// generate days, weeks*7 covers all possible months, so we just loop through and add each day
		for (int i = 0; i < (weeks*7); i++)
		{
			MonthDay md = new MonthDay(referenceDay.toDateTime(), getMarker(referenceDay));
			inside.add(md);
			md.reBorder(i < 7, (i % 7 ) == 0, i >= 5 * 7);
			this.days.put(referenceDay.getDayOfYear(), md);
			referenceDay.addDays(1); // go to next day
		}
		
		monthLabel.setText(this.getTime().toString(Months.monthLblFormat));

		// notify mini-calendar to change
		mainPanel.miniMove(time);
		
		// repaint when changed
		inside.revalidate();
	}

	/**
	 * Gets the DayStyle of given date
	 * @param date
	 * @return
	 */
	protected DayStyle getMarker(ReadableDateTime date)
	{
		if (date.getMonthOfYear() == time.getMonthOfYear())
		{
			return (isToday(date) ? DayStyle.Today: DayStyle.Normal);
		}
		else
			return DayStyle.OutOfMonth;
	}

	public DateTime getTime()
	{
		return time;
	}
}
