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

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class CommitmentModel {

	public static final DateTimeFormatter serializer = ISODateTimeFormat.basicDateTime();


	public List<Commitment> getCommitments(DateTime from, DateTime to) {
		return ServerManager.get("cal/commitments/", Commitment[].class, "filter-commitments-by-range", 
				from.toString(serializer),
				to.toString(serializer));
	}

	public List<Commitment> getAllCommitments() {
		return ServerManager.get("cal/commitments/", Commitment[].class, "get-all-commitments");
	}

	public boolean putCommitment(Commitment toAdd){
		return ServerManager.put("cal/commitments", toAdd.toJSON());
	}
	public boolean updateCommitment(Commitment toUpdate)
	{
		return ServerManager.post("cal/commitments", toUpdate.toJSON());
	}
	
	

}
