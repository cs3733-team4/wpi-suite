/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;

public abstract class CachingDisplayableModel<T extends Model & Displayable, SA extends CachingModel.SerializedAction<T>> extends CachingModel<T, SA>
{
	public CachingDisplayableModel(String urlname, Class<SA[]> serializedActionClass, Class<T[]> singleClass)
	{
		super(urlname, serializedActionClass, singleClass);
	}

	@Override
	protected void applySerializedChange(SA serializedAction)
	{

		T obj = serializedAction.object;
		if (serializedAction.isDeleted)
		{
			obj = buildUuidOnlyObject(serializedAction.uuid);
			cache.remove(serializedAction.uuid);
		}
		else
		{
			cache.put(serializedAction.uuid, obj);
		}
		MainPanel.getInstance().getMOCA().updateDisplayable(obj, !serializedAction.isDeleted);
	}

	protected abstract T buildUuidOnlyObject(UUID uuid);
	
	@Override
	protected UUID getUuidFrom(T obj)
	{
		return obj.getIdentification();
	}
	
	@Override
	protected boolean filter(T obj)
	{
		return (obj.isProjectwide() ? MainPanel.getInstance().showTeam : MainPanel.getInstance().showPersonal);
	}
	

	/**
	 * Gets all visible events in the range [from..to]
	 * @param from
	 * @param to
	 * @return list of visible events
	 */
	protected List<T> getRange(DateTime from, DateTime to)
	{
		validateCache();
		// set up to filter events based on booleans in MainPanel
		List<T> filteredEvents = new ArrayList<T>();
		final Interval range = new Interval(from, to);

		// loop through and add only if isProjectEvent() matches corresponding
		// boolean
		for (T e : cache.values())
		{
			if (range.overlaps(e.getInterval()) && filter(e))
			{
				filteredEvents.add(e);
			}
		}
		return filteredEvents;
	}
}
