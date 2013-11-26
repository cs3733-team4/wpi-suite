package edu.wpi.cs.wpisuitetng.modules.cal.models;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockData;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventEntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

public class CommitmentEntityManagerTest {

        MockData db = new MockData(new HashSet<Object>());
           
        DateTime one=new DateTime(2000,1,1,1,1);
        
        DateTime two=new DateTime(2000,1,2,2,1);
        DateTime three=new DateTime(2000,1,3,3,1);
        DateTime four=new DateTime(2000,1,4,4,1);
        
        
        
        Commitment e = new Commitment().addName("First").setDueDate(one);
        String eString=e.toJSON();
        
        Commitment ee=new Commitment().setDueDate(two).addName("Second");
        String eeString=ee.toJSON();
        
        Commitment eee=new Commitment().setDueDate(three).addName("Third");
        String eeeString=eee.toJSON();
        
        
        Project p=new Project("p","26");
        User u1 = new User("User1", "U1", null, 0);
        Session ses1 = new Session(u1, p, "26");
           
        
        
        @Test
        public void testCanCreate() {
        		CommitmentEntityManager eem = new CommitmentEntityManager(db);
                assertNotNull("An Event Entity Manager can be created given a database",eem);
        }
         
        @Test
        public void testCount() throws WPISuiteException {
        	CommitmentEntityManager eem = new CommitmentEntityManager(db);
                assertEquals("An Event Entity Manager can return the # of Events stored in the database, with an initial count of 0", 0, eem.Count());
                // Basic test to check if Count() can function correctly; Testing for count incrementing if adding events is in testMakeEntity
        }
        
        @Test
        public void testMakeEntity() throws WPISuiteException {
        	CommitmentEntityManager eem = new CommitmentEntityManager(db);
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eString));
                assertEquals("After making events, Count() will return the updated # of events", 1,eem.Count());
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eeString));
                assertEquals("After making events, Count() will return the updated # of events", 2,eem.Count());
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eeeString));
                assertEquals("After making events, Count() will return the updated # of events", 3,eem.Count());
                //Testing that each individual event is saved in the database correctly is tested in the Get* tests
        }
           
        @Test
        public void testGetAllSingle() throws WPISuiteException {
        	CommitmentEntityManager eem = new CommitmentEntityManager(db);
                eem.makeEntity(ses1, eString);
                
                assertEquals("GetAll will return event event in the database in Event[] form; in the cae of only 1 event being stored, it will return that event", e.getName(), eem.getAll(ses1)[0].getName());
                assertEquals("GetAll will return event event in the database in Event[] form; in the cae of only 1 event being stored, it will return that event", e.getDate(), eem.getAll(ses1)[0].getDate());
        }
        
        
        @Test
        public void testGetAllMultiple() throws WPISuiteException {
        	CommitmentEntityManager eem = new CommitmentEntityManager(db);
                // Adding events to the database
                eem.makeEntity(ses1, eString);
                eem.makeEntity(ses1, eeString);
                eem.makeEntity(ses1, eeeString);
                Commitment[] eList=eem.getAll(ses1);
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
        public void testGetEventsByRangeAll() throws WPISuiteException {
        	CommitmentEntityManager eem = new CommitmentEntityManager(db);
                eem.makeEntity(ses1, eString);
                eem.makeEntity(ses1, eeString);
                eem.makeEntity(ses1, eeeString);
                
                String before="19500101T010100.050Z";
                String after ="20500102T010100.050Z";
                Commitment[] eList=eem.getCommitmentsByRange(ses1,before,after);
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
        	CommitmentEntityManager eem = new CommitmentEntityManager(db);
                // adding events to the database
                eem.makeEntity(ses1, eString);
                eem.makeEntity(ses1, eeString);
                eem.makeEntity(ses1, eeeString);
                
                // It appears that this function only returns events within the given parameters only if those parameters start and end at different days, so checking for
                // events within a # of hours doesn't work, but within a # of day works. IE: Tests looking to return an event that runs from 2-3 by looking for events from 1-4 
                // will return nothing; you'll have to check from the day it starts to the next day. This also means the hours / minutes of the input don't matter
                
                
                String before="20000101T010100.000Z"; // DateTime string at 1/1/2000, 1:00am; ie datetime one in basicDateTime string format
                String after ="20000102T010100.000Z"; // DateTime string at 1/2/2000, 2:00am; ie datetime two in basicDateTime string format
                Commitment[] eList=eem.getCommitmentsByRange(ses1,before,after);
                boolean hasEvent=false;
                
                if(eList[0].getName().equals("First"))
                        hasEvent=true;
                assertTrue("GetEventsByRange, if given a time range that only one event is within, will return only that event",hasEvent);
                
                after="20000103T010100.000Z"; // DateTime string at 1/3/2000, 2:00am; ie datetime three in basicDateTime string format
                eList=eem.getCommitmentsByRange(ses1,before,after);
                Boolean hasFirst=false, hasSecond=false;
                if(eList[0].getName().equals("First")||eList[1].getName().equals("First"))
                    hasFirst=true;
                if(eList[0].getName().equals("Second")||eList[1].getName().equals("Second"))
                    hasSecond=true;
                assertTrue("GetEventsByRange, if given a time range that some events are within, will return only those events in a random order",hasFirst);
                assertTrue("GetEventsByRange, if given a time range that some events are within, will return only those events in a random order",hasSecond);
                
                eList=eem.getCommitmentsByRange(ses1, before, before);
                assertTrue("GetEventsByRange, if given a time range that no events are within, will return an empty Event[]",eList.length==0);
                
        }
        
        
        @Test
        public void testGetEntity() throws WPISuiteException {
        	CommitmentEntityManager eem = new CommitmentEntityManager(db);
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eString));
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eeString));
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eeeString));
                //assertNotNull(eem.getEntity(ses1,"????"));
                // What string goes in the second argument? Code suggests 2 dateTime strings that represent start / end times?
        }
        
}