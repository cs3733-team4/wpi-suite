package edu.wpi.cs.wpisuitetng.modules.cal.year;

import javax.swing.JComponent;

public class DefaultCalendarYearModule implements CalendarYearModule {

	int month, year;
	CalendarYearModule previous, next;
	
	public DefaultCalendarYearModule(int month, int year)
	{
		this.month = month;
		this.year = year;
		this.preloadFollowing(1);
		this.preloadPrevious(1);
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
	public void preloadPrevious(int depth) {
		if (depth > 0)
		{
			if (previous == null)
			{
				if (month == 1)
				{
					previous = new DefaultCalendarYearModule(12, year-1);
				}
				else
				{
					previous = new DefaultCalendarYearModule(month-1, year);
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
	public void preloadFollowing(int depth) {
		if (depth > 0)
		{
			if (previous == null)
			{
				if (month == 12)
				{
					next = new DefaultCalendarYearModule(1, year+1);
				}
				else
				{
					next = new DefaultCalendarYearModule(month+1, year);
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
	public JComponent renderComponent() {
		// TODO Auto-generated method stub
		return null;
	}

}
