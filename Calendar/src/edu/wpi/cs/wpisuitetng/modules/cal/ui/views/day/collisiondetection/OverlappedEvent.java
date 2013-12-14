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

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class OverlappedEvent implements Comparable<OverlappedEvent>
{
	private int collisionCount = 0;
	private Event event;
	private Rational xpos = new Rational(1, 1);
	private List<OverlappedEvent> collisions = new ArrayList<>();
	
	/**
	 * 
	 * @param event the event that this encapsulates
	 */
	public OverlappedEvent(Event event)
	{
		this.event = event;
	}
	
	/**
	 * 
	 * @return the maximum number of collisions this event has
	 * 		   at any given time
	 */
	public int getCollisions()
	{
		return collisionCount;
	}
	
	/**
	 * 
	 * @param collisions set the max num of collisions
	 */
	public void setCollisions(int collisions)
	{
		this.collisionCount = collisions;
	}
	
	/**
	 * 
	 * @return get this event's horizontal position
	 */
	public Rational getXpos()
	{
		return xpos;
	}
	
	/**
	 * 
	 * @param xpos set this event's horizontal position
	 */
	public void setXpos(Rational xpos)
	{
		this.xpos = xpos;
	}
	
	/**
	 * 
	 * @return the event we encapsulate
	 */
	public Event getEvent()
	{
		return event;
	}
	
	/**
	 * 
	 * @param overlapped add an overlapping event to the list of events that this overlaps with
	 */
	public void addOverlappedEvent(OverlappedEvent overlapped)
	{
		if (!collisions.contains(overlapped))
			collisions.add(overlapped);
	}
	
	/**
	 * 
	 * @return all of the events that we overlap with
	 */
	public List<OverlappedEvent> getOverlappedEvents()
	{
		return collisions;
	}

	@Override
	public int compareTo(OverlappedEvent toCompare) {
		int res = Integer.compare(toCompare.xpos.toInt(10000), xpos.toInt(10000));
		if (res == 0)
			return toCompare.event.getStart().compareTo(event.getStart());
		return res;
	}
	
	@Override
	public String toString()
	{
		return super.toString() + "{" + getEvent().getName() + "@" + getEvent().getStart().toString() + " collisions: " + collisionCount + "}";
	}
}
