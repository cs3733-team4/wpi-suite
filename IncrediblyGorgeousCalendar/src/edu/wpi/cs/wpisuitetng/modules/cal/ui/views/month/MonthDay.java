/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.DayStyle;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;

/**
 * UI for showing events on a day in a month view. This is the entire day block.
 */
public class MonthDay extends JPanel
{
	private boolean borderTop;
	JLabel header = new JLabel();
	private Displayable selected;
	private List<Event> events = new ArrayList<Event>();
	private List<Commitment> commitments = new ArrayList<Commitment>();
	private DateTime day;

	
	public MonthDay(DateTime initDay, DayStyle style)
	{
		this.day=initDay;
		Color grayit, textit = Colors.TABLE_TEXT, bg = Colors.TABLE_BACKGROUND;
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
		header.setFont(new java.awt.Font("DejaVu Sans",	style == DayStyle.Today ? Font.BOLD : Font.PLAIN, 12));
		header.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		header.setText(Integer.toString(initDay.getDayOfMonth()));
		header.setAutoscrolls(true);
		header.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
		header.setMaximumSize(new java.awt.Dimension(10000, 17));
		header.setOpaque(true);
		add(header);

		addMouseListener(new MouseListener()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				MainPanel.getInstance().display(day);
				MainPanel.getInstance().clearSelected();
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{

			}

			@Override
			public void mouseReleased(MouseEvent e)
			{

			}

			@Override
			public void mouseEntered(MouseEvent e)
			{

			}

			@Override
			public void mouseExited(MouseEvent e)
			{

			}
		});
	}

	public void reBorder(boolean top, boolean left, boolean bottom)
	{
		setBorder(javax.swing.BorderFactory.createMatteBorder(
				(top || borderTop) ? 1 : 0, left ? 1 : 0, bottom ? 1 : 0, 1,
				Colors.BORDER));
	}

	/**
	 * Add an event to a given day of the month
	 * 
	 * @param e
	 */
	public void addEvent(Event e)
	{
		this.events.add(e);
		revalidate();
	}

	/**
	 * Add a commitment to a given day of the month.
	 * 
	 * @param c
	 */
	public void addCommitment(Commitment c)
	{
		this.commitments.add(c);
		revalidate();
	}

	/**
	 * Remove an event from a given day of the month
	 * 
	 * @param e
	 */
	public void removeEvent(Event e)
	{
		this.events.remove(e);
		revalidate();
	}

	public void removeCommitment(Commitment c)
	{
		this.commitments.remove(c);
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
		
		ArrayList<Displayable> allitems = new ArrayList<>(events.size() + commitments.size());
		allitems.addAll(events);
		allitems.addAll(commitments);
		
		Collections.sort(allitems, new Comparator<Displayable>() {

			@Override
			public int compare(Displayable o1, Displayable o2) {
				if ((o1 instanceof Event) && (o2 instanceof Commitment))
					return -1;
				else if ((o1 instanceof Commitment) && (o2 instanceof Event))
					return 1;
				else if ((o1 instanceof Event) && (o2 instanceof Event))//both events so check multiday event
				{
					if (((Event)o1).isMultiDayEvent() && !((Event)o2).isMultiDayEvent())
						return -1;
					else if (!((Event)o1).isMultiDayEvent() && ((Event)o2).isMultiDayEvent())
						return 1;
				}
				//if it gets to this poing then they are both commitments, or both multi day events, or both single day events
				if (o1.getDate().isBefore(o2.getDate()))
					return -1;
				else//will default to 1, no need to check if they start at the same time....
					return 1;
			}
		});

		for (Displayable elt : allitems)
		{
			if (hidden > 0)
			{
				hidden++;
			}
			else
			{
				total -= 24; // TODO: don't use constant. getHeight fails when
								// slow resizing to min though...
				if (total <= 10)
				{
					hidden = 1;
				}
				else
				{
					this.add(MonthItem.generateFrom(elt, selected, day));
				}
			}
		}

		if (hidden == 1) // silly, add it anyway
		{
			this.add(MonthItem.generateFrom(allitems.get(allitems.size() - 1), selected, day));
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
		return events.contains(e);
	}

	public void clear()
	{
		events.clear();
		revalidate();
	}

	public void clearComms()
	{
		commitments.clear();
		revalidate();
	}

	public void select(Displayable item)
	{
		selected = item;
		for (Component c : getComponents())
		{
			if (c instanceof MonthItem)
			{
				MonthItem mi = ((MonthItem) c);
				mi.setSelected(mi.getDisplayable() == item);
			}
		}
	}
}
