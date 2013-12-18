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

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockData;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.CommitmentEntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

public class CommitmentEntityManagerTest {

        MockData db = new MockData(new HashSet<Object>());
           

        DateTime one=new DateTime(2000,1,1,1,1, DateTimeZone.UTC);
        DateTime two=new DateTime(2000,1,2,2,1, DateTimeZone.UTC);
        DateTime three=new DateTime(2000,1,3,3,1, DateTimeZone.UTC);
        DateTime four=new DateTime(2000,1,4,4,1, DateTimeZone.UTC);
        
        
        
        Commitment e = new Commitment().addName("First").setDueDate(one).addStatus(Commitment.Status.NOT_STARTED);
        String eString=e.toJSON();
        
        Commitment ee=new Commitment().setDueDate(two).addName("Second").addStatus(Commitment.Status.IN_PROGRESS);
        String eeString=ee.toJSON();
        
        Commitment eee=new Commitment().setDueDate(three).addName("Third").addStatus(Commitment.Status.COMPLETE);

        String eeeString=eee.toJSON();
        
        
        Project p=new Project("p","26");
        User u1 = new User("User1", "U1", null, 0);
        Session ses1 = new Session(u1, p, "26");
           
        
        
        @Test
        public void testCanCreate() {

                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                assertNotNull("An Commitment Entity Manager can be created given a database",cem);

        }
         
        @Test
        public void testCount() throws WPISuiteException {

                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                assertEquals("A Commitment Entity Manager can return the # of commitments stored in the database, with an initial count of 0", 0, cem.Count());
                // Basic test to check if Count() can function correctly; Testing for count incrementing if adding Commitments is in testMakeEntity

        }
        
        @Test
        public void testMakeEntity() throws WPISuiteException {

                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                assertNotNull("An Commitment Entity Manager will return a commitment upon sucessfully sending an Commitment into the database", cem.makeEntity(ses1, eString));
                assertEquals("After making Commitments, Count() will return the updated # of Commitments", 1, cem.Count());
                assertNotNull("An Commitment Entity Manager will return a commitment upon sucessfully sending an Commitment into the database", cem.makeEntity(ses1, eeString));
                assertEquals("After making Commitments, Count() will return the updated # of Commitments", 2, cem.Count());
                assertNotNull("An Commitment Entity Manager will return a commitment upon sucessfully sending an Commitment into the database", cem.makeEntity(ses1, eeeString));
                assertEquals("After making Commitments, Count() will return the updated # of Commitments", 3, cem.Count());
                //Testing that each individual Commitment is saved in the database correctly is tested in the Get* tests

        }
           
        @Test
        public void testGetAllSingle() throws WPISuiteException {

                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                cem.makeEntity(ses1, eString);
                
                assertEquals("GetAll will return commitments in the database in Commitment[] form; in the case of only 1 commitment being stored, it will return that Commitment", e.getName(), cem.getAll(ses1)[0].getName());
                assertEquals("GetAll will return commitments in the database in Commitment[] form; in the case of only 1 commitment being stored, it will return that Commitment", e.getDate(), cem.getAll(ses1)[0].getDate());
                assertEquals("GetAll will return commitments in the database in Commitment[] form; in the case of only 1 commitment being stored, it will return that Commitment", e.getStatus(), cem.getAll(ses1)[0].getStatus());

        }
        
        
        @Test
        public void testGetAllMultiple() throws WPISuiteException {

                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                // Adding Commitments to the database
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                cem.makeEntity(ses1, eeeString);
                Commitment[] eList=cem.getAll(ses1);

                boolean hasFirst=false, hasSecond=false, hasThird=false;
                
                if(eList[0].getName().equals("First")||eList[1].getName().equals("First")||eList[2].getName().equals("First"))
                        hasFirst=true;

                assertTrue("GetAll will return multiple commitments in a random order; if the result has all of the inputs, this method is working correctly",hasFirst);
                
                if(eList[0].getName().equals("Second")||eList[1].getName().equals("Second")||eList[2].getName().equals("Second"))
                        hasSecond=true;
                assertTrue("GetAll will return multiple commitments in a random order; if the result has all of the inputs, this method is working correctly",hasSecond);
                
                if(eList[0].getName().equals("Third")||eList[1].getName().equals("Third")||eList[2].getName().equals("Third"))
                        hasThird=true;
                assertTrue("GetAll will return multiple commitments in a random order; if the result has all of the inputs, this method is working correctly",hasThird);
        }
        
        @Test
        public void testGetEntityAll() throws WPISuiteException {
                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                cem.makeEntity(ses1, eeeString);
                
                // The huge string being used as input is "get-all-commitments," + string form of starting dateTime + "," + string form of ending dateTime
                // This method is really just another way of calling getAll, and as such the strings for the start / end time don't matter so long as they're valid DateTime strings
                
                assertEquals("getEntity will return all commitments in the database if get-all-commitments is the initial string argument", 3, cem.getAll(ses1).length);
                assertEquals("getEntity will return all commitments in the database if get-all-commitments is the initial string argument, regardless of the datetime strings", 3, cem.getAll(ses1).length);
        }
        
        @Test
        public void testDeleteEntity() throws WPISuiteException {
                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                assertEquals("At this point, there should be 2 commitments in the database", 2, cem.Count());											// Commitments from 1/1/2000 1:00 - 1/2/2000 1:00
                assertEquals("The deleteEntity method will return true if the deletion was successful", true, cem.deleteEntity(ses1, e.getIdentification().toString()));
                assertEquals("At this point, there should be only one commitment in the database", 1, cem.Count());
                assertEquals("At this point, only the second commitment should still be in the database", "Second", cem.getAll(ses1)[0].getName());
                
        }
        
        @Test
        public void testDeleteAll() throws WPISuiteException {
                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                cem.makeEntity(ses1, eeeString);
                assertEquals("At this point, there should be 3 commitments in the database", 3, cem.Count());											
                cem.deleteAll(ses1);
                assertEquals("At this point, there should be no commitments for session 1 in the database", 0, cem.Count());

        }
        
}