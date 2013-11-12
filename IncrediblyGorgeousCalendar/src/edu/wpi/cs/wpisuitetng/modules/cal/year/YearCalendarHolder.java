package edu.wpi.cs.wpisuitetng.modules.cal.year;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.joda.time.DateTime;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;

public class YearCalendarHolder extends JPanel {
	
	private CalendarYearModule calendarPreloader;
	private JComponent miniCalendar;
	private JLabel monthName;
	
	public YearCalendarHolder(DateTime date, MainPanel mainPanel)
	{
		this.setPreferredSize(new Dimension(200, 200));
		setUpUI(date, mainPanel);
	}
	
	//changed MainPanel argument name from moca to mainPanel.  MainPanel contains moca...access with getMOCA()
	private void setUpUI(final DateTime date, final MainPanel mainPanel)
	{
		monthName = this.getMonthLabel(date);
		this.removeAll();
		this.setLayout(new BorderLayout());
		
		JPanel titleBar = new JPanel();
		JButton next = new JButton(">");
		JButton prev = new JButton("<");
		
		//adding in today button
		JPanel gotoPane = new JPanel();
		JButton gotoToday = new JButton("Goto Today");
		
		gotoPane.setLayout(new BorderLayout());
		gotoPane.add(gotoToday, BorderLayout.NORTH);
		//goto specified date will probably go in center pane
		
		titleBar.setLayout(new BorderLayout());
		
		titleBar.add(next, BorderLayout.EAST);
		titleBar.add(prev, BorderLayout.WEST);
		
		prev.setFocusable(false);
		prev.setBackground(UIManager.getDefaults().getColor("Panel.background"));
		next.setFocusable(false);
		next.setBackground(UIManager.getDefaults().getColor("Panel.background"));
		
		prev.setBorder(new EmptyBorder(5, 5, 5, 5));
		next.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		titleBar.add(monthName, BorderLayout.CENTER);
		
		calendarPreloader = new CalendarYearModule(date, mainPanel);
		this.miniCalendar = this.calendarPreloader.renderComponent();
		
		this.add(miniCalendar, BorderLayout.CENTER);
		this.add(titleBar, BorderLayout.NORTH);
		
		//adding goto today to sidebar pane
		this.add(gotoPane, BorderLayout.SOUTH);
		
		ActionListener prevListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setUpUI(Months.prevMonth(date), mainPanel);
			}
		};
		
		ActionListener nextListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setUpUI(Months.nextMonth(date), mainPanel);
			}
		};
		
		//action listener for gotoToday
		ActionListener todayListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
				mainPanel.getMOCA().display(DateTime.now());
			}
		};
		
		next.addActionListener(nextListener);
		prev.addActionListener(prevListener);
		gotoToday.addActionListener(todayListener);
		
		this.revalidate();
		this.repaint();
	}
	
	private JLabel getMonthLabel(DateTime dt)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(dt.monthOfYear().getAsText());
		sb.append(", ");
		sb.append(dt.year().get());
		return new JLabel(sb.toString(), JLabel.CENTER);
	}
}
