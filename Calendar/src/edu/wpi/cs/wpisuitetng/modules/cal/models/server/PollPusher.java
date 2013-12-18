/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.Cache;

/**
 * Keeps track of who needs to be notified when using long polling.
 * @param <T> Type of objects to be notified of
 */
public class PollPusher<T>
{	
	private Cache<String, String> changesc = new Cache<>(" ");
	private List<PushedInfo> waiting = new LinkedList<>();
	@SuppressWarnings("rawtypes")
	private static HashMap<Class, PollPusher> instances = new HashMap<>(3);
	
	@SuppressWarnings("unchecked")
	public static <T> PollPusher<T> getInstance(Class<T> type)
	{
		if (instances.containsKey(type))
		{
			return (PollPusher<T>)instances.get(type);
		}
		PollPusher<T> pp = new PollPusher<>();
		instances.put(type, pp);
		return pp;
	}
	
	/**
	 * Send a new update to all listeners, attached or not.
	 * @param item JSON data to send to all clients
	 */
	public synchronized void updated(String item)
	{
		//TODO: expire
		changesc.pushChange(item);
		for (PushedInfo pi : waiting)
		{
			pi.pushUpdates(item);
			changesc.bringUpToHead(pi.getSessionID());
		}
		waiting.clear();
	}
	
	/**
	 * Register a listener for data pushes. If there are pending requests, this returns the json data of all 
	 * pending requests, otherwise null. If changes are returned, the listener is not registered. Otherwise,
	 * unlistenSession needs to be called when time has expired
	 * @param listener Listener to register
	 * @return JSON data or null
	 */
	public synchronized String listenSession(PushedInfo listener)
	{
		Iterable<String> iter = changesc.timeOrderedCallIterator(listener.getSessionID());
		if (!iter.iterator().hasNext())
		{
			waiting.add(listener);
			return null;
		}
		else
		{
			Iterator<String> is = iter.iterator();
			StringBuilder sb = new StringBuilder("[");
			while (is.hasNext())
			{
				sb.append(is.next());
				sb.append(",");
			}
			sb.setCharAt(sb.length() - 1, ']');
			return sb.toString();
		}
	}
	
	/**
	 * Removes the given listener from the pending updates chain
	 * @param listener Listener to remove
	 */
	public synchronized void unlistenSession(PushedInfo listener)
	{
		waiting.remove(listener);
	}
	
	/**
	 * Interface for pushing updates to clients
	 */
	public static abstract class PushedInfo
	{
		private String session;
		public PushedInfo(String session)
		{
			this.session = session;
		}
		public String getSessionID()
		{
			return session;
		}
		/**
		 * This method is called asynchronously on an undefined thread when changes occur
		 * @param item single JSON data item
		 */
		public abstract void pushUpdates(String item);
	}
}
