/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.cs.wpisuitetng.modules.cal.month;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.DayStyle;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;


public class MonthDay extends JPanel
{ 	
	private boolean borderTop;
	JLabel header = new JLabel();
	private List<Event> items = new ArrayList<Event>();

	public MonthDay(DateTime day, DayStyle style)
	{
		Color grayit, textit = Colors.TABLE_TEXT,
			bg = Colors.TABLE_BACKGROUND;
		switch (style)
		{
			case Normal:
				grayit = Colors.TABLE_GRAY_HEADER;
				textit = Colors.TABLE_GRAY_TEXT;
				break;
			case OutOfMonth:
				grayit = bg;
				break;
			case Today:
				grayit = Colors.SELECTED_BACKGROUND;
				textit = Colors.SELECTED_TEXT;
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
	}

	public void reBorder(boolean top, boolean left, boolean bottom)
	{
		setBorder(javax.swing.BorderFactory.createMatteBorder((top || borderTop) ? 1 : 0, left ? 1 : 0, bottom ? 1 : 0, 1, Colors.BORDER));
	}
	/**
	 * Add an event to a given day of the month
	 * @param e
	 */
	public void addEvent(Event e)
	{
		this.items.add(e);
		revalidate();
	}
	
	/**
	 * Remove an event from a given day of the month
	 * @param e
	 */
	public void removeEvent(Event e)
	{
		this.items.remove(e);
		revalidate();
	}
	
	// call revalidate, not this method directly, it is an override
	@Override
	public void doLayout()
	{
		int total = this.getHeight();
		int hidden = 0;
		removeAll();
		add(header);
		total -= header.getHeight();
		if (items!=null){
			for (Event elt : this.items)
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
						this.add(MonthItem.generateFrom(elt));
					}
				}
			}
		}
		if (hidden == 1) // silly, add it anyway
		{
			this.add(MonthItem.generateFrom(this.items.get(this.items.size() - 1)));
			
			
		}
		else if (hidden > 1)
		{
			this.add(new CollapsedMonthItem(hidden));
		}
		super.doLayout();
	}
	
	// Added for testing purposes
	boolean hasEvent(Event e)
	{
		return items.contains(e);
	}

	public void clear()
	{
		items.clear();
		revalidate();
	}
}
