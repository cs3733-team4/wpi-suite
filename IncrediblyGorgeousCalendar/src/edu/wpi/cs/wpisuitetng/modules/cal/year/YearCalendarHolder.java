package edu.wpi.cs.wpisuitetng.modules.cal.year;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
	final private static DateTimeFormatter gotoExampleField = DateTimeFormat.forPattern("M/d/yyyy");
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
		//Title Bar Pane
		monthName = new JLabel(date.toString(Months.monthLblFormat), JLabel.CENTER);
		monthName.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		this.removeAll();
		this.setLayout(new BorderLayout());
		
		JPanel titlePane = new JPanel();
		JButton nextButton = new JButton(">");
		JButton prevButton = new JButton("<");
	
		titlePane.setLayout(new BorderLayout());
		
		titlePane.add(nextButton, BorderLayout.EAST);
		titlePane.add(prevButton, BorderLayout.WEST);
		
		prevButton.setFocusable(false);
		nextButton.setFocusable(false);
		
		prevButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		nextButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		titlePane.add(monthName, BorderLayout.CENTER);
		
		//Goto Pane
		JPanel gotoPane = new JPanel();
		
		final JTextField gotoDateField = new JTextField(date.toString(gotoExampleField));
		JLabel gotoDateText = new JLabel("Go to: ");
		gotoErrorText = new JLabel(" ");
		gotoErrorText.setHorizontalAlignment(SwingConstants.CENTER);
		gotoErrorText.setForeground(Color.RED);
		
		JButton updateGotoButton = new JButton(">");
		updateGotoButton.setFocusable(false);
		updateGotoButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		//Goto Date Pane within Goto Pane
		JPanel gotoDatePane = new JPanel();
		gotoDatePane.setLayout(new BorderLayout());
		gotoDatePane.add(gotoDateField, BorderLayout.CENTER);
		gotoDatePane.add(gotoDateText,BorderLayout.WEST);
		gotoDatePane.add(updateGotoButton, BorderLayout.EAST);
		
		gotoPane.setLayout(new BorderLayout());
		gotoPane.add(gotoDatePane, BorderLayout.NORTH);
		gotoPane.add(gotoErrorText, BorderLayout.CENTER);
				
		calendarPreloader = new CalendarYearModule(date, mainPanel);
		this.miniCalendar = this.calendarPreloader.renderComponent();
		
		this.add(miniCalendar, BorderLayout.CENTER);
		this.add(titlePane, BorderLayout.NORTH);
		this.add(gotoPane, BorderLayout.SOUTH);

		//add event listeners
		nextButton.addActionListener(nextListener);
		prevButton.addActionListener(prevListener);
		updateGotoButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parseGoto(gotoDateField.getText());
			}
		});
		
		gotoDateField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parseGoto(gotoDateField.getText());
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
				gotoErrorText.setText("* Invalid format, use: mm/dd/yyyy");
			else
				gotoErrorText.setText("* Year out of range (1900-2100)");
		}
	}
}
