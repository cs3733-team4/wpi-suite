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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.DayGridLabel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.collisiondetection.DayPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.collisiondetection.DayItem;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Months;

/**
 * Calendar to display the 7 days of the selected week. A timetable and 7 day
 * views are scaled to fit in the current window size.
 */
public class WeekCalendar extends AbstractCalendar
{

	private DateTime time;
	private DateTime weekStartTime;
	private DateTime weekEndTime;
	private DayItem selected;
	private MainPanel mainPanel;

	private Displayable lastSelection;

	private DayPanel[] daysOfWeekArray = new DayPanel[7];
	private List<Event> eventList;
	private int currentDayUnderMouse;
	private DateTimeFormatter monthDayFmt = DateTimeFormat.forPattern("MMM d");
	private DateTimeFormatter dayYearFmt = DateTimeFormat.forPattern("d, yyyy");
	private DateTimeFormatter monthDayYearFmt = DateTimeFormat.forPattern("MMM d, yyyy");
	private DateTimeFormatter dayTitleFmt = DateTimeFormat.forPattern("E M/d");
	private JLabel weekTitle = new JLabel();
	private JScrollPane headerScroller = new JScrollPane();
	private ScrollableBox headerBox = new ScrollableBox();
	private JScrollPane smithsonianScroller = new JScrollPane();
	private JPanel smithsonian = new JPanel();
	private JLabel dayHeaders[] = new JLabel[7];

	/**
	 * 
	 * @param on
	 *            the DateTime that the Week Calendar is focused/centered on
	 */
	public WeekCalendar(DateTime on)
	{
		this.selected = null;
		this.mainPanel = MainPanel.getInstance();
		this.time = on;
		this.currentDayUnderMouse = -1;
		updateWeekStartAndEnd(time);
		
		// ui layout
		String rs = (new JScrollBar().getPreferredSize().width) + "px";
		setLayout(new MigLayout("insets 0,gap 0", "[50px:50px:50px][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow]["+rs+":"+rs+":"+rs+"]", "[][][::100px,grow][grow]"));
		
		weekTitle.setFont(new Font("DejaVu Sans", Font.BOLD, 25));
		add(weekTitle, "cell 0 0 9 1,alignx center");
		
		for (int i = 0; i < 7; i++)
		{
			add(dayHeaders[i] = new JLabel("", JLabel.CENTER), "cell " + (i+1) + " 1,alignx center");
		}
		
		headerScroller.setBorder(BorderFactory.createEmptyBorder());
		headerScroller.setViewportBorder(BorderFactory.createEmptyBorder());
		headerScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(headerScroller, "cell 1 2 7 1,grow"); // default to 7 as no scrollbars
		
		headerBox.setBorder(BorderFactory.createEmptyBorder());
		headerScroller.setViewportView(headerBox);
		

		smithsonianScroller.setBorder(null);
		smithsonianScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		smithsonianScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		smithsonianScroller.getVerticalScrollBar().setUnitIncrement(20);
		add(smithsonianScroller, "cell 0 3 9 1,grow");

		smithsonianScroller.setViewportView(smithsonian);
		smithsonian.setLayout(new MigLayout("insets 0,gap 0", "[50px:50px:50px][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow]", "[grow]"));
		
		generateDay();
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if(selected != null)
				{
					if(selected.getEvent().getStart().getDayOfWeek() != currentDayUnderMouse)
					{
						daysOfWeekArray[selected.getEvent().getStart().getDayOfWeek()].remove(selected);
						daysOfWeekArray[currentDayUnderMouse].add(selected);
						revalidate();
					}
				}
			}
		});
	}

	/**
	 * Generates the day displays in the week panel
	 */
	private void generateDay()
	{
		// clear out the specifics
		smithsonian.removeAll();
		headerBox.removeAll();
		// add the day grid back in
		smithsonian.add(new DayGridLabel(), "cell 0 0,grow");

		MutableDateTime increment = new MutableDateTime(weekStartTime);
		increment.setMillisOfDay(0);

		eventList = getVisibleEvents(increment.toDateTime());

		for (int i = 0; i < 7; i++)
		{
			// add day views to the day grid
			this.daysOfWeekArray[i] = new DayPanel(true, this);
			this.daysOfWeekArray[i].setEvents(getEventsInInterval(increment.toDateTime(), increment.toDateTime().plusDays(1)), increment.toDateTime());
			if (i < 6)
				this.daysOfWeekArray[i].setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Colors.BORDER));
			
			this.smithsonian.add(this.daysOfWeekArray[i], "cell " + (i+1) + " 0,grow");

			// add day titles to the title grid
			dayHeaders[i].setText(increment.toDateTime().toString(dayTitleFmt));

			increment.addDays(1);
		}

		// populate and set up the multiDayEventGrid
		populateMultidayEventGrid();

		// setup week title
		increment.addDays(-1);

		// smart titles
		if (weekStartTime.getYear() != increment.getYear())
			weekTitle.setText(weekStartTime.toString(monthDayYearFmt) + " - " + increment.toString(monthDayYearFmt));
		else if (weekStartTime.getMonthOfYear() != increment.getMonthOfYear())
			weekTitle.setText(weekStartTime.toString(monthDayFmt) + " - " + increment.toString(monthDayYearFmt));
		else
			weekTitle.setText(weekStartTime.toString(monthDayFmt) + " - " + increment.toString(dayYearFmt));

		// notify mini-calendar to change
		mainPanel.miniMove(time);
	}

	private void populateMultidayEventGrid()
	{
		List<Event> multidayEvents = getMultidayEvents();
		Collections.sort(multidayEvents, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2)
			{
				// TODO: sort by length/smarter
				return o1.getStart().compareTo(o2.getStart());
			}

		});
		int rows = 0;
		
		while (!multidayEvents.isEmpty())
		{
			System.out.print("multiGridContainer Comp Count: " + headerBox.getComponentCount() + "\n");

			JPanel multiGrid = new JPanel();
			multiGrid.setBorder(BorderFactory.createEmptyBorder());
			multiGrid.setLayout(new MigLayout("insets 0,gap 0", "[sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow][sizegroup a,grow]", "[]"));

			int gridIndex = 0;

			next: while (gridIndex < 7)
			{
				System.out.print("Current grid index: " + gridIndex + "\n");
				Interval mInterval = new Interval(daysOfWeekArray[gridIndex].getDisplayDate(), daysOfWeekArray[gridIndex].getDisplayDate().plusDays(1));

				for (Event currEvent : multidayEvents)
				{
					if (isEventInInterval(currEvent, mInterval))
					{
						boolean firstPanel = true;
						System.out.print("currEvent Name: " + currEvent.getName() + "\n");
						do
						{
							if (firstPanel)
							{
								System.out.print("currEvent Name:Name  " + currEvent.getName() + "\n");
								JLabel multidayPanel = new JLabel(" " + currEvent.getName());
								multidayPanel.setMinimumSize(new Dimension(0, 0));
								multidayPanel.setBackground(currEvent.getColor());
								if (multiGrid.getComponentCount() > 0)
									((JComponent)multiGrid.getComponents()[multiGrid.getComponentCount() - 1]).setBorder(BorderFactory.createMatteBorder(rows == 0 ? 1 : 0, (gridIndex == 1) ? 1 : 0, 1, 0, Colors.BORDER));
								multidayPanel.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(rows == 0 ? 1 : 0, 1, 1, 1, currEvent.getColor().darker()), new EmptyBorder(3, 3, 3, 3)));
								multidayPanel.setOpaque(true);
								multiGrid.add(multidayPanel, "cell " + gridIndex + " 0, grow");
								firstPanel = false;
							}
							else
							{
								System.out.print("currEvent Color: " + currEvent.getName() + "\n");
								JPanel multidayPanel = new JPanel();
								multidayPanel.setBackground(currEvent.getColor());
								multidayPanel.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(rows == 0 ? 1 : 0, 0, 1, 1, currEvent.getColor().darker()), new EmptyBorder(3, 3, 3, 3)));
								multiGrid.add(multidayPanel, "cell " + gridIndex + " 0, grow");
							}
							gridIndex++;
						} while (gridIndex < 7 && daysOfWeekArray[gridIndex].getDisplayDate().isBefore(currEvent.getEnd()));

						multidayEvents.remove(currEvent);
						continue next;
					}
				}

				// if we don't find anything, add spacer and go to next day
				gridIndex++;
				JPanel spacer = new JPanel();
				spacer.setBackground(Colors.TABLE_BACKGROUND);
				spacer.setBorder(BorderFactory.createMatteBorder(rows == 0 ? 1 : 0, (gridIndex == 1) ? 1 : 0, 1, 1, Colors.BORDER));
				multiGrid.add(spacer, "cell " + (gridIndex-1) + " 0, grow");
			}

			System.out.print("multiGrid Comp Count: " + multiGrid.getComponentCount() + "\n");
			if (multiGrid.getComponentCount() > 0)
				headerBox.add(multiGrid);
			rows++;
		}
		// need to set this manually, silly because scrollpanes are silly and don't resize
		if (rows > 3)
		{
			add(headerScroller, "cell 1 2 8 1,grow");
			rows = 3;
		}
		else
		{
			add(headerScroller, "cell 1 2 7 1,grow");
		}
		((MigLayout)getLayout()).setRowConstraints("[][][" + (rows * 23) +  "px:n:80px,grow]" + (rows > 0 ? "6" : "") + "[grow]");
	}

	/**
	 * Get a list of Events for the current day
	 * 
	 * @param curDay
	 *            DateTime that the calendar is focused on
	 * @return
	 */
	private List<Event> getVisibleEvents(DateTime curDay)
	{
		
		if (MainPanel.getInstance().showEvents()){
			List<Event> visibleEvents =  EventModel.getInstance().getEvents(weekStartTime, weekEndTime);
			
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
		} else
			return new ArrayList<Event>();
	}

	@Override
	public void next()
	{
		display(Months.nextWeek(this.time));
	}

	@Override
	public void previous()
	{
		display(Months.prevWeek(this.time));
	}

	@Override
	public void display(DateTime newTime)
	{
		this.time = newTime;
		updateWeekStartAndEnd(time);

		this.generateDay();

		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				// Scroll to now
				BoundedRangeModel jsb = smithsonianScroller.getVerticalScrollBar().getModel();
				double day = time.getMinuteOfDay();
				day /= time.minuteOfDay().getMaximumValue();
				day *= (jsb.getMaximum()) - jsb.getMinimum();
				jsb.setValue((int) day);
			}
		});
		
		// repaint
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void updateWeekStartAndEnd(DateTime time)
	{
		MutableDateTime mdt = new MutableDateTime(time);
		mdt.addDays(-(time.getDayOfWeek() % 7));
		mdt.setMillisOfDay(0);
		this.weekStartTime = mdt.toDateTime();
		mdt.addDays(7);
		this.weekEndTime = mdt.toDateTime();
	}

	@Override
	public void updateEvents(Event events, boolean added)
	{
		// at the moment, we don't care, and just re-pull from the DB. TODO:
		// this should change
		this.generateDay();
	}

	@Override
	public void select(Displayable item)
	{
		Displayable oitem = item;
		DayPanel day;
		if (item == null && lastSelection == null)
			return;
		if (item == null)
			oitem = lastSelection;

		if (lastSelection != null)
		{
			if (lastSelection instanceof Event)
				selectEvents((Event) lastSelection, null);
			else
			{
				day = null;
				for (DayPanel lt : daysOfWeekArray)
				{
					if (lastSelection.getDate().getDayOfYear() == lt.getDisplayDate().getDayOfYear())
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
			selectEvents((Event) item, item);
		else
		{
			day = null;
			for (DayPanel lt : daysOfWeekArray)
			{
				if (oitem.getDate().getDayOfYear() == lt.getDisplayDate().getDayOfYear())
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
	 *            Event being selected
	 * @param setTo
	 *            Displayable of Event being selected
	 */
	private void selectEvents(Event on, Displayable setTo)
	{
		// TODO: refactor this pattern
		DayPanel mLouvreTour;
		MutableDateTime startDay = new MutableDateTime(on.getStart());
		MutableDateTime endDay = new MutableDateTime(on.getEnd());

		endDay.setMillisOfDay(0);
		endDay.addDays(1);
		endDay.addMillis(-1);
		startDay.setMillisOfDay(0);

		int index = 0;

		for (int i = 0; i < 7; i++)
		{
			if (startDay.getDayOfYear() == daysOfWeekArray[i].getDisplayDate().getDayOfYear())
			{
				index = i;
				break;
			}
		}

		while (index < 7 && !endDay.isBefore(daysOfWeekArray[index].getDisplayDate()))
		{
			mLouvreTour = daysOfWeekArray[index];
			try
			{
				mLouvreTour.select(setTo);
			}
			catch (NullPointerException ex)
			{
				// silently ignore as this is apparently not in the view
			}
			index++;
		}
	}

	/**
	 * Get the time from a Date Time
	 * 
	 * @return
	 */
	public DateTime getTime()
	{
		return time;
	}

	private List<Event> getEventsInInterval(DateTime intervalStart, DateTime intervalEnd)
	{
		List<Event> retrievedEvents = new ArrayList<>();
		Interval mInterval = new Interval(intervalStart, intervalEnd);

		for (Event event : eventList)
		{
			if (event.getStart().getDayOfYear() != event.getEnd().getDayOfYear() || event.getStart().getYear() != event.getEnd().getYear())
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

		for (Event event : eventList)
		{
			DateTime s = event.getStart(), e = event.getEnd();
			if (s.getDayOfYear() != e.getDayOfYear() || s.getYear() != e.getYear())
				retrievedEvents.add(event);
		}

		return retrievedEvents;
	}

	private boolean isEventInInterval(Event mEvent, Interval mInterval)
	{
		DateTime s = mEvent.getStart(), e = mEvent.getEnd();
		if (this.weekStartTime.isAfter(s))
			s = weekStartTime;

		return (s.isBefore(e) && mInterval.contains(s));
	}
	
	public void passTo(int day, DayItem toPass)
	{
		selected = toPass;
	}
	public void mouseOverDay(int day)
	{
		this.currentDayUnderMouse = day;
	}
}