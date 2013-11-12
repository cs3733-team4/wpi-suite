package edu.wpi.cs.wpisuitetng.modules.cal.year;

import javax.swing.JComponent;

public class DefaultCalendarYearModule implements CalendarYearModule {

	private int month, year;
	private CalendarYearModule previous, next;
	private MiniMonth calendar;
	
	public DefaultCalendarYearModule(int month, int year, int preload)
	{
		this.month = month;
		this.year = year;
		this.preloadFollowing(preload);
		this.preloadPrevious(preload);
	}
	
	@Override
	public CalendarYearModule getPrevious() {
		return previous;
	}

	@Override
	public CalendarYearModule getFollowing() {
		return next;
	}

	@Override
	public void preloadPrevious(int depth)
	{
		if (depth > 0)
		{
			if (previous == null)
			{
				if (month == 1)
				{
					previous = new DefaultCalendarYearModule(12, year-1, depth-1);
				}
				else
				{
					previous = new DefaultCalendarYearModule(month-1, year, depth-1);
				}
			}
			else {
				previous.preloadPrevious(depth-1);
			}
		}
		else
		{
			previous = null;
		}
	}

	@Override
	public void preloadFollowing(int depth)
	{
		if (depth > 0)
		{
			if (previous == null)
			{
				if (month == 12)
				{
					next = new DefaultCalendarYearModule(1, year+1, depth-1);
				}
				else
				{
					next = new DefaultCalendarYearModule(month+1, year, depth-1);
				}
			}
			else {
				previous.preloadFollowing(depth-1);
			}
		}
		else
		{
			previous = null;
		}
	}

	@Override
	public JComponent renderComponent()
	{
		if (calendar == null)
		{
			calendar = new MiniMonth(this.month, this.year);
		}
		return calendar;
	}
	
	@Override
	public int getCurrentMonth()
	{
		return this.month;
	}
}
