package edu.wpi.cs.wpisuitetng.modules.cal.year;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;

public class YearCalendarHolder extends JPanel {
	
	private CalendarYearModule calendarPreloader;
	private JComponent miniCalendar;
	private JLabel titleBar;
	
	public YearCalendarHolder(DateTime date)
	{
		JPanel p = new JPanel();
		p.add(new JLabel("<"), BorderLayout.EAST);
		p.add(new JLabel(">"), BorderLayout.WEST);
		
		titleBar = new JLabel();
		p.add(titleBar, BorderLayout.CENTER);
		
		this.setLayout(null);
		calendarPreloader = new DefaultCalendarYearModule(date.getMonthOfYear(), date.getYear(), 1);
		this.miniCalendar = this.calendarPreloader.renderComponent();
		this.add(miniCalendar, BorderLayout.CENTER);
		this.add(p, BorderLayout.NORTH);
	}
	
	public void update(DateTime date)
	{
		int month = this.calendarPreloader.getCurrentMonth();
		if (month != date.getMonthOfYear())
		{
			this.remove(miniCalendar);
			this.upateTitle(date.getMonthOfYear());
			
			if (month > date.getMonthOfYear())
			{ // this will move it backwards
				this.calendarPreloader = this.calendarPreloader.getPrevious();
			}
			else if (month==1 && date.getMonthOfYear()==12)
			{
				this.calendarPreloader = this.calendarPreloader.getPrevious();
			}
			else
			{
				this.calendarPreloader = this.calendarPreloader.getFollowing();
			}
			
			this.miniCalendar = this.calendarPreloader.renderComponent();
			this.add(miniCalendar, BorderLayout.CENTER);
			this.revalidate();
			this.repaint();
		}
		
	}
	
	public void upateTitle(int month)
	{
		titleBar.setText(Months.getMonthName(month));
	}
	
}
