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

public class EventEntityManagerTest {

        MockData db = new MockData(new HashSet<Object>());
           
        DateTime one=new DateTime(2000,1,1,1,1);
        
        DateTime two=new DateTime(2000,1,1,2,1);
        DateTime three=new DateTime(2000,1,1,3,1);
        DateTime four=new DateTime(2000,1,1,4,1);
        
        
        
        Event e = new Event().addStartTime(one).addEndTime(two).addName("First");
        String eString=e.toJSON();
        
        Event ee=new Event().addStartTime(two).addEndTime(three).addName("Second");
        String eeString=ee.toJSON();
        
        Event eee=new Event().addStartTime(three).addEndTime(four).addName("Third");
        String eeeString=eee.toJSON();
        
        
        Project p=new Project("p","26");
        User u1 = new User("User1", "U1", null, 0);
        Session ses1 = new Session(u1, p, "26");
           
        
        
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
                assertEquals("After making events, Count() will return the updated # of events", 1,eem.Count());
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eeString));
                assertEquals("After making events, Count() will return the updated # of events", 2,eem.Count());
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eeeString));
                assertEquals("After making events, Count() will return the updated # of events", 3,eem.Count());
                //Testing that each individual event is saved in the database correctly is tested in the Get* tests
        }
           
        @Test
        public void testGetAllSingle() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, eString);
                
                assertEquals("GetAll will return event event in the database in Event[] form; in the cae of only 1 event being stored, it will return that event", e.getName(), eem.getAll(ses1)[0].getName());
                assertEquals("GetAll will return event event in the database in Event[] form; in the cae of only 1 event being stored, it will return that event", e.getStart(), eem.getAll(ses1)[0].getStart());
                assertEquals("GetAll will return event event in the database in Event[] form; in the cae of only 1 event being stored, it will return that event", e.getEnd(), eem.getAll(ses1)[0].getEnd());
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
        public void testGetEventsByRangeAll() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                eem.makeEntity(ses1, eString);
                eem.makeEntity(ses1, eeString);
                eem.makeEntity(ses1, eeeString);
                
                String before="19500101T010100.050Z";
                String after ="20500101T040100.050Z";
                Event[] eList=eem.getEventsByRange(ses1,before,after);
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
                eem.makeEntity(ses1, eString);
                eem.makeEntity(ses1, eeString);
                eem.makeEntity(ses1, eeeString);
                
                String before="20000101T010100.000Z"; // DateTime string at 1/1/2000, 1:00am; ie datetime one in basicDateTime string format
                String after ="20000101T020100.000Z"; // DateTime string at 1/1/2000, 2:00am; ie datetime two in basicDateTime string format
                Event[] eList=eem.getEventsByRange(ses1,before,after);
                boolean hasEvent=false;
                
                if(eList[0].getName().equals("First"))
                        hasEvent=true;
                assertTrue("GetEventsByRange, if given a time range that only one event is within, will return only that event",hasEvent);
                // This should be correct? Range from start of event one to end of event one returns nothing
                }
        
        
        @Test
        public void testGetEntity() throws WPISuiteException {
                EventEntityManager eem = new EventEntityManager(db);
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eString));
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eeString));
                assertNotNull("An Event Entity Manager will return an event upon sucessfully sending an Event into the database", eem.makeEntity(ses1, eeeString));
                //assertNotNull(eem.getEntity(ses1,"????"));
                // What string goes in the second argument? Code suggests 2 dateTime strings that represent start / end times?
        }
        
}