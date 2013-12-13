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

/**
 * Style of a given status for commitments.
 */
public enum CommitmentStatus
{
	NotStarted("Not Started"),
	InProgress("In Progress"),
	Complete("Complete");
	
	private String currstatus;
	private CommitmentStatus(String currstatus)
	{
		this.currstatus = currstatus;
	}
	
	@Override
	public String toString()
	{
		return this.currstatus;
	}
}

