/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.joda.time.DateTime;

/**
 *
 * @author patrick
 */
public class MonthDay extends JPanel
{
	JLabel header = new JLabel();
	private boolean borderTop;
	private MonthItem[] items;

	public MonthDay(DateTime day, MonthItem[] items, DayStyle style)
	{
		Color grayit, textit = UIManager.getDefaults().getColor("Label.foreground"),
			bg = UIManager.getDefaults().getColor("Table.background");
		switch (style)
		{
			case Normal:
				grayit = UIManager.getDefaults().getColor("Table.focusCellBackground");
				textit = UIManager.getDefaults().getColor("Table.focusCellForeground");
				if (bg.equals(grayit))
				{
					if ((bg.getBlue() + bg.getGreen() + bg.getRed()) / 3.0 > 255.0 / 2) // light
						grayit = grayit.darker();
					else
						grayit = grayit.brighter();
				}
				break;
			case OutOfMonth:
				grayit = bg;
				break;
			case Today:
				grayit = UIManager.getDefaults().getColor("textHighlight");
				textit = UIManager.getDefaults().getColor("textHighlightText");
				break;
			default:
				throw new IllegalStateException("DayStyle is not a valid DayStyle!");
		}
		setBackground(bg);
		setForeground(textit);
		borderTop = grayit.equals(bg);
		setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

		header.setBackground(grayit);
		header.setForeground(textit);
		header.setFont(new java.awt.Font("DejaVu Sans", style == DayStyle.Today ? Font.BOLD : Font.PLAIN, 12));
		header.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		header.setText(Integer.toString(day.getDayOfMonth()));
		header.setAutoscrolls(true);
		header.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
		header.setMaximumSize(new java.awt.Dimension(10000, 17));
		header.setOpaque(true);
		add(header);

		Arrays.sort(items, new Comparator<MonthItem>()
		{
			@Override
			public int compare(MonthItem o1, MonthItem o2)
			{
				return o1.getWhen().compareTo(o2.getWhen());
			}
		});
		this.items = items;
		for (MonthItem monthItem : items)
		{
			add(monthItem);
		}
	}

	public void reBorder(boolean top, boolean left, boolean bottom)
	{
		setBorder(javax.swing.BorderFactory.createMatteBorder((top || borderTop) ? 1 : 0, left ? 1 : 0, bottom ? 1 : 0, 1, UIManager.getDefaults().getColor("Separator.foreground")));
	}
	
	@Override
	public void doLayout()
	{
		int total = this.getHeight();
		int hidden = 0;
		removeAll();
		add(header);
		total -= header.getHeight();
		for (MonthItem elt : this.items)
		{
			if (hidden > 0)
			{
				hidden++;
			}
			else
			{
				total -= 24; //TODO: don't use constant. getHeight fails when slow resizing to min though...
				if (total <= 10)
				{
					hidden = 1;
				}
				else
				{
					this.add(elt);
				}
			}
		}
		if (hidden == 1) // silly, add it anyway
		{
			this.add(this.items[this.items.length - 1]);
		}
		else if (hidden > 1)
		{
			this.add(new CollapsedMonthItem(hidden));
		}
		super.doLayout();
	}
}
