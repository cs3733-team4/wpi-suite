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
import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.TimeOrderedList;

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
	
	public synchronized void updated(String item)
	{
		changesc.pushChange(item);
		for (PushedInfo pi : waiting)
		{
			pi.pushUpdates(item);
			TimeOrderedList<String> iter = changesc.timeOrderedCallIterator(pi.getSessionID());
			Iterator<String> is =  iter.iterator();
			while(is.hasNext())
			{
				iter.setValue(is.next());
			}
		}
		waiting.clear();
	}
	
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
	
	public synchronized void unlistenSession(PushedInfo listener)
	{
		waiting.remove(listener);
	}
	
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
		public abstract void pushUpdates(String item);
	}
}
