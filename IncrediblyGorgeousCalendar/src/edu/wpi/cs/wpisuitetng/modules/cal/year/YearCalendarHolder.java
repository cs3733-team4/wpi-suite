package edu.wpi.cs.wpisuitetng.modules.cal.year;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;

public class YearCalendarHolder extends JPanel
{
	
	private CalendarYearModule calendarPreloader;
	private JComponent miniCalendar;
	private JLabel monthName;
	private DateTime currentDate;
	private MainPanel mainPanel;
	private JLabel gotoErrorText;
	final private static DateTimeFormatter gotoField = DateTimeFormat.forPattern("M/d/yy");
	final private static DateTimeFormatter gotoFieldShort = DateTimeFormat.forPattern("M/d");
	
	public YearCalendarHolder(DateTime date, MainPanel mainPanel)
	{
		this.setPreferredSize(new Dimension(200, 250));
		currentDate = date;
		this.mainPanel = mainPanel;
		display(date);
	}
	
	public void display(DateTime date)
	{
		monthName = new JLabel(date.toString(Months.monthLblFormat), JLabel.CENTER);
		monthName.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		this.removeAll();
		this.setLayout(new BorderLayout());
		
		JPanel titleBar = new JPanel();
		JButton next = new JButton(">");
		JButton prev = new JButton("<");
		
		//adding goto pane
		JPanel gotoPane = new JPanel();
		JButton gotoToday = new JButton("Go to Today");
		
		final JTextField gotoDate = new JTextField(date.toString(gotoField));
		JLabel gotoDateText = new JLabel("Go to: ");
		gotoErrorText = new JLabel(" ");
		gotoErrorText.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton updateGoto = new JButton(">");
		
		//adding text field pane to goto pane
		JPanel gotoDatePane = new JPanel();
		gotoDatePane.setLayout(new BorderLayout());
		gotoDatePane.add(gotoDate, BorderLayout.CENTER);
		gotoDatePane.add(gotoDateText,BorderLayout.WEST);
		gotoDatePane.add(updateGoto, BorderLayout.EAST);
		
		gotoPane.setLayout(new BorderLayout());
		gotoPane.add(gotoToday, BorderLayout.NORTH);
		gotoPane.add(gotoDatePane, BorderLayout.CENTER);
		gotoPane.add(gotoErrorText, BorderLayout.SOUTH);
	
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

		next.addActionListener(nextListener);
		prev.addActionListener(prevListener);
		gotoToday.addActionListener(todayListener);
		updateGoto.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parseGoto(gotoDate.getText());
			}
		});
		gotoDate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parseGoto(gotoDate.getText());
			}
		});
		
				
		currentDate = date;
		
		this.revalidate();
		this.repaint();
	}
	

	ActionListener prevListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			display(Months.prevMonth(currentDate));
		}
	};
	
	ActionListener nextListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			display(Months.nextMonth(currentDate));
		}
	};
	
	//action listener for gotoToday
	ActionListener todayListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e)
		{
			mainPanel.display(DateTime.now());
		}
	};
	
	private void parseGoto(String text)
	{
		DateTime dt;
		boolean isValidYear=true;
		try
		{
			dt = gotoField.parseDateTime(text);
			if(dt.getYear() < 1900 || dt.getYear() > 2100)
			{
				isValidYear=false;
				dt = null;
			}
		}
		catch (IllegalArgumentException illArg)
		{
			try
			{
				MutableDateTime mdt = gotoFieldShort.parseMutableDateTime(text);
				mdt.setYear(currentDate.getYear()); // this format does not provide years. add it
				dt = mdt.toDateTime();						
			}
			catch (IllegalArgumentException varArg)
			{
				dt = null;
			}
		}
		if (dt != null)
			mainPanel.display(dt);
		else
		{
			if(isValidYear)
				gotoErrorText.setText("Use format: mm/dd/yy");
			else
				gotoErrorText.setText("Year out of range (1900-2100)");
		}
	}
}
