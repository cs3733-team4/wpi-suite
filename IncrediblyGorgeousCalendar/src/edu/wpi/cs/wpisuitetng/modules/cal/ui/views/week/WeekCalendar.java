/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.week;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.joda.time.DateTime;
import org.joda.time.Interval;
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
	private DateTime weekStartTime;
	private DateTime weekEndTime;
	
	private MainPanel mainPanel;
	
	private Displayable lastSelection;
	
	private LouvreTour[] daysOfWeekArray = new LouvreTour[7];
	private List<Event> eventList;
	
	private JPanel multidayEventGridContainer = new JPanel();
	private JPanel dayTitleAndMultidayContainer = new JPanel();
	
	private JPanel dayHolderPanel = new JPanel();
	private JPanel dayGrid = new JPanel();
	private JPanel dayGridContainer = new JPanel();
	
	private JPanel dayTitleGrid = new JPanel();
	private JPanel dayTitlesContainer = new JPanel();
	
	private JScrollPane scroll = new JScrollPane(dayGridContainer);

	private EventModel eventModel;

	private DateTimeFormatter monthDayFmt = DateTimeFormat.forPattern("MMM d");
	private DateTimeFormatter dayYearFmt = DateTimeFormat.forPattern("d, yyyy");
	private DateTimeFormatter monthDayYearFmt = DateTimeFormat.forPattern("MMM d, yyyy");
	private DateTimeFormatter dayTitleFmt = DateTimeFormat.forPattern("E M/d");
	
	public WeekCalendar(DateTime on, EventModel emodel)
	{
		this.mainPanel = MainPanel.getInstance();
		this.time = on;
		
		updateWeekStartAndEnd(time);
		
		eventModel = emodel;
		scroll.setBackground(Colors.TABLE_BACKGROUND);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		dayGrid.setBackground(Colors.TABLE_BACKGROUND);
		
		this.setLayout(new BorderLayout());
		this.multidayEventGridContainer.setLayout(new BoxLayout(multidayEventGridContainer, BoxLayout.Y_AXIS));
		this.dayTitleAndMultidayContainer.setLayout(new BoxLayout(dayTitleAndMultidayContainer, BoxLayout.Y_AXIS));
		this.dayHolderPanel.setLayout(new BorderLayout());
		this.dayGrid.setLayout(new GridLayout(1, 7));
		this.dayTitleGrid.setLayout(new GridLayout(1, 7));
		this.dayTitlesContainer.setLayout(new BoxLayout(dayTitlesContainer, BoxLayout.X_AXIS));
		this.dayGridContainer.setLayout(new BorderLayout());
		
		generateDay();
	}

	private void generateDay()
	{
		this.removeAll();
		this.dayGrid.removeAll();
		this.dayTitleGrid.removeAll();
		this.dayTitlesContainer.removeAll();
		
		MutableDateTime increment=new MutableDateTime(weekStartTime);
		increment.setMillisOfDay(0);
		
		eventList = getVisibleEvents(increment.toDateTime());
		
		for(int index=0;index<7;index++)
		{
			
			//add day views to the day grid
			this.daysOfWeekArray[index] = new LouvreTour();
			this.daysOfWeekArray[index].setEvents(getEventsInInterval(increment.toDateTime(), increment.toDateTime().plusDays(1)), increment.toDateTime());
			this.daysOfWeekArray[index].setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Colors.BORDER));
			this.dayGrid.add(this.daysOfWeekArray[index]);
			
			//add day titles to the title grid
			JLabel currDayTitle = new JLabel(increment.toDateTime().toString(dayTitleFmt), JLabel.CENTER);
			this.dayTitleGrid.add(currDayTitle);
			
			increment.addDays(1);
		}

		//initialize spacer label to offset dayGridLabel
		JLabel spacerLabel = new JLabel(" 10:00 PM ");
		spacerLabel.setForeground(spacerLabel.getBackground());
		spacerLabel.setBorder(BorderFactory.createLineBorder(spacerLabel.getBackground()));
		this.dayTitlesContainer.add(spacerLabel);
		
		//initialize spacer label to offset scrollbar
		spacerLabel = new JLabel("     ");
		spacerLabel.setForeground(spacerLabel.getBackground());
		spacerLabel.setBorder(BorderFactory.createLineBorder(spacerLabel.getBackground()));
		
		//populate and set up the multiDayEventGrid
		populateMultidayEventGrid();		
		
		//put multiday event handling and day title grid in a container
		dayTitleAndMultidayContainer.add(dayTitleGrid);
		dayTitleAndMultidayContainer.add(multidayEventGridContainer);

		//add grids to box container
		this.dayTitlesContainer.add(dayTitleAndMultidayContainer);
		this.dayTitlesContainer.add(spacerLabel);

		//add spacer and time labels to container
		this.dayGridContainer.add(DayGridLabel.getInstance(), BorderLayout.WEST);
		this.dayGridContainer.add(dayGrid, BorderLayout.CENTER);
		
		//add the sidebar and the day grid to the container panel
		this.dayHolderPanel.add(dayTitlesContainer, BorderLayout.NORTH);
		this.dayHolderPanel.add(scroll, BorderLayout.CENTER);
		
		//setup week title
		increment.addDays(-1);
		JLabel weekTitle = new JLabel();
		
		if(weekStartTime.getYear() != increment.getYear())
			weekTitle.setText(weekStartTime.toString(monthDayYearFmt) + " - " +
							increment.toString(monthDayYearFmt));
		else if(weekStartTime.getMonthOfYear() != increment.getMonthOfYear())
			weekTitle.setText(weekStartTime.toString(monthDayFmt) + " - " +
							increment.toString(monthDayYearFmt));
		else
			weekTitle.setText(weekStartTime.toString(monthDayFmt) + " - " +
							increment.toString(dayYearFmt));
			
		weekTitle.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 25));
		weekTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		//add title and week view to this
		this.add(weekTitle, BorderLayout.NORTH);
		this.add(dayHolderPanel, BorderLayout.CENTER);
		
		// notify mini-calendar to change
		mainPanel.miniMove(time);
	}
	
	private void populateMultidayEventGrid() {
		System.out.print("\n\nStart of Popultation: \n\n");

		multidayEventGridContainer.removeAll();
		
		List<Event> multidayEvents = getMultidayEvents();
		
		while(!multidayEvents.isEmpty())
		{
			System.out.print("multiGridContainer Comp Count: " + multidayEventGridContainer.getComponentCount() + "\n");

			JLabel multiGrid = new JLabel();
			multiGrid.setLayout(new GridLayout(1, 7));
			
			int gridIndex=0;
			Interval mInterval;
			Event currEvent = null;
			
			while(gridIndex<7)
			{
				System.out.print("Current grid index: " + gridIndex + "\n");
				mInterval = new Interval(daysOfWeekArray[gridIndex].getDisplayDate(), 
						daysOfWeekArray[gridIndex].getDisplayDate().plusDays(1));
				
				currEvent = null;
				
				for(Event e: multidayEvents)
				{
					if(isEventInInterval(e, mInterval))
					{
						currEvent = e;
						break;
					}
				}
				
				if(currEvent==null)
				{
					gridIndex++;
					multiGrid.add(new JPanel());
				} else
				{
					boolean firstPanel = true;
					System.out.print("currEvent Name: " + currEvent.getName() + "\n");
					do
					{
						if(firstPanel)
						{
							JLabel multidayPanel = new JLabel(currEvent.getName());
							multidayPanel.setBackground(currEvent.getColor());
							multiGrid.add(multidayPanel);
							firstPanel = false;
						}else
						{
							JPanel multidayPanel = new JPanel();
							multidayPanel.setBackground(currEvent.getColor());
							multiGrid.add(multidayPanel);
						}
						gridIndex++;
					}while(gridIndex < 7 && daysOfWeekArray[gridIndex].getDisplayDate().isBefore(currEvent.getEnd()));
					
					multidayEvents.remove(currEvent);
				}
			}
			
			System.out.print("multiGrid Comp Count: " + multiGrid.getComponentCount() + "\n");
			if(multiGrid.getComponentCount()>0)
				multidayEventGridContainer.add(multiGrid);
		}
	}

	private List<Event> getVisibleEvents(DateTime curDay)
	{
		MutableDateTime f = new MutableDateTime(curDay);
		f.setMillisOfDay(0);
		DateTime from = f.toDateTime();
		f.addDays(7);
		DateTime to = f.toDateTime();
		// TODO: this is where filtering should go
		return eventModel.getEvents(from, to);
	}

	@Override
	public void next()
	{
		this.time = Months.nextWeek(this.time);
		updateWeekStartAndEnd(time);
		this.generateDay();
	}

	@Override
	public void previous()
	{
		this.time = Months.prevWeek(this.time);
		updateWeekStartAndEnd(time);
		this.generateDay();

	}

	@Override
	public void display(DateTime newTime)
	{
		this.time = newTime;
		updateWeekStartAndEnd(time);
		
		this.generateDay();

		for(int index=0;index<7;index++)
		{
			this.daysOfWeekArray[index].repaint();
		}
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void updateWeekStartAndEnd(DateTime time) {
		MutableDateTime mdt = Months.getWeekStart(time).toMutableDateTime();
		mdt.setMillisOfDay(0);
		this.weekStartTime = mdt.toDateTime();
		mdt.addDays(7);
		this.weekEndTime = mdt.toDateTime();
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
				day = null;
				for(LouvreTour lt : daysOfWeekArray)
				{
					if(lastSelection.getDate().getDayOfYear()==lt.getDisplayDate().getDayOfYear())
					{
						day = lt;
						break;
					}
				}

				if (day != null)
				{
					day.select(null);
				}
			}
		}

		if (item != null && item instanceof Event)
			selectEvents((Event)item, item);
		else
		{
			day = null;
			for(LouvreTour lt : daysOfWeekArray)
			{
				if(oitem.getDate().getDayOfYear()==lt.getDisplayDate().getDayOfYear())
				{
					day = lt;
					break;
				}
			}

			if (day != null)
			{
				day.select(item);
			}
		}
		lastSelection = item;
	}
	
	private void selectEvents(Event on, Displayable setTo)
	{
		// TODO: refactor this pattern
		LouvreTour mLouvreTour;
		MutableDateTime startDay = new MutableDateTime(on.getStart());
		MutableDateTime endDay = new MutableDateTime(on.getEnd());
		
		endDay.setMillisOfDay(0);
		endDay.addDays(1);
		endDay.addMillis(-1);
		startDay.setMillisOfDay(0);
		
		int index = 0;
		
		for(int i = 0; i< 7; i++)
		{
			if(startDay.getDayOfYear()==daysOfWeekArray[i].getDisplayDate().getDayOfYear())
			{
				index = i;
				break;
			}
		}
		
		while (index<7 && !endDay.isBefore(daysOfWeekArray[index].getDisplayDate()))
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
			index++;
		}
	}

	public DateTime getTime() {
		return time;
	}
	
	private List<Event> getEventsInInterval(DateTime intervalStart, DateTime intervalEnd)
	{
		List<Event> retrievedEvents = new ArrayList<>();
		Interval mInterval = new Interval(intervalStart, intervalEnd);
		
		for(Event event : eventList)
		{
			if(event.getStart().getDayOfYear() != event.getEnd().getDayOfYear() || 
					event.getStart().getYear() != event.getEnd().getYear())
				continue;
			
			if (isEventInInterval(event, mInterval))
			{
				retrievedEvents.add(event);
			}
		}
		
		return retrievedEvents;
	}
	
	private List<Event> getMultidayEvents()
	{
		List<Event> retrievedEvents = new ArrayList<>();
		
		for(Event event : eventList)
		{
			DateTime s = event.getStart(), e = event.getEnd();
			
			if(s.getDayOfYear() != e.getDayOfYear() || 
					event.getStart().getYear() != event.getEnd().getYear())
				retrievedEvents.add(event);
		}
		
		return retrievedEvents;
	}
	
	private boolean isEventInInterval(Event mEvent, Interval mInterval)
	{
		DateTime s = mEvent.getStart(), e = mEvent.getEnd();
		
		if (s.isBefore(e) && mInterval.overlaps(new Interval(s, e)))
		{
			return true;
		}
		
		return false;
	}
}
