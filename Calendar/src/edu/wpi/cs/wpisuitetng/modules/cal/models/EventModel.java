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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gdata.util.ServiceException;

import edu.wpi.cs.wpisuitetng.modules.cal.models.google.DisplayableSyncer;
import edu.wpi.cs.wpisuitetng.modules.cal.models.google.GoogleSync;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;

public class EventModel
{
	private static EventModel instance;
	public static final DateTimeFormatter serializer = ISODateTimeFormat.basicDateTime();

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
							System.out.println("found acts!");
							MainPanel.getInstance().refreshView();
						}
					}
					catch (Exception ex)
					{
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
		final List<Event> events = ServerManager.get("Advanced/cal/events/filter-events-by-range/" + from.toString(serializer) + "/" + to.toString(serializer),
				Event[].class);

		// set up to filter events based on booleans in MainPanel
		List<Event> filteredEvents = new ArrayList<Event>();
		boolean showPersonal = MainPanel.getInstance().showPersonal;
		boolean showTeam = MainPanel.getInstance().showTeam;
		
		//add all database events to the gcal syncer
		//pulls all events back out of gcal syncer before filter;
		GoogleSync gs = MainPanel.getInstance().getGoogleCalendarSyncer();
		if (gs != null)
		{
			try
			{
				gs.pullEventsBetween(from, to);
				for(Event e : gs.getAllEvents())
				{
					updateEvent(e);
				}
				gs.addAllDisplayablesToMap(filteredEvents);
				
				filteredEvents = gs.getAllEvents();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		// loop through and add only if isProjectEvent() matches corresponding
		// boolean
		for (Event e : events)
		{
			if (e.isProjectEvent() && showTeam)
			{
				filteredEvents.add(e);
			}
			if (!e.isProjectEvent() && showPersonal)
			{
				filteredEvents.add(e);
			}
		}
		//for(Event e : events)
		//{
		//	new DisplayableSyncer(MainPanel.getInstance().getGoogleCalendarSyncer(), e);
		//}
		
		//GoogleSync gs = MainPanel.getInstance().getGoogleCalendarSyncer();
		//if (gs != null)
		//{
		//	try {
		//		gs.pullEventsBetween(from, to);
		//	} catch (IOException | ServiceException e1) {
		//		e1.printStackTrace();
		//	}
		//}
		
		return filteredEvents;
	}

	public boolean putEvent(Event toAdd)
	{
		return ServerManager.put("cal/events", toAdd.toJSON());
	}

	public boolean updateEvent(Event toUpdate)
	{
		return ServerManager.post("cal/events", toUpdate.toJSON());
	}

	public boolean deleteEvent(Event toRemove)
	{
		return ServerManager.delete("cal/events", toRemove.getEventID().toString());
	}

}
