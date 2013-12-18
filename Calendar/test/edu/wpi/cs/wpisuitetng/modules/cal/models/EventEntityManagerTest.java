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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockData;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.EventEntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class EventEntityManagerTest {

        MockData db = new MockData(new HashSet<Object>());
           
        DateTime one=new DateTime(2000,1,1,1,30, DateTimeZone.UTC);   // Datetime at Jan 1st, 2000: 1:30
        DateTime two=new DateTime(2000,1,2,2,30, DateTimeZone.UTC);   // Datetime at Jan 2nd, 2000: 2:30
        DateTime three=new DateTime(2000,1,3,3,30, DateTimeZone.UTC); // Datetime at Jan 3rd, 2000: 3:30
        DateTime four=new DateTime(2000,1,4,4,30, DateTimeZone.UTC);  // Datetime at Jan 4th, 2000: 4:30
        
        
        
        Event e = new Event().addStartTime(one).addEndTime(two).addName("First");
        String eString=e.toJSON();
        
        Event ee=new Event().addStartTime(two).addEndTime(three).addName("Second");
        String eeString=ee.toJSON();
        
        Event eee=new Event().addStartTime(three).addEndTime(four).addName("Third");
        String eeeString=eee.toJSON();
        
        
        Project p=new Project("p","26");
        User u1 = new User("User1", "U1", null, 0);
        Session ses1 = new Session(u1, p, "26");
        
        @After
        public void resetDB()
        {
        	db = new MockData(new HashSet<Object>());
        }
        
        
        @Test
        public void testCanCreate() {
                EventEntityManager eem = new EventEntityManager(db);
                assertNotNull("An Event Entity Manager can be created given a database",eem);
        }
         
        @Test
        public void testCount() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                assertEquals("An Event Entity Manager can return the # of Events stored in the database, with an initial count of 0", 0, eem.Count());
                // Basic test to check if Count() can function correctly; Testing for count incrementing if adding events is in testMakeEntity
        }
        
        @Test
        public void testMakeEntity() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eString));
                assertEquals("After making events, Count() will return the updated # of events", 1, eem.Count());
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eeString));
                assertEquals("After making events, Count() will return the updated # of events", 2, eem.Count());
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eeeString));
                assertEquals("After making events, Count() will return the updated # of events", 3, eem.Count());
                //Testing that each individual event is saved in the database correctly is tested in the Get* tests
        }
           
        @Test
        public void testGetAllSingle() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, eString);
                
                assertEquals("GetAll will return event event in the database in Event[] form; in the case of only 1 event being stored, it will return that event", e.getName(), eem.getAll(ses1)[0].getName());
                assertEquals("GetAll will return event event in the database in Event[] form; in the case of only 1 event being stored, it will return that event", e.getStart(), eem.getAll(ses1)[0].getStart());
                assertEquals("GetAll will return event event in the database in Event[] form; in the case of only 1 event being stored, it will return that event", e.getEnd(), eem.getAll(ses1)[0].getEnd());
        }
        
        
        @Test
        public void testGetAllMultiple() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                // Adding events to the database
                eem.makeEntity(ses1, eString);
                eem.makeEntity(ses1, eeString);
                eem.makeEntity(ses1, eeeString);
                Event[] eList=eem.getAll(ses1);
                boolean hasFirst=false, hasSecond=false, hasThird=false;
                
                if(eList[0].getName().equals("First")||eList[1].getName().equals("First")||eList[2].getName().equals("First"))
                        hasFirst=true;
                assertTrue("GetAll will return multiple events in a random order; if the result has all of the inputs, this method is working correctly",hasFirst);
                
                if(eList[0].getName().equals("Second")||eList[1].getName().equals("Second")||eList[2].getName().equals("Second"))
                        hasSecond=true;
                assertTrue("GetAll will return multiple events in a random order; if the result has all of the inputs, this method is working correctly",hasSecond);
                
                if(eList[0].getName().equals("Third")||eList[1].getName().equals("Third")||eList[2].getName().equals("Third"))
                        hasThird=true;
                assertTrue("GetAll will return multiple events in a random order; if the result has all of the inputs, this method is working correctly",hasThird);
        }
        
        @Test
        public void testDeleteEntity() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, eString);
                eem.makeEntity(ses1, eeString);
                assertEquals("At this point, there should be 2 events in the database", 2, eem.Count());											// Events from 1/1/2000 1:00 - 1/2/2000 1:00
                assertEquals("The deleteEntity method will return true if the deletion was successful", true, eem.deleteEntity(ses1, e.getUuid().toString()));
                assertEquals("At this point, there should be only one event in the database", 1, eem.Count());
                assertEquals("At this point, only the second event should still be in the database", "Second", eem.getAll(ses1)[0].getName());
                
        }
        
        @Test
        public void testDeleteAll() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, eString);
                eem.makeEntity(ses1, eeString);
                eem.makeEntity(ses1, eeeString);
                assertEquals("At this point, there should be 3 events in the database", 3, eem.Count());											
                eem.deleteAll(ses1);
                assertEquals("At this point, there should be no events for session 1 in the database", 0, eem.Count());
        }
}