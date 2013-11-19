package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.util.UUID;

import org.joda.time.DateTime;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Basic event class that contains the information required to represent an
 * event on a calendar.
 * 
 */
public class Event extends AbstractModel
{
	private UUID eventID = UUID.randomUUID();
	private String name;
	private String description;
	private DateTime start;
	private DateTime end;
	private boolean isProjectEvent;
	private boolean isAllDay;
	private String[] categories;
	private String participants;
	private User owner;

	/**
	 * Create an event with the default characteristics.
	 */
	public Event()
	{
		super();
	}

	public static Event fromJson(String json)
	{
		final Gson parser = new Gson();
		return parser.fromJson(json, Event.class);
	}

	@Override
	public void save()
	{
		// This is never called by the core ?
	}

	@Override
	public void delete()
	{
		// This is never called by the core ?
	}

	public String toJSON()
	{
		return new Gson().toJson(this, Event.class);
	}

	@Override
	public Boolean identify(Object o)
	{
		return null;
	}

	/**
	 * @return the eventID
	 */
	public UUID getEventID()
	{
		return eventID;
	}

	/**
	 * @param eventID
	 *            the eventID to set
	 */
	public void setEventID(UUID eventID)
	{
		this.eventID = eventID;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the start
	 */
	public DateTime getStart()
	{
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(DateTime start)
	{
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public DateTime getEnd()
	{
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(DateTime end)
	{
		this.end = end;
	}

	/**
	 * @return the isProjectEvent
	 */
	public boolean isProjectEvent()
	{
		return isProjectEvent;
	}

	/**
	 * @param isProjectEvent
	 *            the isProjectEvent to set
	 */
	public void setProjectEvent(boolean isProjectEvent)
	{
		this.isProjectEvent = isProjectEvent;
	}

	/**
	 * @return the categories
	 */
	public String[] getCategories()
	{
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(String[] categories)
	{
		this.categories = categories;
	}

	/**
	 * @return the participants
	 */
	public String getParticipants()
	{
		return participants;
	}

	/**
	 * @param participants
	 *            the participants to set
	 */
	public void setParticipants(String participants)
	{
		this.participants = participants;
	}

	boolean isAllDay()
	{
		return isAllDay;
	}

	void setAllDay(boolean isAllDay)
	{
		this.isAllDay = isAllDay;
	}
	 
	/**
	 * @return the owner
	 */
	public User getOwner()
	{
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner)
	{
		this.owner = owner;
	}

	// Accessor and Mutator Methods:

}