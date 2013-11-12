package edu.wpi.cs.wpisuitetng.modules.cal.year;

import javax.swing.JComponent;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

public class CalendarYearModule{

	private DateTime time;
	private MiniMonth calendar;
	
	public CalendarYearModule(DateTime time)
	{
		this.time = time;
	}
	
	public CalendarYearModule getPrevious() {
		MutableDateTime next = new MutableDateTime(time);
		next.addMonths(-1);
		return new CalendarYearModule(next.toDateTime());
	}
	
	/**
	 * Added getTime for testing purposes
	 * @return the time of the CalendarYearModule object
	 */
	
	public DateTime getTime(){
		return time;
	}

	public CalendarYearModule getFollowing() {
		MutableDateTime next = new MutableDateTime(time);
		next.addMonths(1);
		return new CalendarYearModule(next.toDateTime());
	}

	public JComponent renderComponent()
	{
		if (calendar == null)
		{
			calendar = new MiniMonth(time);
		}
		return calendar;
	}
}
