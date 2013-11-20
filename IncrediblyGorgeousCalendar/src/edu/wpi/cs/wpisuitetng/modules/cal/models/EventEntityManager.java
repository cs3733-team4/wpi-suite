/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
	 * Saves a Event when it is received from a client
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public Event makeEntity(Session s, String content) throws WPISuiteException {
		System.out.println(content+ " was just sent!");
		final Event newEvent = Event.fromJson(content);
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
	public Event[] getEntity(Session s, String data) throws NotFoundException {
		System.out.println(data+ " was just sent!");
		String[] args = data.split(",");
		
		Event[] retrievedEvents = null;
		
		switch (args[0]) {
			case "filter-events-by-range":
				return getEventsByRange(s, args[1], args[2]);
			default:
				System.out.println("Error: " + args[0] + " not a valid method");
		}

	
		return retrievedEvents;
	}
	
	/**
	 * Query database to retrieve events with overlapping range
	 * @param sfrom date from, DateTime formatted as String
	 * @param sto date to, DateTime formatted as String
	 * @return retrieved events with overlapping range
	 */
	private Event[] getEventsByRange(Session s, String sfrom, String sto) {
		DateTime from = serializer.parseDateTime(sfrom);
		DateTime to = serializer.parseDateTime(sto);
		List<Event> retrievedEvents = new ArrayList<>();
		
		Event[] all = getAll(s);

		final Interval range = new Interval(from, to);
		
		for (Event event : all)
		{
			if (range.overlaps(new Interval(event.getStart(), event.getEnd())))
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
		System.out.println("GET ALL!");
		return db.retrieveAll(new Event(), s.getProject()).toArray(new Event[0]);
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
		List<Model> oldEvents = db.retrieve(Event.class, "eventID", updatedEvent.getEventID(), session.getProject());
		if(oldEvents.size() < 1 || oldEvents.get(0) == null) {
			throw new BadRequestException("Event with ID does not exist.");
		}
				
		Event existingEvent = (Event)oldEvents.get(0);		

		// copy values to old event and fill in our changeset appropriately
		// TODO: existingEvent.copyFrom(updatedEvent);
		
		if(!db.save(existingEvent, session.getProject())) {
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