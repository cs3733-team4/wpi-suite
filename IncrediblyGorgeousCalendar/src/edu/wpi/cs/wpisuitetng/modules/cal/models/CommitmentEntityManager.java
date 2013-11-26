/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models;

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
 * This is the entity manager for the Commitment in the
 * CommitmentManager module.
 *
 * @version $Revision: 1.0 $
 * @author BKMcLeod
 */
public class CommitmentEntityManager implements EntityManager<Commitment> {

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
	public CommitmentEntityManager(Data db) {
		this.db = db;
	}

	/**
	 * Saves an Commitment when it is received from a client
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public Commitment makeEntity(Session s, String content) throws WPISuiteException {
		final Commitment newCommitment = Commitment.fromJson(content);
		newCommitment.setOwner(s.getUser());
		newCommitment.setProject(s.getProject());
		if(!db.save(newCommitment, s.getProject())) {
			throw new WPISuiteException();
		}
		return newCommitment;
	}
	
	/**
	 * Retrieves commitments from the database in the range of String data
	 * @param s the session
	 * @param data the data (String from and String to) of the events to retrieve
	 * @return the event matching the given id * @throws NotFoundException * @throws NotFoundException * @throws NotFoundException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(Session, String) */
	@Override
	public Commitment[] getEntity(Session s, String data) throws NotFoundException {
		String[] args = data.split(",");		
		switch (args[0]) {
			case "filter-commitments-by-range":
				return getCommitmentsByRange(s, args[1], args[2]);
			case "get-all-commitments":
				return getAll(s);
			default:
				throw new NotFoundException("Error: " + args[0] + " not a valid method");
		}
	}
	
	/**
	 * Query database to retrieve commitments with overlapping range
	 * @param sfrom date from, DateTime formatted as String
	 * @param sto date to, DateTime formatted as String
	 * @return retrieved events with overlapping range
	 */
	private Commitment[] getCommitmentsByRange(Session ses, String sfrom, String sto) {
		DateTime from = serializer.parseDateTime(sfrom);
		DateTime to = serializer.parseDateTime(sto);
		List<Commitment> retrievedCommitments = new ArrayList<>();
		
		Commitment[] all = getAll(ses);

		final Interval range = new Interval(from, to);
		
		for (Commitment commitment : all)
		{
			DateTime s = commitment.getDate();
			if (range.contains(s))
			{
				retrievedCommitments.add(commitment);
			}
		}
		return retrievedCommitments.toArray(new Commitment[0]);
	}

	/**
	 * Retrieves all commitments from the database
	 * @param s the session
	 * @return array of all stored events * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session)
	 */
	@Override
	public Commitment[] getAll(Session s) {
		return db.retrieveAll(new Commitment(), s.getProject()).toArray(new Commitment[0]);
	}

	/**
	 * Saves a data model to the database
	 * @param s the session
	 * @param model the model to be saved
	 */
	@Override
	public void save(Session s, Commitment model) {
		
		db.save(model);

	}
	

	/**
	 * Deletes a commitment from the database
	 * @param s the session
	 * @param id the id of the event to delete
	 * @return true if the deletion was successful * @throws WPISuiteException * @throws WPISuiteException * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(Session, String) */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}
	
	/**
	 * Deletes all commitments from the database
	 * @param s the session
	 * @throws WPISuiteException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(Session)
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new Commitment(), s.getProject());
	}
	
	/**
	 * Returns the number of commitments in the database	
	 * @return number of events stored * @throws WPISuiteException * @throws WPISuiteException * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#Count() */
	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new Commitment()).size();
	}

	/**
	 * Method update.
	 * @param session Session
	 * @param content String
	 * @return Commitment * @throws WPISuiteException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(Session, String) * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(Session, String)
	 */
	@Override
	public Commitment update(Session session, String content) throws WPISuiteException {
		
		Commitment updatedCommitment = Commitment.fromJson(content);
		/*
		 * Because of the disconnected objects problem in db4o, we can't just save Commitments.
		 * We have to get the original defect from db4o, copy properties from updatedCommitments,
		 * then save the original Commitment again.
		 */
		List<Model> oldCommitments = db.retrieve(Commitment.class, "commitmentID", updatedCommitment.getCommitmentID(), session.getProject());
		if(oldCommitments.size() < 1 || oldCommitments.get(0) == null) {
			throw new BadRequestException("Commitment with ID does not exist.");
		}
				
		Commitment existingCommitment = (Commitment)oldCommitments.get(0);		

		db.delete(existingCommitment);
		
		
		if(!db.save(updatedCommitment, session.getProject())) {
			throw new WPISuiteException();
		}
		
		return updatedCommitment;
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
