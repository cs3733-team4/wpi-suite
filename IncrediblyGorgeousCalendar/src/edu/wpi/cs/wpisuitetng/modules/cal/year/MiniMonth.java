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

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;

public class MiniMonth extends JPanel {

	/**
	 * random numbers ftw
	 */
	private static final long serialVersionUID = 153498539485L;

	/**
	 * space for holding all the days
	 */
	private DayButton[] days = new DayButton[42]; //6*7

	public MiniMonth(DateTime time)
	{
		this.setLayout(new GridLayout(6,7));
		int daysThisMonth = time.dayOfMonth().getMaximumValue();
		MutableDateTime prevMonth = new MutableDateTime(time);
		prevMonth.setDayOfMonth(1);
		int startingDayThisMonth = prevMonth.getDayOfWeek();
		prevMonth.addMonths(-1);
		int daysLastMonth = prevMonth.dayOfMonth().getMaximumValue();
		
		for(int i = 0; i < 42; i++) //6*7
		{
			if (i < startingDayThisMonth)
			{ // display some days of the previous month
				days[i] = new InactiveDayButton(daysLastMonth-startingDayThisMonth+i+2);
			}
			else if (i > daysThisMonth+startingDayThisMonth-1)
			{
				days[i] = new InactiveDayButton(i-daysThisMonth-startingDayThisMonth+2);
			}
			else
			{
				days[i] = new ActiveDayButton(i-startingDayThisMonth+1);
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
