package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.joda.time.*;

/**
 *
 * @author patrick
 */
public class MonthCalendar extends JPanel
{
	private JPanel inside = new JPanel(), top = new JPanel();
	private DateTime time;

	public MonthCalendar(DateTime on)
	{
		time = on;

		// layout code
        setBackground(UIManager.getDefaults().getColor("Table.background"));
		setLayout(new BorderLayout());
        inside.setLayout(new java.awt.GridLayout(6, 7));
		top.setLayout(new GridLayout(1, 7));
		add(top, BorderLayout.NORTH);
		add(inside, BorderLayout.CENTER);
		// end layout code

		MutableDateTime fom = new MutableDateTime(on);
		fom.setDayOfMonth(1);
		fom.setMillisOfDay(0);
		int first = fom.getDayOfWeek();
		fom.addDays(-first);

		// generate days on top
		for (int i = 0; i < 7; i++)
		{
			JLabel jl = new JLabel(fom.dayOfWeek().getAsText());
			jl.setHorizontalAlignment(SwingConstants.CENTER);
			fom.addDays(1);
			top.add(jl);
		}
		generateDays(fom);
	}

	private MonthItem[] randItems(ReadableDateTime dt)
	{
		MutableDateTime mtd = new MutableDateTime(dt);
		mtd.addHours(9);
		DateTime a = mtd.toDateTime();
		mtd = new MutableDateTime(dt);
		mtd.addHours(13);
		DateTime b = mtd.toDateTime();
		mtd = new MutableDateTime(dt);
		mtd.addHours(16);
		mtd.addMinutes(30);
		DateTime c = mtd.toDateTime();

		if (Math.random() > .8)
		{
			return new MonthItem[]{new MonthItem(a, "Meeting")};
		}
		else if (Math.random() > .7)
		{
			return new MonthItem[]{new MonthItem(b, "Eat Pizza")};
		}
		else if (Math.random() > .6)
		{
			return new MonthItem[]{new MonthItem(b, "Eat Pizza"),new MonthItem(a, "Meeting")};
		}
		else if (Math.random() > .5)
		{
			return new MonthItem[]{new MonthItem(c, "Watch videos"),new MonthItem(a, "Meeting")};
		}
		else if (Math.random() > .4)
		{
			return new MonthItem[]{new MonthItem(c, "Watch videos"),new MonthItem(a, "Meeting"),new MonthItem(b, "Eat Pizza")};
		}

		return new MonthItem[]{};
	}

	boolean isToday(ReadableDateTime fom)
	{
		DateTime now = DateTime.now();
		return fom.getYear() == now.getYear() && fom.getDayOfYear() == now.getDayOfYear();
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
		int first = referenceDay.getDayOfWeek();
		referenceDay.addDays(-first);

		// remove all old days
		inside.removeAll();

		// generate days, 6*7 covers all possible months, so we just loop through and add each day
		for (int i = 0; i < (6*7); i++)
		{
			MonthDay md = new MonthDay(referenceDay.toDateTime(), randItems(referenceDay), getMarker(referenceDay));
			inside.add(md);
			md.reBorder(i < 7, (i % 7 ) == 0, i >= 5 * 7);
			referenceDay.addDays(1); // go to next day
		}

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
