package edu.wpi.cs.wpisuitetng.modules.cal.year;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;

public class YearCalendarHolder extends JPanel {
	
	private CalendarYearModule calendarPreloader;
	private JComponent miniCalendar;
	private JLabel monthName;
	
	public YearCalendarHolder(DateTime date)
	{
		setUpUI(date);
	}
	
	private void setUpUI(final DateTime date)
	{
		monthName = this.getMonthLabel(date);
		this.removeAll();
		this.setLayout(new BorderLayout());
		
		JPanel titleBar = new JPanel();
		JButton next = new JButton(">");
		JButton prev = new JButton("<");
		
		titleBar.setLayout(new BorderLayout());
		
		titleBar.add(next, BorderLayout.EAST);
		titleBar.add(prev, BorderLayout.WEST);
		
		prev.setFocusable(false);
		prev.setBorder(null);
		next.setFocusable(false);
		next.setBorder(null);
		
		titleBar.add(monthName, BorderLayout.CENTER);
		
		calendarPreloader = new CalendarYearModule(date);
		this.miniCalendar = this.calendarPreloader.renderComponent();
		
		this.add(miniCalendar, BorderLayout.CENTER);
		this.add(titleBar, BorderLayout.NORTH);
		
		ActionListener prevListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setUpUI(Months.prevMonth(date));
			}
		};
		
		ActionListener nextListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setUpUI(Months.nextMonth(date));
			}
		};
		
		next.addActionListener(nextListener);
		prev.addActionListener(prevListener);
		
		this.revalidate();
		this.repaint();
	}
	
	private JLabel getMonthLabel(DateTime dt)
	{
		return new JLabel(dt.monthOfYear().getAsText(), JLabel.CENTER);
	}
}
