package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;

public class MiniMonth extends JPanel
{
	/**
	 * space for holding all the days
	 */
	private DayLabel[] days = new DayLabel[49]; // 6*7

	public MiniMonth(DateTime time, final MiniCalendarHostIface mc) {
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
					mc.display(d.getMonth());
				}
			}
		};
		
		for (int i = 0; i < 49; i++) // 6*7
		{
			MutableDateTime dayTime = new MutableDateTime(time);
			if (i < 7) {
				days[i] = new DescriptiveDayLabel(dayLabel[i], time);
			}
			else if (i-7 < startingDayThisMonth) { // display some days of prev. month
				dayTime.setMonthOfYear(Months.prevMonth(time).getMonthOfYear());
				dayTime.setDayOfMonth(daysLastMonth - startingDayThisMonth + i - 6);
				days[i] = new InactiveDayLabel(daysLastMonth - startingDayThisMonth + i - 6, dayTime.toDateTime());
			} else if (i-7 > daysThisMonth + startingDayThisMonth - 1) {
				dayTime.setMonthOfYear(Months.nextMonth(time).getMonthOfYear());
				dayTime.setDayOfMonth(i - daysThisMonth - startingDayThisMonth - 6);
				days[i] = new InactiveDayLabel(i - daysThisMonth - startingDayThisMonth - 6, dayTime.toDateTime());
			} else {
				dayTime.setDayOfMonth(i - startingDayThisMonth - 6);
				days[i] = new ActiveDayLabel(i - startingDayThisMonth - 6, dayTime.toDateTime());
			}
			days[i].borderize((i % 7) == 0, i >= 6*7, (i % 7) == 6);
			this.add(days[i]);
			days[i].addMouseListener(monthChanger);
		}
	}
	
	private class DayLabel extends JLabel {
		private DateTime day;
		
		public DayLabel(String day, DateTime time) {
			this.setForeground(Color.BLACK);
			this.setText(day);
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.day = time;
		}
		
		public DateTime getMonth()
		{
			return day;
		}
		
		public void borderize(boolean left, boolean bottom, boolean right)
		{
			setBorder(javax.swing.BorderFactory.createMatteBorder(0, left?1:0, bottom?1:0, right?1:0, Colors.BORDER));
		}
	}

	private class ActiveDayLabel extends DayLabel {
		public ActiveDayLabel(int day, DateTime time) {
			super(Integer.toString(day), time);
			setForeground(Colors.TABLE_TEXT);
			setBackground(Colors.TABLE_BACKGROUND);
			setOpaque(true);
		}
	}

	private class InactiveDayLabel extends DayLabel {
		public InactiveDayLabel(int day, DateTime time) {
			super(Integer.toString(day), time);
			setBackground(Colors.TABLE_GRAY_HEADER);
			setForeground(Colors.TABLE_GRAY_TEXT);
			this.setOpaque(true);
		}
	}
	
	private class DescriptiveDayLabel extends DayLabel {
		public DescriptiveDayLabel(String day, DateTime time) {
			super(day, time);
			this.setFont(getFont().deriveFont(Font.ITALIC));
			setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, Colors.BORDER));
		}
		@Override
		public void borderize(boolean left, boolean bottom, boolean right) {
			// don't screw with borders. we don't need them here
		}
	}

}
