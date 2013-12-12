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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;

public class CommitmentModel {

	private static CommitmentModel instance;
	public static final DateTimeFormatter serializer = ISODateTimeFormat.basicDateTime();

	/**
	 * the privatized default constructor
	 */
	private CommitmentModel(){}
	
	/**
	 * 
	 * @return the singleton commitment model
	 */
	public static CommitmentModel getInstance()
	{
		if (instance == null)
		{
			instance = new CommitmentModel();
		}
		return instance;
	}
	
	/**
	 * gets a list of commitments within a specified range
	 * 
	 * @param from the start time of the range
	 * @param to the end time of the range
	 * @return all commitments within this range
	 */
	public List<Commitment> getCommitments(DateTime from, DateTime to) {
		/*return ServerManager.get("cal/commitments/", Commitment[].class, "filter-commitments-by-range", 
				from.toString(serializer),
				to.toString(serializer));*/
		final List<Commitment> commitments = ServerManager.get("cal/commitments/", Commitment[].class, "filter-commitments-by-range", from.toString(serializer),
				to.toString(serializer));
		
		//set up to filter events based on booleans in MainPanel
		List<Commitment> filteredCommitments = new ArrayList<Commitment>();
		boolean showPersonal = MainPanel.getInstance().showPersonal;
		boolean showTeam = MainPanel.getInstance().showTeam;
		
		//loop through and add only if isProjectEvent() matches corresponding boolean
		for(Commitment e: commitments){
			if(e.isProjectCommitment()&&showTeam){
				filteredCommitments.add(e);
			}
			if(!e.isProjectCommitment()&&showPersonal){
				filteredCommitments.add(e);
			}
		}
		return filteredCommitments;
		}

	/**
	 * 
	 * @return get all the commitments that the user has access to
	 */
	public List<Commitment> getAllCommitments() {
		return ServerManager.get("cal/commitments/", Commitment[].class, "get-all-commitments");
	}

	/**
	 * 
	 * @param toAdd the commitment to add to the database
	 * @return whether the event was added successfully
	 */
	public boolean putCommitment(Commitment toAdd){
		return ServerManager.put("cal/commitments", toAdd.toJSON());
	}
	
	/**
	 * 
	 * @param toUpdate the commitment to modify
	 * @return whether the commitment was updated successfully
	 */
	public boolean updateCommitment(Commitment toUpdate)
	{
		return ServerManager.post("cal/commitments", toUpdate.toJSON());
	}
	
	/**
	 * 
	 * @param toDelete the commitment to delete
	 * @return whether the commitment was deleted successfully
	 */
	public boolean deleteCommitment(Commitment toDelete)
	{
		return ServerManager.delete("cal/commitments", "filter-commitment-by-uuid", toDelete.getCommitmentID().toString());
	}

}
