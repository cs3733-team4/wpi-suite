package edu.wpi.cs.wpisuitetng.modules.cal.year;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.MonthCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;

public class MiniMonth extends JPanel {

	/**
	 * random numbers ftw
	 */
	private static final long serialVersionUID = 153498539485L;

	/**
	 * space for holding all the days
	 */
	private DayLabel[] days = new DayLabel[49]; // 6*7

	public MiniMonth(DateTime time, final MainPanel mc) {
		this.setLayout(new GridLayout(7, 7));
		int daysThisMonth = time.dayOfMonth().getMaximumValue();
		MutableDateTime prevMonth = new MutableDateTime(time);
		prevMonth.setDayOfMonth(1);
		int startingDayThisMonth = prevMonth.getDayOfWeek();
		prevMonth.addMonths(-1);
		int daysLastMonth = prevMonth.dayOfMonth().getMaximumValue();
		String[] dayLabel = {"S", "M", "T", "W", "R", "F", "S"};

		MouseListener monthChanger = new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent me) {}
			@Override
			public void mouseEntered(MouseEvent me) {}
			@Override
			public void mouseExited(MouseEvent me) {}
			@Override
			public void mousePressed(MouseEvent me) {}
			@Override
			public void mouseReleased(MouseEvent me) {
				DayLabel d = (DayLabel)(me.getSource());
				if (!(d instanceof DescriptiveDayLabel))
				{
					mc.getMOCA().display(d.getMonth());
				}
			}
		};
		
		for (int i = 0; i < 49; i++) // 6*7
		{
			if (i < 7) {
				days[i] = new DescriptiveDayLabel(dayLabel[i], time);
			}
			else if (i-7 < startingDayThisMonth) { // display some days of prev. month
				days[i] = new InactiveDayLabel(daysLastMonth - startingDayThisMonth + i - 6, Months.prevMonth(time));
			} else if (i-7 > daysThisMonth + startingDayThisMonth - 1) {
				days[i] = new InactiveDayLabel(i - daysThisMonth - startingDayThisMonth - 6, Months.nextMonth(time));
			} else {
				days[i] = new ActiveDayLabel(i - startingDayThisMonth - 6, time);
			}
			this.add(days[i]);
			days[i].addMouseListener(monthChanger);
		}
	}

	@SuppressWarnings("serial")
	private class DayLabel extends JLabel {
		private DateTime day;
		
		public DayLabel(int day, DateTime time) {
			this.setForeground(Color.BLACK);
			this.setText(Integer.toString(day));
			this.day = time;
		}
		
		public DayLabel(String day, DateTime time) {
			this.setForeground(Color.BLACK);
			this.setText(day);
			this.day = time;
		}
		
		public DateTime getMonth()
		{
			return day;
		}
	}

	@SuppressWarnings("serial")
	private class ActiveDayLabel extends DayLabel {
		public ActiveDayLabel(int day, DateTime time) {
			super(day, time);
			this.setBackground(Color.WHITE);
		}
	}

	@SuppressWarnings("serial")
	private class InactiveDayLabel extends DayLabel {
		public InactiveDayLabel(int day, DateTime time) {
			super(day, time);
			this.setBackground(Color.LIGHT_GRAY);
		}
	}
	
	@SuppressWarnings("serial")
	private class DescriptiveDayLabel extends DayLabel {
		public DescriptiveDayLabel(String day, DateTime time) {
			super(day, time);
			this.setBackground(Color.LIGHT_GRAY);
		}
	}

}
