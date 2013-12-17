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
import java.util.LinkedList;
import java.util.List;

public class PollPusher<T>
{
	private HashMap<String, Integer> indexqueue = new HashMap<>();
	private List<String> changes = new LinkedList<>();
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
		changes.add(item);
		int newidx = changes.size() - 1;
		for (PushedInfo pi : waiting)
		{
			pi.pushUpdates(item);
			indexqueue.put(pi.getSessionID(), newidx);
		}
		waiting.clear();
	}
	
	public synchronized String listenSession(PushedInfo listener)
	{
		// TODO: expire
		Integer previdx = indexqueue.get(listener.getSessionID());
		indexqueue.put(listener.getSessionID(), changes.size() - 1);
		if (previdx == null || previdx == changes.size() - 1)
		{
			waiting.add(listener);
			return null;
		}
		else
		{
			StringBuilder sb = new StringBuilder("[");
			for (int i = previdx; i < changes.size(); i++)
			{
				sb.append(changes.get(i));// TODO: not efficient
				if (i + 1 < changes.size())
					sb.append(",");
			}
			sb.append("]");
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
