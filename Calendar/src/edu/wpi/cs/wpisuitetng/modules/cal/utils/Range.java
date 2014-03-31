/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.utils;

/**
 * 
 * @author team4YOCO
 * 
 * meant to be a clone of some of the functionality in guava ranges
 *
 */
public class Range
{
	public final static int[] range(int start, int end)
	{
		return range(start, end, 1);
	}
	
	public final static int[] range(int start, int end, int by)
	{
		int[] range = new int[(end-start)/by];
		range(range, start, by);
		return range;
	}
	
	public static void range(int[] storage, int start, int by)
	{
		for(int i=0; i<storage.length; i++)
		{
			storage[i] = start+(i*by);
		}
	}
	
	public final static Integer[] Irange(int start, int end)
	{
		return Irange(start, end, 1);
	}
	
	public final static Integer[] Irange(int start, int end, int by)
	{
		Integer[] range = new Integer[(end-start)/by];
		Irange(range, start, by);
		return range;
	}
	
	public static void Irange(Integer[] storage, int start, int by)
	{
		for(int i=0; i<storage.length; i++)
		{
			storage[i] = start+(i*by);
		}
	}
	
	
}
