/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.DayStyle;
import edu.wpi.cs.wpisuitetng.modules.cal.ReflectUtils;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Event;

public class MonthDayTest {

	DateTime now=new DateTime(2000,10,10,0,0);
	
	@Test
	public void testExists() {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		assertNotNull("A MonthDay can be created", MD);
	}
	
	@Test
	public void testAddEvents() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Event eatIcecream=new Event().addName("Eat icecream")
									 .addDescription("Yummy!")
									 .addStartTime(new DateTime(2000, 10, 10, 0, 0))
									 .addEndTime(new DateTime(2000, 10, 10, 0, 30));
									
		MD.addDisplayable(eatIcecream);
		
		List<Displayable> f= ReflectUtils.getFieldValue(MD, "allitems");
		
		assertTrue("New events can be added to a MonthDay", f.contains(eatIcecream));
		
		Event throwUpIcecream=new Event().addName("Throw up icecream")
				 .addDescription("Ugh!")
				 .addStartTime(new DateTime(2000, 10, 10, 0, 30))
				 .addEndTime(new DateTime(2000, 10, 10, 1, 30));
		MD.addDisplayable(throwUpIcecream);
		assertTrue("Multiple events can be added to a MonthDay", f.contains(throwUpIcecream));
	}
	
	@Test
	public void testRemoveEvents() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Event eatIcecream=new Event().addName("Eat icecream")
				 					 .addDescription("Yummy!")
				 					 .addStartTime(new DateTime(2000, 10, 10, 0, 0))
				 					 .addEndTime(new DateTime(2000, 10, 10, 0, 30));
				
		MD.addDisplayable(eatIcecream);

		List<Displayable> f= ReflectUtils.getFieldValue(MD, "allitems");

		assertTrue("Events can be added,", f.contains(eatIcecream));
		
		MD.removeDisplayable(eatIcecream);

		assertFalse("Events can be removed from a MonthDay", f.contains(eatIcecream));
	}
	
	@Test
	public void testRemoveEventsMultiple() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Event eatIcecream=new Event().addName("Eat icecream")
				 					 .addDescription("Yummy!")
				 					 .addStartTime(new DateTime(2000, 10, 10, 0, 0))
				 					 .addEndTime(new DateTime(2000, 10, 10, 0, 30));
				
		MD.addDisplayable(eatIcecream);
		
		Event throwUpIcecream=new Event().addName("Throw up icecream")
										 .addDescription("Ugh!")
										 .addStartTime(new DateTime(2000, 10, 10, 0, 30))
										 .addEndTime(new DateTime(2000, 10, 10, 1, 30));
		MD.addDisplayable(throwUpIcecream);
		
		MD.removeDisplayable(throwUpIcecream);
		

		List<Displayable> f= ReflectUtils.getFieldValue(MD, "allitems");
		
		assertFalse("Events can be removed from a MonthDay", f.contains(throwUpIcecream));
		assertTrue("Without removing any of the other events", f.contains(eatIcecream));
		
		
		
		Event seekRevenge=new Event().addName("Seek vengance")
				 					 .addDescription("Try to sell me bad icecream, will he?")
				 					 .addStartTime(new DateTime(2000, 10, 10, 1, 30))
				 					 .addEndTime(new DateTime(2000, 10, 10, 4, 0));
		MD.addDisplayable(throwUpIcecream);
		MD.addDisplayable(seekRevenge);
		
		MD.removeDisplayable(throwUpIcecream);
		
		assertTrue("Removing events between other events still won't affect the other events", f.contains(eatIcecream));
		assertTrue("Removing events between other events still won't affect the other events", f.contains(seekRevenge));
	}
	
	@Test
	public void testAddCommitments() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Commitment icecreamDay=new Commitment().addName("Icecream day")
									 .addDescription("Eat icecream by today!")
									 .setDate(new DateTime(2000, 10, 10, 0, 0));
									
		MD.addDisplayable(icecreamDay);
		
		List<Displayable> f= ReflectUtils.getFieldValue(MD, "allitems");
		
		assertTrue("New commitments can be added to a MonthDay", f.contains(icecreamDay));
		
		Commitment venganceDay=new Commitment().addName("Vengance day")
				 .addDescription("Exact vengance by today!")
				 .setDate(new DateTime(2000, 10, 10, 0, 30));
		MD.addDisplayable(venganceDay);
		
		assertTrue("Multiple commitments can be added to a MonthDay", f.contains(icecreamDay));
		assertTrue("Multiple commitments can be added to a MonthDay", f.contains(venganceDay));
	}
	
	@Test
	public void testRemoveCommitments() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Commitment icecreamDay=new Commitment().addName("Icecream day")
				 					 .addDescription("Eat icecream by today!")
				 					 .setDate(new DateTime(2000, 10, 10, 0, 0));
				
		MD.addDisplayable(icecreamDay);
		

		List<Displayable> f= ReflectUtils.getFieldValue(MD, "allitems");
		
		assertTrue("Commitments can be added,",f.contains(icecreamDay));
		
		MD.removeDisplayable(icecreamDay);
		
		assertFalse("and removed from a MonthDay", f.contains(icecreamDay));
	}
	
	@Test
	public void testRemoveCommitmentsMultiple() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Commitment icecreamDay=new Commitment().addName("Icecream day")
				 					 .addDescription("Eat icecream by today!")
				 					 .setDate(new DateTime(2000, 10, 10, 0, 0));
				
		MD.addDisplayable(icecreamDay);
		
		Commitment venganceDay=new Commitment().addName("Vengance day")
										 .addDescription("Exact vengace by today!")
										 .setDate(new DateTime(2000, 10, 10, 0, 30));
		MD.addDisplayable(venganceDay);
		
		MD.removeDisplayable(venganceDay);
		

		List<Displayable> f= ReflectUtils.getFieldValue(MD, "allitems");
		
		assertFalse("Commitments can be removed from a MonthDay", f.contains(venganceDay));
		assertTrue("Without removing any of the other commitments", f.contains(icecreamDay));
	}

}
