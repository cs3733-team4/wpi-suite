/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.awt.Color;
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
	private UUID category;
	private String participants;
	private boolean isProjectCommitment;
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
	 * This does the same things as setDate, it is only kept for compatibility with older code.
	 * @param date the starting time
	 * @return this event after having its start date set
	 */
	public Commitment setDueDate(DateTime date)
	{
		setDate(date);
		return this;
	}
	
	/**
	 * @return the category
	 */
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
		CommitmentModel.getInstance().deleteCommitment(this);
	}

	public String toJSON()
	{
		return new Gson().toJson(this, Commitment.class);
	}

	@Override
	public Boolean identify(Object o)
	{
		if (o instanceof String)
			return getCommitmentID().toString().equals((String)(o));
		else if (o instanceof UUID)
			return getCommitmentID().equals((UUID)(o));
		else if (o instanceof Commitment)
			return getCommitmentID().equals(((Commitment)(o)).getCommitmentID());
		return false;
	}

	/**
	 * @return the eventID
	 */
	public UUID getCommitmentID()
	{
		return commitmentID;
	}

	/**
	 * @param CommitmentID
	 *            the CommitmentID to set
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
	
	public boolean isProjectCommitment()
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
		return CategoryModel.getInstance().getCategoryByUUID(category);
	}
	
	public Color getColor()
	{
		Color fallbackColor = isProjectCommitment ? new Color(125,157,227) : new Color(227,125,147);
		Category cat = CategoryModel.getInstance().getCategoryByUUID(category);
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

}
