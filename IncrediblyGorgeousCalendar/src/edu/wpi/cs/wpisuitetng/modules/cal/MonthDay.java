/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.cs.wpisuitetng.modules.cal;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.joda.time.DateTime;

/**
 *
 * @author patrick
 */
public class MonthDay extends JPanel
{
	JLabel header = new JLabel();
	public MonthDay(DateTime day, MonthItem[] items, DayStyle style)
	{

		int grayit = 0;
		switch (style)
		{
			case Normal:
				grayit = 204;
				break;
			case OutOfMonth:
				grayit = 245;
				break;
			case Today:
				System.out.println("Today!");
				grayit = 153;
				break;
		}
        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        header.setBackground(new java.awt.Color(grayit, grayit, grayit));
        header.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        header.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        header.setText(Integer.toString(day.getDayOfMonth()));
        header.setAutoscrolls(true);
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        header.setMaximumSize(new java.awt.Dimension(10000, 17));
        header.setOpaque(true);
        add(header);

		Arrays.sort(items, new Comparator<MonthItem>() {

			@Override
			public int compare(MonthItem o1, MonthItem o2)
			{
				return o1.getWhen().compareTo(o2.getWhen());
			}
		});
		for (MonthItem monthItem : items)
		{
			add(monthItem);
		}
	}
}
