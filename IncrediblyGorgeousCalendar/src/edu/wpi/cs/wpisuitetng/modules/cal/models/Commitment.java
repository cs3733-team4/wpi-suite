package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Basic Commitment class that contains the information required to represent a
 * Commitment on a calendar.
 * 
 */
public class Commitment extends AbstractModel implements Displayable
{
	private UUID commitmentID = UUID.randomUUID();
	private String name;
	private String description;
	private Date duedate;
	private String participants;
	private User owner;

	/**
	 * 
	 * @param name the name of the event
	 * @return this event after having it's name set
	 */
	public Commitment addName(String name)
	{
		setName(name);
		return this;
	}

	/**
	 * 
	 * @param description the event's description
	 * @return this event after having it's description set
	 */
	public Commitment addDescription(String description)
	{
		setDescription(description);
		return this;
	}
	
	/**
	 * 
	 * @param date the starting time
	 * @return this event after having its start date set
	 */
	public Commitment addStartTime(DateTime date)
	{
		setDate(date);
		return this;
	}
	
	
	
	
	/**
	 * Create an event with the default characteristics.
	 */
	public Commitment()
	{
		super();
	}

	public static Commitment fromJson(String json)
	{
		final Gson parser = new Gson();
		return parser.fromJson(json, Commitment.class);
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
		return new Gson().toJson(this, Commitment.class);
	}

	@Override
	public Boolean identify(Object o)
	{
		return null;
	}

	/**
	 * @return the eventID
	 */
	public UUID getCommitmentID()
	{
		return commitmentID;
	}

	/**
	 * @param eventID
	 *            the eventID to set
	 */
	public void setCommitmentID(UUID commitmentID)
	{
		this.commitmentID = commitmentID;
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
	public DateTime getDate()
	{
		return new DateTime(duedate);
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setDate(DateTime start)
	{
		this.duedate = start.toDate();
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

}
