/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.utils;

import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.When;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.Pair;

public class EventDualityFactory {
	
	private String title, description;
	private Date start, end;
	private UUID id = UUID.randomUUID();
	private boolean project;
	
	/**
	 * private constructor for the EventDualityFactory
	 */
	private EventDualityFactory(){}
	
	/**
	 * the access point for the factory class
	 * 
	 * @param title the defualt title for the event
	 * @return a factory builder for making a pair of janeway and google calendar events
	 */
	public static EventDualityFactory init(String title)
	{
		EventDualityFactory edf = new EventDualityFactory();
		edf.title = title;
		return edf;
	}
	
	/**
	 * adds the start time to the builder
	 * 
	 * @param start the start time
	 * @return the updated builder
	 */
	public EventDualityFactory setDisplayableStart(Date start)
	{
		this.start = start;
		return this;
	}
	
	/**
	 * adds the end time to the builder
	 * 
	 * @param end the end time
	 * @return the updated builder
	 */
	public EventDualityFactory setDisplayableEnd(Date end)
	{
		this.end = end;
		return this;
	}
	
	/**
	 * adds the start time to the builder as a long (in millis!)
	 * 
	 * @param start the start time of the event
	 * @return the updated builder
	 */
	public EventDualityFactory setDisplayableStart(long start)
	{
		this.start = new Date(start);
		return this;
	}
	
	/**
	 * adds the end time to the builder as a long (in millis!)
	 * 
	 * @param end the end time of the event
	 * @return the updated builder
	 */
	public EventDualityFactory setDisplayableEnd(long end)
	{
		this.end = new Date(end);
		return this;
	}
	
	/**
	 * adds the end time to the builder as a long (in millis!)
	 * 
	 * @param description the description of the event
	 * @return the updated builder
	 */
	public EventDualityFactory setDisplayableDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	/**
	 * adds the ID of the displayable to the factory
	 * 
	 * @param id the ID of the event
	 * @return the updated builder
	 */
	public EventDualityFactory setDisplayableID(UUID id)
	{
		this.id = id;
		return this;
	}
	
	/**
	 * adds the flag for project event
	 * 
	 * @param project whether this is a project event
	 * @return the updated builder
	 */
	public EventDualityFactory setProject(boolean project)
	{
		this.project = project;
		return this;
	}
	
	/**
	 * get the pair of events that are built from the information
	 * 
	 * @return a pair of events
	 */
	public Pair<Displayable, CalendarEventEntry> getDuality()
	{
		Event e = new Event();
		e.addDescription(description)
		 .addEndTime(new DateTime(end))
		 .addName(title)
		 .addStartTime(new DateTime(start));
		e.addIsProjectEvent(project);
		e.setCategory(Category.DEFAULT_CATEGORY.getCategoryID());
		
		CalendarEventEntry cee = new CalendarEventEntry();
		cee.setTitle(new PlainTextConstruct(title));
		cee.setContent(new PlainTextConstruct(description));
		When eventTimes = new When();
		com.google.gdata.data.DateTime startTime = new com.google.gdata.data.DateTime(this.start);
		com.google.gdata.data.DateTime endTime = new com.google.gdata.data.DateTime(this.end);
		eventTimes.setStartTime(startTime);
		eventTimes.setEndTime(endTime);
		cee.addTime(eventTimes);
		
		return new Pair<Displayable, CalendarEventEntry>(e, cee);
		
	}
}
