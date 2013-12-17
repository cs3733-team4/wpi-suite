package edu.wpi.cs.wpisuitetng.modules.cal.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.CommitmentClient;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.CommitmentEntityManager;
import edu.wpi.cs.wpisuitetng.network.Network;



public class CommitmentClientTest {


    DateTime one=new DateTime(2000,1,1,1,1, DateTimeZone.UTC);
    DateTime two=new DateTime(2000,1,2,2,1, DateTimeZone.UTC);
    DateTime three=new DateTime(2000,1,3,3,1, DateTimeZone.UTC);
    DateTime four=new DateTime(2000,1,4,4,1, DateTimeZone.UTC);
    
    
    
    Commitment e = new Commitment().addName("First").setDueDate(one);
    String eString=e.toJSON();
    
    Commitment ee=new Commitment().setDueDate(two).addName("Second");
    String eeString=ee.toJSON();
    
    Commitment eee=new Commitment().setDueDate(three).addName("Third");

    String eeeString=eee.toJSON();
	
	   @Test
       public void testGetCommitmentsByRangeAll() throws WPISuiteException {
    	   CommitmentClient cem = new NonFilteringCommitmentClient();
               cem.put(e);
               cem.put(ee);
               cem.put(eee);
               
               DateTime before= new DateTime(1950, 01, 01, 01, 01); //"19500101T010100.050Z"; // String representing a DateTime at Jan 1, 1950
               DateTime after = new DateTime(2050, 01, 02, 01, 01); //"20500102T010100.050Z"; // String representing a DateTime at Jan 1, 2050
               List<Commitment> eList=cem.getCommitments(before,after);
               
               assertEquals(3, eList.size());

               boolean hasFirst=false, hasSecond=false, hasThird=false;
               
               if(eList.get(0).getName().equals("First")||eList.get(1).getName().equals("First")||eList.get(2).getName().equals("First"))
                       hasFirst=true;

               assertTrue("GetCommitmentsByRange, with relatively high / low inputs, will return all commitments in a random order; if the result has all of the inputs, this method is working correctly",hasFirst);
               
               if(eList.get(0).getName().equals("Second")||eList.get(1).getName().equals("Second")||eList.get(2).getName().equals("Second"))
                       hasSecond=true;
               assertTrue("GetCommitmentsByRange, with relatively high / low inputs, will return all commitments in a random order; if the result has all of the inputs, this method is working correctly",hasSecond);
               
               if(eList.get(0).getName().equals("Third")||eList.get(1).getName().equals("Third")||eList.get(2).getName().equals("Third"))
                       hasThird=true;
               assertTrue("GetCommitmentsByRange, with relatively high / low inputs, will return all commitments in a random order; if the result has all of the inputs, this method is working correctly",hasThird);
       }
       
       @Test
       public void testGetCommitmentsByRangeSome() throws WPISuiteException {
    	   CommitmentClient cem = new NonFilteringCommitmentClient();
               // adding Commitments to the database
               cem.put(e);
               cem.put(ee);
               cem.put(eee);
               
               // It appears that this function only returns commitments within the given parameters only if those parameters start and end at different days, so checking for
               // Commitments within a # of hours doesn't work, but within a # of day works. IE: Tests looking to return a commitment that runs from 2-3 by looking for commitments from 1-4 
               // will return nothing; you'll have to check from the day it starts to the next day. This also means the hours / minutes of the input don't matter
               
               
               DateTime before= new DateTime(2000, 01, 01, 01, 00);  //"20000101T010000.000Z"; // DateTime string at 1/1/2000, 1:00; ie a little before datetime one in basicDateTime string format
               DateTime after = new DateTime(2000, 01, 02, 02, 00);  //"20000102T020000.000Z"; // DateTime string at 1/2/2000, 2:00; ie a little before datetime two in basicDateTime string format
               List<Commitment> eList=cem.getCommitments(before,after);
               boolean hasCommitment=false;
               if(eList.get(0).getName().equals("First"))
                       hasCommitment=true;
               assertTrue("GetCommitmentsByRange, if given a time range that only one Commitment is within, will return only that Commitment",hasCommitment);
               
               after = new DateTime(2000, 01, 03, 02, 00);  //"20000103T020000.000Z"; // DateTime string at 1/3/2000, 2:00am; ie a little before datetime three in basicDateTime string format
               eList=cem.getCommitments(before,after);
               
               assertEquals("GetCommitmentsByRange, if given a time range that some Commitments are within, will return only those Commitments in a random order", 2, eList.size());
               

               Boolean hasFirst=false, hasSecond=false;
               if(eList.get(0).getName().equals("First")||eList.get(1).getName().equals("First"))
                   hasFirst=true;
               if(eList.get(0).getName().equals("Second")||eList.get(1).getName().equals("Second"))
                   hasSecond=true;

               assertTrue("GetCommitmentsByRange, if given a time range that some commitments are within, will return only those commitments in a random order",hasFirst);
               assertTrue("GetCommitmentsByRange, if given a time range that some commitments are within, will return only those commitments in a random order",hasSecond);
               
               eList=cem.getCommitments(before, before);
               assertEquals("GetCommitmentsByRange, if given a time range that no Commitments are within, will return an empty commitment[]", 0, eList.size());
               
       }
       
       @Test
       public void testGetCommitmentsByRangeByHour() throws WPISuiteException {
    	   CommitmentClient cem = new NonFilteringCommitmentClient();
               // adding Commitments to the database
               cem.put(e);
               cem.put(ee);
               cem.put(eee);
               
               // tests to prove the aforementioned lack of hour recognition
               // Possibly intentional; remove this test if it is
               
               DateTime before=new DateTime(2000, 01, 01, 01, 00); //"20000101T010000.000Z"; // DateTime string at 1/1/2000, 1:00am; ie a little before datetime one in basicDateTime string format
               DateTime after =new DateTime(2000, 01, 01, 06, 00); //"20000101T060000.000Z"; // DateTime string at 1/1/2000, 6:00am; ie a little past datetime one in basicDateTime string format
               List<Commitment> eList=cem.getCommitments(before,after);
               boolean hasCommitment=false;
               
               assertEquals("GetCommitmentsByRange, if given a time range that only one commitment is within, *should* return only that commitment", 1, eList.size());
               
               if(eList.get(0).getName().equals("First"))
                       hasCommitment=true;
               assertTrue("GetCommitmentsByRange, if given a time range that only one commitment is within, will return only that commitment",hasCommitment);

       }
       
       @Test
       public void testGetEntity() throws WPISuiteException {
    	   CommitmentClient cem = new NonFilteringCommitmentClient();
               // adding Commitments to the database
               cem.put(e);
               cem.put(ee);
               cem.put(eee);
               
               // This method is really just another way of calling getCommitmentsByRange with new inputs; as such, it has the same limitations and only needs basic testing
               
               assertEquals("getEntity will return a commitment in the database if it was stored there before",e.getName(),cem.getCommitments(new DateTime(2000,01,01,01,00),new DateTime(2000,01,02,02,00)).get(0).getName());
               assertEquals("getEntity will return a commitment in the database if it was stored there before",eee.getName(),cem.getCommitments(new DateTime(2000,01,03,03,00),new DateTime(2000,01,04,07,01)).get(0).getName());
               assertEquals("getEntity will return an empty array if no commitments are within the given range", 0 ,cem.getCommitments(new DateTime(2050,01,01,01,01),new DateTime(2050,01,01,01,01)).size());
       }
       
       
       @Test(expected=NullPointerException.class)
       public void testGetEntityWrongInput() throws WPISuiteException {
    	   CommitmentClient cem = new NonFilteringCommitmentClient();
              // adding Commitments to the database
              cem.put(e);
              cem.put(ee);
              cem.put(eee);
               
               assertNotNull("getEntity return an error if anything but the previous two strings are the first string argument", cem.getCommitments(new DateTime(2000,01,01,00,00), new DateTime(2000,01,02,01,00)).get(0).getName());
       }
	
	private static class NonFilteringCommitmentClient extends CommitmentClient
	{
		public NonFilteringCommitmentClient()
		{
			super();
			((MockNetwork)Network.getInstance()).clearCache();
		}
		@Override
		protected boolean filter(Commitment obj)
		{
			return true;
		}
	}
}
