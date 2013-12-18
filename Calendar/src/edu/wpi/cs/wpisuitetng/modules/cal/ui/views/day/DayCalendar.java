/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.lowagie.text.Font;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.CommitmentClient;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.EventClient;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.collisiondetection.DayPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Months;

public class DayCalendar extends AbstractCalendar
{

	private DateTime time;
	private DateTime dayStart;
	private DateTime dayEnd;
	
	private DayPanel current;
	
	private List<Displayable> displayableList = new ArrayList<Displayable>();
	
	private JPanel holder = new JPanel();
	private JScrollPane scroll = new JScrollPane(holder);

	private DateTimeFormatter titleFmt = DateTimeFormat.forPattern("EEEE, MMM d, yyyy");

	public DayCalendar(DateTime on)
	{
		this.time = on;
		
		MutableDateTime mdt = time.toMutableDateTime();
		mdt.setMillis(0);
		this.dayStart = mdt.toDateTime();
		mdt.addDays(1);
		mdt.addMillis(-1);
		this.dayEnd = mdt.toDateTime();
		
		scroll.setBackground(Colors.TABLE_BACKGROUND);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		holder.setBackground(Colors.TABLE_BACKGROUND);
		
		this.setLayout(new BorderLayout());
		this.holder.setLayout(new BorderLayout());
		
		generateDay();
	}

	private void generateDay()
	{
		this.holder.removeAll();
		this.removeAll();
		this.add(scroll, BorderLayout.CENTER);
		
		JLabel dayTitle = new JLabel(time.toString(titleFmt));
		dayTitle.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 25));
		dayTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(dayTitle, BorderLayout.NORTH);

		this.displayableList = getVisibleDisplayables();
		
		this.current = new DayPanel();
		this.current.setEvents(getDisplayablesInInterval(time,time.plusDays(1)), time);
		this.current.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Colors.BORDER));
		
		this.holder.add(new DayGridLabel(), BorderLayout.WEST);
		this.holder.add(this.current, BorderLayout.CENTER);
		
		BoundedRangeModel jsb = scroll.getVerticalScrollBar().getModel();
		double day = time.getMinuteOfDay();
		day /= time.minuteOfDay().getMaximumValue();
		day *= (jsb.getMaximum()) - jsb.getMinimum();
		jsb.setValue((int)day);
		
		// notify mini-calendar to change
		MainPanel.getInstance().miniMove(time);
	}
	
	/**
	 * Get visible events for the current day view
	 * @return returns the list of events to display
	 */
	private List<Displayable> getVisibleDisplayables()
	{
		// Set up from and to datetime for search
		MutableDateTime f = new MutableDateTime(time);
		f.setMillisOfDay(0);
		DateTime from = f.toDateTime();
		f.addDays(1);
		DateTime to = f.toDateTime();
		
		// Return list of events to be displayed
		List<Displayable> visibleDisplayables = new ArrayList<Displayable>();
		visibleDisplayables.addAll(EventClient.getInstance().getEvents(from, to));
		visibleDisplayables.addAll(CommitmentClient.getInstance().getCommitments(from, to));
		
		Collections.sort(visibleDisplayables, new Comparator<Displayable>() {
			public int compare(Displayable d1, Displayable d2) {
		        return d1.getInterval().getStart().getMinuteOfDay() < d2.getInterval().getStart().getMinuteOfDay() ? -1 :
		        		d1.getInterval().getStart().getMinuteOfDay() > d2.getInterval().getStart().getMinuteOfDay() ? 1 : 0;
		    }
		});
		
		return visibleDisplayables;
	}

	@Override
	public void next()
	{
		display(Months.nextDay(this.time));
	}

	@Override
	public void previous()
	{
		display(Months.prevDay(this.time));
	}

	@Override
	public void display(DateTime newTime)
	{
		this.time = newTime;
		this.generateDay();

		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				// Scroll to now
				BoundedRangeModel jsb = scroll.getVerticalScrollBar().getModel();
				
				double day;
				
				if(!displayableList.isEmpty())
				{
					day = displayableList.get(0).getInterval().getStart().getMinuteOfDay();
				}else
				{
					day = DateTime.now().getMinuteOfDay();
				}
				
				day-= (day > 60) ? 60 : day;
				
				day /= time.minuteOfDay().getMaximumValue();
				day *= (jsb.getMaximum()-jsb.getMinimum());
				jsb.setValue((int) day);
			}
		});

		MainPanel.getInstance().revalidate();
		MainPanel.getInstance().repaint();
	}

	@Override
	public void updateDisplayable(Displayable events, boolean added)
	{
		// at the moment, we don't care, and just re-pull from the DB. TODO: this should change
		this.generateDay();
	}

	@Override
	public void select(Displayable item)
	{
		current.select(item);		
	}
	
	@Override
	public void setSelectedDay(DateTime time)
	{
		
	}
	
	/**
	 * Gets all the events in the week that also are in the given interval
	 * @param intervalStart start of the interval to check
	 * @param intervalEnd end of the interval to check
	 * @return list of events that are both in the week and interval
	 */
	private List<Displayable> getDisplayablesInInterval(DateTime intervalStart, DateTime intervalEnd)
	{
		List<Displayable> retrievedDisplayables = new ArrayList<>();
		Interval mInterval = new Interval(intervalStart, intervalEnd);

		for (Displayable d : displayableList)
		{
			if (d.getInterval().toDuration().getStandardHours()>24)
				continue;

			if (isDisplayableInInterval(d, mInterval))
			{
				retrievedDisplayables.add(d);
			}
		}

		return retrievedDisplayables;
	}
	
	private boolean isDisplayableInInterval(Displayable mDisplayable, Interval mInterval)
	{
		DateTime s = mDisplayable.getInterval().getStart(), e = mDisplayable.getInterval().getEnd();
		if (this.time.isAfter(s))
			s = this.time;

		return (s.isBefore(e) && mInterval.contains(s));
	}
}
