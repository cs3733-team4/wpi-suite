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

import org.junit.BeforeClass;
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

	 static Category c1 = new Category();
     static String cat1String;
	 
     static Category c2 = new Category();
	 static String cat2String;
	 
	 static Category c3 = new Category();
	 static String cat3String;
	 
	 static Project p1 = new Project("p1","26");
	 static Project p2 = new Project("p2","27");
	 static Project p3 = new Project("p3","28");
	 static User u1 = new User("User1", "U1", null, 0);
	 static User u2 = new User("User2", "U2", null, 0);
	 static User u3 = new User("User3", "U3", null, 0);
	 static Session ses1 = new Session(u1, p1, "26");
	 static Session ses2 = new Session(u2, p2, "27");
	 static Session ses3 = new Session(u3, p3, "28");
    
     @BeforeClass
    public static void setupCategories(){
    	c1.setName("cat1");
    	c1.setColor(Color.BLUE);
    	
    	c2.setName("cat2");
    	c2.setColor(Color.GREEN);
    	
    	c3.setName("cat3");
    	c3.setColor(Color.ORANGE);
    	
    	cat1String=c1.toJSON();
    	cat2String=c2.toJSON();
    	cat3String=c3.toJSON();
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
            assertNotNull("A Category Entity Manager will return a category upon sucessfully sending a category into the database", cem.makeEntity(ses1, cat1String));
            assertEquals("After making Category, Count() will return the updated # of Categories", 1, cem.Count());
            assertNotNull("A Category Entity Manager will return a category upon sucessfully sending a category into the database", cem.makeEntity(ses1, cat2String));
            assertEquals("After making Category, Count() will return the updated # of Categories", 2, cem.Count());
            assertNotNull("A Category Entity Manager will return a category upon sucessfully sending a category into the database", cem.makeEntity(ses1, cat3String));
            assertEquals("After making Category, Count() will return the updated # of Categories", 3, cem.Count());
            //Testing that each individual Category is saved in the database correctly is tested in the Get* tests
    }
    
    @Test
    public void testGetAllSingle() throws WPISuiteException {

            CategoryEntityManager cem = new CategoryEntityManager(db);
            cem.makeEntity(ses1, cat1String);
            setupCategories();
            
            assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getName(), cem.getAll(ses1)[0].getName());
            assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getColor(), cem.getAll(ses1)[0].getColor());
            //assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getOwner(), cem.getAll(ses1)[0].getOwner());
            //assertEquals("GetAll will return categories in the database in Category[] form; in the case of only 1 category being stored, it will return that Category", c1.getCategoryID(), cem.getAll(ses1)[0].getCategoryID());
    }
}
