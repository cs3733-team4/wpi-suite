package edu.wpi.cs.wpisuitetng.modules.cal.models;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;

import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockData;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentEntityManager;

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
        public void testGetCommitmentsByRangeAll() throws WPISuiteException {
                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                cem.makeEntity(ses1, eeeString);
                
                String before="19500101T010100.050Z"; // String representing a DateTime at Jan 1, 1950
                String after ="20500102T010100.050Z"; // String representing a DateTime at Jan 1, 2050
                Commitment[] eList=cem.getCommitmentsByRange(ses1,before,after);

                boolean hasFirst=false, hasSecond=false, hasThird=false;
                
                if(eList[0].getName().equals("First")||eList[1].getName().equals("First")||eList[2].getName().equals("First"))
                        hasFirst=true;

                assertTrue("GetCommitmentsByRange, with relatively high / low inputs, will return all commitments in a random order; if the result has all of the inputs, this method is working correctly",hasFirst);
                
                if(eList[0].getName().equals("Second")||eList[1].getName().equals("Second")||eList[2].getName().equals("Second"))
                        hasSecond=true;
                assertTrue("GetCommitmentsByRange, with relatively high / low inputs, will return all commitments in a random order; if the result has all of the inputs, this method is working correctly",hasSecond);
                
                if(eList[0].getName().equals("Third")||eList[1].getName().equals("Third")||eList[2].getName().equals("Third"))
                        hasThird=true;
                assertTrue("GetCommitmentsByRange, with relatively high / low inputs, will return all commitments in a random order; if the result has all of the inputs, this method is working correctly",hasThird);
        }
        
        @Test
        public void testGetCommitmentsByRangeSome() throws WPISuiteException {
                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                // adding Commitments to the database
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                cem.makeEntity(ses1, eeeString);
                
                // It appears that this function only returns commitments within the given parameters only if those parameters start and end at different days, so checking for
                // Commitments within a # of hours doesn't work, but within a # of day works. IE: Tests looking to return a commitment that runs from 2-3 by looking for commitments from 1-4 
                // will return nothing; you'll have to check from the day it starts to the next day. This also means the hours / minutes of the input don't matter
                
                
                String before="20000101T010000.000Z"; // DateTime string at 1/1/2000, 1:00; ie a little before datetime one in basicDateTime string format
                String after ="20000102T020000.000Z"; // DateTime string at 1/2/2000, 2:00; ie a little before datetime two in basicDateTime string format
                Commitment[] eList=cem.getCommitmentsByRange(ses1,before,after);
                boolean hasCommitment=false;
                
                if(eList[0].getName().equals("First"))
                        hasCommitment=true;
                assertTrue("GetCommitmentsByRange, if given a time range that only one Commitment is within, will return only that Commitment",hasCommitment);
                
                after="20000103T020000.000Z"; // DateTime string at 1/3/2000, 2:00am; ie a little before datetime three in basicDateTime string format
                eList=cem.getCommitmentsByRange(ses1,before,after);
                
                assertEquals("GetCommitmentsByRange, if given a time range that some Commitments are within, will return only those Commitments in a random order", 2, eList.length);
                

                Boolean hasFirst=false, hasSecond=false;
                if(eList[0].getName().equals("First")||eList[1].getName().equals("First"))
                    hasFirst=true;
                if(eList[0].getName().equals("Second")||eList[1].getName().equals("Second"))
                    hasSecond=true;

                assertTrue("GetCommitmentsByRange, if given a time range that some commitments are within, will return only those commitments in a random order",hasFirst);
                assertTrue("GetCommitmentsByRange, if given a time range that some commitments are within, will return only those commitments in a random order",hasSecond);
                
                eList=cem.getCommitmentsByRange(ses1, before, before);
                assertEquals("GetCommitmentsByRange, if given a time range that no Commitments are within, will return an empty commitment[]", 0, eList.length);
                
        }
        
        @Test
        public void testGetCommitmentsByRangeByHour() throws WPISuiteException {
                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                // adding Commitments to the database
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                cem.makeEntity(ses1, eeeString);
                
                // tests to prove the aforementioned lack of hour recognition
                // Possibly intentional; remove this test if it is
                
                String before="20000101T010000.000Z"; // DateTime string at 1/1/2000, 1:00am; ie a little before datetime one in basicDateTime string format
                String after ="20000101T060000.000Z"; // DateTime string at 1/1/2000, 6:00am; ie a little past datetime one in basicDateTime string format
                Commitment[] eList=cem.getCommitmentsByRange(ses1,before,after);
                boolean hasCommitment=false;
                
                assertEquals("GetCommitmentsByRange, if given a time range that only one commitment is within, *should* return only that commitment", 1, eList.length);
                
                if(eList[0].getName().equals("First"))
                        hasCommitment=true;
                assertTrue("GetCommitmentsByRange, if given a time range that only one commitment is within, will return only that commitment",hasCommitment);

        }
        
        
        @Test
        public void testGetEntity() throws WPISuiteException {

                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                cem.makeEntity(ses1, eeeString);
                
                // The huge string being used as input is "filter-commitments-by-range," + string form of starting dateTime + "," + string form of ending dateTime
                // This method is really just another way of calling getCommitmentsByRange with new inputs; as such, it has the same limitations and only needs basic testing
                
                assertEquals("getEntity will return a commitment in the database if it was stored there before",e.getName(),cem.getEntity(ses1,"filter-commitments-by-range,20000101T010000.000Z,20000102T030000.000Z")[0].getName());
                assertEquals("getEntity will return a commitment in the database if it was stored there before",eee.getName(),cem.getEntity(ses1,"filter-commitments-by-range,20000104T030000.000Z,20000104T070100.000Z")[0].getName());
                assertEquals("getEntity will return an empty array if no commitments are within the given range", 0 ,cem.getEntity(ses1,"filter-commitments-by-range,20500101T010100.000Z,20500101T010100.000Z").length);
        }
        
        @Test
        public void testGetEntityAll() throws WPISuiteException {
                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                cem.makeEntity(ses1, eeeString);
                
                // The huge string being used as input is "get-all-commitments," + string form of starting dateTime + "," + string form of ending dateTime
                // This method is really just another way of calling getAll, and as such the strings for the start / end time don't matter so long as they're valid DateTime strings
                
                assertEquals("getEntity will return all commitments in the database if get-all-commitments is the initial string argument", 3, cem.getEntity(ses1,"get-all-commitments,20000101T010000.000Z,20000102T030000.000Z").length);
                assertEquals("getEntity will return all commitments in the database if get-all-commitments is the initial string argument, regardless of the datetime strings", 3, cem.getEntity(ses1,"get-all-commitments,20000101T000000.000Z,12000101T000000.000Z").length);
        }
        
        @Test(expected=NotFoundException.class)
        public void testGetEntityWrongInput() throws WPISuiteException {
                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                cem.makeEntity(ses1, eeeString);
                
                assertNotNull("getEntity return an error if anything but the previous two strings are the first string argument", cem.getEntity(ses1,"filter-Commitments-by-time,20000101T000000.000Z,20000102T010000.000Z")[0].getName());
        }
        
        @Test
        public void testDeleteEntity() throws WPISuiteException {
                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                assertEquals("At this point, there should be 2 commitments in the database", 2, cem.Count());											// Commitments from 1/1/2000 1:00 - 1/2/2000 1:00
                assertEquals("The deleteEntity method will return true if the deletion was successful", true, cem.deleteEntity(ses1, "filter-commitments-by-range,20000101T000000.000Z,20000102T010000.000Z"));
                assertEquals("At this point, there should be only one commitment in the database", 1, cem.Count());
                assertEquals("At this point, only the second commitment should still be in the database", "Second", cem.getAll(ses1)[0].getName());
                
        }
        
        @Test
        public void testDeleteEntityOverLap() throws WPISuiteException {
                CommitmentEntityManager cem = new CommitmentEntityManager(db);
                
                // Pointing out that the function will continue if more than one commitment is within the ID field; if this will never happen in a real run, delete this test
                
                cem.makeEntity(ses1, eString);
                cem.makeEntity(ses1, eeString);
                assertEquals("At this point, there should be 2 commitments in the database", 2, cem.Count());											// Commitments from 1/1/2000 1:00 - 1/3/2000 1:00 
                assertEquals("The deleteEntity method will return true if the deletion was successful", true, cem.deleteEntity(ses1, "filter-commitments-by-range,20000101T000000.000Z,20000103T010000.000Z"));
                assertEquals("At this point, there should be only one commitment in the database", 1, cem.Count());
                // This will randomly delete one of the commitments within that timeframe; either commitment "First" or "Second" can still be in the database
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