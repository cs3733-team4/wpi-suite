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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import edu.wpi.cs.wpisuitetng.modules.Model;

public abstract class CachingClient<T extends Model, SA extends CachingClient.SerializedAction<T>>
{
	protected HashMap<UUID, T> cache = new HashMap<>();
	protected boolean valid = false;
	final private String urlname;
	final private Class<T[]> singleClass;

	public CachingClient(final String urlname, final Class<SA[]> serializedActionClass, final Class<T[]> singleClass)
	{
		this.urlname = urlname;
		this.singleClass = singleClass;
		// long-polling/comet thread to get instant updates
		Thread t = new Thread(new Runnable() {

			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						List<SA> acts = ServerClient.get("Advanced/cal/" + urlname + "/poll", serializedActionClass);
						for (SA serializedAction : acts)
						{
							applySerializedChange(serializedAction);
						}
					}
					catch (Exception ex)
					{
						invalidateCache();
						ex.printStackTrace();// network went down?
						try
						{
							Thread.sleep(20000);
						}
						catch (InterruptedException e)
						{

						}
					}
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	protected abstract void applySerializedChange(SA serializedAction);
	protected abstract UUID getUuidFrom(T obj);
	protected abstract boolean filter(T obj);

	public T getByUUID(UUID id)
	{
		validateCache();
		T e = cache.get(id);
		return filter(e) ? e : null;
	}

	protected List<T> getAll()
	{
		validateCache();
		List<T> filteredEvents = new ArrayList<T>();
		
		for (T e : cache.values())
		{
			if (filter(e))
				filteredEvents.add(e);
		}
		return filteredEvents;
	}
	
	protected void cache(T obj)
	{
		cache.put(getUuidFrom(obj), obj);
	}

	public void invalidateCache()
	{
		valid = false;
	}

	protected void validateCache()
	{
		if (valid)
			return;
		cache.clear();
		List<T> all = ServerClient.get("cal/" + urlname, this.singleClass);
		for (T event : all)
		{
			cache(event);
		}
		valid = true;
	}

	public boolean put(T toAdd)
	{
		cache(toAdd);
		return ServerClient.put("cal/" + urlname, toAdd.toJSON());
	}

	public boolean update(T toUpdate)
	{
		cache(toUpdate);
		return ServerClient.post("cal/" + urlname, toUpdate.toJSON());
	}

	public boolean delete(T toRemove)
	{
		cache(toRemove);
		return ServerClient.delete("cal/" + urlname, getUuidFrom(toRemove).toString());
	}
	
	public static abstract class SerializedAction<T>
	{
		public T object;
		public UUID uuid;
		public boolean isDeleted;
	}
}
