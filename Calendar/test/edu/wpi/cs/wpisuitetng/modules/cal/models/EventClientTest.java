package edu.wpi.cs.wpisuitetng.modules.cal.models;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.EventClient;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.EventEntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.network.Network;

public class EventClientTest {

	DateTime one=new DateTime(2000,1,1,1,30);   // Datetime at Jan 1st, 2000: 1:30
    DateTime two=new DateTime(2000,1,2,2,30);   // Datetime at Jan 2nd, 2000: 2:30
    DateTime three=new DateTime(2000,1,3,3,30); // Datetime at Jan 3rd, 2000: 3:30
    DateTime four=new DateTime(2000,1,4,4,30);  // Datetime at Jan 4th, 2000: 4:30
    
    Category cat1 = new Category();
    
    Event e = new Event().addStartTime(one).addEndTime(two).addName("First").addCategory(cat1.getCategoryID());
    String eString=e.toJSON();
    
    Event ee=new Event().addStartTime(two).addEndTime(three).addName("Second");
    String eeString=ee.toJSON();
    
    Event eee=new Event().addStartTime(three).addEndTime(four).addName("Third");
    String eeeString=eee.toJSON();
    
    
    Project p=new Project("p","26");
    User u1 = new User("User1", "U1", null, 0);
    Session ses1 = new Session(u1, p, "26");
    
    
	 @Test
     public void testGetEventsByRangeAll() throws WPISuiteException {
             EventClient eem = new NonFilteringEventClient();
             eem.put(e);
             eem.put(ee);
             eem.put(eee);
             
             DateTime before= new DateTime(1950, 01, 01, 01, 01); //"19500101T010100.050Z"; // String representing a DateTime at Jan 1, 1950
             DateTime after = new DateTime(2050, 01, 02, 01, 01); //"20500102T010100.050Z"; // String representing a DateTime at Jan 1, 2050
             List<Event> eList=eem.getEvents(before,after);
             boolean hasFirst=false, hasSecond=false, hasThird=false;
             
             if(eList.get(0).getName().equals("First")||eList.get(1).getName().equals("First")||eList.get(2).getName().equals("First"))
                     hasFirst=true;
             assertTrue("GetEventsByRange, with relatively high / low inputs, will return all events in a random order; if the result has all of the inputs, this method is working correctly",hasFirst);
             
             if(eList.get(0).getName().equals("Second")||eList.get(1).getName().equals("Second")||eList.get(2).getName().equals("Second"))
                     hasSecond=true;
             assertTrue("GetEventsByRange, with relatively high / low inputs, will return all events in a random order; if the result has all of the inputs, this method is working correctly",hasSecond);
             
             if(eList.get(0).getName().equals("Third")||eList.get(1).getName().equals("Third")||eList.get(2).getName().equals("Third"))
                     hasThird=true;
             assertTrue("GetEventsByRange, with relatively high / low inputs, will return all events in a random order; if the result has all of the inputs, this method is working correctly",hasThird);
     }
     
     @Test
     public void testGetEventsByRangeSome() throws WPISuiteException {
             EventClient eem = new NonFilteringEventClient();
             // adding events to the database
             eem.put(e);
             eem.put(ee);
             eem.put(eee);
             
             // It appears that this function only returns events within the given parameters only if those parameters start and end at different days, so checking for
             // events within a # of hours doesn't work, but within a # of day works. IE: Tests looking to return an event that runs from 2-3 by looking for events from 1-4 
             // will return nothing; you'll have to check from the day it starts to the next day. This also means the hours / minutes of the input don't matter
             
             
             DateTime before= new DateTime(2000, 01, 01, 01, 00); //"20000101T010000.000Z"; // DateTime string at 1/1/2000, 1:00; ie a little before datetime one in basicDateTime string format
             DateTime after = new DateTime(2000, 01, 02, 02, 00); //"20000102T020000.000Z"; // DateTime string at 1/2/2000, 2:00; ie a little before datetime two in basicDateTime string format
             List<Event> eList=eem.getEvents(before,after);
             boolean hasEvent=false;
             
             if(eList.get(0).getName().equals("First"))
                     hasEvent=true;
             assertTrue("GetEventsByRange, if given a time range that only one event is within, will return only that event",hasEvent);
             
             after= new DateTime(2000, 01, 03, 00, 00); //"20000103T020000.000Z"; // DateTime string at 1/3/2000, 2:00am; ie a little before datetime three in basicDateTime string format
             eList=eem.getEvents(before,after);
             
             assertEquals("GetEventsByRange, if given a time range that some events are within, will return only those events in a random order", 2, eList.size());
             
             Boolean hasFirst=false, hasSecond=false;
             if(eList.get(0).getName().equals("First")||eList.get(1).getName().equals("First"))
                 hasFirst=true;
             if(eList.get(0).getName().equals("Second")||eList.get(1).getName().equals("Second"))
                 hasSecond=true;
             assertTrue("GetEventsByRange, if given a time range that some events are within, will return only those events in a random order",hasFirst);
             assertTrue("GetEventsByRange, if given a time range that some events are within, will return only those events in a random order",hasSecond);
             
             eList=eem.getEvents(before, before);
             assertEquals("GetEventsByRange, if given a time range that no events are within, will return an empty Event[]", 0, eList.size());
             
     }
     
     @Test
     public void testGetEventsByRangeByHour() throws WPISuiteException {
             EventClient eem = new NonFilteringEventClient();
             // adding events to the database
             eem.put(e);
             eem.put(ee);
             eem.put(eee);
             
             // tests to prove the aforementioned lack of hour recognition
             // Possibly intentional; remove this test if it is
             
             DateTime before= new DateTime(2000, 01, 01, 01, 00); //"20000101T010000.000Z"; // DateTime string at 1/1/2000, 1:00am; ie a little before datetime one in basicDateTime string format
             DateTime after = new DateTime(2000, 01, 01, 06, 00); //"20000101T060000.000Z"; // DateTime string at 1/1/2000, 6:00am; ie a little past datetime one in basicDateTime string format
             List<Event> eList=eem.getEvents(before,after);
             boolean hasEvent=false;
             
             assertEquals("GetEventsByRange, if given a time range that only one event is within, *should* return only that event", 1, eList.size());
             
             if(eList.get(0).getName().equals("First"))
                     hasEvent=true;
             assertTrue("GetEventsByRange, if given a time range that only one event is within, will return only that event",hasEvent);
     }

     
     
     @Test
     public void testGetEntity() throws WPISuiteException {

 	   	EventClient cem = new NonFilteringEventClient();
            // adding Commitments to the database
            cem.put(e);
            cem.put(ee);
            cem.put(eee);
            // This method is really just another way of calling getEventsByRange with new inputs; as such, it has the same limitations and only needs basic testing

            assertEquals("getEntity will return a commitment in the database if it was stored there before",e.getName(),cem.getEvents(new DateTime(2000,01,01,01,00),new DateTime(2000,01,01,02,00)).get(0).getName());
            assertEquals("getEntity will return a commitment in the database if it was stored there before",eee.getName(),cem.getEvents(new DateTime(2000,01,04,03,00),new DateTime(2000,01,04,07,01)).get(0).getName());
            assertEquals("getEntity will return an empty array if no commitments are within the given range", 0 ,cem.getEvents(new DateTime(2050,01,01,01,01),new DateTime(2050,01,01,01,01)).size());

     }
     
     @Test
     public void testGetEntityByCategory() throws WPISuiteException {        	
     	EventClient eem = new NonFilteringEventClient();
     	eem.put(e);
     	eem.put(ee);
     	eem.put(eee);
         
         assertEquals("GetEventsByCategory, if given a category UUID, should return all events with that category UUID", e.getName(), eem.getEventsByCategory(cat1.getCategoryID()).get(0).getName());
     }

 	private static class NonFilteringEventClient extends EventClient
 	{
 		public NonFilteringEventClient()
 		{
 			super();
 			((MockNetwork)Network.getInstance()).clearCache();
 		}
 		@Override
 		protected boolean filter(Event obj)
 		{
 			return true;
 		}
 	}
}
