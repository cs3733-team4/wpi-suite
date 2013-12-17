/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.collisiondetection;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Event;

/**
 * Class used for day calendar collisions
 */
public class EventEndpoints implements Comparable<EventEndpoints>
{
	private Event event;
	private DateTime time;
	private boolean isEnd;
	private OverlappedEvent result;
	
	/**
	 * 
	 * @param event the event to getEndpoints from
	 * @param isEnd whether this is the end of an event (for multiday)
	 * @param displayedDay the day on which the event is being displayed
	 */
	public EventEndpoints(Event event, boolean isEnd, DateTime displayedDay)
	{
		this.event = event;
		this.isEnd = isEnd;
		
		if (!isEnd)
		{
			this.time=event.getStartTimeOnDay(displayedDay);
		}
		else
		{
			this.time=event.getEndTimeOnDay(displayedDay);
		}
	}
	
	/**
	 * 
	 * @return this event
	 */
	public Event getEvent()
	{
		return event;
	}
	
	/**
	 * 
	 * @return this time
	 */
	public DateTime getTime()
	{
		return time;
	}
	
	/**
	 * 
	 * @return return whether this is the end
	 */
	public boolean isEnd()
	{
		return isEnd;
	}
	
	/**
	 * 
	 * @param isEnd set whether this is the end
	 */
	public void setEnd(boolean isEnd)
	{
		this.isEnd = isEnd;
	}
	
	/**
	 * 
	 * @return get the overlappingEvent that we encapsulate
	 */
	public OverlappedEvent getResult()
	{
		return result;
	}
	
	/**
	 * 
	 * @param result the overlap to set
	 * @return this overlapping event once it has been set
	 */
	public OverlappedEvent setResult(OverlappedEvent result)
	{
		this.result = result;
		return result;
	}

	@Override
	public int compareTo(EventEndpoints o)
	{
		int res = time.compareTo(o.time);
		if (res == 0 && isEnd != o.isEnd)
			res = isEnd ? -1 : 1;
		if (res == 0 && !isEnd) // sort by start, and if they are the same, by last end time
			res = o.event.getEnd().compareTo(event.getEnd());
		return res;
	}
}
