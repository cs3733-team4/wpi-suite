package edu.wpi.cs.wpisuitetng.modules.cal.models.google;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.joda.time.DateTime;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import edu.wpi.cs.wpisuitetng.modules.cal.CalendarLogger;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.EventDualityFactory;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.Pair;

public class GoogleSync {
	
	private static GoogleSync instance;
	
	/**
	 * 
	 * @param a the authentication request
	 * @return an authenticated version of this GCalSyncer
	 */
	public static GoogleSync getInstance(AuthRequest a)
	{
		if (instance == null || a == null)
		{
			Pair<String, String> nameAndPass = a.getAuthenticationInformation();
			try
			{
				instance = new GoogleSync(nameAndPass.getA(), nameAndPass.getB());
				a.succede();
			}
			catch (AuthenticationException ae)
			{
				instance = null;
				a.handleError(ae);
				CalendarLogger.LOGGER.severe(ae.toString());
			}
			catch (MalformedURLException mue)
			{
				instance = null;
				a.handleError(mue);
				CalendarLogger.LOGGER.severe(mue.toString());
			}
		}
		return instance;
	}
	
	/**
	 * disconnect this syncer from google.
	 */
	public static void desync()
	{
		instance = null;
	}
	
	/**
	 * 
	 * @return whether this syncer is in sync
	 */
	public static boolean isInSync()
	{
		return instance != null;
	}
	
	
	
	
	
	private CalendarService service;
	private boolean hasUpdated = true;
	private URL feedURL;
	private Map<UUID, Pair<Displayable, CalendarEventEntry>> syncedEvents;
	private Map<UUID, Pair<Displayable, CalendarEventEntry>> updateMap;
	
	private GoogleSync(String username, String password) throws AuthenticationException, MalformedURLException
	{
		syncedEvents = new HashMap<UUID, Pair<Displayable, CalendarEventEntry>>();
		updateMap = new HashMap<UUID, Pair<Displayable, CalendarEventEntry>>();
		service = new CalendarService("WPISuiteCalendarSync");
		service.setUserCredentials(username, password);
		feedURL = new URL("https://www.google.com/calendar/feeds/" + username + "/private/full");
	}
	
	
	/**
	 * pulls all events between the specified dates.
	 * NOTE: this just puts them into a map, it doesn't
	 * actually DO anything with them
	 * 
	 * 
	 * @param start the start time
	 * @param end the end time 
	 * @throws IOException
	 * @throws ServiceException
	 */
	public void pullEventsBetween(DateTime start, DateTime end) throws IOException, ServiceException
	{
		
		com.google.gdata.data.DateTime startTime = new com.google.gdata.data.DateTime(start.toDate());
		com.google.gdata.data.DateTime endTime = new com.google.gdata.data.DateTime(end.toDate());
		
		CalendarQuery myQuery = new CalendarQuery(feedURL);
		myQuery.setMinimumStartTime(startTime);
		myQuery.setMaximumStartTime(endTime);
		
		CalendarEventFeed resultFeed = service.query(myQuery, CalendarEventFeed.class);
		
		for(int i = 0; i < resultFeed.getTotalResults(); i++)
		{
			CalendarEventEntry cee = resultFeed.getEntries().get(i);
			When times = cee.getTimes().remove(0);
			long startMillis = times.getStartTime().getValue(),
			     endMillis = times.getEndTime().getValue();
			String name, description;
			
			name = cee.getTitle()!=null ? cee.getTitle().getPlainText() : "Event";
			description = cee.getSummary()!=null ? cee.getSummary().getPlainText() : "";
			UUID id = new UUID(startMillis ^ name.hashCode(), endMillis ^ description.hashCode());
			
			EventDualityFactory edf = 
					EventDualityFactory.init(name)
									   .setDisplayableDescription(description)
									   .setDisplayableStart(startMillis)
									   .setDisplayableID(id)
									   .setDisplayableEnd(endMillis);
			
			this.syncedEvents.put(id, edf.getDuality());
		}
	}
	
	/**
	 * get all the events from the janeway database as well as from the google servers
	 * 
	 * @return a list of events
	 */
	public List<Event> getAllEvents()
	{
		List<Event> result = new LinkedList<Event>();
		for(Entry<UUID, Pair<Displayable, CalendarEventEntry>> e : this.syncedEvents.entrySet())
		{
			if (!updateMap.containsKey(e.getKey()))
			{
				this.updateMap.put(e.getKey(), e.getValue());
				result.add((Event) e.getValue().getA());
			}
		}
		this.hasUpdated = result.size() > 0;
		this.syncedEvents.clear();
		return result;
	}
	
	/**
	 * add the displayables to the sync pool
	 * 
	 * @param disp a list of displayables
	 */
	public void addAllDisplayablesToMap(List<? extends Displayable> disp)
	{
		for(Displayable d : disp)
		{
			CalendarEventEntry cee = d.getGoogleCalendarEntry();
			this.syncedEvents.put(d.getUuid(), new Pair<Displayable, CalendarEventEntry>(d, cee));
		}
	}
	
	/**
	 * Has google given us any new events?
	 * 
	 * @return a boolean
	 */
	public boolean hasUpdates()
	{
		return this.hasUpdated;
	}
}
