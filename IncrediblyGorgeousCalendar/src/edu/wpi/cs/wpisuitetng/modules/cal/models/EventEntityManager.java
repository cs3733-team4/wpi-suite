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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
/**
 * This is the entity manager for the Event in the
 * EventManager module.
 *
 * @version $Revision: 1.0 $
 * @author NileshP
 */
public class EventEntityManager implements EntityManager<Event> {

	private static final DateTimeFormatter serializer = ISODateTimeFormat.basicDateTime();
	/** The database */
	Data db;
	
	/**
	 * Constructs the entity manager. This constructor is called by
	 * {@link edu.wpi.cs.wpisuitetng.ManagerLayer#ManagerLayer()}. To make sure
	 * this happens, be sure to place add this entity manager to the map in
	 * the ManagerLayer file.
	 * 
	 * @param db a reference to the persistent database
	 */
	public EventEntityManager(Data db) {
		this.db = db; 
	}

	/**
	 * Saves an Event when it is received from a client
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public Event makeEntity(Session s, String content) throws WPISuiteException {
		final Event newEvent = Event.fromJson(content);
		newEvent.setOwner(s.getUser());
		newEvent.setProject(s.getProject());
		if(!db.save(newEvent, s.getProject())) {
			throw new WPISuiteException();
		}
		return newEvent;
	}
	
	/**
	 * Retrieves events from the database in the range of String data
	 * @param s the session
	 * @param data the data (String from and String to) of the events to retrieve
	 * @return the event matching the given id * @throws NotFoundException * @throws NotFoundException * @throws NotFoundException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(Session, String) */
	@Override
	public Event[] getEntity(Session s, String data) throws NotFoundException
	{
		String[] args = data.split(",");
		
		
		switch (args[0])
		{
			case "filter-events-by-range":
				return getEventsByRange(s, args[1], args[2]);
			case "filter-event-by-uuid":
				return getEventByUUID(s, args[1]);
			default:
				throw new NotFoundException("Error: " + args[0] + " not a valid method");
		}

	
	}
	
	/**
	 * gets the event with the current UUID
	 * 
	 * @param ses the session
	 * @param uuid the event's UUID
	 * @return an array containing just this event
	 * @throws NotFoundException
	 */
	private Event[] getEventByUUID(Session ses, String uuid) throws NotFoundException
	{
		UUID from = UUID.fromString(uuid);
		try 
		{
			return db.retrieve(Event.class, "eventID", from, ses.getProject()).toArray(new Event[0]);
		}
		catch (WPISuiteException e)
		{
			throw new NotFoundException(uuid);
		}
		
	}

	/**
	 * Query database to retrieve events with overlapping range
	 * @param sfrom date from, DateTime formatted as String
	 * @param sto date to, DateTime formatted as String
	 * @return retrieved events with overlapping range
	 */
	Event[] getEventsByRange(Session ses, String sfrom, String sto) {
		DateTime from = serializer.parseDateTime(sfrom);
		DateTime to = serializer.parseDateTime(sto);
		List<Event> retrievedEvents = new ArrayList<>();
		
		Event[] all = getAll(ses);

		final Interval range = new Interval(from, to);
		
		for (Event event : all)
		{
			DateTime s = event.getStart(), e = event.getEnd();
			if (s.isBefore(e) && range.overlaps(new Interval(s, e)))
			{
				retrievedEvents.add(event);
			}
		}
		return retrievedEvents.toArray(new Event[0]);
	}
	
	/**
	 * Retrieves all events from the database
	 * @param s the session
	 * @return array of all stored events * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session)
	 */
	@Override
	public Event[] getAll(Session s) {
		Event [] allEvents = db.retrieveAll(new Event(), s.getProject()).toArray(new Event[0]);
		ArrayList<Event> eventArray = new ArrayList<>();
		for (Event e: allEvents)
		{
			if (s.getUser().equals(e.getOwner()) || e.isProjectEvent())
					eventArray.add(e);
		}
		return eventArray.toArray(new Event[0]);
	}

	/**
	 * Saves a data model to the database
	 * @param s the session
	 * @param model the model to be saved
	 */
	@Override
	public void save(Session s, Event model) {
		if (model.isProjectEvent())
			model.setProject(s.getProject());
		db.save(model);
	}
	

	/**
	 * Deletes a event from the database
	 * @param s the session
	 * @param id the id of the event to delete	
	 * @return true if the deletion was successful * @throws WPISuiteException * @throws WPISuiteException * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(Session, String) */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}
	
	/**
	 * Deletes all events from the database
	 * @param s the session
	 * @throws WPISuiteException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(Session)
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new Event(), s.getProject());
	}
	
	/**
	 * Returns the number of events in the database
	 * @return number of events stored * @throws WPISuiteException * @throws WPISuiteException * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#Count() */
	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new Event()).size();
	}

	/**
	 * Method update.
	 * @param session Session
	 * @param content String
	 * @return Event * @throws WPISuiteException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(Session, String) * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(Session, String)
	 */
	@Override
	public Event update(Session session, String content) throws WPISuiteException {
		
		Event updatedEvent = Event.fromJson(content);
		/*
		 * Because of the disconnected objects problem in db4o, we can't just save Events.
		 * We have to get the original defect from db4o, copy properties from updatedEvent,
		 * then save the original Event again.
		 */
		List<Model> oldEvents = db.retrieve(Event.class, "eventID", updatedEvent.getIdentification(), session.getProject());
		if(oldEvents.size() < 1 || oldEvents.get(0) == null) {
			throw new BadRequestException("Event with ID does not exist.");
		}
				
		Event existingEvent = (Event)oldEvents.get(0);		
		
		db.delete(existingEvent);
		
		updatedEvent.setOwner(existingEvent.getOwner());
		updatedEvent.setProject(existingEvent.getProject());
		if(!db.save(updatedEvent, session.getProject())) {
			throw new WPISuiteException();
		}
		
		return existingEvent;
		
	}

	/**
	 * Method advancedGet.
	 * @param arg0 Session
	 * @param arg1 String[]
	
	
	
	 * @return String * @throws NotImplementedException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedGet(Session, String[]) * @throws NotImplementedException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedGet(Session, String[])
	 */
	@Override
	public String advancedGet(Session arg0, String[] arg1) throws NotImplementedException {
		throw new NotImplementedException();
	}

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
