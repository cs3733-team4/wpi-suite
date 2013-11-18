package edu.wpi.cs.wpisuitetng.modules.cal;

import java.util.List;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public interface CalendarInterface
{
	/**
	 * switches the calendar to the next block of time
	 */
	public void next();
	
	/**
	 * switches the calendar to the previous block of time
	 */
	public void previous();
	
	/**
	 * switches the calendar to the provided block of time
	 * 
	 * @param newTime the time to display
	 */
	public void display(DateTime newTime);
	
	/**
	 * adds or removes the provided events
	 * 
	 * @param events the events
	 * @param addOrRemove whether these events should be added or removed
	 */
	public void updateEvents(List<Event> events, boolean addOrRemove);
}
