package edu.wpi.cs.wpisuitetng.modules.cal;

import java.util.List;

import javax.swing.JComponent;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

@SuppressWarnings("serial")
public abstract class AbstractCalendar extends JComponent
{
	public abstract void next();
	public abstract void previous();
	public abstract void display(DateTime newTime);
	public abstract void addEvent(Event event);
	public abstract void addEvents(List<Event> eventList);
	public abstract void removeEvent(Event event);
	public abstract void removeEvents(List<Event> event);
}
