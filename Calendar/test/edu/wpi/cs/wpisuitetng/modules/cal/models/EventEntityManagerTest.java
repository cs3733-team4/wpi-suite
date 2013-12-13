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

import java.awt.Color;
import java.util.HashSet;

import org.junit.After;
import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockData;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventEntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

public class EventEntityManagerTest {

        MockData db = new MockData(new HashSet<Object>());
           
        DateTime one = new DateTime(2000,1,1,1,30, DateTimeZone.UTC);   // Datetime at Jan 1st, 2000: 1:30
        DateTime two = new DateTime(2000,1,2,2,30, DateTimeZone.UTC);   // Datetime at Jan 2nd, 2000: 2:30
        DateTime three = new DateTime(2000,1,3,3,30, DateTimeZone.UTC); // Datetime at Jan 3rd, 2000: 3:30
        DateTime four = new DateTime(2000,1,4,4,30, DateTimeZone.UTC);  // Datetime at Jan 4th, 2000: 4:30
        
        Category cat1 = new Category().addName("work").addColor(Color.BLACK);
        Category cat2 = new Category().addName("test").addColor(Color.WHITE);
                
        Event event1 = new Event().addStartTime(one).addEndTime(two).addName("First").addCategory(cat1.getCategoryID());
        String event1String = event1.toJSON();
        
        Event event2 = new Event().addStartTime(two).addEndTime(three).addName("Second").addCategory(cat2.getCategoryID());
        String event2String = event2.toJSON();
        
        Event event3 = new Event().addStartTime(three).addEndTime(four).addName("Third").addCategory(cat2.getCategoryID());
        String event3String = event3.toJSON();
        
        Project p = new Project("p","26");
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
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, event1String));
                assertEquals("After making events, Count() will return the updated # of events", 1, eem.Count());
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, event2String));
                assertEquals("After making events, Count() will return the updated # of events", 2, eem.Count());
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, event3String));
                assertEquals("After making events, Count() will return the updated # of events", 3, eem.Count());
                //Testing that each individual event is saved in the database correctly is tested in the Get* tests
        }
           
        @Test
        public void testGetAllSingle() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, event1String);
                
                assertEquals("GetAll will return event event in the database in Event[] form; in the case of only 1 event being stored, it will return that event", event1.getName(), eem.getEntity(ses1, "filter-all")[0].getName());
                assertEquals("GetAll will return event event in the database in Event[] form; in the case of only 1 event being stored, it will return that event", event1.getStart(), eem.getEntity(ses1, "filter-all")[0].getStart());
                assertEquals("GetAll will return event event in the database in Event[] form; in the case of only 1 event being stored, it will return that event", event1.getEnd(), eem.getEntity(ses1, "filter-all")[0].getEnd());
        }
        
        
        @Test
        public void testGetAllMultiple() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                // Adding events to the database
                eem.makeEntity(ses1, event1String);
                eem.makeEntity(ses1, event2String);
                eem.makeEntity(ses1, event3String);
                Event[] eList = eem.getEntity(ses1, "filter-all");
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
        public void testGetEventByUUID() throws WPISuiteException {
        	EventEntityManager eem = new EventEntityManager(db);
            eem.makeEntity(ses1, event1String);

            Event[] elist = eem.getEntity(ses1, "filter-event-by-uuid,"+event1.getEventID().toString());
            assertEquals("GetEventByUUID, if given an UUID for an event, should return that same event", event1.getEventID(), elist[0].getEventID());
        }
        
        @Test
        public void testGetEventsByRangeAll() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, event1String);
                eem.makeEntity(ses1, event2String);
                eem.makeEntity(ses1, event3String);
                
                String before="19500101T010100.050Z"; // String representing a DateTime at Jan 1, 1950
                String after ="20500102T010100.050Z"; // String representing a DateTime at Jan 1, 2050
                Event[] eList=eem.getEntity(ses1, "filter-events-by-range,"+before+","+after);
                boolean hasFirst=false, hasSecond=false, hasThird=false;
                
                if(eList[0].getName().equals("First")||eList[1].getName().equals("First")||eList[2].getName().equals("First"))
                        hasFirst=true;
                assertTrue("GetEventsByRange, with relatively high / low inputs, will return all events in a random order; if the result has all of the inputs, this method is working correctly",hasFirst);
                
                if(eList[0].getName().equals("Second")||eList[1].getName().equals("Second")||eList[2].getName().equals("Second"))
                        hasSecond=true;
                assertTrue("GetEventsByRange, with relatively high / low inputs, will return all events in a random order; if the result has all of the inputs, this method is working correctly",hasSecond);
                
                if(eList[0].getName().equals("Third")||eList[1].getName().equals("Third")||eList[2].getName().equals("Third"))
                        hasThird=true;
                assertTrue("GetEventsByRange, with relatively high / low inputs, will return all events in a random order; if the result has all of the inputs, this method is working correctly",hasThird);
        }
        
        @Test
        public void testGetEventsByRangeSome() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                // adding events to the database
                eem.makeEntity(ses1, event1String);
                eem.makeEntity(ses1, event2String);
                eem.makeEntity(ses1, event3String);
                
                // It appears that this function only returns events within the given parameters only if those parameters start and end at different days, so checking for
                // events within a # of hours doesn't work, but within a # of day works. IE: Tests looking to return an event that runs from 2-3 by looking for events from 1-4 
                // will return nothing; you'll have to check from the day it starts to the next day. This also means the hours / minutes of the input don't matter
                
                
                String before="20000101T010000.000Z"; // DateTime string at 1/1/2000, 1:00; ie a little before datetime one in basicDateTime string format
                String after ="20000102T020000.000Z"; // DateTime string at 1/2/2000, 2:00; ie a little before datetime two in basicDateTime string format
                Event[] eList=eem.getEntity(ses1, "filter-events-by-range,"+before+","+after);
                boolean hasEvent=false;
                
                if(eList[0].getName().equals("First"))
                        hasEvent=true;
                assertTrue("GetEventsByRange, if given a time range that only one event is within, will return only that event",hasEvent);
                
                after="20000103T020000.000Z"; // DateTime string at 1/3/2000, 2:00am; ie a little before datetime three in basicDateTime string format
                eList=eem.getEntity(ses1, "filter-events-by-range,"+before+","+after);
                
                assertEquals("GetEventsByRange, if given a time range that some events are within, will return only those events in a random order", 2, eList.length);
                
                Boolean hasFirst=false, hasSecond=false;
                if(eList[0].getName().equals("First")||eList[1].getName().equals("First"))
                    hasFirst=true;
                if(eList[0].getName().equals("Second")||eList[1].getName().equals("Second"))
                    hasSecond=true;
                assertTrue("GetEventsByRange, if given a time range that some events are within, will return only those events in a random order",hasFirst);
                assertTrue("GetEventsByRange, if given a time range that some events are within, will return only those events in a random order",hasSecond);
                
                eList=eem.getEntity(ses1, "filter-events-by-range,"+before+","+before);
                assertEquals("GetEventsByRange, if given a time range that no events are within, will return an empty Event[]", 0, eList.length);
                
        }
        
        @Test
        public void testGetEventsByRangeByHour() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                // adding events to the database
                eem.makeEntity(ses1, event1String);
                eem.makeEntity(ses1, event2String);
                eem.makeEntity(ses1, event3String);
                
                // tests to prove the aforementioned lack of hour recognition
                // Possibly intentional; remove this test if it is
                
                String before="20000101T010000.000Z"; // DateTime string at 1/1/2000, 1:00am; ie a little before datetime one in basicDateTime string format
                String after ="20000101T060000.000Z"; // DateTime string at 1/1/2000, 6:00am; ie a little past datetime one in basicDateTime string format
                Event[] eList=eem.getEntity(ses1, "filter-events-by-range,"+before+","+after);
                boolean hasEvent=false;
                
                assertEquals("GetEventsByRange, if given a time range that only one event is within, *should* return only that event", 1, eList.length);
                
                if(eList[0].getName().equals("First"))
                        hasEvent=true;
                assertTrue("GetEventsByRange, if given a time range that only one event is within, will return only that event",hasEvent);
        }
        
        
        @Test
        public void testGetEntity() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, event1String);
                eem.makeEntity(ses1, event2String);
                eem.makeEntity(ses1, event3String);
                
                // The huge string being used as input is "filter-events-by-range," + string form of starting dateTime + "," + string form of ending dateTime
                // This method is really just another way of calling getEventsByRange with new inputs; as such, it has the same limitations and only needs basic testing
                
                assertEquals("getEntity will return an event in the database if it was stored there before",event1.getName(),eem.getEntity(ses1,"filter-events-by-range,20000101T010000.000Z,20000102T020000.000Z")[0].getName());
                assertEquals("getEntity will return an event in the database if it was stored there before",event3.getName(),eem.getEntity(ses1,"filter-events-by-range,20000104T030000.000Z,20000104T070100.000Z")[0].getName());
                assertEquals("getEntity will return an empty array if no events are within the given range", 0 ,eem.getEntity(ses1,"filter-events-by-range,20500101T010100.000Z,20500101T010100.000Z").length);
        }
        
        @Test(expected=NotFoundException.class)
        public void testGetEntityWrongInput() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, event1String);
                eem.makeEntity(ses1, event2String);
                eem.makeEntity(ses1, event3String);
                
                assertEquals("getEntity return an error if anything but filter-events-by-range is the first string argument", null, eem.getEntity(ses1,"filter-events-by-time,20000101T000000.000Z,20000102T010000.000Z")[0].getName());
        }
        
        @Test
        public void testDeleteEntity() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, event1String);
                eem.makeEntity(ses1, event2String);
                assertEquals("At this point, there should be 2 events in the database", 2, eem.Count());											// Events from 1/1/2000 1:00 - 1/2/2000 1:00
                assertEquals("The deleteEntity method will return true if the deletion was successful", true, eem.deleteEntity(ses1, "filter-events-by-range,20000101T000000.000Z,20000102T010000.000Z"));
                assertEquals("At this point, there should be only one event in the database", 1, eem.Count());
                assertEquals("At this point, only the second event should still be in the database", "Second", eem.getEntity(ses1,"filter-all")[0].getName());
                
        }
        
        @Test
        public void testDeleteEntityOverLap() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                
                // Pointing out that the function will continue if more than one event is within the ID field; if this will never happen in a real run, delete this test
                
                eem.makeEntity(ses1, event1String);
                eem.makeEntity(ses1, event2String);
                assertEquals("At this point, there should be 2 events in the database", 2, eem.Count());											// Events from 1/1/2000 1:00 - 1/3/2000 1:00 
                assertEquals("The deleteEntity method will return true if the deletion was successful", true, eem.deleteEntity(ses1, "filter-events-by-range,20000101T000000.000Z,20000103T010000.000Z"));
                assertEquals("At this point, there should be only one event in the database", 1, eem.Count());
                // This will randomly delete one of the events within that timeframe; either event "First" or "Second" can still be in the database
        }
        
        @Test
        public void testDeleteAll() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, event1String);
                eem.makeEntity(ses1, event2String);
                eem.makeEntity(ses1, event3String);
                assertEquals("At this point, there should be 3 events in the database", 3, eem.Count());											
                eem.deleteAll(ses1);
                assertEquals("At this point, there should be no events for session 1 in the database", 0, eem.Count());
        }
        
        @Test
        public void testGetEntityByCategory() throws WPISuiteException {        	
        	EventEntityManager eem = new EventEntityManager(db);
        	eem.makeEntity(ses1, event1String);
            eem.makeEntity(ses1, event2String);
            eem.makeEntity(ses1, event3String);
            
            Event[] elist = eem.getEntity(ses1, "filter-events-by-category,"+cat1.getCategoryID().toString()); //Event e is the only event with category cat1
            assertEquals("GetEventsByCategory, if given a category UUID, should return all events with that category UUID", event1.getName(), elist[0].getName());

        }
        
}