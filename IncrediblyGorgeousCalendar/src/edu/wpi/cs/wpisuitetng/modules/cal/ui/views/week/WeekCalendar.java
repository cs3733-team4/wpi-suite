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
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Months;

/**
 * Calendar to display the 7 days of the selected week. A timetable and 
 * 7 day views are scaled to fit in the current window size. 
 */
public class WeekCalendar extends AbstractCalendar
{

	private DateTime time;
	private MainPanel mainPanel;
	
	private Displayable lastSelection;
	
	private LouvreTour[] daysOfWeekArray = new LouvreTour[7];
	
	private JPanel dayHolderPanel = new JPanel();
	private JPanel dayGrid = new JPanel();
	private JPanel dayGridContainer = new JPanel();
	
	private JPanel dayTitleGrid = new JPanel();
	private JPanel dayTitlesContainer = new JPanel();
	
	private JScrollPane scroll = new JScrollPane(dayGridContainer);

	private DateTimeFormatter monthDayFmt = DateTimeFormat.forPattern("MMM d");
	private DateTimeFormatter dayYearFmt = DateTimeFormat.forPattern("d, yyyy");
	private DateTimeFormatter monthDayYearFmt = DateTimeFormat.forPattern("MMM d, yyyy");
	private DateTimeFormatter dayTitleFmt = DateTimeFormat.forPattern("E M/d");
	
	/**
	 * 
	 * @param on
	 * 			the DateTime that the Week Calendar is focused/centered on
	 */
	public WeekCalendar(DateTime on)
	{
		this.mainPanel = MainPanel.getInstance();
		this.time = on;
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

	/**
	 * Generates the day displays in the week panel
	 */
	private void generateDay()
	{
		this.removeAll();
		this.dayGrid.removeAll();
		this.dayTitleGrid.removeAll();
		this.dayTitlesContainer.removeAll();
		
		MutableDateTime increment=new MutableDateTime(Months.getWeekStart(time));
		
		for(int index=0;index<7;index++)
		{
			//add day views to the day grid
			this.daysOfWeekArray[index] = new LouvreTour();
			this.daysOfWeekArray[index].setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
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
		
		//initialize spacer label to offset scroll bar
		spacerLabel = new JLabel("     ");
		spacerLabel.setForeground(spacerLabel.getBackground());
		spacerLabel.setBorder(BorderFactory.createLineBorder(spacerLabel.getBackground()));
		
		//add grids to box container
		this.dayTitlesContainer.add(dayTitleGrid);
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
		
		if(Months.getWeekStart(time).getYear() != increment.getYear())
			weekTitle.setText(Months.getWeekStart(time).toString(monthDayYearFmt) + " - " +
							increment.toString(monthDayYearFmt));
		else if(Months.getWeekStart(time).getMonthOfYear() != increment.getMonthOfYear())
			weekTitle.setText(Months.getWeekStart(time).toString(monthDayFmt) + " - " +
							increment.toString(monthDayYearFmt));
		else
			weekTitle.setText(Months.getWeekStart(time).toString(monthDayFmt) + " - " +
							increment.toString(dayYearFmt));
			
		weekTitle.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 25));
		weekTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		//add title and week view to this
		this.add(weekTitle, BorderLayout.NORTH);
		this.add(dayHolderPanel, BorderLayout.CENTER);
		
		// notify mini-calendar to change
		mainPanel.miniMove(time);
	}
	
	/**
	 * Get a list of Events for the current day
	 * 
	 * @param curDay
	 * 				DateTime that the calendar is focused on
	 * @return
	 */
	private List<Event> getVisibleEvents(DateTime curDay)
	{
		MutableDateTime f = new MutableDateTime(curDay);
		f.setMillisOfDay(0);
		DateTime from = f.toDateTime();
		f.addDays(1);
		DateTime to = f.toDateTime();
		// TODO: this is where filtering should go
		return EventModel.getInstance().getEvents(from, to);
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
	
	/**
	 * Selects an event's corresponding Displayable
	 * 
	 * @param on
	 * 			Event being selected
	 * @param setTo
	 * 			Displayable of Event being selected
	 */
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

	/**
	 * Get the time from a Date Time
	 * 
	 * @return
	 */
	public DateTime getTime() {
		return time;
	}
}
