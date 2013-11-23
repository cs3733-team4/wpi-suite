package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import javax.swing.JComponent;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;

public class CalendarNavigationModule{

	private DateTime time;
	private MiniMonth calendar;
	private MiniCalendarHostIface mc;
	
	public CalendarNavigationModule(DateTime time, MiniCalendarHostIface mc)
	{
		this.mc = mc;
		this.time = time;
	}
	
	public CalendarNavigationModule getPrevious() {
		MutableDateTime next = new MutableDateTime(time);
		next.addMonths(-1);
		return new CalendarNavigationModule(next.toDateTime(), mc);
	}
	
	/**
	 * Added getTime for testing purposes
	 * @return the time of the CalendarYearModule object
	 */
	
	public DateTime getTime(){
		return time;
	}

	public CalendarNavigationModule getFollowing() {
		MutableDateTime next = new MutableDateTime(time);
		next.addMonths(1);
		return new CalendarNavigationModule(next.toDateTime(), mc);
	}

	public JComponent renderComponent()
	{
		if (calendar == null)
		{
			calendar = new MiniMonth(time, mc);
		}
		return calendar;
	}
}
