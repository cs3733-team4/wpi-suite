package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class MiniCalendarPanel extends JPanel
{
	private CalendarNavigationModule calendarPreloader;
	private JComponent miniCalendar;
	private JLabel monthName;
	private DateTime currentDate;
	private MiniCalendarHostIface mainPanel;

	
	public MiniCalendarPanel(DateTime date, MiniCalendarHostIface mainPanel) {
		init(date, mainPanel);
	}
	
	
	// Initialize variables.
	public void init(DateTime date, MiniCalendarHostIface mainPanel) {
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
		prevButton.setBackground(UIManager.getDefaults().getColor("Panel.background"));
		nextButton.setFocusable(false);
		nextButton.setBackground(UIManager.getDefaults().getColor("Panel.background"));
		
		prevButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		nextButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		titlePane.add(monthName, BorderLayout.CENTER);
		
		calendarPreloader = new CalendarNavigationModule(date, mainPanel);
		this.miniCalendar = this.calendarPreloader.renderComponent();
		
		this.add(miniCalendar, BorderLayout.CENTER);
		this.add(titlePane, BorderLayout.NORTH);

		//add event listeners
		nextButton.addActionListener(nextListener);
		prevButton.addActionListener(prevListener);
				
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
	
}