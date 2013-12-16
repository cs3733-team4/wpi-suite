package edu.wpi.cs.wpisuitetng.modules.cal.utils;

import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.When;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.Pair;

public class EventDualityFactory {
	
	private String title, description;
	private Date start, end;
	private UUID id = UUID.randomUUID();
	private boolean project;
	
	private EventDualityFactory(){}
	
	public static EventDualityFactory init(String title)
	{
		EventDualityFactory edf = new EventDualityFactory();
		edf.title = title;
		return edf;
	}
	
	public EventDualityFactory setDisplayableStart(Date start)
	{
		this.start = start;
		return this;
	}
	
	public EventDualityFactory setDisplayableEnd(Date end)
	{
		this.end = end;
		return this;
	}
	
	public EventDualityFactory setDisplayableStart(long start)
	{
		this.start = new Date(start);
		return this;
	}
	
	public EventDualityFactory setDisplayableEnd(long end)
	{
		this.end = new Date(end);
		return this;
	}
	
	public EventDualityFactory setDisplayableDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public EventDualityFactory setDisplayableID(UUID id)
	{
		this.id = id;
		return this;
	}
	
	public EventDualityFactory setProject(boolean project)
	{
		this.project = project;
		return this;
	}
	
	public Pair<Displayable, CalendarEventEntry> getDuality()
	{
		Event e = new Event();
		e.addDescription(description)
		 .addEndTime(new DateTime(end))
		 .addName(title)
		 .addStartTime(new DateTime(start));
		e.addIsProjectEvent(project);
		e.setCategory(Category.DEFAULT_CATEGORY.getCategoryID());
		
		CalendarEventEntry cee = new CalendarEventEntry();
		cee.setTitle(new PlainTextConstruct(title));
		cee.setContent(new PlainTextConstruct(description));
		When eventTimes = new When();
		com.google.gdata.data.DateTime startTime = new com.google.gdata.data.DateTime(this.start);
		com.google.gdata.data.DateTime endTime = new com.google.gdata.data.DateTime(this.end);
		eventTimes.setStartTime(startTime);
		eventTimes.setEndTime(endTime);
		cee.addTime(eventTimes);
		
		return new Pair<Displayable, CalendarEventEntry>(e, cee);
		
	}

	
	
	
	
	
	
	
}
