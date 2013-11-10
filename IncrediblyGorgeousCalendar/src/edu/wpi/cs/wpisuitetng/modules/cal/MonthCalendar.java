/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

/**
 *
 * @author patrick
 */
public class MonthCalendar extends JPanel
{
	JPanel inside = new JPanel(), top = new JPanel();
	public MonthCalendar(DateTime on)
	{
        setBackground(new java.awt.Color(255, 255, 255));
        inside.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 0, new java.awt.Color(0, 0, 0)));
		setLayout(new BorderLayout());
        inside.setLayout(new java.awt.GridLayout(6, 7));
		add(top, BorderLayout.NORTH);
		add(inside, BorderLayout.CENTER);
		top.setLayout(new GridLayout(1, 7));

		MutableDateTime fom = new MutableDateTime(on);
		fom.setDayOfMonth(1);
		fom.setMillisOfDay(0);
		int first = (fom.getDayOfWeek() % 7);
		fom.addDays(-first);

		// generate days
		MutableDateTime weeks = new MutableDateTime(fom);
		for (int i = 0; i < 7; i++)
		{
			JLabel jl = new JLabel(weeks.dayOfWeek().getAsText());
			jl.setHorizontalAlignment(SwingConstants.CENTER);
			weeks.addDays(1);
			top.add(jl);
		}

		for (int i = 0; i < (6*7); i++)
		{
			inside.add(new MonthDay(fom.toDateTime(), randItems(fom.toDateTime()), (fom.getMonthOfYear() == on.getMonthOfYear() ? (isToday(fom) ? DayStyle.Today: DayStyle.Normal): DayStyle.OutOfMonth)));
			fom.addDays(1);
		}
	}

	private MonthItem[] randItems(DateTime dt)
	{
		MutableDateTime mtd = new MutableDateTime(dt);
		mtd.addHours(9);
		DateTime a = mtd.toDateTime();
		mtd = new MutableDateTime(dt);
		mtd.addHours(13);
		DateTime b = mtd.toDateTime();
		mtd = new MutableDateTime(dt);
		mtd.addHours(16);
		mtd.addMinutes(30);
		DateTime c = mtd.toDateTime();

		if (Math.random() > .8)
		{
			return new MonthItem[]{new MonthItem(a, "Meeting")};
		}
		else if (Math.random() > .7)
		{
			return new MonthItem[]{new MonthItem(b, "Eat Pizza")};
		}
		else if (Math.random() > .6)
		{
			return new MonthItem[]{new MonthItem(b, "Eat Pizza"),new MonthItem(a, "Meeting")};
		}
		else if (Math.random() > .5)
		{
			return new MonthItem[]{new MonthItem(c, "Watch videos"),new MonthItem(a, "Meeting")};
		}
		else if (Math.random() > .4)
		{
			return new MonthItem[]{new MonthItem(c, "Watch videos"),new MonthItem(a, "Meeting"),new MonthItem(b, "Eat Pizza")};
		}

		return new MonthItem[]{};
	}

	private boolean isToday(MutableDateTime fom)
	{
		DateTime now = DateTime.now();
		return fom.getYear() == now.getYear() && fom.getDayOfYear() == now.getDayOfYear();
	}

}
