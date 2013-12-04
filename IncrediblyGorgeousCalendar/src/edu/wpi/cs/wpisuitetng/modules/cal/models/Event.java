/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.awt.Color;
import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Basic event class that contains the information required to represent an
 * event on a calendar.
 * 
 */
public class Event extends AbstractModel implements Displayable
{
	private UUID eventID = UUID.randomUUID();
	private String name;
	private String description;
	private Date start;
	private Date end;
	private boolean isProjectEvent;
	private boolean isAllDay;
	private UUID category;
	private String participants;
	private User owner;

	/**
	 * 
	 * @param name the name of the event
	 * @return this event after having it's name set
	 */
	public Event addName(String name)
	{
		setName(name);
		return this;
	}

	/**
	 * 
	 * @param description the event's description
	 * @return this event after having it's description set
	 */
	public Event addDescription(String description)
	{
		setDescription(description);
		return this;
	}
	
	/**
	 * 
	 * @param date the starting time
	 * @return this event after having its start date set
	 */
	public Event addStartTime(DateTime date)
	{
		setStart(date);
		return this;
	}
	
	/**
	 * 
	 * @param date the end time of this event
	 * @return this event after having it's end time set
	 */
	public Event addEndTime(DateTime date)
	{
		setEnd(date);
		return this;
	}
	
	/**
	 * 
	 * @param pe whether this is a project event
	 * @return this event after having it's project flag set
	 */
	public Event addIsProjectEvent(boolean pe)
	{
		setProjectEvent(pe);
		return this;
	}
	
	
	/**
	 * Create an event with the default characteristics.
	 */
	public Event()
	{
		super();
	}

	public static Event fromJson(String json)
	{
		final Gson parser = new Gson();
		return parser.fromJson(json, Event.class);
	}

	@Override
	public void save()
	{
		// This is never called by the core ?
	}

	@Override
	public void delete()
	{
		// This is never called by the core ?
	}

	public String toJSON()
	{
		return new Gson().toJson(this, Event.class);
	}

	@Override
	public Boolean identify(Object o)
	{
		if (o instanceof String)
			return getEventID().toString().equals((String)(o));
		else if (o instanceof UUID)
			return getEventID().equals((UUID)(o));
		else if (o instanceof Event)
			return getEventID().equals(((Event)(o)).getEventID());
		return false;
	}

	/**
	 * @return the eventID
	 */
	public UUID getEventID()
	{
		return eventID;
	}

	/**
	 * @param eventID
	 *            the eventID to set
	 */
	public void setEventID(UUID eventID)
	{
		this.eventID = eventID;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the start
	 */
	public DateTime getStart()
	{
		return new DateTime(start);
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(DateTime start)
	{
		this.start = start.toDate();
	}

	/**
	 * @return the end
	 */
	public DateTime getEnd()
	{
		return new DateTime(end);
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(DateTime end)
	{
		this.end = end.toDate();
	}

	/**
	 * @return the isProjectEvent
	 */
	public boolean isProjectEvent()
	{
		return isProjectEvent;
	}

	/**
	 * @param isProjectEvent
	 *            the isProjectEvent to set
	 */
	public void setProjectEvent(boolean isProjectEvent)
	{
		this.isProjectEvent = isProjectEvent;
	}

	/**
	 * @return the category
	 */
	public UUID getCategory()
	{
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(UUID category)
	{
		this.category = category;
	}

	/**
	 * @return the participants
	 */
	public String getParticipants()
	{
		return participants;
	}

	/**
	 * @param participants
	 *            the participants to set
	 */
	public void setParticipants(String participants)
	{
		this.participants = participants;
	}

	boolean isAllDay()
	{
		return isAllDay;
	}

	void setAllDay(boolean isAllDay)
	{
		this.isAllDay = isAllDay;
	}
	 
	/**
	 * @return the owner
	 */
	public User getOwner()
	{
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner)
	{
		this.owner = owner;
	}

	public Color getColor()
	{
		// TODO: if category, get category color
		return isProjectEvent ? new Color(125,157,227) : new Color(227,125,147);
	}
	public boolean isMultiDayEvent()
	{
		return (getEnd().getYear()!=getStart().getYear() || getEnd().getDayOfYear()!=getStart().getDayOfYear());
			
	}
	@Override
	public DateTime getDate()
	{
		return this.getStart();
	}
	public DateTime getStartTimeOnDay(DateTime givenDay)
	{
		MutableDateTime mDisplayedDay = new MutableDateTime(givenDay);
		mDisplayedDay.setMillisOfDay(1);
		//if it starts before the beginning of the day then its a multi day event, or all day event
		if (this.getStart().isBefore(mDisplayedDay)){
			mDisplayedDay.setMillisOfDay(0);
			return(mDisplayedDay.toDateTime());
		}
		else
			return this.getStart();
	}
	public DateTime getEndTimeOnDay(DateTime givenDay)
	{
		MutableDateTime mDisplayedDay = new MutableDateTime(givenDay);;
		mDisplayedDay.setMillisOfDay(86400000-2);
		if (this.getEnd().isAfter(mDisplayedDay))
		{
			return mDisplayedDay.toDateTime();
		}
		else
			return this.getEnd();
	}
}
