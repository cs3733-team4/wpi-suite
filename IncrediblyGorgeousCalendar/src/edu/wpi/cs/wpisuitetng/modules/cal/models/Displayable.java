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

import org.joda.time.DateTime;

/**
 * Any object that is displayable on the calendar with a date and time, such as events and commitments.
 */
public interface Displayable
{
	public String getName();

	public String getDescription();

	public String getParticipants();

	/**
	 * The date to display. If there are more than one, the default date (start)
	 */
	public DateTime getDate();
}
