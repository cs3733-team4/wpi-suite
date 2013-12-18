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
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Any object that is displayable on the calendar with a date and time, such as events and commitments.
 */
public interface Displayable
{
	/**
	 * Gets the name of the event/commitment.
	 * @return the name
	 */
	public String getName();

	/**
	 * Get the description for the event/commitment.
	 * @return the respective description
	 */
	public String getDescription();

	/**
	 * Gets the participants for the event/commitment.
	 * @return the participants for said event/commitment.
	 */
	public String getParticipants();
	
	/**
	 * Get the time interval
	 * @return the interval
	 */
	public Interval getInterval();
	
	/**
	 * Sets the spanning interval. Note that commitments ignore the end time
	 * @param newInterval the interval to update
	 */
	public void setInterval(Interval newInterval);
	
	/**
	 * See if event/commitment pertains to the project.
	 * @return true if it pertains to the project.
	 */
	public boolean isProjectwide();
	
	/**
	 * deletes this Displayable
	 */
	public void delete();
	
	/**
	 * updates this event (sends call to db layer)
	 */
	public void update();
	
	/**
	 * gets the duration of this event as a string
	 * 
	 * @return a String
	 */
	public String getFormattedHoverTextTime();
	
	/**
	 * returns the dateRange for this event (an empty string for single day events and commitments)
	 * 
	 * @return a String
	 */
	public String getFormattedDateRange();
	
	/**
	 * gets the displayables identification UUID
	 * 
	 * @return a UUID
	 */
	public UUID getUuid();
	
	/**
	 * @return the category
	 */
	public UUID getCategory();
	
	/**
	 * get the color that this event wants to be drawn
	 * @return a Color Object
	 */
	public Color getColor();
	
	/**
	 * 
	 * @return
	 */
	public String toJSON();
	
	/**
	 * Gets the start time of the event on a given day.
	 * @param givenDay the day to check
	 * @return the start time for that day
	 */
	public Interval getIntervalOnDay(DateTime givenDay);
}
