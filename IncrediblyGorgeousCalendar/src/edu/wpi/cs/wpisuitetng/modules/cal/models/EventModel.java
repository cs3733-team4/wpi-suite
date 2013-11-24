package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

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
	
	

}
