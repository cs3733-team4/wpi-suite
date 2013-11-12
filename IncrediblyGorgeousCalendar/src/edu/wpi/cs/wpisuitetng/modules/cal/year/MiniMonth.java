package edu.wpi.cs.wpisuitetng.modules.cal.year;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;

public class MiniMonth extends JPanel {

	/**
	 * random numbers ftw
	 */
	private static final long serialVersionUID = 153498539485L;

	/**
	 * space for holding all the days
	 */
	private DayButton[] days = new DayButton[42];

	public MiniMonth(int monthNumber, int yearNumber)
	{
		this.setLayout(new GridLayout(6,7));
		int daysThisMonth = Months.getDaysInMonth(monthNumber, yearNumber);
		int daysLastMonth = Months.getDaysInMonth(monthNumber==1?12:monthNumber-1, monthNumber==1?yearNumber-1:yearNumber);
		int startingDayThisMonth = Months.getStartingDay(yearNumber, monthNumber);
		
		System.out.println(monthNumber+":  "+daysThisMonth);
		
		for(int i = 0; i < 42; i++)
		{
			if (i+1 < startingDayThisMonth)
			{ // display some days of the previous month
				days[i] = new InactiveDayButton(daysLastMonth-startingDayThisMonth+i+2);
			}
			else if (i > daysThisMonth+startingDayThisMonth-2)
			{
				days[i] = new InactiveDayButton(i-daysThisMonth-startingDayThisMonth+2);
			}
			else
			{
				days[i] = new ActiveDayButton(i-startingDayThisMonth+2);
			}
			this.add(days[i]);
		}
	}

	private class DayButton extends JButton {
		public DayButton(int day) {
			this.setForeground(Color.black);
			this.setBorder(new CompoundBorder(new LineBorder(Color.black),
					new EmptyBorder(5, 5, 5, 5)));
			this.setText(new StringBuilder().append(day).toString());
		}
	}

	private class ActiveDayButton extends DayButton {
		public ActiveDayButton(int day) {
			super(day);
			this.setEnabled(true);
			this.setBackground(Color.white);
		}
	}

	private class InactiveDayButton extends DayButton {
		public InactiveDayButton(int day) {
			super(day);
			this.setEnabled(false);
			this.setBackground(Color.gray);
		}
	}

}
