/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.france;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

/**
 * Class used for day calendar collisions
 */
public class LeadParticle implements Comparable<LeadParticle>
{
	private Event event;
	private DateTime time;
	private boolean isEnd; //is this the start of an event or the end of an event
	private TimeTraveller result;
	
	public LeadParticle(Event event, boolean isEnd, DateTime displayedDay)
	{
		this.event = event;
		this.isEnd = isEnd;
		MutableDateTime mDisplayedDay = new MutableDateTime(displayedDay);
		
		if (!isEnd)
		{
			this.time=event.getStartTimeOnDay(displayedDay);
		}
		else
		{
			this.time=event.getEndTimeOnDay(displayedDay);
		}
	}
	
	public Event getEvent()
	{
		return event;
	}
	
	public DateTime getTime()
	{
		return time;
	}
	public boolean isEnd()
	{
		return isEnd;
	}
	public void setEnd(boolean isEnd)
	{
		this.isEnd = isEnd;
	}
	public TimeTraveller getResult()
	{
		return result;
	}
	public TimeTraveller setResult(TimeTraveller result)
	{
		this.result = result;
		return result;
	}

	@Override
	public int compareTo(LeadParticle o)
	{
		int res = time.compareTo(o.time);
		if (res == 0 && !isEnd) // sort by start, and if they are the same, by last end time
			res = o.event.getEnd().compareTo(event.getEnd());
		return res;
	}
}
