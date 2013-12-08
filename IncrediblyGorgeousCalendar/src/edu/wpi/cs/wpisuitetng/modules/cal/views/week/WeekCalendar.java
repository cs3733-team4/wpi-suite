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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthDay;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Months;

public class WeekCalendar extends AbstractCalendar
{

	private DateTime time;
	private MainPanel mainPanel;
	private LouvreTour sun, mon, tue, wed, thu, fri, sat;
	
	private Displayable lastSelection;
	
	private LouvreTour[] daysOfWeekArray = {sun, mon, tue, wed, thu, fri, sat};
	
	private JPanel dayHolderPanel = new JPanel();
	private JPanel dayGrid = new JPanel();
	private JPanel dayGridContainer = new JPanel();
	
	private JPanel dayTitleGrid = new JPanel();
	private JPanel dayTitlesContainer = new JPanel();
	
	private JScrollPane scroll = new JScrollPane(dayGridContainer);

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
		dayGrid.setBackground(Colors.TABLE_BACKGROUND);
		
		this.setLayout(new BorderLayout());
		this.dayHolderPanel.setLayout(new BorderLayout());
		this.dayGrid.setLayout(new GridLayout(1, 7));
		this.dayTitleGrid.setLayout(new GridLayout(1, 7));
		this.dayTitlesContainer.setLayout(new BoxLayout(dayTitlesContainer, BoxLayout.X_AXIS));
		this.dayGridContainer.setLayout(new BorderLayout());
		
		generateDay();
	}

	private void generateDay()
	{
		this.dayGrid.removeAll();
		this.removeAll();

		MutableDateTime increment=new MutableDateTime(Months.getWeekStart(time));
		
		for(int index=0;index<7;index++)
		{
			this.daysOfWeekArray[index] = new LouvreTour();
			this.daysOfWeekArray[index].setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
			this.daysOfWeekArray[index].setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Colors.BORDER));
			this.dayGrid.add(this.daysOfWeekArray[index]);
			increment.addDays(1);
		}

		//add titles to grid
		this.dayTitleGrid.removeAll();
		this.dayTitleGrid.add(new JLabel("Sun", JLabel.CENTER));
		this.dayTitleGrid.add(new JLabel("Mon", JLabel.CENTER));
		this.dayTitleGrid.add(new JLabel("Tue", JLabel.CENTER));
		this.dayTitleGrid.add(new JLabel("Wed", JLabel.CENTER));
		this.dayTitleGrid.add(new JLabel("Thu", JLabel.CENTER));
		this.dayTitleGrid.add(new JLabel("Fri", JLabel.CENTER));
		this.dayTitleGrid.add(new JLabel("Sat", JLabel.CENTER));
		
		//initialize spacer label for dayGridLabel
		JLabel spacerLabel = new JLabel(" 10:00 PM  ");
		spacerLabel.setForeground(spacerLabel.getBackground());
		spacerLabel.setBorder(BorderFactory.createLineBorder(spacerLabel.getBackground()));
		
		//add grids to box container
		this.dayTitlesContainer.removeAll();
		this.dayTitlesContainer.add(spacerLabel);
		this.dayTitlesContainer.add(dayTitleGrid);		

		//add spacer and time labels to container
		this.dayGridContainer.add(DayGridLabel.getInstance(), BorderLayout.WEST);
		this.dayGridContainer.add(dayGrid, BorderLayout.CENTER);
		
		//add the sidebar and the day grid to the container panel
		this.dayHolderPanel.add(dayTitlesContainer, BorderLayout.NORTH);
		this.dayHolderPanel.add(scroll, BorderLayout.CENTER);
		
		//setup week title
		JLabel weekTitle = new JLabel("Week of " + time.toString(titleFmt));
		weekTitle.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 25));
		weekTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		//add title and week view to this
		this.add(weekTitle, BorderLayout.NORTH);
		this.add(dayHolderPanel, BorderLayout.CENTER);
		
		// notify mini-calendar to change
		mainPanel.miniMove(time);
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

		for(int index=0;index<7;index++)
		{
			this.daysOfWeekArray[index].repaint();
		}
		
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
		Displayable oitem = item;
		LouvreTour day;
		if (item == null && lastSelection == null)
			return;
		if (item == null)
			oitem = lastSelection;
		
		if (lastSelection != null)
		{
			if (lastSelection instanceof Event)
				selectEvents((Event)lastSelection, null);
			else
			{
				/*
				day = days.get(lastSelection.getDate().getDayOfYear());
				if (day != null)
				{
					day.select(null);
				}
				*/
			}
		}

		if (item != null && item instanceof Event)
			selectEvents((Event)item, item);
		else
		{
			/*
			day = days.get(oitem.getDate().getDayOfYear());
			if (day != null)
			{
				day.select(item);
			}
			*/
		}
		lastSelection = item;
	}
	
	private void selectEvents(Event on, Displayable setTo)
	{
		// TODO: refactor this pattern
		LouvreTour mLouvreTour;
		MutableDateTime startDay = new MutableDateTime(on.getStart());
		MutableDateTime endDay = new MutableDateTime(on.getEnd());
		MutableDateTime startWeek = new MutableDateTime(Months.getWeekStart(time));
		MutableDateTime endWeek = new MutableDateTime(Months.getWeekStart(time));
		endWeek.addDays(7);
		
		endDay.setMillisOfDay(0);
		startDay.setMillisOfDay(0);
		
		if (startDay.isBefore(startWeek))
			startDay=new MutableDateTime(startWeek);
		if (endDay.isAfter(endWeek))
			endDay = new MutableDateTime(endWeek);
			
		int index = 0;
		while (!endDay.isBefore(startDay))
		{
			mLouvreTour = daysOfWeekArray[index];
			try
			{
				mLouvreTour.select(setTo);
			}
			catch(NullPointerException ex)
			{
				// silently ignore
			}
			startDay.addDays(1);
			index++;
		}
	}
}
