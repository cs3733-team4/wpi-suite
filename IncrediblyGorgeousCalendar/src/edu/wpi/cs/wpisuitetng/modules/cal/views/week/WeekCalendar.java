/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.views.week;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.lowagie.text.Font;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.DayGridLabel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.france.LouvreTour;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Months;

public class WeekCalendar extends AbstractCalendar
{

	private DateTime time;
	private MainPanel mainPanel;
	private LouvreTour sun, mon, tue, wed, thu, fri, sat;
	
	private JPanel holder = new JPanel();
	private JScrollPane scroll = new JScrollPane(holder);

	private EventModel eventModel;

	private DateTimeFormatter titleFmt = DateTimeFormat.forPattern("MMM d, yyyy");

	public WeekCalendar(DateTime on, EventModel emodel)
	{
		this.mainPanel = MainPanel.getInstance();
		this.time = on;
		eventModel = emodel;
		scroll.setBackground(Colors.TABLE_BACKGROUND);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
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
		JLabel weekTitle = new JLabel("Week of " + time.toString(titleFmt));
		weekTitle.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 25));
		weekTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(weekTitle, BorderLayout.NORTH);

		MutableDateTime increment=new MutableDateTime(Months.getWeekStart(time));
		
		this.sun = new LouvreTour();
		this.sun.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		increment.addDays(1);
		this.mon = new LouvreTour();
		this.mon.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		increment.addDays(1);
		this.tue = new LouvreTour();
		this.tue.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		increment.addDays(1);
		this.wed = new LouvreTour();
		this.wed.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		increment.addDays(1);
		this.thu = new LouvreTour();
		this.thu.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		increment.addDays(1);
		this.fri = new LouvreTour();
		this.fri.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		increment.addDays(1);
		this.sat = new LouvreTour();
		this.sat.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());

		this.holder.add(DayGridLabel.getInstance(), BorderLayout.WEST);
		this.holder.add(this.sun, BorderLayout.CENTER);
		this.holder.add(this.mon, BorderLayout.EAST);
		this.holder.add(this.tue, BorderLayout.EAST);
		this.holder.add(this.wed, BorderLayout.EAST);
		this.holder.add(this.thu, BorderLayout.EAST);
		this.holder.add(this.fri, BorderLayout.EAST);
		this.holder.add(this.sat, BorderLayout.EAST);
		// notify mini-calendar to change
		mainPanel.miniMove(Months.getWeekStart(time));
	}
	
	private List<Event> getVisibleEvents(DateTime curDay)
	{
		MutableDateTime f = new MutableDateTime(curDay);
		f.setMillisOfDay(0);
		DateTime from = f.toDateTime();
		f.addDays(1);
		DateTime to = f.toDateTime();
		// TODO: this is where filtering should go
		return eventModel.getEvents(from, to);
	}

	@Override
	public void next()
	{
		this.time = Months.nextWeek(this.time);
		this.generateDay();
	}

	@Override
	public void previous()
	{
		this.time = Months.prevWeek(this.time);
		this.generateDay();

	}

	@Override
	public void display(DateTime newTime)
	{
		this.time = newTime;
		this.generateDay();

		this.sun.repaint();
		this.mon.repaint();
		this.tue.repaint();
		this.wed.repaint();
		this.thu.repaint();
		this.fri.repaint();
		this.sat.repaint();
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	@Override
	public void updateEvents(Event events, boolean added)
	{
		// at the moment, we don't care, and just re-pull from the DB. TODO: this should change
		this.generateDay();
	}

	@Override
	public void select(Displayable item)
	{
		// What are we doing for selection? Going to the dayview or going to that event?
		//current.select(item);		
	}
}
