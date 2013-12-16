package edu.wpi.cs.wpisuitetng.modules.cal.models.google;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
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
			try {
				instance = new GoogleSync(nameAndPass.getA(), nameAndPass.getB());
				a.succede();
			} catch (AuthenticationException ae) {
				instance = null;
				a.handleError(ae);
				ae.printStackTrace();
			} catch (MalformedURLException mue) {
				instance = null;
				a.handleError(mue);
				mue.printStackTrace();
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
	private URL feedURL;
	private Map<String, Pair<Displayable, CalendarEventEntry>> syncedEvents;
	
	private GoogleSync(String username, String password) throws AuthenticationException, MalformedURLException
	{
		syncedEvents = new HashMap<String, Pair<Displayable, CalendarEventEntry>>();
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
			
			EventDualityFactory edf = EventDualityFactory.init(cee.getTitle().getPlainText());
			edf.setDisplayableDescription(cee.getContent().toString())
			   .setDisplayableStart(times.getStartTime().getValue())
			   .setDisplayableEnd(times.getEndTime().getValue());
			   
			this.syncedEvents.put(cee.getId(), edf.getDuality());
			
			System.out.println("Syncing: "+cee.getTitle().getPlainText());
		}
		System.out.println("Synced down "+resultFeed.getEntries().size()+" total events from google");
	}
	
	
	public List<Event> getAllEvents()
	{
		List<Event> result = new LinkedList<Event>();
		for(Entry<String, Pair<Displayable, CalendarEventEntry>> e : this.syncedEvents.entrySet())
		{
			result.add((Event) e.getValue().getA());
		}
		return result;
	}
	
	
	public void addAllDisplayablesToMap(List<? extends Displayable> disp)
	{
		for(Displayable d : disp)
		{
			CalendarEventEntry cee = d.getGoogleCalendarEntry();
			this.syncedEvents.put(cee.getId(), new Pair<Displayable, CalendarEventEntry>(d, cee));
		}
	}
	
	
	/**
	 * 
	 * @param toConvert
	 * @throws IOException
	 * @throws ServiceException
	 */
	public void addEventToCalendar(Displayable toConvert) throws IOException, ServiceException
	{
		CalendarEventEntry myEntry = toConvert.getGoogleCalendarEntry();
		service.insert(feedURL, myEntry);
	}
}
