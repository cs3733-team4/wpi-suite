/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models.client;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;

public class CommitmentClient extends CachingDisplayableClient<Commitment, Commitment.SerializedAction>
{
	private static CommitmentClient instance;

	protected CommitmentClient()
	{
		super("commitments", Commitment.SerializedAction[].class, Commitment[].class);
	}
	
	public static CommitmentClient getInstance()
	{
		if (instance == null)
		{
			instance = new CommitmentClient();
		}
		return instance;
	}
	
	@Override
	protected Commitment buildUuidOnlyObject(UUID uuid)
	{
		Commitment c = new Commitment();
		c.setIdentification(uuid);
		return c;
	}

	/**
	 * gets a list of commitments within a specified range
	 * 
	 * @param from the start time of the range
	 * @param to the end time of the range
	 * @return all commitments within this range
	 */
	public List<Commitment> getCommitments(DateTime from, DateTime to)
	{
		return getRange(from, to);
	}
	
	/**
	 * 
	 * @return get all the commitments that the user has access to
	 */
	public List<Commitment> getAllCommitments()
	{
		return getAll();
	}
	
	@Override
	protected boolean visibleCategory(Commitment obj)
	{
		return MainPanel.getInstance().showCommitments() && super.visibleCategory(obj);
	}
}
