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

import java.awt.Color;
import java.util.HashSet;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockData;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.CategoryEntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class CategoryEntityManagerTest {
	 MockData db = new MockData(new HashSet<Object>());

	 Category c1 = new Category();
     String eString=c1.toJSON();
	 
     Category c2 = new Category();
	 String eeString=c2.toJSON();
	 
	 Category c3 = new Category();
	 String eeeString=c3.toJSON();	 
	 
	 Project p=new Project("p","26");
     User u1 = new User("User1", "U1", null, 0);
     Session ses1 = new Session(u1, p, "26");
     
    private void setupCategories(){
    	c1.setName("cat1");
    	c1.setColor(Color.BLUE);
    	
    	c2.setName("cat2");
    	c2.setColor(Color.GREEN);
    	
    	c3.setName("cat3");
    	c3.setColor(Color.ORANGE);    	
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
            cem.makeEntity(ses1, eString);
            setupCategories();
            
            assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getName(), cem.getAll(ses1)[0].getName());
            assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getColor(), cem.getAll(ses1)[0].getColor());
            //assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getOwner(), cem.getAll(ses1)[0].getOwner());
            //assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getCategoryID(), cem.getAll(ses1)[0].getCategoryID());
    }
    
}
