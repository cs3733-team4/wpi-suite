/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models.data;

import java.awt.Color;
import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.MutableDateTime;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentStatus;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.CachingClient;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.CategoryClient;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.CommitmentClient;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Months;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Basic Commitment class that contains the information required to represent a
 * Commitment on a calendar.
 * 
 */
public class Commitment extends AbstractModel implements Displayable
{
	private UUID uuid = UUID.randomUUID();
	private String name;
	private String description;
	private Date duedate;
	private UUID category;
	private String participants;
	private boolean isProjectCommitment;
	private User owner;
	private CommitmentStatus status;
	// Default status for new commitments.
	public static final CommitmentStatus DEFAULT_STATUS = CommitmentStatus.NotStarted;

	/**
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
	
	@Override
	public UUID getCategory()
	{
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(UUID category)
	{
		this.category = category;
	}
	
	/**
	 * Create an event with the default characteristics.
	 */
	public Commitment()
	{
		super();
		status=DEFAULT_STATUS;
	}

	/**
	 * 
	 * @param json the JSON string that represents this object
	 * @return a commitment with fields matching the JSON
	 */
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
		CommitmentClient.getInstance().delete(this);
	}

	/**
	 * @return this object in JSON form
	 */
	public String toJSON()
	{
		return new Gson().toJson(this, Commitment.class);
	}

	@Override
	public Boolean identify(Object o)
	{
		if (o instanceof String)
			return getUuid().toString().equals((String)(o));
		else if (o instanceof UUID)
			return getUuid().equals((UUID)(o));
		else if (o instanceof Commitment)
			return getUuid().equals(((Commitment)(o)).getUuid());
		return false;
	}

	/**
	 * @param CommitmentID
	 *            the CommitmentID to set
	 */
	public void setUuid(UUID commitmentID)
	{
		this.uuid = commitmentID;
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
	@Override
	public Interval getInterval()
	{
		return new Interval(new DateTime(duedate), new DateTime(duedate));
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setInterval(Interval start)
	{
		this.duedate = start.getStart().toDate();
	}
	
	/**
	 * @param dt date to set the commitment to
	 */
	public Commitment setDate(DateTime dt)
	{
		duedate = dt.toDate();
		return this;
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
	
	public boolean isProjectwide()
	{
		return isProjectCommitment;
	}
	
	/**
	 * @param isProjectCommitment
	 *            the isProjectCommitment to set
	 */
	public void setProjectCommitment(boolean isProjectCommitment)
	{
		this.isProjectCommitment = isProjectCommitment;
	}
	
	public Category getAssociatedCategory()
	{
		return CategoryClient.getInstance().getCategoryByUUID(category);
	}
	
	public Color getColor()
	{
		Color fallbackColor = isProjectCommitment ? new Color(125,157,227) : new Color(227,125,147);
		Category cat = CategoryClient.getInstance().getCategoryByUUID(category);
		if (cat == null)
		{
			return fallbackColor;
		}
		Color commitmentColor = cat.getColor();
		if (commitmentColor != null)
		{
			return commitmentColor;
		}
		return fallbackColor;
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

	@Override
	public void update()
	{
		CommitmentClient.getInstance().update(this);
	}
	
	@Override
	public String getFormattedHoverTextTime()
	{
		DateTime s = new DateTime(this.duedate);
		StringBuilder timeFormat = new StringBuilder()
			.append(s.getHourOfDay())
			.append(":")
			.append(s.getMinuteOfHour());
		return timeFormat.toString();
	}

	@Override
	public String getFormattedDateRange()
	{
		DateTime s = new DateTime(this.duedate);
		StringBuilder timeFormat = new StringBuilder()
			.append(s.monthOfYear().getAsShortText())
			.append(", ")
			.append(Months.getDescriptiveNumber(s.getDayOfMonth()));
		return timeFormat.toString();
	}
	
	@Override
	public UUID getUuid()
	{
		return uuid;
	}

	/**
	 * this is primarily used for multiday events
	 * 
	 * @param givenDay gets the time that this event starts on a given day
	 * @return when this event starts
	 */
	@Override
	public Interval getIntervalOnDay(DateTime givenDay)
	{
		DateTime start = new DateTime(duedate);
		MutableDateTime mDisplayedDay = new MutableDateTime(givenDay);
		mDisplayedDay.setMillisOfDay(0);
		
		if (start.isBefore(mDisplayedDay))
		{
			return new Interval(mDisplayedDay, mDisplayedDay);
		}
		mDisplayedDay.addDays(1);
		mDisplayedDay.addMillis(-1);
		if (start.isAfter(mDisplayedDay))
		{
			return new Interval(mDisplayedDay, mDisplayedDay);
		}
		return new Interval(start, start);
	}
	
	/**
	 * Gets the current status the commitment is at.
	 * @return the current commitment status as a String.
	 */
	public CommitmentStatus getStatus()
	{
		return this.status;
	}
	
	public Commitment addStatus(CommitmentStatus status) {
		this.status=status;
		return this;
	}
	
	/**
	 * Set the status to a given status input.
	 * @param status
	 */
	public void setStatus(CommitmentStatus status)
	{
		this.status = status;
	}
	
	public Color getStatusColor()
	{
		return status == CommitmentStatus.NotStarted ? Colors.COMMITMENT_NOT_STARTED :
				status == CommitmentStatus.InProgress ? Colors.COMMITMENT_IN_PROGRESS :
														Colors.COMMITMENT_COMPLETE;
	}
	
	public static class SerializedAction extends CachingClient.SerializedAction<Commitment>
	{
		public SerializedAction(Commitment e, UUID eventID, boolean b)
		{
			object = e;
			uuid = eventID;
			isDeleted = b;
		}
	}
}