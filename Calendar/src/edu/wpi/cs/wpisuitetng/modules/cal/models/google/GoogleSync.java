package edu.wpi.cs.wpisuitetng.modules.cal.models.google;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
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
			} catch (AuthenticationException ae) {
				a.handleError(ae);
			} catch (MalformedURLException mue) {
				a.handleError(mue);
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
	
	private GoogleSync(String username, String password) throws AuthenticationException, MalformedURLException
	{
		service = new CalendarService("WPISuiteCalendarSync");
		service.setUserCredentials(username, password);
		feedURL = new URL("https://www.google.com/calendar/feeds/" + username + "/private/full");
	}
	
	
	public boolean makeCalendar(String calendarName, String calendarColor) throws IOException, ServiceException
	{
		CalendarEntry newCalendar = new CalendarEntry();
		newCalendar.setColor(new ColorProperty("#A32929"));
		newCalendar.setTitle(new PlainTextConstruct(calendarName));
		return service.insert(feedURL, newCalendar) != null;
	}
	
	
	
	public void addEventToCalendar(Displayable toConvert) throws IOException, ServiceException
	{
		CalendarEventEntry myEntry = toConvert.getGoogleCalendarEntry();
		service.insert(feedURL, myEntry);
	}
}
