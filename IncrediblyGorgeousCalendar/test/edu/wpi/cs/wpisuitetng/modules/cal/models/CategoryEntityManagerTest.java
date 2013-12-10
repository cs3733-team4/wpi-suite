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

	 Category c1 = new Category();
     Category c2 = new Category();
	 Category c3 = new Category();
	 
	 Project p1=new Project("p1","29");
	 Project p2=new Project("p2","30");
	 Project p3=new Project("p3","31");
     User u1 = new User("User1", "U1", null, 0);
     User u2 = new User("User2", "U2", null, 0);
     User u3 = new User("User3", "U3", null, 0);
     Session ses1 = new Session(u1, p1, "29");
     Session ses2 = new Session(u2, p2, "30");
     Session ses3 = new Session(u3, p3, "31");
     
	String eString;
    String eeString;
	String eeeString;
	
	public void setupCategories() {
		c1.setName("cat1");
    	c1.setColor(Color.BLUE);
    	c1.setOwner(u1);
    	
    	c2.setName("cat2");
    	c2.setColor(Color.CYAN);
    	c2.setOwner(u2);
    	
    	c3.setName("cat3");
    	c3.setColor(Color.ORANGE);
    	c3.setOwner(u3);
    	
      	eString=c1.toJSON();
      	eeString=c2.toJSON();
      	eeeString=c3.toJSON();
	}
     	 
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
            setupCategories();


            assertNotNull("A Category Entity Manager will return a category upon sucessfully sending a category into the database", cem.makeEntity(ses1, eString));
            assertEquals("After making Category, Count() will return the updated # of Categories", 1, cem.Count());
            assertNotNull("A Category Entity Manager will return a category upon sucessfully sending a category into the database", cem.makeEntity(ses1, eeString));
            assertEquals("After making Category, Count() will return the updated # of Categories", 2, cem.Count());
            assertNotNull("A Category Entity Manager will return a category upon sucessfully sending a category into the database", cem.makeEntity(ses1, eeeString));
            assertEquals("After making Category, Count() will return the updated # of Categories", 3, cem.Count());
            //Testing that each individual Category is saved in the database correctly is tested in the Get* tests
    }
    
    @Test

    public void testGetAllSingle() throws WPISuiteException {

            CategoryEntityManager cem = new CategoryEntityManager(db);
            setupCategories();
            cem.makeEntity(ses1, eString);

            
            assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getName(), cem.getAll(ses1)[0].getName());
            assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getColor(), cem.getAll(ses1)[0].getColor());
            assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getOwner(), cem.getAll(ses1)[0].getOwner());
            assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getCategoryID(), cem.getAll(ses1)[0].getCategoryID());
    } 
 
    @Test
    public void testGetAllMultiple() throws WPISuiteException {
            CategoryEntityManager cem = new CategoryEntityManager(db);
            setupCategories();
            // Adding events to the database
            cem.makeEntity(ses1, eString);
            cem.makeEntity(ses1, eeString);
            cem.makeEntity(ses1, eeeString);
            Category[] cList=cem.getAll(ses1);

            boolean hasFirst=false, hasSecond=false, hasThird=false;
            
            if(cList[0].getName().equals("cat1")||cList[1].getName().equals("cat1")||cList[2].getName().equals("cat1"))
                    hasFirst=true;
            assertTrue("GetAll will return multiple categories in a random order; if the result has all of the inputs, this method is working correctly",hasFirst);
            
            if(cList[0].getName().equals("cat2")||cList[1].getName().equals("cat2")||cList[2].getName().equals("cat2"))
                    hasSecond=true;
            assertTrue("GetAll will return multiple categories in a random order; if the result has all of the inputs, this method is working correctly",hasSecond);
            
            if(cList[0].getName().equals("cat3")||cList[1].getName().equals("cat3")||cList[2].getName().equals("cat3"))
                    hasThird=true;
            assertTrue("GetAll will return multiple categories in a random order; if the result has all of the inputs, this method is working correctly",hasThird);
    } 
    
  /* @Test
    public void testGetEntity() throws WPISuiteException {

            CategoryEntityManager cem = new CategoryEntityManager(db);
            setupCategories();
            cem.makeEntity(ses1, eString);
            cem.makeEntity(ses2, eeString);
            cem.makeEntity(ses3, eeeString);
            
            // The huge string being used as input is "filter-commitments-by-range," + string form of starting dateTime + "," + string form of ending dateTime
            // This method is really just another way of calling getCommitmentsByRange with new inputs; as such, it has the same limitations and only needs basic testing
            
            assertEquals("getEntity will return a commitment in the database if it was stored there before",c1.getName(), ((Category) cem.getEntity(ses1,"get-category-by-name")[0]).getName());
            //assertEquals("getEntity will return a commitment in the database if it was stored there before",eee.getName(),cem.getEntity(ses1,"filter-commitments-by-range,20000103T030000.000Z,20000104T070100.000Z")[0].getName());
            //assertEquals("getEntity will return an empty array if no commitments are within the given range", 0 ,cem.getEntity(ses1,"filter-commitments-by-range,20500101T010100.000Z,20500101T010100.000Z").length);
    } */
    
}
