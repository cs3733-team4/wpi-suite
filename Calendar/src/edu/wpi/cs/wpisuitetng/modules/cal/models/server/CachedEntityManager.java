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

import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.PollPusher.PushedInfo;
/**
 * This is the entity manager for the Event in the
 * EventManager module.
 */
public abstract class CachedEntityManager<T extends Model> implements EntityManager<T> {
	/** The database */
	Data db;
	protected PollPusher<T> pollPusher;
	
	/**
	 * Constructs the entity manager. This constructor is called by
	 * {@link edu.wpi.cs.wpisuitetng.ManagerLayer#ManagerLayer()}. To make sure
	 * this happens, be sure to place add this entity manager to the map in
	 * the ManagerLayer file.
	 * 
	 * @param db a reference to the persistent database
	 */
	public CachedEntityManager(Data db) {
		this.db = db; 
	}
	
	/**
	 * Comet/Long Polling implementation for real-time updating. Waits 20s for any changes, then returns
	 * @param s Session this is on
	 * @return json string with the results
	 */
	protected String getFromPoll(Session s)
	{
		PollPusher<T> pp = pollPusher;
		final String[] stringList = new String[]{"[]"}; // so we can modify the string from the listener
		final Thread thisthread = Thread.currentThread();
		PushedInfo listener = (new PushedInfo(s.getSessionId()) {
			
			@Override
			public void pushUpdates(String item)
			{
				// poor mans json. its only one item
				stringList[0] = "[" + item + "]";
				thisthread.interrupt();
			}
		});
		String events = pp.listenSession(listener);
		if (events == null)
		{
			try
			{
				Thread.sleep(20000);
				pp.unlistenSession(listener);
			}
			catch (InterruptedException e)
			{
				// we were interruped!
			}
			return stringList[0];
		}
		else
			return events;
	}

	/**
	 * Deletes a event from the database
	 * @param s the session
	 * @param id the id of the event to delete	
	 * @return true if the deletion was successful * @throws WPISuiteException * @throws WPISuiteException * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(Session, String) */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		boolean res = (db.delete(getEntity(s, id)[0]) != null) ? true : false;
		if (res)
			pollPusher.updated(deleted(UUID.fromString(id)));
		return res;
	}
	
	/**
	 * Simple wrapper to make json from event array
	 * @param events list of events to stringify
	 * @return json data
	 */
	protected String json(Object... events)
	{
		return new Gson().toJson(events);
	}
	
	/**
	 * Simple wrapper to get json from an updated/created T
	 * @param e the item to wrap as updated
	 * @return JSON data for an update
	 */
	protected abstract String updated(T e);

	/**
	 * Simple wrapper to get json from an deleted T
	 * @param e the item to wrap as updated
	 * @return JSON data for an update
	 */
	protected abstract String deleted(UUID id);

	/**
	 * Method advancedPost.
	 * @param arg0 Session
	 * @param arg1 String
	 * @param arg2 String
	
	
	
	 * @return String * @throws NotImplementedException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPost(Session, String, String) * @throws NotImplementedException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPost(Session, String, String)
	 */
	@Override
	public String advancedPost(Session arg0, String arg1, String arg2) throws NotImplementedException {
		throw new NotImplementedException();
	}

	/**
	 * Method advancedPut.
	 * @param arg0 Session
	 * @param arg1 String[]
	 * @param arg2 String
	
	
	
	 * @return String * @throws NotImplementedException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPut(Session, String[], String) * @throws NotImplementedException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPut(Session, String[], String)
	 */
	@Override
	public String advancedPut(Session arg0, String[] arg1, String arg2) throws NotImplementedException {
		throw new NotImplementedException();
	}
}
