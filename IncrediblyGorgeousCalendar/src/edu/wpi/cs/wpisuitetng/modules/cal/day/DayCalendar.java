package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

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

	public DayCalendar(DateTime on, EventModel emodel)
	{
		this.mainPanel = MainPanel.getInstance();
		this.time = on;
		eventModel = emodel;
		this.setBackground(Colors.TABLE_BACKGROUND);
		
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
		this.add(new JLabel(new StringBuilder().append(time.monthOfYear().getAsText())
											   .append("  ")
											   .append(String.valueOf(time.getDayOfMonth()))
											   .toString()),
							BorderLayout.NORTH);

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
		// TODO: this is where filtering should go
		return eventModel.getEvents(from, to);
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
