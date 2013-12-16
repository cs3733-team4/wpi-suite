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
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * 
	 * @return
	 */
	public String getParticipants();

	/**
	 * The date to display. If there are more than one, the default date (start)
	 */
	public DateTime getDate();
	
	/**
	 * Sets the date to display. If there are more than one, the default date (start)
	 */
	public void setDate(DateTime newDate);
	
	public DateTime getEnd();
	
	public Interval getInterval();
	
	public boolean isProjectwide();
	
	/**
	 * deletes this Displayable
	 */
	public void delete();
	
	/**
	 * sets the time (for easy updating)
	 */
	public void setTime(DateTime newTime);
	
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
	public UUID getIdentification();
	
	/**
	 * @return the category
	 */
	public UUID getCategory();
	
	/**
	 * get the color that this event wants to be drawn
	 * @return a Color Object
	 */
	public Color getColor();
	
	public String toJSON();
	
	public DateTime getStartTimeOnDay(DateTime givenDay);
	
	public DateTime getEndTimeOnDay(DateTime givenDay);
	
}
