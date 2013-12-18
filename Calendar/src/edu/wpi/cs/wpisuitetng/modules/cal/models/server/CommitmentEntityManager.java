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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Commitment;

/**
 * This is the entity manager for the Commitment in the
 * CommitmentManager module.
 */
public class CommitmentEntityManager extends CachedEntityManager<Commitment> {
	
	/**
	 * Constructs the entity manager. This constructor is called by
	 * {@link edu.wpi.cs.wpisuitetng.ManagerLayer#ManagerLayer()}. To make sure
	 * this happens, be sure to place add this entity manager to the map in
	 * the ManagerLayer file.
	 * 
	 * @param db a reference to the persistent database
	 */
	public CommitmentEntityManager(Data db) {
		super(db);
		pollPusher = PollPusher.getInstance(Commitment.class);
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
		PollPusher.getInstance(Commitment.class).updated(updated(newCommitment));
		return newCommitment;
	}
	
	/**
	 * Retrieves commitments from the database in the range of String data
	 * @param s the session
	 * @param data the data (String from and String to) of the events to retrieve
	 * @return the event matching the given id * @throws NotFoundException * @throws NotFoundException * @throws NotFoundException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(Session, String) */
	@Override
	public Commitment[] getEntity(Session s, String data) throws NotFoundException
	{
		return getCommitmentByUUID(s, data);
	}
	
	/**
	 * Gets the Commitment with the current UUID
	 * @param ses the session
	 * @param uuid the uuid of the commitment
	 * @return an array containing the commitment that matches this uuid
	 * @throws NotFoundException 
	 */
	private Commitment[] getCommitmentByUUID(Session ses, String uuid) throws NotFoundException {
		UUID from = UUID.fromString(uuid);
		try 
		{
			return db.retrieve(Commitment.class, "uuid", from, ses.getProject()).toArray(new Commitment[0]);
		}
		catch (WPISuiteException e)
		{
			throw new NotFoundException(uuid);
		}
	}

	/**
	 * Retrieves all commitments from the database
	 * @param s the session
	 * @return array of all stored events * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session) * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session)
	 */
	@Override
	public Commitment[] getAll(Session s) {
		Commitment [] allCommitments = db.retrieveAll(new Commitment(), s.getProject()).toArray(new Commitment[0]);
		ArrayList<Commitment> commitmentArray = new ArrayList<>();
		for (Commitment c: allCommitments)
		{
			if (s.getUser().equals(c.getOwner()))
					commitmentArray.add(c);
		}
		return commitmentArray.toArray(new Commitment[0]);
	}

	/**
	 * Saves a data model to the database
	 * @param s the session
	 * @param model the model to be saved
	 */
	@Override
	public void save(Session s, Commitment model) {
		if (model.isProjectwide())
			model.setProject(s.getProject());
		db.save(model);
		PollPusher.getInstance(Commitment.class).updated(updated(model));
	}
	

	/**
	 * Deletes a commitment from the database
	 * @param s the session
	 * @param id the id of the event to delete
	 * @return true if the deletion was successful * @throws WPISuiteException * @throws WPISuiteException * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(Session, String) */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		boolean res = (db.delete(getEntity(s, id)[0]) != null) ? true : false;
		if (res)
			PollPusher.getInstance(Commitment.class).updated(deleted(UUID.fromString(id)));
		return res;
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
		List<Model> oldCommitments = db.retrieve(Commitment.class, "uuid", updatedCommitment.getUuid(), session.getProject());
		if(oldCommitments.size() < 1 || oldCommitments.get(0) == null) {
			throw new BadRequestException("Commitment with ID does not exist.");
		}
				
		Commitment existingCommitment = (Commitment)oldCommitments.get(0);		

		db.delete(existingCommitment);
		

		updatedCommitment.setOwner(existingCommitment.getOwner());
		updatedCommitment.setProject(existingCommitment.getProject());
		if(!db.save(updatedCommitment, session.getProject())) {
			throw new WPISuiteException();
		}

		PollPusher.getInstance(Commitment.class).updated(updated(updatedCommitment));
		
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
	public String advancedGet(Session s, String[] args) throws NotFoundException {
		// shift cal/events off
		args = Arrays.copyOfRange(args, 2, args.length);
		switch (args[0])
		{
			case "filter-commitments-by-uuid":
				return json((Object[])getCommitmentByUUID(s, args[1]));
			case "poll":
				return getFromPoll(s);
			default:
				System.out.println(args[0]);
				throw new NotFoundException("Error: " + args[0] + " not a valid method");
		}
	}
	
	@Override
	protected String updated(Commitment e)
	{
		return new Gson().toJson(new Commitment.SerializedAction(e, e.getUuid(), false));
	}

	@Override
	protected String deleted(UUID id)
	{
		return new Gson().toJson(new Commitment.SerializedAction(null, id, true));
	}
}
