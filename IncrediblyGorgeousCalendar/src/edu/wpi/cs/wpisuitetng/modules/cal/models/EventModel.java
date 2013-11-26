package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;

public class EventModel {

	public static final DateTimeFormatter serializer = ISODateTimeFormat.basicDateTime();


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

	public boolean putEvent(Event toAdd){
		return ServerManager.put("cal/events", toAdd.toJSON());
	}
	
	public boolean updateEvent(Event toUpdate)
	{
		return ServerManager.post("cal/events", toUpdate.toJSON());
	}
	
	

}
