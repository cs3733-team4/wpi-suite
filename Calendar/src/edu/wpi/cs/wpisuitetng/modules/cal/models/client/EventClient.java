/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models.client;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;

public class EventClient extends CachingDisplayableClient<Event, Event.SerializedAction>
{
	private static EventClient instance;

	protected EventClient()
	{
		super("events", Event.SerializedAction[].class, Event[].class);
	}
	
	@Override
	protected Event buildUuidOnlyObject(UUID uuid)
	{
		Event e = new Event();
		e.setUuid(uuid);
		return e;
	}

	public static EventClient getInstance()
	{
		if (instance == null)
		{
			instance = new EventClient();
		}
		return instance;
	}

	/**
	 * Gets all visible events in the range [from..to]
	 * @param from
	 * @param to
	 * @return list of visible events
	 */
	public List<Event> getEvents(DateTime from, DateTime to)
	{
		return getRange(from, to);
	}

	public Event getEventById(UUID id)
	{
		return getByUUID(id);
	}

	public List<Event> getAllEvents()
	{
		return getAll();
	}
	
	@Override
	protected boolean visibleCategory(Event obj)
	{
		return MainPanel.getInstance().showEvents() && super.visibleCategory(obj);
	}
}
