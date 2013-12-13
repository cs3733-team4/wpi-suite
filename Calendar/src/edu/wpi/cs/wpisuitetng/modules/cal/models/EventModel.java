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
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;

public class EventModel {

	private static EventModel instance;
	public static final DateTimeFormatter serializer = ISODateTimeFormat.basicDateTime();

	
	private EventModel()
	{}
	
	public static EventModel getInstance()
	{
		if (instance == null)
		{
			instance = new EventModel();
		}
		return instance;
	}

	public List<Event> getEvents(DateTime from, DateTime to) {
		final List<Event> events = ServerManager.get("cal/events/", Event[].class, "filter-events-by-range", from.toString(serializer),
				to.toString(serializer));
		
		//set up to filter events based on booleans in MainPanel
		List<Event> filteredEvents = new ArrayList<Event>();
		boolean showPersonal = MainPanel.getInstance().showPersonal;
		boolean showTeam = MainPanel.getInstance().showTeam;
		
		//loop through and add only if isProjectEvent() matches corresponding boolean
		for(Event e: events){
			if(e.isProjectEvent()&&showTeam){
				filteredEvents.add(e);
			}
			if(!e.isProjectEvent()&&showPersonal){
				filteredEvents.add(e);
			}
		}
		return filteredEvents;		
	}
	
	public List<Event> getEvents(UUID categoryID)
	{
		final List<Event> events = ServerManager.get("cal/events/", Event[].class, "filter-events-by-category", categoryID.toString());
		
		return events;
	}

	public boolean putEvent(Event toAdd){
		return ServerManager.put("cal/events", toAdd.toJSON());
	}
	
	public boolean updateEvent(Event toUpdate)
	{
		return ServerManager.post("cal/events", toUpdate.toJSON());
	}
	
	public boolean deleteEvent(Event toRemove)
	{
		return ServerManager.delete("cal/events", "filter-event-by-uuid", toRemove.getEventID().toString());
	}
	

}
