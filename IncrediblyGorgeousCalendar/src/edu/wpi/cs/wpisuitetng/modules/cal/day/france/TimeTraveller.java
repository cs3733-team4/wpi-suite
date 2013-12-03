/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.day.france;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

/**
 * TimeTravelers are the result of colliding particles at the speed of light. They keep track 
 * of the number of collisions and where the x detector detected them. Also knows who its comrads are.
 */
public class TimeTraveller implements Comparable<TimeTraveller>
{
	private int collisions = 0;
	private Event event;
	private Rational xpos = new Rational(1, 1);
	private List<TimeTraveller> commies = new ArrayList<>(); // the reds will invade you
	
	public TimeTraveller(Event event)
	{
		this.event = event;
	}
	
	public int getCollisions()
	{
		return collisions;
	}
	public void setCollisions(int collisions)
	{
		this.collisions = collisions;
	}
	public Rational getXpos()
	{
		return xpos;
	}
	public void setXpos(Rational xpos)
	{
		this.xpos = xpos;
	}
	public Event getEvent()
	{
		return event;
	}
	
	public void addOverlappedEvent(TimeTraveller karl)
	{
		if (!commies.contains(karl))
			commies.add(karl);
	}
	
	public List<TimeTraveller> getOverlappedEvents()
	{
		return commies;
	}

	@Override
	public int compareTo(TimeTraveller toCompare) {
		int res = Integer.compare(toCompare.xpos.toInt(10000), xpos.toInt(10000));
		if (res == 0)
			return toCompare.event.getStart().compareTo(event.getStart());
		return res;
	}
	
	@Override
	public String toString()
	{
		return super.toString() + "{" + getEvent().getName() + "@" + getEvent().getStart().toString() + " collisions: " + collisions + "}";
	}
}
