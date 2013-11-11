package edu.wpi.cs.wpisuitetng.modules.cal.year;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.joda.time.DateTime;

public class YearCalendarHolder extends JPanel {
	
	private CalendarYearModule calendarPreloader;
	private JComponent miniCalendar;
	
	public YearCalendarHolder(DateTime date)
	{
		calendarPreloader = new DefaultCalendarYearModule(date.getMonthOfYear(), date.getYear(), 1);
		this.miniCalendar = this.calendarPreloader.renderComponent();
		this.add(miniCalendar);
	}
	
	public void update()
	{
		this.remove(miniCalendar);
		this.miniCalendar = this.calendarPreloader.renderComponent();
		this.add(miniCalendar);
		this.revalidate();
		this.repaint();
	}
	
}
