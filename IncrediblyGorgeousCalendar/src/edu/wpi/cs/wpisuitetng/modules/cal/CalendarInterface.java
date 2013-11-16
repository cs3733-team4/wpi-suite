package edu.wpi.cs.wpisuitetng.modules.cal;

import java.util.List;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public interface CalendarInterface
{
	public void next();
	public void previous();
	public void display(DateTime newTime);
	public void addEvent(Event event);
	public void addEvents(List<Event> eventList);
	public void removeEvent(Event event);
	public void removeEvents(List<Event> event);
}
