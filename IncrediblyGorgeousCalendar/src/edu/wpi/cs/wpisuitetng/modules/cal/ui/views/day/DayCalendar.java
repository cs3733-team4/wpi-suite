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
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.swing.BoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.lowagie.text.Font;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.france.LouvreTour;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Months;

public class DayCalendar extends AbstractCalendar
{

	private DateTime time;
	private MainPanel mainPanel;
	private LouvreTour current;
	
	private JPanel holder = new JPanel();
	private JScrollPane scroll = new JScrollPane(holder);

	private EventModel eventModel;

	private DateTimeFormatter titleFmt = DateTimeFormat.forPattern("EEEE, MMM d, yyyy");

	public DayCalendar(DateTime on, EventModel emodel)
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
		
		JLabel dayTitle = new JLabel(time.toString(titleFmt));
		dayTitle.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 25));
		dayTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(dayTitle, BorderLayout.NORTH);

		this.current = new LouvreTour();
		this.current.setEvents(getVisibleEvents(), time);

		this.holder.add(new DayGridLabel(), BorderLayout.WEST);
		this.holder.add(this.current, BorderLayout.CENTER);
		// notify mini-calendar to change
		mainPanel.miniMove(time);
	}
	
	/**
	 * Get visible events for the current day view
	 * @return returns the list of events to display
	 */
	private List<Event> getVisibleEvents()
	{
		
		// Set up from and to datetime for search
		MutableDateTime f = new MutableDateTime(time);
		f.setMillisOfDay(0);
		DateTime from = f.toDateTime();
		f.addDays(1);
		DateTime to = f.toDateTime();
		
		// Filter events by date
		List<Event> visibleEvents = eventModel.getEvents(from, to);
		
		// Filter for selected categories
		Collection<UUID> selectedCategories = MainPanel.getInstance().getSelectedCategories();
		List<Event> categoryFilteredEvents = new ArrayList<Event>();
		
		// Else, loop through events and filter by selected categories
		for (Event e : visibleEvents){
			if (selectedCategories.contains(e.getCategory()))
				categoryFilteredEvents.add(e);
		}
		
		// Return list of events to be displayed
		return categoryFilteredEvents;
	}

	@Override
	public void next()
	{
		this.time = Months.nextDay(this.time);
		this.generateDay();
	}

	@Override
	public void previous()
	{
		this.time = Months.prevDay(this.time);
		this.generateDay();

	}

	@Override
	public void display(DateTime newTime)
	{
		this.time = newTime;
		this.generateDay();

		this.current.repaint();
		
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
		current.select(item);		
	}
}
