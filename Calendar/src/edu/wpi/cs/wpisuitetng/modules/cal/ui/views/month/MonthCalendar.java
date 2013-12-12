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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDateTime;

import java.awt.Font;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.DayStyle;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.HSLColor;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Months;

/**
 * Main Month view.
 */
public class MonthCalendar extends AbstractCalendar
{

	private JPanel inside = new JPanel(), top = new JPanel(),
			mainCalendarView = new JPanel(), calendarTitlePanel = new JPanel();

	private JLabel monthLabel = new JLabel();
	private DateTime time;
	private MainPanel mainPanel;
	private Displayable lastSelection;
	private DateTime firstOnMonth;
	private DateTime lastOnMonth;
	private HashMap<Integer, MonthDay> days = new HashMap<Integer, MonthDay>();

	private EventModel eventModel;
	private CommitmentModel commitmentModel;
	private boolean escaped; //has the user dragged an event off it's starting day?
	private boolean external; //has the user dragged an event off of the calendar?
	private boolean tooltip;

	public MonthCalendar(DateTime on, EventModel emodel, CommitmentModel cmodel)
	{
		this.eventModel = emodel;
		this.commitmentModel = cmodel;
		this.mainPanel = MainPanel.getInstance();
		this.time = on;

		this.setLayout(new BorderLayout());
		this.add(calendarTitlePanel, BorderLayout.NORTH);

		generateDays(new MutableDateTime(on));
		generateHeaders(new MutableDateTime(on));
		
		addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{	
				MainPanel p = MainPanel.getInstance();
				Displayable d = p.getSelectedEvent();
				
				MonthDay md = getMonthDayAtCursor();
				
				if (d != null && md != null)
				{
					md.setBackground(new Color(255, 255, 200));
					d.setTime(md.getDay());
				}
				else
				{
					escaped = false;
					e.consume();
					repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e)
			{
				if (tooltip)
				{
					repaint();
				}
				setEscaped(false);
			}
			
		});
		
		addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				external = false;
			}

			@Override
			public void mouseExited(MouseEvent arg0)
			{
				external = true;
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				external = false;
			}

			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				if (external)
				{
					System.out.println("drop the events");
					display(null);
					repaint();
					setEscaped(false);
				}
				else
				{
					Displayable selected = MainPanel.getInstance().getSelectedEvent();
					if (selected != null && escaped)
					{
						display(selected.getDate());
					}
				}
			}
			
		});
	}
	
	public MonthDay getMonthDayAtCursor()
	{
		Point l = MouseInfo.getPointerInfo().getLocation();
		Point pp = inside.getLocationOnScreen();
		
		int x = l.x-pp.x;
		int y = l.y-pp.y;
		
		Component jc = inside.getComponentAt(x, y);
		if (jc instanceof MonthDay)
		{
			return (MonthDay) jc;
		}
		return null;
		
	}

	/**
	 * 
	 * @param fom
	 *            the mutable date time
	 */
	public void generateHeaders(MutableDateTime fom)
	{
		// Set up label for month title
		monthLabel.setHorizontalAlignment(JLabel.CENTER);
		monthLabel.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 25));

		// Set up the container title panel (only holds monthLabel for now)
		calendarTitlePanel.setLayout(new BorderLayout());
		calendarTitlePanel.add(monthLabel, BorderLayout.CENTER);

		// layout code
		mainCalendarView.setBackground(Colors.TABLE_BACKGROUND);
		mainCalendarView.setLayout(new BorderLayout());
		top.setLayout(new GridLayout(1, 7));

		mainCalendarView.add(top, BorderLayout.NORTH);
		mainCalendarView.add(inside, BorderLayout.CENTER);

		this.add(mainCalendarView, BorderLayout.CENTER);
		this.add(calendarTitlePanel, BorderLayout.NORTH);
		// end layout code

		fom.setDayOfMonth(1);
		fom.setMillisOfDay(0);
		int first = (fom.getDayOfWeek() % 7);
		fom.addDays(-first);

		// generate days on top
		for (int i = 0; i < 7; i++)
		{
			JLabel jl = new JLabel(fom.dayOfWeek().getAsText());
			jl.setHorizontalAlignment(SwingConstants.CENTER);
			fom.addDays(1);
			top.add(jl);
		}
	}

	/**
	 * clears and sets a list of events
	 * 
	 * @param events
	 */
	public void setEvents(List<Event> events)
	{
		clearEvents();
		for (Event e : events)
		{
			this.addEvent(e);
		}
	}

	public void setCommitments(List<Commitment> commitments)
	{
		clearCommitments();
		for (Commitment c : commitments)
		{
			this.addCommitment(c);
		}
	}

	
	public void addEvent(Event e)
	{
		
		MonthDay md;
		MutableDateTime startDay = new MutableDateTime(e.getStart());
		MutableDateTime endDay = new MutableDateTime(e.getEnd());
		endDay.setMillisOfDay(0);
		startDay.setMillisOfDay(0);
		
		if (startDay.isBefore(firstOnMonth))
			startDay=new MutableDateTime(firstOnMonth);
		if (endDay.isAfter(lastOnMonth))
			endDay= new MutableDateTime(lastOnMonth);
			
		while (!endDay.isBefore(startDay))
		{
			md = this.days.get(startDay.getDayOfYear());
			try{
				md.addEvent(e);
			}
			catch(NullPointerException ex)
			{
				System.err.println("Error when adding Event: " + e.toJSON());
			}
			startDay.addDays(1);
		
		}
	}

	
	public void addCommitment(Commitment c)
	{
		MonthDay md = this.days.get(c.getDate().getDayOfYear());
		md.addCommitment(c);
	}

	/**
	 * Remove a single event
	 * 
	 * @param e
	 */
	public void removeEvent(Event e)
	{
		MonthDay md = this.days.get(e.getStart().getDayOfYear());
		md.removeEvent(e);
	}

	public void removeCommitment(Commitment c)
	{
		MonthDay md = this.days.get(c.getDate().getDayOfYear());
		md.removeCommitment(c);
	}

	public void clearEvents()
	{
		for (Component i : inside.getComponents())
		{
			((MonthDay)i).clearEvents();
		}
	}

	public void clearCommitments()
	{
		for (Component i : inside.getComponents())
		{
			((MonthDay)i).clearComms();
		}
	}
	
	public boolean isToday(ReadableDateTime fom)
	{
		DateTime now = DateTime.now();
		return fom.getYear() == now.getYear() && fom.getDayOfYear() == now.getDayOfYear();
	}

	public void display(DateTime newTime)
	{
		this.escaped = false;
		time = newTime;
		generateDays(new MutableDateTime(time));
		mainPanel.miniMove(time);
	}

	public void next()
	{
		MutableDateTime fom = new MutableDateTime(time);
		fom.addMonths(1);
		time = fom.toDateTime();
		generateDays(fom);
	}

	public void previous()
	{
		MutableDateTime fom = new MutableDateTime(time);
		fom.addMonths(-1);
		time = fom.toDateTime();
		generateDays(fom);
	}

	/**
	 * Fill calendar with month in referenceDay
	 * 
	 * @param referenceDay
	 *            what month should we display
	 */
	protected void generateDays(MutableDateTime referenceDay)
	{
		// reset to the first of the month at midnight, then find Sunday
		referenceDay.setDayOfMonth(1);
		referenceDay.setMillisOfDay(0);
		int first = (referenceDay.getDayOfWeek() % 7);
		int daysInView = first + referenceDay.dayOfMonth().getMaximumValue();
		int weeks = (int) Math.ceil(daysInView / 7.0);

		inside.setLayout(new java.awt.GridLayout(weeks, 7));
		referenceDay.addDays(-first);

		firstOnMonth = new DateTime(referenceDay);
		
		// remove all old days
		inside.removeAll();

		DateTime from = referenceDay.toDateTime();

		// generate days, weeks*7 covers all possible months, so we just loop
		// through and add each day
		for (int i = 0; i < (weeks * 7); i++)
		{
			MonthDay md = new MonthDay(referenceDay.toDateTime(), getMarker(referenceDay), this);
			inside.add(md);
			md.reBorder(i < 7, (i % 7) == 0, i >= (weeks - 1) * 7);
			this.days.put(referenceDay.getDayOfYear(), md);
			referenceDay.addDays(1); // go to next day
		}

		referenceDay.addDays(-1);// go back one to counteract last add one
		
		lastOnMonth = new DateTime(referenceDay);
		setEvents(getVisibleEvents(from, referenceDay.toDateTime()));
		setCommitments(getVisibleCommitments(from, referenceDay.toDateTime()));

		monthLabel.setText(this.getTime().toString(Months.monthLblFormat));

		// notify mini-calendar to change
		mainPanel.miniMove(time);

		// repaint when changed
		mainPanel.revalidate();
	}

	private List<Event> getVisibleEvents(DateTime from, DateTime to)
	{
		if (MainPanel.getInstance().showEvents())
		{
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
		} else
			return new ArrayList<Event>();
	}

	private List<Commitment> getVisibleCommitments(DateTime from, DateTime to)
	{	
		if (MainPanel.getInstance().showCommitments())
		{
			List<Commitment> visibleCommitments = commitmentModel.getCommitments(from, to);
			
			// Filter for selected categories
			Collection<UUID> selectedCategories = MainPanel.getInstance().getSelectedCategories();
			List<Commitment> categoryFilteredCommitments = new ArrayList<Commitment>();
			
			// Else, loop through events and filter by selected categories
			for (Commitment c : visibleCommitments){
				if (selectedCategories.contains(c.getCategory()))
					categoryFilteredCommitments.add(c);
			}
			
			// Return list of events to be displayed
			return categoryFilteredCommitments;
		}
		else
			return new ArrayList<Commitment>();
	}

	/**
	 * Gets the DayStyle of given date
	 * 
	 * @param date
	 * @return
	 */
	protected DayStyle getMarker(ReadableDateTime date)
	{
		if (date != null && time != null && date.getMonthOfYear() == time.getMonthOfYear())
		{
			return (isToday(date) ? DayStyle.Today : DayStyle.Normal);
		}
		
		return DayStyle.OutOfMonth;
	}

	// Added for testing purposes
	public DateTime getTime()
	{
		return time;
	}

	@Override
	public void updateEvents(Event events, boolean addOrRemove)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void select(Displayable item)
	{
		if (this.lastSelection != null)
		{
			this.lastSelection.deselect(this);
		}
		
		if (item != null)
		{
			item.select(this);
			lastSelection = item;
		}
	}

	/**
	 * 
	 * @param e
	 */
	public void select(Event e)
	{
		MonthDay md;
		MutableDateTime startDay = new MutableDateTime(e.getStart());
		MutableDateTime endDay = new MutableDateTime(e.getEnd());
		endDay.setMillisOfDay(0);
		startDay.setMillisOfDay(0);
		
		if (startDay.isBefore(firstOnMonth))
			startDay=new MutableDateTime(firstOnMonth);
		if (endDay.isAfter(lastOnMonth))
			endDay= new MutableDateTime(lastOnMonth);
			
		while (!endDay.isBefore(startDay))
		{
			md = this.days.get(startDay.getDayOfYear());
			if (md != null)
			{
				md.select(e);
			}
			startDay.addDays(1);
		}
	}
	
	/**
	 * 
	 * @param m
	 */
	public void select(Commitment m)
	{
		MonthDay md = this.days.get(m.getDate().getDayOfYear());
		if (md != null)
		{
			md.select(m);
		}
	}
	
	/**
	 * 
	 * @param e
	 */
	public void deselect(Event e)
	{
		MonthDay md;
		MutableDateTime startDay = new MutableDateTime(e.getStart());
		MutableDateTime endDay = new MutableDateTime(e.getEnd());
		endDay.setMillisOfDay(0);
		startDay.setMillisOfDay(0);
		
		if (startDay.isBefore(firstOnMonth))
			startDay=new MutableDateTime(firstOnMonth);
		if (endDay.isAfter(lastOnMonth))
			endDay= new MutableDateTime(lastOnMonth);
			
		while (!endDay.isBefore(startDay))
		{
			md = this.days.get(startDay.getDayOfYear());
			if (md != null)
			{
				md.clearSelected();
			}
			startDay.addDays(1);
		}
	}
	
	/**
	 * 
	 * @param m
	 */
	public void deselect(Commitment m)
	{
		MonthDay md = this.days.get(m.getDate().getDayOfYear());
		if (md != null)
		{
			md.clearSelected();
		}
	}

	/**
	 * @return the escaped
	 */
	public boolean isEscaped() {
		return escaped;
	}

	/**
	 * @param escaped the escaped to set
	 */
	public void setEscaped(boolean escaped) {
		this.escaped = escaped;
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		tooltip = escaped;
		if (escaped)
		{
			Displayable dp = MainPanel.getInstance().getSelectedEvent();
			if (dp != null)
			{
				String name = dp.getName();
				if (name != null)
				{	
					//get Cursor info
					Point l = MouseInfo.getPointerInfo().getLocation();
					Point pp = inside.getLocationOnScreen();
					int x = l.x-pp.x;
					int y = l.y-pp.y;
					
					//get String properties of displayables
					String time = dp.getFormattedHoverTextTime();
					String days = dp.getFormattedDateRange();
					
					//get widths of stringProperties and find longest
					int timeSize = g.getFontMetrics().stringWidth(time);
					int nameSize = g.getFontMetrics().stringWidth(name);
					int daysSize = g.getFontMetrics().stringWidth(days);
					
					int width = Math.max(
							Math.max(timeSize+10, nameSize+10),
							Math.max(daysSize+10, 60));
					
					
					//generate the polygon
					Polygon dropdown = getDropTextPolygon(width, x, y);
					
					HSLColor background = new HSLColor(dp.getColor());
					Color c = background.adjustLuminance(90);
					
					//draw the polygon
					//g.setColor(new Color(255,255,255,160));
					g.setColor(c);
					g.fillPolygon(dropdown);
					g.setColor(Color.BLACK);
					g.drawPolygon(dropdown);
					
					//draw the text
					g.drawString(name, x+(width-nameSize)/2, y+90);
					g.drawString(time, x+(width-timeSize)/2, y+110);
					g.drawString(days, x+(width-daysSize)/2, y+130);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param width the width of the box (for text)
	 * @param x the mouse's position (x)
	 * @param y the mouse's position (y)
	 * @return the drawing polygon
	 */
	private Polygon getDropTextPolygon(int width, int x, int y)
	{
		y += 40;
		int[] xs = {
				x+0,x+30,x+0,x+0,x+width,x+width,x+60
		};
		int[] ys = {
				y+0,y+30,y+30,y+100,y+100,y+30,y+30
		};
		
		return new Polygon(xs, ys, 7);
	}
}
