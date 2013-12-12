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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

/**
 * Takes events, which are made of two particles colliding, and smashes them together so fast
 * they turn back time to the renaissance
 */
public class CollisionAlgorithms
{
	/**
	 * Do everything, spin back time, steal paintings
	 * @param events Events to display on day calendar
	 * @return paintings
	 */
	public static List<DayItem> createEventsReallyNicely(List<Event> events, DateTime displayedDay)
	{
		EventEndpoints[] particles = splitEvents(events, displayedDay);
		List<OverlappedEvent> travellers = collideEvents(particles); // shoot at speed of light to go back in time
		xSort(particles);
		Collections.sort(travellers); // Currently traveling backwards, trying to sort things out.
		return generateUI(travellers, displayedDay);
	}
	
	/**
	 * Split collision events into constituent particles that collide
	 * @param events List of events to split
	 * @return Array of lead particles, two for each event (start and end)
	 */
	private static EventEndpoints[] splitEvents(List<Event> events, DateTime displayedDay)
	{
		EventEndpoints re[] = new EventEndpoints[events.size()*2];
		for(int i = 0; i < events.size(); i++)
		{
			re[i*2] = new EventEndpoints(events.get(i), false, displayedDay);
			re[i*2 + 1] = new EventEndpoints(events.get(i), true, displayedDay);
		}
		Arrays.sort(re);
		return re;
	}
	
	/**
	 * Smash particles together in the particle accelerator. Note that the LHC is current
	 * undergoing maintenance, so we are contracting this out to the Fermilab Tevatron.
	 * @param particles Particles to accelerate
	 * @return A bunch of time travelers, one for each collision. 
	 */
	private static List<OverlappedEvent> collideEvents(EventEndpoints[] particles)
	{
		int counter = -1;
		List<OverlappedEvent> out = new ArrayList<OverlappedEvent>(particles.length/2);
		HashMap<Event, OverlappedEvent> active = new HashMap<Event, OverlappedEvent>();
		
		for(EventEndpoints c : particles) // c = speed of light
		{
			if(!c.isEnd())
			{
				OverlappedEvent t = new OverlappedEvent(c.getEvent());
				active.put(c.getEvent(), c.setResult(t));
				counter++;
				// max active
				for (OverlappedEvent who : active.values())
				{
					// count the number of hits we register
					who.setCollisions(Math.max(who.getCollisions(), counter));
					if (t != who)
					{
						who.addOverlappedEvent(t);
						t.addOverlappedEvent(who);
					}
				}
			}
			else
			{
				OverlappedEvent who = active.remove(c.getEvent());
				out.add(who);
				c.setResult(who);
				counter--;
			}
		}
		return out;
	}
	
	/**
	 * Disperses the particles from a collision and sorts by x position where they can fit in 
	 * the detectors.
	 * @param particles The particles to disperse, sorted by time
	 */
	private static void xSort(EventEndpoints[] particles)
	{
		ArrayList<Boolean> state = new ArrayList<>();
		for (EventEndpoints x : particles)
		{
			if (!x.isEnd())
			{
				int i = 0;
				boolean foundPos = false;
				for (; i < state.size(); i++)
				{
					if (!state.get(i))
					{
						state.set(i, true);
						foundPos = true;
						break;
					}
				}
				x.getResult().setXpos(new Rational(i, 1 + x.getResult().getCollisions()));
				if (!foundPos)
				{
					state.add(true);
				}
			}
			else
			{
				state.set(x.getResult().getXpos().getNumerator(), false);
			}
		}
	}
	
	/**
	 * Let the time travelers steal painting from past
	 * @param travellers
	 * @return Stolen Van Gogh Paintings for each time traveler
	 */
	private static List<DayItem> generateUI(List<OverlappedEvent> travellers, DateTime displayedDay)
	{
		List<DayItem> paintings = new ArrayList<>(travellers.size());
		for(OverlappedEvent t : travellers)
		{
			paintings.add(new DayItem(t, displayedDay));
		}
		return paintings;
	}
	
}
