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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event.SerializedAction;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;

public class EventModel
{
	private static EventModel instance;
	public static final DateTimeFormatter serializer = ISODateTimeFormat.basicDateTime();
	private HashMap<UUID, Event> cache = new HashMap<>();
	boolean valid = false;

	private EventModel()
	{
		Thread t = new Thread(new Runnable() {

			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						//TODO: cache
						List<Event.SerializedAction> acts = ServerManager.get("Advanced/cal/events/poll", Event.SerializedAction[].class);
						if (acts.size() > 0)
						{
							for (SerializedAction serializedAction : acts)
							{
								Event obj = serializedAction.object;
								if (serializedAction.isDeleted)
								{
									obj = new Event();
									obj.setEventID(serializedAction.uuid);
									cache.remove(serializedAction.uuid);
								}
								else
								{
									cache.put(serializedAction.uuid, obj);
								}
								MainPanel.getInstance().getMOCA().updateDisplayable(obj, !serializedAction.isDeleted);
							}
						}
					}
					catch (Exception ex)
					{
						invalidateCache();
						ex.printStackTrace();// network went down?
						try
						{
							Thread.sleep(20000);
						}
						catch (InterruptedException e)
						{
							
						}
					}
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

	public static EventModel getInstance()
	{
		if (instance == null)
		{
			instance = new EventModel();
		}
		return instance;
	}

	public List<Event> getEvents(DateTime from, DateTime to)
	{
		validateCache();
		// set up to filter events based on booleans in MainPanel
		List<Event> filteredEvents = new ArrayList<Event>();
		boolean showPersonal = MainPanel.getInstance().showPersonal;
		boolean showTeam = MainPanel.getInstance().showTeam;
		final Interval range = new Interval(from, to);

		// loop through and add only if isProjectEvent() matches corresponding
		// boolean
		for (Event e : cache.values())
		{
			DateTime s = e.getStart(), end = e.getEnd();
			if (s.isBefore(end) && range.overlaps(new Interval(s, end)))
			{
				if (e.isProjectEvent() && showTeam)
				{
					filteredEvents.add(e);
				}
				else if (!e.isProjectEvent() && showPersonal)
				{
					filteredEvents.add(e);
				}
			}
		}
		return filteredEvents;
	}
	
	public Event getEventById(UUID id)
	{
		validateCache();
		Event e = cache.get(id);
		
		if (e.isProjectEvent() && !MainPanel.getInstance().showTeam)
			e = null;
		else if (!e.isProjectEvent() && !MainPanel.getInstance().showPersonal)
			e = null;
		
		return e;
	}
	
	public List<Event> getAllEvents()
	{
		validateCache();
		// set up to filter events based on booleans in MainPanel
		List<Event> filteredEvents = new ArrayList<Event>();
		boolean showPersonal = MainPanel.getInstance().showPersonal;
		boolean showTeam = MainPanel.getInstance().showTeam;

		// loop through and add only if isProjectEvent() matches corresponding
		// boolean
		for (Event e : cache.values())
		{
			if (e.isProjectEvent() && showTeam)
			{
				filteredEvents.add(e);
			}
			else if (!e.isProjectEvent() && showPersonal)
			{
				filteredEvents.add(e);
			}
		}
		return filteredEvents;
	}
	
	private void invalidateCache()
	{
		valid = false;
	}

	private void validateCache()
	{
		if (valid)
			return;
		cache.clear();
		List<Event> all = ServerManager.get("cal/events/", Event[].class);
		for (Event event : all)
		{
			cache.put(event.getIdentification(), event);
		}
		valid = true;
	}

	public boolean putEvent(Event toAdd)
	{
		cache.put(toAdd.getIdentification(), toAdd);
		return ServerManager.put("cal/events", toAdd.toJSON());
	}

	public boolean updateEvent(Event toUpdate)
	{
		cache.put(toUpdate.getIdentification(), toUpdate);
		return ServerManager.post("cal/events", toUpdate.toJSON());
	}

	public boolean deleteEvent(Event toRemove)
	{
		cache.remove(toRemove.getEventID());
		return ServerManager.delete("cal/events", toRemove.getEventID().toString());
	}

}
