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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.HashSet;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockData;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class CategoryEntityManagerTest {
	MockData db = new MockData(new HashSet<Object>());

	Category c1 = new Category().addColor(Color.BLUE).addName("cat1");
	String cat1String = c1.toJSON();

	Category c2 = new Category().addColor(Color.GREEN).addName("cat2");
	String cat2String = c2.toJSON();

	Category c3 = new Category().addColor(Color.ORANGE).addName("cat3");
	String cat3String = c3.toJSON();	 

	Project p1 = new Project("p1","26");
	Project p2 = new Project("p2","27");
	Project p3 = new Project("p3","28");
	User u1 = new User("User1", "U1", null, 0);
	User u2 = new User("User2", "U2", null, 0);
	User u3 = new User("User3", "U3", null, 0);
	Session ses1 = new Session(u1, p1, "26");
	Session ses2 = new Session(u2, p2, "27");
	Session ses3 = new Session(u3, p3, "28");


	@Test
	public void testCanCreate() {
		CategoryEntityManager cem = new CategoryEntityManager(db);
		assertNotNull("A Category Entity Manager can be created given a database",cem);

	}

	@Test
	public void testCount() throws WPISuiteException {
		CategoryEntityManager cem = new CategoryEntityManager(db);
		assertEquals("A Category Entity Manager can return the # of categories stored in the database, with an initial count of 0", 0, cem.Count());
		// Basic test to check if Count() can function correctly; Testing for count incrementing if adding Categories is in testMakeEntity
	}

	@Test
	public void testMakeEntity() throws WPISuiteException {
		CategoryEntityManager cem = new CategoryEntityManager(db);
		assertNotNull("A Category Entity Manager will return a category upon sucessfully sending a category into the database", cem.makeEntity(ses1, cat1String));
		assertEquals("After making Category, Count() will return the updated # of Categories", 1, cem.Count());
		assertNotNull("A Category Entity Manager will return a category upon sucessfully sending a category into the database", cem.makeEntity(ses1, cat2String));
		assertEquals("After making Category, Count() will return the updated # of Categories", 2, cem.Count());
		assertNotNull("A Category Entity Manager will return a category upon sucessfully sending a category into the database", cem.makeEntity(ses1, cat3String));
		assertEquals("After making Category, Count() will return the updated # of Categories", 3, cem.Count());
		//Testing that each individual Category is saved in the database correctly is tested in the Get* tests
	}

	@Test
	public void testDeleteEntity() throws WPISuiteException {
		CategoryEntityManager cem = new CategoryEntityManager(db);
		assertNotNull("A Category Entity Manager will return a category upon sucessfully sending a category into the database", cem.makeEntity(ses1, cat1String));
		assertEquals("After making Category, Count() will return the updated # of Categories", 1, cem.Count());
		assertEquals("The deleteEntity method will return true if the deletion was successful", true, cem.deleteEntity(ses1, "get-category-by-id,"+c1.getCategoryID()));
		assertEquals("After deleting Category, Count() will return the updated # of Categories", 0, cem.Count());
	}

	@Test
	public void testGetAllSingle() throws WPISuiteException {
		CategoryEntityManager cem = new CategoryEntityManager(db);
		cem.makeEntity(ses1, cat1String);
		assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getName(), cem.getEntity(ses1, "get-all-categories")[0].getName());
	}

	@Test
	public void testGetAllMultiple() throws WPISuiteException {
		CategoryEntityManager cem = new CategoryEntityManager(db);
		cem.makeEntity(ses1, cat1String);
		cem.makeEntity(ses1, cat2String);
		cem.makeEntity(ses1, cat3String);

		Category[] catList = cem.getEntity(ses1, "get-all-categories");
		boolean hasCat1 = false, hasCat2 = false, hasCat3 = false;

		if(catList[0].getName().equals("cat1")||catList[1].getName().equals("cat1")||catList[2].getName().equals("cat1"))
			hasCat1=true;
		assertTrue("GetAll will return multiple events in a random order; if the result has all of the inputs, this method is working correctly",hasCat1);

		if(catList[0].getName().equals("cat2")||catList[1].getName().equals("cat2")||catList[2].getName().equals("cat2"))
			hasCat2=true;
		assertTrue("GetAll will return multiple events in a random order; if the result has all of the inputs, this method is working correctly",hasCat2);

		if(catList[0].getName().equals("cat3")||catList[1].getName().equals("cat3")||catList[2].getName().equals("cat3"))
			hasCat3=true;
		assertTrue("GetAll will return multiple events in a random order; if the result has all of the inputs, this method is working correctly",hasCat3);
	}

}
