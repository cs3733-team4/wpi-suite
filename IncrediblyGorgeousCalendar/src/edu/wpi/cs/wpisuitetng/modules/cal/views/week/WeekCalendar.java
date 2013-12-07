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

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
	
	private JPanel dayHolderPanel = new JPanel();
	private JPanel dayGrid = new JPanel();
	private JPanel dayGridContainer = new JPanel();
	
	private JPanel dayTitleGrid = new JPanel();
	private JPanel dayTitlesContainer = new JPanel();
	
	private JScrollPane scroll = new JScrollPane(dayGridContainer);

	private EventModel eventModel;

	private DateTimeFormatter titleFmt = DateTimeFormat.forPattern("EEEE, MMM d, yyyy");

	public WeekCalendar(DateTime on, EventModel emodel)
	{
		this.mainPanel = MainPanel.getInstance();
		this.time = Months.getWeekStart(on);
		eventModel = emodel;
		scroll.setBackground(Colors.TABLE_BACKGROUND);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		dayGrid.setBackground(Colors.TABLE_BACKGROUND);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.dayHolderPanel.setLayout(new BorderLayout());
		this.dayGrid.setLayout(new GridLayout(1, 7));
		this.dayTitleGrid.setLayout(new GridLayout(1, 7));
		this.dayTitlesContainer.setLayout(new BoxLayout(dayTitlesContainer, BoxLayout.X_AXIS));
		this.dayGridContainer.setLayout(new BorderLayout()); //Nested BorderLayouts arent the best
		
		generateDay();
	}

	private void generateDay()
	{
		this.dayGrid.removeAll();
		this.removeAll();
		
		MutableDateTime increment=new MutableDateTime(time);
		
		this.sun = new LouvreTour();
		this.sun.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		this.sun.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
		increment.addDays(1);
		this.mon = new LouvreTour();
		this.mon.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		this.mon.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
		increment.addDays(1);
		this.tue = new LouvreTour();
		this.tue.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		this.tue.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
		increment.addDays(1);
		this.wed = new LouvreTour();
		this.wed.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		this.wed.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
		increment.addDays(1);
		this.thu = new LouvreTour();
		this.thu.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		this.thu.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
		increment.addDays(1);
		this.fri = new LouvreTour();
		this.fri.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		this.fri.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
		increment.addDays(1);
		this.sat = new LouvreTour();
		this.sat.setEvents(getVisibleEvents(increment.toDateTime()), increment.toDateTime());
		this.sat.setBorder(BorderFactory.createLineBorder(Colors.BORDER));

		//add days to grid
		this.dayGrid.add(this.sun);
		this.dayGrid.add(this.mon);
		this.dayGrid.add(this.tue);
		this.dayGrid.add(this.wed);
		this.dayGrid.add(this.thu);
		this.dayGrid.add(this.fri);
		this.dayGrid.add(this.sat);
		
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
		JLabel spacerLabel = new JLabel("10:00    ");
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
		
		//add title and week view to this
		this.add(new JLabel(time.toString(titleFmt)));
		this.add(dayHolderPanel);
		
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
